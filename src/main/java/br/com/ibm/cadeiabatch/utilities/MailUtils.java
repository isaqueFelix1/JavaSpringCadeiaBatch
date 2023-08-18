package br.com.ibm.cadeiabatch.utilities;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.mail.MessagingException;

import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import org.springframework.beans.factory.annotation.Value;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;

import br.com.ibm.cadeiabatch.entity.Log;
import br.com.ibm.cadeiabatch.repository.ConfigRepository;
import br.com.ibm.cadeiabatch.repository.LogRepository;

@Component
public class MailUtils {

	@Autowired
	private LogRepository rep;

	@Autowired
	private ConfigRepository conf;

	@Autowired
	private DataUtils dataUtils;
	
	private final String TEMPLATE_DAILY_HTML = "templates/mail_report.twig";
	private final String TEMPLATE_MONTHLY_HTML = "templates/mail_report_mensal.twig";
	private final String TEMPLATE_PENDENCY_HTML = "templates/mail_pendency.twig";
	private final String TEMPLATE_GS_HTML = "templates/mail_gs.twig";
	private final String TEMPLATE_BACKLOG_HTML = "templates/mail_report_backlog.twig";
	
	private final String TEMPLATE_DAILY_TXT = "templates/mail_report_txt.twig";
	private final String TEMPLATE_MONTHLY_TXT = "templates/mail_report_txt_mensal.twig";
	private final String TEMPLATE_PENDENCY_TXT = "templates/mail_pendency_txt.twig";
	private final String TEMPLATE_GS_TXT = "templates/mail_gs_txt.twig";
	private final String TEMPLATE_BACKLOG_TXT = "templates/mail_report_txt_backlog.twig";
	
	private final String TEMPLATE_NEWPASS_HTML = "templates/new_password.twig";
	private final String TEMPLATE_NEWPASS_TXT = "templates/new_password_txt.twig";

	private final String MAIL_SUBJECT_DAILY = "Relatório Malha Noturna - Dia %s ao %s";
	private final String MAIL_SUBJECT_MONTHLY = "Relatório Mensal - Dia %s ao %s";
	private final String MAIL_SUBJECT_PENDENCY = "Pendência WorkHours";
	private final String MAIL_SUBJECT_GS = "Comunicado sobre Processamento Noturno - Dia %s ao %s";
	private final String MAIL_SUBJECT_BACKLOG = "Status Backlog";
	
	private final String MAIL_SUBJECT_PASS = "Recuperação de Senha Work Hours - Usuário %s";

	private final String SENDER_CONF_NAME = "MAIL_SENDER";
	private final String CC_MAIL_CONF_NAME = "REPORT_MAIL_TO";
	private final String TO_PORTO_1_CONF_NAME = "REPORT_MAIL_TO_PORTO_1";
	private final String TO_PORTO_2_CONF_NAME = "REPORT_MAIL_TO_PORTO_2";
	private final String TO_GS_1_CONF_NAME = "REPORT_MAIL_TO_GS_1";
	private final String TO_GS_2_CONF_NAME = "REPORT_MAIL_TO_GS_2";
	private final String CC_GS_1_CONF_NAME = "REPORT_MAIL_CC_GS_1";
	
	private final String CC_MAIL_INTER_CONF_NAME = "INTERNAL_MAIL_CC";
	private final String TO_INTER_CONF_NAME = "REPORT_MAIL_TO_INTER";

	@Value("${mail.token}")
	private String SENDGRID_API_KEY;

	private Logger logger = LogManager.getLogger(MailUtils.class);
	
	private SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy");

	public void sendEmail(Date date, String to, String cc, Boolean monthly)
			throws EmailException, IOException {
		sendEmail(date, to, cc, monthly, false, false);
	}

	public void sendEmailResetPass(String to, String cc, String usuario, String pass)
			throws IOException, EmailException {

		String html = null;
		String txt = null;

		txt = generatePageNewPass(pass, TEMPLATE_NEWPASS_TXT);
		html = generatePageNewPass(pass, TEMPLATE_NEWPASS_HTML);
		logger.info("Reset da Senha para " + usuario);
		sendPass(html, txt, usuario, to);

	}

	public String generatePageNewPass(String pass, String tmplt) throws UnsupportedEncodingException {
		JtwigTemplate template = JtwigTemplate.classpathTemplate(tmplt);
		HashMap<String, Object> obs = new HashMap<String, Object>();
		obs.put("newPass", pass);
		JtwigModel model = JtwigModel.newModel(obs);
		return template.render(model);
	}

