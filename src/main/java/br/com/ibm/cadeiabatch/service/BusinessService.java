package br.com.ibm.cadeiabatch.service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URLConnection;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import br.com.ibm.cadeiabatch.dao.ExtratoHorasDao;
import br.com.ibm.cadeiabatch.entity.Categoria;
import br.com.ibm.cadeiabatch.entity.Chamado;
import br.com.ibm.cadeiabatch.entity.ExtratoHoras;
import br.com.ibm.cadeiabatch.entity.HistoricoHoras;
import br.com.ibm.cadeiabatch.entity.Hours;
import br.com.ibm.cadeiabatch.entity.Usuario;
import br.com.ibm.cadeiabatch.enums.CategoriaEnum;
import br.com.ibm.cadeiabatch.enums.TipoChamadoEnum;
import br.com.ibm.cadeiabatch.repository.CategoriaRepository;
import br.com.ibm.cadeiabatch.repository.ChamadoRepository;
import br.com.ibm.cadeiabatch.repository.HistoricoHorasRepository;
import br.com.ibm.cadeiabatch.utilities.DataUtils;
import br.com.ibm.cadeiabatch.utilities.GeradorExcel;

@Service
public class BusinessService {

	@Autowired
	private ChamadoRepository chamadoRepo;

	@Autowired
	private CategoriaRepository categoriaRepo;
	
	@Autowired
	private HistoricoHorasRepository historicoRepo;
	
	@Autowired
	private ExtratoHorasDao extratoDao;
	
	@Autowired
	private UserService serv;

	@Autowired
	private DataUtils dataUtils;
	
	private Logger logger = LogManager.getLogger(BusinessService.class);

	public void incluirNovoIncidente(Long numChamado, String descricao, TipoChamadoEnum tipo, CategoriaEnum categoria) {

		Usuario usuarioLogado = serv.getUsuarioLogado();

		Chamado chamado = new Chamado();
		chamado.setNumero(numChamado);
		chamado.setResponsavel(usuarioLogado);
		chamado.setDescricao(descricao);
		chamado.setTipo(tipo);
		chamado.setCategoria(categoria);
		chamado.setFechado(false);
		chamado.setHistorico(null);
		chamadoRepo.save(chamado);

	}

	public RedirectView closeIncBusiness(Long numero) {

		Chamado chamado = chamadoRepo.findById(numero).get();
		chamado.setFechado(true);

		chamadoRepo.save(chamado);

		RedirectView rv = new RedirectView("/hours");

		return rv;
	}

	public void reopenChamado(Long numero) {
		Usuario usuario = serv.getUsuarioLogado();

		Chamado chamado = chamadoRepo.findByNumeroAndResponsavel(numero, usuario);
		chamado.setFechado(false);
		chamadoRepo.save(chamado);
	}

	public ModelAndView getEditarUsuario(Usuario usuario) {
		ModelAndView mv = new ModelAndView("edituser");
		mv.addObject("usuario", usuario);

		return  mv;
	}

	public ModelAndView getChamadosByUserNew(int page) {
		
		List<Calendar> daysOfWeek = null;		
		switch(page) {
			case 1: daysOfWeek = dataUtils.getDaysOfLastWeek();
				break;
			case 2: daysOfWeek = dataUtils.getDaysOfCurrentWeek();
				break;
			default: daysOfWeek = dataUtils.getDaysOfCurrentWeek();
				break;
		}		
		
		SimpleDateFormat df = new SimpleDateFormat("dd-MM");
		List<String> dates = new ArrayList<>();		 
		List<Boolean> workDays = new ArrayList<>();
		
		Usuario usuarioLogado = serv.getUsuarioLogado();
		List<Chamado> chamados = chamadoRepo.findByResponsavelAndFechadoIsFalseOrderByIdAsc(usuarioLogado);

		for (Calendar cal : daysOfWeek) {
			String date = df.format(cal.getTime());
			dates.add(date);
			workDays.add(dataUtils.isWorkDay(cal));
		}

		for (Chamado chamado : chamados) {

			List<HistoricoHoras> historicosSemanal = new ArrayList<>();

			for (Calendar cal : daysOfWeek) {

				HistoricoHoras historicoHoras = historicoRepo.findByChamadoAndData(chamado, cal);

				if (historicoHoras != null) {
					historicosSemanal.add(historicoHoras);
				} else {
					historicosSemanal.add(new HistoricoHoras(chamado, cal, BigDecimal.ZERO));
				}
			}
			
			chamado.setHistorico(historicosSemanal);
			chamado.calculaTotal();
		}				
		
		ModelAndView mv = new ModelAndView("home-hours");
		mv.addObject("chamados", chamados);
		mv.addObject("datas", dates);
		mv.addObject("workDays", workDays);
		mv.addObject("totais", getTotalPorDia(daysOfWeek, usuarioLogado));
		mv.addObject("nivel", usuarioLogado.getNivel());
		mv.addObject("empresa", usuarioLogado.getEmpresaLogada());
		return mv;
	}
	
