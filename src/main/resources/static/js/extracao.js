$(document).ready(function () {
	$("#data-inicial").datepicker({
		dateFormat: "dd-mm-yy"
	});
	$("#data-final").datepicker({
		dateFormat: "dd-mm-yy"
	});
	
	$("#gera-extrato-periodo").click(function () {
		
		//PEGA A DATA INICIAL E FINAL EM MILISEGUNDOS
		var d = $("#data-inicial").val();
		var dateParts = new Date((Number(d.split("-")[2])), (Number(d.split("-")[1]) - 1), (Number(d.split("-")[0])));
		var dataInicial = dateParts.getTime();

		d = $("#data-final").val();
		dateParts = new Date((Number(d.split("-")[2])), (Number(d.split("-")[1]) - 1), (Number(d.split("-")[0])));
		var dataFinal = dateParts.getTime();

		//ENVIA A REQUICAO AO SERVICO
		$.get("/logs/pesquisa/periodo/dataInicial/" + dataInicial + "/dataFinal/" + dataFinal, function (data) {

		})

		//NÃO REDIRECIONAR
		return false;
	});
	
	
	$("#btnPeriodo").click(function () {
		$("#btnPeriodo").addClass('active');
		//NÃO REDIRECIONAR
		return false;
	});

});