	public void sendEmail(Date date, String to, String cc, Boolean monthly, Boolean includeInfo, Boolean includeGS)
			throws EmailException, IOException {
		Calendar procDate = Calendar.getInstance();
		procDate.setTime(date);
		Calendar startDate = null;
		if (monthly) {
			startDate = dataUtils.getInicialDateMonthly(procDate);
			procDate.set(Calendar.HOUR, 23);
			procDate.set(Calendar.MINUTE, 59);
			procDate.set(Calendar.SECOND, 59);
		} else {
			startDate = dataUtils.getInicialDateDaily(procDate);
			procDate.set(Calendar.HOUR, 8);
		}
		List<Log> logs = null;
		
		//Enviar e-mail para GS.
		if(includeGS) {
			logs = rep.buscarPorMalhaDataOptionalMail(startDate, procDate);
			if(logs == null || logs.isEmpty()) {								
				return;
			}
		} else {
			if (includeInfo) {
				logs = rep.buscarPorMalhaData(startDate, procDate);
			} else {
				logs = rep.buscarPorMalhaDataParaReport(startDate, procDate);
			}
		}

		String html = null;
		String txt = null;
		if (monthly) {
			html = generateReport(logs, startDate, procDate, TEMPLATE_MONTHLY_HTML);
			txt = generateReport(logs, startDate, procDate, TEMPLATE_MONTHLY_TXT);
		} else if(includeGS) {
			html = generateReport(logs, startDate, procDate, TEMPLATE_GS_HTML);
			txt = generateReport(logs, startDate, procDate, TEMPLATE_GS_TXT);			
		} else {
			html = generateReport(logs, startDate, procDate, TEMPLATE_DAILY_HTML);
			txt = generateReport(logs, startDate, procDate, TEMPLATE_DAILY_TXT);
		}
		send(html, txt, startDate, procDate, monthly, to, cc, includeGS);
	}

	public String generateReport(List<Log> logs, Calendar startDate, Calendar endDate, String tmplt)
			throws UnsupportedEncodingException {
		JtwigTemplate template = JtwigTemplate.classpathTemplate(tmplt);
		HashMap<String, Object> obs = new HashMap<String, Object>();
		obs.put("dataInicio", formater.format(startDate.getTime()));
		obs.put("dataFim", formater.format(endDate.getTime()));
		obs.put("logs", logs);
		JtwigModel model = JtwigModel.newModel(obs);
		return template.render(model);
	}
	
	public void send(String html, String txt, Calendar startDate, Calendar endDate, Boolean monthly, String to,
			String cc, Boolean includeGS) throws EmailException, IOException {		
		
		Email from = new Email(conf.buscarPorNomeConfig(SENDER_CONF_NAME).getValorConfig());
		Personalization personalization = new Personalization();
	    Email para = new Email();
	    Email cc_final = new Email();
	    
	    Content content = new Content("text/html", html);	    	    

	    if (cc.contains(";")) {

			String[] allMails = cc.split(";");
			for (String mail : allMails) {
				cc_final.setEmail(mail);
				personalization.addCc(cc_final);
			}
		} else if (!cc.isEmpty()) {
			cc_final.setEmail(cc);
			personalization.addCc(cc_final);
		}
		if (to.contains(";")) {

			String[] allMails = to.split(";");
			for (String mail : allMails) {
				para.setEmail(mail);
				personalization.addTo(para);
			}
		} else if (!to.isEmpty()) {
			para.setEmail(to);
			personalization.addTo(para);
		}
	    
		String subj = null;
		if (!monthly) {
			if(includeGS){
				subj = String.format(MAIL_SUBJECT_GS, formater.format(startDate.getTime()),
						formater.format(endDate.getTime()));
			} else {
				subj = String.format(MAIL_SUBJECT_DAILY, formater.format(startDate.getTime()),
						formater.format(endDate.getTime()));
			}
		} else {
			subj = String.format(MAIL_SUBJECT_MONTHLY, formater.format(startDate.getTime()),
					formater.format(endDate.getTime()));
		}		
		System.out.println("testando envio cloud");		
		Mail mail = new Mail();		
		mail.addPersonalization(personalization);
		mail.addContent(content);
		mail.setSubject(subj);
		mail.setFrom(from);
		
	    SendGrid sg = new SendGrid(SENDGRID_API_KEY);
	    Request request = new Request();
				
	    try {
	        request.setMethod(Method.POST);
	        request.setEndpoint("mail/send");
	        request.setBody(mail.build());
	        Response response = sg.api(request);
	        System.out.println(response.getStatusCode());
	        System.out.println(response.getBody());
	        System.out.println(response.getHeaders());
	      } catch (IOException ex) {
	    	  throw ex;
	      }
	}

