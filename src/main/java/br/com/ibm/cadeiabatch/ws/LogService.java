package br.com.ibm.cadeiabatch.ws;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.ibm.cadeiabatch.dao.LogDao;
import br.com.ibm.cadeiabatch.entity.Log;
import br.com.ibm.cadeiabatch.entity.LogOut;
import br.com.ibm.cadeiabatch.enums.Nivel;
import br.com.ibm.cadeiabatch.enums.TipoChamadoEnum;
import br.com.ibm.cadeiabatch.service.UserService;

@Controller
@RequestMapping("/logs")
public class LogService {

	@Autowired
	private LogDao dao;

	@Autowired
	private UserService serv;

	private final int RECENT_ROW_COUNT = 15;

	@RequestMapping(value = "/recentes", method = RequestMethod.GET)
	@ResponseBody
	public List<LogOut> logsRecentes() {
		
		List<Log> logsRecentes = dao.logsRecentesBanco(RECENT_ROW_COUNT).getContent();
		List<LogOut> logRet = new ArrayList<LogOut>();
		for (Log log : logsRecentes){
			logRet.add(new LogOut(log));			
		}

		logRet.sort((p1, p2) -> p2.getDataHoraCriacao().compareTo(p1.getDataHoraCriacao()));


		return logRet;
	}

	//@ResponseStatus(code = HttpStatus.CREATED)
	@RequestMapping(value = "/salvar", method = RequestMethod.POST)
	public String incluirLog(HttpServletRequest request) {
		Log log = new Log();		
		log.setIncidente(Long.parseLong(request.getParameter("incidente").toString()));		
		log.setDescricao(request.getParameter("descricao").toString());
		log.setJob(request.getParameter("job").toString());		
		System.out.println("Nivel: " + Integer.parseInt(request.getParameter("nivel").toString()));
		log.setNivel(Nivel.values()[Integer.parseInt(request.getParameter("nivel"))]);
		log.setTipo(TipoChamadoEnum.values()[Integer.parseInt(request.getParameter("tipo"))]);
		log.setDescricao(request.getParameter("descricao").toString());
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR, cal.get(Calendar.HOUR)-3);
		log.setDataHoraCriacao(cal);
		log.setDataHoraAtualizacao(cal);
		log.setDataCriacao(new Date());
		log.setDataAtualizacao(new Date());
		log.setUsuario(serv.getUsuarioLogado());		
		
		dao.incluir(log);
		return "redirect:/journal";
	}

	@RequestMapping(value = "/pesquisa/periodo/dataInicial/{dataInicial}/dataFinal/{dataFinal}", method = RequestMethod.GET)
	@ResponseBody
	public List<LogOut> pesquisarPorPeriodo(@PathVariable long dataInicial, @PathVariable long dataFinal) {
		List<Log> logsExtraidos = dao.pesquisarPorPeriodoBanco(new Date(dataInicial), new Date(dataFinal));
		List<LogOut> logsRet = new ArrayList<LogOut>();
		for (Log log : logsExtraidos){
			logsRet.add(new LogOut(log));
		}
		return logsRet;
	}
