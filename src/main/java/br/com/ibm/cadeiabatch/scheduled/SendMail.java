package br.com.ibm.cadeiabatch.scheduled;

import java.util.Calendar;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import br.com.ibm.cadeiabatch.repository.UserRepository;
import br.com.ibm.cadeiabatch.service.BusinessService;
import br.com.ibm.cadeiabatch.utilities.DataUtils;
import br.com.ibm.cadeiabatch.utilities.MailUtils;

@Component
public class SendMail {

    @Autowired
    private DataUtils dataUtils;

    @Autowired
    private MailUtils mailUtils;
    
    @Autowired
    private BusinessService service;

    @Autowired
    private UserRepository userRepo;

    private Logger logger = LogManager.getLogger(SendMail.class);

    @Scheduled(cron = "0 00 11 * * *")
    public void sendMailReport() throws Exception {
        try {
            Calendar curDate = Calendar.getInstance();
            if (!dataUtils.isWorkDay(curDate)) {
                return;
            }           
            mailUtils.sendEmail(curDate.getTime(), mailUtils.getToList(), mailUtils.getCCList(), false);
            mailUtils.sendEmail(curDate.getTime(), mailUtils.getInternalToList(), mailUtils.getInternalCCList(), false, true, false);
            //mailUtils.sendEmail(curDate.getTime(), mailUtils.getToGSList(), mailUtils.getCCGSList(), false, false, true);                                              
            
            Calendar monthEnd = dataUtils.GetMonthEndBetween(dataUtils.getInicialDateDaily(curDate), curDate);
            if (monthEnd != null) {
                mailUtils.sendEmail(monthEnd.getTime(), mailUtils.getToList(), mailUtils.getCCList(), true);
            }
                      
            //E-mail de pendencia
            /**
            try {            	
	            if(curDate.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY || curDate.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
	            	Calendar finalDate = dataUtils.GetLastWorksDay(curDate, -1);	            	
	            	Calendar startDate = dataUtils.GetLastWorksDay(finalDate, -1);		            	
	            	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");	   
	            	
	            	for (Usuario usuario : userRepo.listarUsuariosPendentes(sdf.format(startDate.getTime()), sdf.format(finalDate.getTime()))) {
	            		if(usuario.getEmail() != null) {
	            			mailUtils.sendEmailPendency(usuario.getEmail());
	            		}
					}
	            } 

	            if(curDate.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY || curDate.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY || curDate.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY) {
	            	//Email de pendencia gerencial..
	            	List<Calendar> semanaPassada = dataUtils.getDaysOfLastWeek();
	            	service.extratorWorkHours(null, null, semanaPassada.get(0).getTimeInMillis(), semanaPassada.get(6).getTimeInMillis(), 
	            			"todos", "todos", null, false, true);	            	
	            	mailUtils.sendEmailPendencyManager("jeckc@br.ibm.com");
	            }
	            
            }catch(Exception e) {
            	e.printStackTrace();
            }*/
            
        } catch (Exception e) {
            logger.error("Erro inesperado na geração automatica de e-mail", e);
        }
    }
   
}