	public void sendPass(String html, String txt, String usuario, String to) throws EmailException, IOException {

		Email from = new Email(conf.buscarPorNomeConfig(SENDER_CONF_NAME).getValorConfig());
		Personalization personalization = new Personalization();
		Email para = new Email();

		Content content = new Content("text/html", html);

		if (to.contains(";")) {

			String[] allMails = to.split(";");
			for (String mail : allMails) {
				para.setEmail(mail);
				personalization.addTo(para);
			}
		} else if (!to.isEmpty()) {
			para.setEmail(to);
			personalization.addTo(para);
		}

		String subj = null;
		
		subj = String.format(MAIL_SUBJECT_PASS, usuario);

		Mail mail = new Mail();
		mail.addPersonalization(personalization);
		mail.addContent(content);
		mail.setSubject(subj);
		mail.setFrom(from);

		SendGrid sg = new SendGrid(SENDGRID_API_KEY);
		Request request = new Request();

		try {
			request.setMethod(Method.POST);
			request.setEndpoint("mail/send");
			request.setBody(mail.build());
			Response response = sg.api(request);
			System.out.println(response.getStatusCode());
			System.out.println(response.getBody());
			System.out.println(response.getHeaders());
		} catch (IOException ex) {
			throw ex;
		}
	}

	public void sendEmailPendency(String to) throws UnsupportedEncodingException, EmailException {		
		
		String html = JtwigTemplate.classpathTemplate(TEMPLATE_PENDENCY_HTML).render(JtwigModel.newModel());
		String txt = JtwigTemplate.classpathTemplate(TEMPLATE_PENDENCY_TXT	).render(JtwigModel.newModel());
		
		HtmlEmail email = new HtmlEmail();
		email.setHostName("ap.relay.ibm.com");
		
		email.setFrom(conf.buscarPorNomeConfig(SENDER_CONF_NAME).getValorConfig());
		email.addTo(to);
		
		String subj = null;
		subj = String.format(MAIL_SUBJECT_PENDENCY);
		
		email.setSubject(subj);
		email.setCharset("utf-8");

		email.setHtmlMsg(html);
		email.setTextMsg(txt);		

		email.send();
	}

	public void sendEmailPendencyManager(String to) throws UnsupportedEncodingException, EmailException, MessagingException {		
		
		String html = JtwigTemplate.classpathTemplate(TEMPLATE_PENDENCY_HTML).render(JtwigModel.newModel());
		String txt = JtwigTemplate.classpathTemplate(TEMPLATE_PENDENCY_TXT	).render(JtwigModel.newModel());
		String filePath = "C:/ExtracaoWorkHours/";
		String fileName = "ExtratoWorkHours.xls";
		
		HtmlEmail email = new HtmlEmail();
		email.setHostName("ap.relay.ibm.com");
		
		email.setFrom(conf.buscarPorNomeConfig(SENDER_CONF_NAME).getValorConfig());
		email.addTo(to);
		
		String subj = null;
		subj = String.format(MAIL_SUBJECT_PENDENCY);
		
		email.setSubject(subj);
		email.setCharset("utf-8");
		
		email.setHtmlMsg(html);
		email.setTextMsg(txt);		

		EmailAttachment attach = new EmailAttachment();
		attach.setPath(filePath + fileName);
		attach.setDisposition(EmailAttachment.ATTACHMENT);
		logger.info("Anexando relatório ao e-mail");
		
		email.attach(attach);
		
		email.send();
	}

