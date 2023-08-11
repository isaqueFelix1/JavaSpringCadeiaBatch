$(document).ready(function() {
	$("#data").datepicker({
	    dateFormat:"dd-mm-yy"
	});

	$("#enviar-relatorio").click(function() {
		
		//ENVIA A REQUICAO AO SERVICO E PREENCHE A TABELA COM A RESPOSTA
		$.post("/relatoriomanual", $("#send-form").serialize(), function(data) {
			if(data === "done"){
				$( "div.success" ).fadeIn( 300 ).delay( 2000 ).fadeOut( 400 );
			}else{
				$( "div.failure" ).html(data);
				$( "div.failure" ).fadeIn( 300 ).delay( 2000 ).fadeOut( 400 );
			}
		})
		//NÃO REDIRECIONAR
		return false;
	});
});