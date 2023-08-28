package br.com.ibm.cadeiabatch.ws;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import br.com.ibm.cadeiabatch.entity.Usuario;
import br.com.ibm.cadeiabatch.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import br.com.ibm.cadeiabatch.entity.Chamado;
import br.com.ibm.cadeiabatch.entity.Hours;
import br.com.ibm.cadeiabatch.enums.CategoriaEnum;
import br.com.ibm.cadeiabatch.enums.TipoChamadoEnum;
import br.com.ibm.cadeiabatch.service.BusinessService;

@RequestMapping("/rest/hours")
@Controller
public class HoursController {
	
	@Autowired
	private BusinessService service;

	@Autowired
	private UserService serv;
	
	@PostMapping("/edit")
	@ResponseStatus(code=HttpStatus.OK)
	public void editItem(@RequestBody List<Hours> hours) {
		service.editHours(hours);
	}
	
	@RequestMapping(value = "/add",method = RequestMethod.POST)
	//@ResponseStatus(code=HttpStatus.OK)
	public String addItem(HttpServletRequest request) {

		Usuario usuarioLogado = serv.getUsuarioLogado();

		Chamado chamado = new Chamado();
		chamado.setNumero(Long.parseLong(request.getParameter("chamado").toString().replaceAll(" ", "")));
		chamado.setDescricao(request.getParameter("descricao").toString());		
		if(usuarioLogado.getEmpresaLogada()!=2)		
			TipoChamadoEnum tipoEnum = TipoChamadoEnum.values()[Integer.parseInt(request.getParameter("newItemTipo").toString())];

		if(usuarioLogado.getEmpresaLogada()==2)		
			TipoChamadoEnum tipoEnum = TipoChamadoEnum.values()[Integer.parseInt(request.getParameter("newItemTipo2").toString())];

		CategoriaEnum categoriaEnum = null;
		if(usuarioLogado.getEmpresaLogada()==0)
			categoriaEnum = CategoriaEnum.values()[Integer.parseInt(request.getParameter("categoria1").toString())];

		if(usuarioLogado.getEmpresaLogada()==1)
			categoriaEnum = CategoriaEnum.values()[Integer.parseInt(request.getParameter("categoria2").toString())];
		if(usuarioLogado.getEmpresaLogada()==2)
			categoriaEnum = CategoriaEnum.values()[Integer.parseInt(request.getParameter("categoria3").toString())];

		service.incluirNovoIncidente(chamado.getNumero(),
				chamado.getDescricao(), tipoEnum, categoriaEnum);
		
		return "redirect:/hours";
		
	}
	
	@PostMapping("/reopen")
	@ResponseStatus(code=HttpStatus.OK)
	public void reopen(@RequestParam("chamado") String chamado) {
		service.reopenChamado(Long.parseLong(chamado));
	}
	
}