	/**
	public void sendEmailBacklog(String to) throws UnsupportedEncodingException, EmailException {		
		
		String html = generateReportBacklog(TEMPLATE_BACKLOG_HTML);
		String txt = JtwigTemplate.classpathTemplate(TEMPLATE_BACKLOG_TXT).render(JtwigModel.newModel());
		
		HtmlEmail email = new HtmlEmail();
		email.setHostName("ap.relay.ibm.com");
		
		email.setFrom(conf.buscarPorNomeConfig(SENDER_CONF_NAME).getValorConfig());
		email.addTo(to);
		
		String subj = null;
		subj = String.format(MAIL_SUBJECT_BACKLOG);
		
		email.setSubject(subj);
		email.setCharset("utf-8");

		email.setHtmlMsg(html);
		email.setTextMsg(txt);		

		email.send();
	}

	public String generateReportBacklog(String tmplt)
			throws UnsupportedEncodingException {
		Date dataAtual = new Date(Calendar.getInstance().getTimeInMillis());
		JtwigTemplate template = JtwigTemplate.classpathTemplate(tmplt);
		HashMap<String, Object> obs = new HashMap<String, Object>();	
		//Put para o twig
		obs.put("paramBacklog", backlogService.getBacklogAtual(dataAtual));
		Integer valor = backlogService.getRelacaoBacklog(dataAtual);
		String proporcao = null;
		if(valor < 0) {
			proporcao = "Redução";
			valor = valor * (-1);
		}else {
			proporcao = "Aumento";
		}
		obs.put("paramProporcao2", proporcao);
		obs.put("paramRelacao30", valor);
		
		obs.put("paramIncidentesPorto", backlogService.getIncidentesPorto(dataAtual));
		obs.put("paramPercentPorto", ((backlogService.getIncidentesPorto(dataAtual) * 100)/backlogService.getBacklogAtual(dataAtual)));
		
		obs.put("paramIncidentesIbm", backlogService.getIncidentesIBM(dataAtual));
		obs.put("paramPercentIbm", ((backlogService.getIncidentesIBM(dataAtual) * 100)/backlogService.getBacklogAtual(dataAtual)));
		
		obs.put("paramAbertosDiaAnterior", backlogService.getAbertosDiaAnterior(dataAtual));
		obs.put("paramFechadosDiaAnterior", backlogService.getFechadosDiaAnterior(dataAtual));
		obs.put("paramAbertosHoje", backlogService.getAbertosAtual(dataAtual));
		obs.put("paramFechadosHoje", backlogService.getFechadosAtual(dataAtual));
		
		//Aging
		obs.put("paramAgingAtual", backlogService.getAgingAtual(dataAtual));
		valor = backlogService.getRelacaoAging(dataAtual);
		proporcao = null;
		if(valor < 0) {
			proporcao = "Redução";
			valor = valor * (-1);
		}else {
			proporcao = "Aumento";
		}
		obs.put("paramProporcao3", proporcao);
		obs.put("paramRelacaoAging", valor);
		obs.put("paramAging", backlogRepo.findByIncidentesAging(dataAtual, dataUtils.subtractDays(dataAtual, 30)));		
		
		//Slaviolado
		obs.put("paramSlaVioladoAtual", backlogService.getSlaVioladoAtual(dataAtual));
		valor = backlogService.getRelacaoSlaViolado(dataAtual);
		proporcao = null;
		if(valor < 0) {
			proporcao = "Redução";
			valor = valor * (-1);
		}else {
			proporcao = "Aumento";
		}
		obs.put("paramProporcao4", proporcao);
		obs.put("paramRelacaoSlaViolado", valor);
		obs.put("paramSlaViolado", backlogRepo.findByIncidentesSlaViolado(dataAtual));
		
		JtwigModel model = JtwigModel.newModel(obs);
		return template.render(model);
	}
	*/
	public String getCCList() {
		return conf.buscarPorNomeConfig(CC_MAIL_CONF_NAME).getValorConfig();
	}

	public String getToList() {
		String mailList = conf.buscarPorNomeConfig(TO_PORTO_1_CONF_NAME).getValorConfig();
		mailList = mailList.endsWith(";") == true ? mailList : mailList + ";";
		mailList = mailList + conf.buscarPorNomeConfig(TO_PORTO_2_CONF_NAME).getValorConfig();
		return mailList;
	}

	public String getInternalCCList() {
		return conf.buscarPorNomeConfig(CC_MAIL_INTER_CONF_NAME).getValorConfig();
	}

	public String getInternalToList() {
		String mailList = conf.buscarPorNomeConfig(TO_INTER_CONF_NAME).getValorConfig();
		mailList = mailList.endsWith(";") == true ? mailList : mailList + ";";
		return mailList;
	}
	
	public String getToGSList() {
		String mailList = conf.buscarPorNomeConfig(TO_GS_1_CONF_NAME).getValorConfig();
		mailList = mailList.endsWith(";") == true ? mailList : mailList + ";";
		mailList = mailList + conf.buscarPorNomeConfig(TO_GS_2_CONF_NAME).getValorConfig();
		return mailList;
	}
	
	public String getCCGSList() {
		return conf.buscarPorNomeConfig(CC_GS_1_CONF_NAME).getValorConfig();
	}
}