	public List<BigDecimal> getTotalPorDia(List<Calendar> days, Usuario usuarioLogado) {
		
		List<BigDecimal> totalPorDia = new ArrayList<>();
		
		for (Calendar day : days) {
			BigDecimal total = new BigDecimal(0);
			
			List<HistoricoHoras> listaHoras = historicoRepo.findByDataAndUsuario(new Date(day.getTimeInMillis()), usuarioLogado.getUsuario());
			
			for (HistoricoHoras hora : listaHoras) {
				total = total.add(hora.getHoras());
			}
			totalPorDia.add(total);
		}
				
		return totalPorDia;
	}

	public void editHours(List<Hours> hours) {
		for (Hours hour : hours) {
			
			String id = hour.getId().split("-")[2];
			BigDecimal horasDia = new BigDecimal(hour.getHour());

			if (!hour.getId().contains("null")) {
				logger.info("EDITANDO HORA COM COM O ID: " + hour.getId());
				
				if (horasDia.compareTo(BigDecimal.ZERO) == 0) {
					historicoRepo.deleteById(Long.parseLong(id));
					continue;
				}

				HistoricoHoras hist = historicoRepo.findById(Long.parseLong(id)).get();
				hist.setHoras(horasDia);
				historicoRepo.save(hist);
			} else {
				
				if (horasDia.compareTo(BigDecimal.ZERO) != 0) {
					String idChamado = hour.getId().split("-")[1];
					String data = hour.getId().split("-")[3];
					Calendar cal = Calendar.getInstance();

					try {
						SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
						cal.setTime(sdf.parse(data));
					} catch (ParseException e) {
						e.printStackTrace();
					}

					Chamado chamado = chamadoRepo.findById(Long.parseLong(idChamado)).get();
					HistoricoHoras historicoHoras = new HistoricoHoras(chamado, cal, horasDia);
					historicoRepo.save(historicoHoras);
					logger.info("HORA LANÇADA PARA O REGISTRO COM O ID: " + hour.getId());
				}
			}
		}
		calculaTotalHorasChamadosAbertos();
	}

	public void calculaTotalHorasChamadosAbertos() {
		Usuario usuario = serv.getUsuarioLogado();
		List<Chamado> chamados = chamadoRepo.findByResponsavelAndFechadoIsFalse(usuario);
		for (Chamado chamado : chamados) {
			chamado.calculaTotal();
			chamadoRepo.save(chamado);
		}
	}

	public void getChamadoDetail(Pageable pageable, Model model, Long id) {

		Chamado chamado = chamadoRepo.findById(id).get();

 		List<Calendar> allDaysFromCreateDate = dataUtils.getAllDaysFrom(dataUtils.getFirstDayLastWeek());		
		
		List<HistoricoHoras> listHist = new ArrayList<>();
		
		for (Calendar day : allDaysFromCreateDate) {
			
			HistoricoHoras hist = historicoRepo.findByChamadoAndData(chamado, day);

			if (hist != null) {
				listHist.add(hist);
			} else {
				HistoricoHoras histNulo = new HistoricoHoras(chamado, day, BigDecimal.ZERO);
				listHist.add(histNulo);
			}						
				
			chamado.setHistorico(listHist);
			chamado.calculaTotal();
		}				
		
		int start = (int) pageable.getOffset();
		int end = (start + pageable.getPageSize()) > listHist.size() ? listHist.size() : (start + pageable.getPageSize());
		Page<HistoricoHoras> page = new PageImpl<HistoricoHoras>(listHist.subList(start, end), pageable, listHist.size());
				
		model.addAttribute("page", page);
		model.addAttribute("chamado", chamado);
	}
	
