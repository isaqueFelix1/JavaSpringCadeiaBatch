package br.com.ibm.cadeiabatch.ws;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import br.com.ibm.cadeiabatch.entity.Hours;
import org.apache.commons.mail.EmailException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import br.com.ibm.cadeiabatch.entity.Usuario;
import br.com.ibm.cadeiabatch.repository.UserRepository;
import br.com.ibm.cadeiabatch.utilities.MailUtils;

@Controller
public class ServiceCustom {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private MailUtils mailUtils;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	private Logger logger = LogManager.getLogger(ServiceCustom.class);

	@RequestMapping(value = "/resetPass", method = RequestMethod.POST)
	public String recPass(@RequestBody String usuario) {

		if(usuario.isEmpty()) {
			logger.info("Campo Vazio!");
			return null;
		}

		logger.info("Buscando user: " + usuario);
		Usuario userObj = userRepository.buscarPorNome(usuario);
		Random random = new Random();
		String password = usuario + random.nextInt(20000);
		logger.info("Reset de Senha");
		userObj.setSenha(bCryptPasswordEncoder.encode(password));

		try {
			userRepository.save(userObj);
			mailUtils.sendEmailResetPass(userObj.getEmail(), "", userObj.getUsuario(), password);
		} catch (EmailException | IOException e) {
			e.printStackTrace();
		}

		return "redirect:home";
	}

}
