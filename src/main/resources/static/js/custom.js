function save() {
	
	var horas = new Array();
	var ids = new Array();
	
	$("#save").prop('disabled',true);	
	
	$("input[id^=val]").each(function(){
		horas.push($(this).val());
		ids.push($(this).attr('id'));
	});
	
	var list = buildJson(horas,ids);
		
	$.ajax({
		type : "POST",
		contentType : "application/json",
		url : "/rest/hours/edit",
		data : JSON.stringify(list),
		success : function(result) {			
			console.log("OK");
			localStorage.setItem("functionSave", true); 
			location.reload();			
		},
		error : function(e) {
			alert("Erro ao inserir/atualizar horas");
			location.reload();
		}
	});
		
	$("input[id^=val" + value + "-]").prop('disabled', false);
	$("input[id$=-edit]").prop('disabled', false);
	$("input[id^=" + value + "-save]").prop('disabled', true);
}

function buildJson(horas,ids){
	var i;	
	var list = new Array();
	
	for(i = 0; i < horas.length; i++){
		var data = {
				id : ids[i],
				hour : horas[i]
		}
		list.push(data);
	}
	
	return list;
}

function addItem(){
	
	$("input[id$=-edit]").prop('disabled', true);
	$("input[id$=-fechar]").prop('disabled', true);
	$("#save").prop('disabled', true);
	$("#add").prop('disabled', true);
	$("#reopenBtn").prop('disabled', true);
	
	$("table").after(
			"<div id='newitem'>" +
			"<hr/>" +
			"<form class='form-inline'>" +
			"<div class='form-group mx-sm-1 mb-1'>" +
			"<input id='newItemChamado' type='text' value='0' class='form-control' required/>" +
			"</div>" +
			"<div class='form-group mx-sm-1 mb-1'>" +
			"<input id='newItemDesc' type='text' placeholder='Breve descrição' class='form-control' required>" +
			"</div>" +
			"<div class='form-group mx-sm-1 mb-1'>" +
			"<select class='form-control' id='newItemTipo' name='newItemTipo'>" +
			"<option value='0'>SOL</option>" +
			"<option value='1'>INC</option>" +
			"<option value='2'>PGP</option>" +
			"<option value='3'>KT</option>" +
			"<option value='4'>OUTRAS ATIVIDADES</option>" +
			"<option value='5'>RDM</option>" +
			"<option value='6'>REUNIAO</option>" +
			"<option value='7'>LICENSA</option>" +
			"<option value='8'>GESTÃO DE SOLICITAÇÃO</option>" +
			"<option value='9'>GESTÃO DE PGP</option>" +
			"<option value='10'>GESTÃO DE INCIDENTE</option>" +
			"<option value='11'>CHECK LIST DIARIO</option>" +
			"<option value='12'>TREINAR BTP</option>" +
			"<option value='13'>TREINAMENTO</option>" +
			"<option value='14'>PROJETOS IBM</option>" +
			"<option value='15'>REVIEW</option>" +
			"<option value='16'>CADENCIA</option>" +
			"<option value='17'>GESTÃO GERAL</option>" +
			"</select>" +
			"</div>" +
			"<div class='form-group mx-sm-1 mb-1'>" +
			"<select class='form-control' id='itemCategoria' name='itemCategoria'>" +
			"</select>" +
			"</div>" +
			"<div class='form-group mx-sm-1 mb-1'>" +
			"<input type='button' class='btn btn-primary' value='Inserir Chamado' id='addSave' onclick='addNewItem()'/>" +
			"</div>" +
			"<div class='form-group mx-sm-1 mb-1'>" +
			"<input type='button' class='btn btn-danger' value='Cancelar' id='addCancel' onclick='cancelNewItem()'/>" +
			"</div>" +
			"</form>"+
			"<hr/>" +
			"</div>"
			
	);
}

function cancelReopenItem(){
	$("#reopen").remove();
	$("input[id$=-edit]").prop('disabled', false);
	$("input[id$=-fechar]").prop('disabled', false);
	$("#add").prop('disabled', false);
	$("#save").prop('disabled', false);
	$("#reopenBtn").prop('disabled', false);
}

function reopenItem(){
	$("input[id$=-edit]").prop('disabled', true);
	$("input[id$=-fechar]").prop('disabled', true);
	$("#save").prop('disabled', true);
	$("#add").prop('disabled', true);
	$("#reopenBtn").prop('disabled', true);
	
	$("table").after(
			"<div id='reopen'>" +
			"<hr/>" +
			"<form class='form-inline'>" +
			"<div class='form-group mx-sm-1 mb-1'>" +
			"<input id='chamadoReopen' type='text' value='0' class='form-control'/>" +
			"</div>" +
			"<div class='form-group mx-sm-1 mb-1'>" +
			"<input type='button' class='btn btn-primary' value='Reabrir' id='reopen' onclick='reopenItemSave()'/>" +
			"</div>" +
			"<div class='form-group mx-sm-1 mb-1'>" +
			"<input type='button' class='btn btn-danger' value='Cancelar' id='reopenCancel' onclick='cancelReopenItem()'/>" +
			"</div>" +
			"</form>" +
			"<hr/>" +
			"</div>"
	);
}

function reopenItemSave(){
	
	$.post('/rest/hours/reopen', { chamado: $("#chamadoReopen").val()}, 
		    function(returnedData){
		         console.log(returnedData);
		         location.reload();
		}).fail(function(){
		      console.log("error");
		});
}

function cancelNewItem(){
	$("#newitem").remove();
	$("input[id$=-edit]").prop('disabled', false);
	$("input[id$=-fechar]").prop('disabled', false);
	$("#add").prop('disabled', false);
	$("#save").prop('disabled', false);
	$("#reopenBtn").prop('disabled', false);
}

function addNewItem(){
	
	var data = {
			chamado : $("#newItemChamado").val(),
			descricao : $("#newItemDesc").val(),
			tipo : $("#newItemTipo").val(),
			categoria : $("#itemCategoria").val(),
	}
	
	console.log(data);
	
	$.ajax({
		type : "POST",
		contentType : "application/json",
		url : "/rest/hours/add",
		data : JSON.stringify(data),
		success : function(result) {
			console.log("OK");
			location.reload();
		},
		error : function(e) {
			console.log("ERRO: ", e);
		}
	});
}

function enableEdit(){
	$("#chamado").prop('disabled', false);
	$("#tipo").prop('disabled', false);
	$("#categoria").prop('disabled', false);
	$("#descricao").prop('disabled', false);
	$("#salvar").prop('disabled', false);
	$("#editar").prop('disabled', true);
	$("#cancel").prop('disabled', false);
}

function desableEdit(){
	$("#chamado").prop('disabled', true);
	$("#tipo").prop('disabled', true);
	$("#categoria").prop('disabled', true);
	$("#descricao").prop('disabled', true);
	$("#salvar").prop('disabled', true);
	$("#editar").prop('disabled', false);
	$("#cancel").prop('disabled', true);
	location.reload();	
}

$(document).ready(function(){
	 if(localStorage.getItem("functionSave")){               
     	$(".alert-success").prop("hidden", false).fadeIn(500).delay(2000).fadeOut(700);
     	localStorage.removeItem("functionSave");
	 } 
	 
	 var url_atual = window.location.href;
	 console.log(url_atual);
});