	public void editHour(long chamadoId, String numChamado, TipoChamadoEnum tipo, String descricao) {
		Chamado chamado = chamadoRepo.findById(chamadoId).get();
		chamado.setTipo(tipo);
		//chamado.setCategoria(categoria);
		chamado.setDescricao(descricao);
		chamado.setNumero(Long.parseLong(numChamado));
		
		chamadoRepo.save(chamado);
	}
	
	public Integer getNivelUsuario() {
		Usuario usuarioLogado = serv.getUsuarioLogado();		
		return usuarioLogado.getNivel();
	}
	
	public List<Categoria> buscarCategoriaUsuario() {
				
		//Buscar Frente de Usuario
		Usuario usuarioLogado = serv.getUsuarioLogado();
		
		List<Categoria> categorias = categoriaRepo.findByUsuarios(usuarioLogado);
		return categorias.stream().distinct().collect(Collectors.toList());
	}
	
	public void extratorWorkHours(HttpServletRequest request, HttpServletResponse response, 
			long dataInicial, long dataFinal, String consultor, String categoria, Integer empresa, String fileName, boolean download, boolean consolidado) {

		List<ExtratoHoras> horasExtraidas = new ArrayList<>();
		logger.info("Inicio da geração de relatório para a Data: " + new Date(dataInicial) + " à "+ new Date(dataFinal));
				
		try {
			if(consolidado) {
				horasExtraidas = extratoDao.extrairPorPeriodoBanco(new Date(dataInicial), new Date(dataFinal), empresa, consolidado);
			}
			else {
				if(consultor.equals("todos") && categoria.equals("todos")) {
					//Incluir Empresa
					horasExtraidas = extratoDao.extrairPorPeriodoBanco(new Date(dataInicial), new Date(dataFinal), empresa, consolidado);
				}else if(consultor.equals("todos") && !categoria.equals("todos")) {
					Integer cat = Integer.parseInt(categoria);
					horasExtraidas = extratoDao.extrairPorPeriodoAndCategoriaBanco(new Date(dataInicial), new Date(dataFinal), cat);				
				} else if(!consultor.equals("todos") && categoria.equals("todos")){
					horasExtraidas = extratoDao.extrairPorPeriodoAndConsultorBanco(new Date(dataInicial), new Date(dataFinal), consultor);				
				}else {
					Integer cat = Integer.parseInt(categoria);
					horasExtraidas = extratoDao.extrairPorPeriodoAndConsultorAndCategoriaBanco(new Date(dataInicial), new Date(dataFinal), consultor, cat);
				}
			}
						
			//FUNCAO DE DOWNLOAD DE ARQUIVO			
			if(download) {
				GeradorExcel g = new GeradorExcel();
				g.gerarExcelWorkHours(horasExtraidas, consolidado);
				
				File file = new File("ExtratoWorkHours.xls");
				if(file.exists()) {
					String mimeType = URLConnection.guessContentTypeFromName(file.getName());
					if(mimeType == null) {
						mimeType = "application/octet-stream";
					}
	
					response.setContentType(mimeType);
					response.setHeader("Content-Disposition", String.format("attachment; filename=\"" + file.getName() + "\""));
					System.out.println("Anexando arquivo..");
					response.setContentLength((int) file.length());
					InputStream inputStream = new BufferedInputStream(new FileInputStream("ExtratoWorkHours.xls"));
					
					FileCopyUtils.copy(inputStream, response.getOutputStream());
				}
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}	
	}
}