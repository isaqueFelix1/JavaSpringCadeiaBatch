
package br.com.ibm.cadeiabatch.ws;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.ibm.cadeiabatch.entity.*;
import br.com.ibm.cadeiabatch.enums.Empresas;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.text.ParseException;

import br.com.ibm.cadeiabatch.dao.LogDao;
import br.com.ibm.cadeiabatch.enums.CategoriaEnum;
import br.com.ibm.cadeiabatch.enums.Nivel;
import br.com.ibm.cadeiabatch.enums.TipoChamadoEnum;
import br.com.ibm.cadeiabatch.exceptions.ForbiddenException;
import br.com.ibm.cadeiabatch.repository.LogRepository;
import br.com.ibm.cadeiabatch.repository.UserRepository;
import br.com.ibm.cadeiabatch.service.BusinessService;
import br.com.ibm.cadeiabatch.service.UserService;
import br.com.ibm.cadeiabatch.utilities.MailUtils;

@Controller
public class Control {

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private UserService serv;

	@Autowired
	private LogDao logDao;
		
	@Autowired
	private MailUtils mailUtils;
	
	@Autowired
	private UserRepository rep;
	
	@Autowired
	private LogRepository lrep;
	
	@Autowired
	private BusinessService service;

	private final DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
	private final DateFormat dateHourFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");

	@RequestMapping(value = { "/", "/home" })
	public String home() {
		return "home.html";
	}
	
	@RequestMapping("/journal")
	public String homeJournal() {
		return "home-journal.html";
	}
	
	@RequestMapping("/hours")
	public ModelAndView homeHours() {				
		return service.getChamadosByUserNew(2);
	}
	
	@RequestMapping("/hours/{page}")
	public ModelAndView homeHoursPageable(@PathVariable int page) {
		return service.getChamadosByUserNew(page);
	}
	
	@RequestMapping("/hours/detail")
	public String houmeHoursDetail(@PageableDefault(size=7) Pageable pageable, @RequestParam("id") String id, Model model) {
		service.getChamadoDetail(pageable, model, Long.parseLong(id));
		return "home-hours-detail";
	}
	
	@RequestMapping("/close/{chamado}")
	public RedirectView closeInc(@PathVariable String chamado) {
		return service.closeIncBusiness(Long.parseLong(chamado));
	}
	
	@RequestMapping("/add")
	public String addInc() {
		return "incluir-chamado";
	}

	@RequestMapping("/incluir")
	public String incluir() {
		return "incluir.html";
	}
	
	@RequestMapping(value = "/hours/editar", method = RequestMethod.POST)
	public String editarHourInformation(HttpServletRequest request) throws Exception {	
		
		String chamado = request.getParameter("chamado").toString();
		String tipo = request.getParameter("tipo").toString();
		//String categoria = request.getParameter("categoria").toString();
		String descricao = request.getParameter("descricao").toString();
		String chamadoId = request.getParameter("chamadoId").toString();
		
		try {			
			service.editHour(Long.parseLong(chamadoId),
							chamado, 
							TipoChamadoEnum.values()[Integer.parseInt(tipo)],
							//CategoriaEnum.values()[Integer.parseInt(categoria)],
							descricao);
			
			return "redirect:/hours";
		} catch (Exception e) {			
			throw e;
		}
	}

	@RequestMapping(value = "/editar/{logNum}", method = RequestMethod.GET)
	public String editar(Model model, @PathVariable long logNum) {
		Log log = lrep.findById(logNum).get();
		Usuario usuario = serv.getUsuarioLogado();
		if (usuario.getId() != log.getUsuario().getId()) {
			throw new ForbiddenException();
		}
		model.addAttribute("editar", "/editar/" + log.getId());
		model.addAttribute("nivel", log.getNivel());
		model.addAttribute("job", log.getJob());
		model.addAttribute("data", dateHourFormat.format(log.getDataHoraCriacao().getTime()));
		model.addAttribute("incidente", log.getIncidente());
		model.addAttribute("descricao", log.getDescricao());
		model.addAttribute("tipochamado", log.getTipo());
		model.addAttribute("optionalMail", log.isOptionalMail());
				
		return "editar.html";
	}