/**
	@RequestMapping(value = "/extrair/periodo/dataInicial/{dataInicial}/dataFinal/{dataFinal}/{fileName:.+}", method = RequestMethod.GET)
	@ResponseBody
	public void extrairPorPeriodo(HttpServletRequest request, HttpServletResponse response,
			@PathVariable long dataInicial, @PathVariable long dataFinal, @PathVariable("fileName") String fileName) throws IOException {
		
		System.out.println("Inicio da Extração!");
		List<Log> logsExtraidos = dao.pesquisarPorPeriodoBanco(new Date(dataInicial), new Date(dataFinal));
		List<LogOut> logsRet = new ArrayList<LogOut>();
		for (Log log : logsExtraidos){
			logsRet.add(new LogOut(log));
		}
		
		String filePath = "C:/ExtracaoJournalBatch/";				
		
		try {
			
			GeradorExcel g = new GeradorExcel();
			g.gerarExcelJournalBatch(logsRet);
			
			//FUNCAO DE DOWNLOAD DE ARQUIVO
			File file = new File(filePath + fileName);
			if(file.exists()) {
				String mimeType = URLConnection.guessContentTypeFromName(file.getName());
				if(mimeType == null) {
					mimeType = "application/octet-stream";
				}

				response.setContentType(mimeType);
				response.setHeader("Content-Disposition", String.format("attachment; filename=\"" + file.getName() + "\""));
				System.out.println("Anexando arquivo..");
				response.setContentLength((int) file.length());
				InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
				
				FileCopyUtils.copy(inputStream, response.getOutputStream());
			}
		
			
		}catch(Exception e) {
			e.printStackTrace();
		}		
	}
*/
	
	@RequestMapping(value = "/pesquisa/periodo/data/{data}/incidente/{incidente}/nivel/{nivel}", method = RequestMethod.GET)
	@ResponseBody
	public List<LogOut> pesquisarPorMalha(@PathVariable long data, @PathVariable long incidente, @PathVariable int nivel) {		
		System.out.println("=================PROCESSANDO=================");
		System.out.println(nivel);
		List<Log> logsExtraidos = null;
		if(data == 0 && incidente == 0 && nivel == 10) {
			return null;
		}else if(data != 0 && incidente == 0 && nivel == 10) {
			System.out.println("=================DATA DE PARAMETRO=================");
			logsExtraidos = dao.pesquisarPorMalhaData(retornaDataInicio(data), retornaDataFim(data));
		}else if(data != 0 && incidente != 0 && nivel == 10) {
			System.out.println("=================DATA E INCIDENTE DE PARAMETRO=================");
			logsExtraidos = dao.pesquisarPorMalhaDataIncidente(retornaDataInicio(data), retornaDataFim(data),incidente);
		}else if(data != 0 && incidente == 0 && nivel != 10) {
			System.out.println("=================DATA E NIVEL DE PARAMETRO=================");
			logsExtraidos = dao.pesquisarPorMalhaDataNivel(retornaDataInicio(data), retornaDataFim(data),Nivel.values()[nivel]);
		}else if(data == 0 && incidente != 0 && nivel == 10) {
			System.out.println("=================INCIDENTE DE PARAMETRO=================");
			logsExtraidos = dao.pesquisarPorMalhaIncidente(incidente);
		}else if(data == 0 && incidente != 0 && nivel != 10) {
			System.out.println("=================INCIDENTE E NIVEL DE PARAMETRO=================");
			logsExtraidos = dao.pesquisarPorMalhaIncidenteNivel(incidente, Nivel.values()[nivel]);
		}else if(data == 0 && incidente == 0 && nivel != 10) {
			System.out.println("=================NIVEL DE PARAMETRO=================");
			logsExtraidos = dao.pesquisarPorMalhaNivel(Nivel.values()[nivel]);
		}else{
			logsExtraidos = dao.pesquisarPorMalha(retornaDataInicio(data), retornaDataFim(data),incidente,Nivel.values()[nivel]);
		}
		List<LogOut> logsRet = new ArrayList<LogOut>();
		for (Log log : logsExtraidos){
			logsRet.add(new LogOut(log));
		}
		return logsRet;
	} 

	private Calendar retornaDataFim(long data) {
		Date date = new Date(data);

		Calendar dataInputada = Calendar.getInstance();
		dataInputada.setTime(date);
		
		Calendar dataFim = Calendar.getInstance();
		dataFim.setTime(date);

		if (dataInputada.get(Calendar.DAY_OF_MONTH) + 1 > dataInputada.getActualMaximum(Calendar.DAY_OF_MONTH)) {
			dataFim.set(Calendar.DAY_OF_MONTH, 1);
			dataFim.set(Calendar.MONTH, dataInputada.get(Calendar.MONTH) + 1);
		} else {
			dataFim.set(Calendar.DAY_OF_MONTH, dataInputada.get(Calendar.DAY_OF_MONTH) + 1);
		}
		dataFim.set(Calendar.HOUR_OF_DAY, 8);
		
		return dataFim;
	}
	
	private Calendar retornaDataInicio(long data) {
		Date date = new Date(data);
		Calendar dataInicio = Calendar.getInstance();
		dataInicio.setTime(date);
		dataInicio.set(Calendar.HOUR_OF_DAY, 18);
		
		return dataInicio;
	}

}