	@RequestMapping(value = "/editar/{logNum}", method = RequestMethod.POST)
	public String editarDo(HttpServletRequest request, @PathVariable long logNum) throws Exception {		
		try {
			Log log = lrep.findById(logNum).get();
			Usuario usuario = serv.getUsuarioLogado();
			if (usuario.getId() != log.getUsuario().getId()) {
				throw new ForbiddenException();
			}
			log.setDescricao(request.getParameter("descricao"));
			log.setNivel(Nivel.values()[Integer.parseInt(request.getParameter("nivel"))]);
			log.setJob(request.getParameter("job"));
			log.setIncidente(Long.parseLong(request.getParameter("incidente")));
			Calendar date = Calendar.getInstance();
			date.setTime(dateHourFormat.parse(request.getParameter("data")));
			log.setDataHoraCriacao(date);
			log.setDataCriacao(dateHourFormat.parse(request.getParameter("data")));
			log.setDataHoraAtualizacao(date);
			log.setDataAtualizacao(dateHourFormat.parse(request.getParameter("data")));
			log.setTipo(TipoChamadoEnum.values()[Integer.parseInt(request.getParameter("tipo"))]);
			boolean optionalMail = false;
			String checked = request.getParameter("optionalMail");
			if(checked != null) {
				if(checked.equals("on")) {
					optionalMail = true;
				}
			}
		
			log.setOptionalMail(optionalMail);
			
			lrep.save(log);
			return "redirect:/journal";
		} catch (Exception e) {			
			throw e;
		}
	}

	@RequestMapping("/pesquisar")
	public String pesquisar() {
		return "pesquisar.html";
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(Locale locale, Model model, String error) {
		if (error != null)
			model.addAttribute("error", "Usuario e senha invalidos.");

		return "login";
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(@ModelAttribute String loginObject, Locale locale, Model model) {
		return "home";
	}


	@RequestMapping(value = "/registrar", method = RequestMethod.POST)
	public String registrationDo(HttpServletRequest request) {
		Usuario user = new Usuario();
		Empresa empresa = new Empresa();

		user.setArea(request.getParameter("area").toString());
		user.setNome(request.getParameter("nome").toString());
		user.setSenha(bCryptPasswordEncoder.encode(request.getParameter("password").toString()));
		user.setUsuario(request.getParameter("username").toString());
		user.setNivel(0);
		user.setEmail(request.getParameter("email").toString());

		if("1".equals(request.getParameter("empresa").toString()))
			user.setArea("");

		// consulta Empresa
		empresa.setId(Long.parseLong(request.getParameter("empresa").toString()));
		empresa.setNome_empresa(Empresas.getName(Integer.parseInt(request.getParameter("empresa").toString())));

		user.setEmpresa(Arrays.asList(empresa));

		Usuario userAux = rep.buscarPorNome(user.getUsuario());

		if(userAux != null)
			return "redirect:registrar?error";

		rep.save(user);

		return "redirect:login";
	}

	@RequestMapping(value = "/registrar", method = RequestMethod.GET)
	public String registrar(Model model, String error) {
		if (error != null)
			model.addAttribute("error", "Usuario ja existe.");

		return "registration";
	}

//	@RequestMapping(value = "/registrar", method = RequestMethod.GET)
//	public String registration() {
//		return "registration";
//	}

	@RequestMapping(value = "/relatoriomanual", method = RequestMethod.GET)
	public String relatorioManual() {
		return "relatorio_manual";
	}

	@RequestMapping(value = "/relatoriomanual", method = RequestMethod.POST)
	@ResponseBody
	public String sendRelatorioManual(HttpServletRequest request) {		
		Date generateDate = null;
		try {
			generateDate = dateFormat.parse(request.getParameter("data"));
		} catch (ParseException e) {
			return "Preencher data";
		}
		Boolean monthly = request.getParameter("mensal") == null ? false : true;
		String toList = request.getParameter("to");
		if (toList == null || toList.trim() == "") {
			return "Preencher list da recebimento";
		}
		String ccList = request.getParameter("cc");		
		try {
			System.out.println("Chamando envio");
			mailUtils.sendEmail(generateDate, toList, ccList, monthly);			
		} catch (Exception e) {			
			return e.getCause() + "---" + e.getMessage() +  "Falha ao enviar, verificar parametros e enviar novamente";
		}
		return "done";
	}

	@RequestMapping(value = "/gerargrafico", method = RequestMethod.POST)
	@ResponseBody
	public List<PontoGrafico> gerarGrafico(HttpServletRequest request) {
		List<PontoGrafico> retList = new ArrayList<PontoGrafico>();		
		try {
			Date dataInicial = dateFormat.parse(request.getParameter("data-inicial-graph"));
			Date dataFinal = dateFormat.parse(request.getParameter("data-final-graph"));
			int nivel = Integer.parseInt(request.getParameter("nivel"));
			String job = request.getParameter("job").isEmpty() ? "" : request.getParameter("job");
			String tipo = request.getParameter("agrupamento");			
			retList = logDao.buscarDadosGrafico(dataInicial, dataFinal, nivel, job, tipo);
		} catch (Exception e) {			
		}

		return retList;
	}
	
	@RequestMapping(value = "/gerarExtrato", method = RequestMethod.GET)
	public String gerarExtrato(){
		return "gerarExtrato";
	}
	/**
	@RequestMapping(value = "/lerExcel/{process}", method = RequestMethod.GET)
	public void LerExcel( @PathVariable Integer process) throws IOException, ParseException, InterruptedException, EmailException, AWTException{		
		backlogService.lerExcel(process);
		mailUtils.sendEmailBacklog("isaque.felix1@ibm.com");				
	}
	*/
	@RequestMapping(value = "/gerarExtrato/periodo/dataInicial/{dataInicial}/dataFinal/{dataFinal}/consultor/{consultor}/categoria/{categoria}/empresa/{empresa}/{fileName:.+}", method = RequestMethod.GET)
	@ResponseBody
	public void extratorWorkHours(HttpServletRequest request, HttpServletResponse response,
			@PathVariable long dataInicial, @PathVariable long dataFinal, @PathVariable String consultor, @PathVariable String categoria, @PathVariable Integer empresa, @PathVariable("fileName") String fileName) throws IOException {
		
			service.extratorWorkHours(request, response, dataInicial, dataFinal, consultor, categoria, empresa, fileName, true, false);
	}
	
	@RequestMapping(value = "/nivelUsuario")
	@ResponseBody
	public Integer getNivelUsuario() {
		return service.getNivelUsuario();
	}
	
	@RequestMapping(value = "/categoriaUsuario", method = RequestMethod.GET)
	@ResponseBody
	public List<Categoria> buscarCategoriaUsuario() {
		return service.buscarCategoriaUsuario();
	}
	/**	
	@RequestMapping(value = "backlog/recentes/{dataInicial}", method = RequestMethod.GET)
	@ResponseBody
	public List<BacklogDiario> backlogRecentes(@PathVariable long dataInicial) {
		
		List<BacklogDiario> backlogRet = backlogService.getIncidentesMes(new Date(dataInicial));
		logger.info("Lista possuí: " + backlogRet.size());

		return backlogRet;
	}
	 
	@RequestMapping(value = "backlog/recentes/fechados/{dataInicial}", method = RequestMethod.GET)
	@ResponseBody
	public List<String> backlogIncidentesFechados(@PathVariable long dataInicial) {
		
		List<String> backlogFechados = backlogService.getIncidentesFechadosMesAtual(new Date(dataInicial));		

		Collections.sort(backlogFechados);
		
		return backlogFechados;
	}
	*/
	@RequestMapping(value = "/pesquisar-backlog", method = RequestMethod.GET)
	public String pesquisarBacklog(){
		return "pesquisar-backlog";
	}

	@RequestMapping(value = "/consultaEmpresa", params={"empresa", "usuario"})
	@ResponseBody
	public Boolean getConsultaEmpresa(
				@RequestParam("empresa") Integer idEmpresa,
				@RequestParam("usuario") String usuario
			){
		var user = rep.buscarPorNome(usuario);
		Long id = idEmpresa.longValue();

		var result = user.getEmpresas().stream().filter(x -> x.getId()==id).findFirst();

		if(result.isPresent()) {
			//atualiza empresa logada
			user.setEmpresaLogada(idEmpresa);
			rep.save(user);
			return true;
		}

		return false;
	}

}
