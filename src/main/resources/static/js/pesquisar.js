$(document).ready(function () {
	$("#data").datepicker({
		dateFormat: "dd-mm-yy"
	});
	$("#data-inicial").datepicker({
		dateFormat: "dd-mm-yy"
	});
	$("#data-final").datepicker({
		dateFormat: "dd-mm-yy"
	});
	$("#data-inicial-graph").datepicker({
		dateFormat: "dd-mm-yy"
	});
	$("#data-final-graph").datepicker({
		dateFormat: "dd-mm-yy"
	});

	$("#div-tabela").hide();
	$("#periodo").hide();
	$("#malha").hide();
	$("#grafico").hide();

	$("#pesquisa-log-periodo").click(function () {
		//LIMPA A TABELA DE PESQUISA
		$("#tabela-pesquisa").text("");

		//PEGA A DATA INICIAL E FINAL EM MILISEGUNDOS
		var d = $("#data-inicial").val();
		var dateParts = new Date((Number(d.split("-")[2])), (Number(d.split("-")[1]) - 1), (Number(d.split("-")[0])));
		var dataInicial = dateParts.getTime();

		d = $("#data-final").val();
		dateParts = new Date((Number(d.split("-")[2])), (Number(d.split("-")[1]) - 1), (Number(d.split("-")[0])));
		var dataFinal = dateParts.getTime();

		//ENVIA A REQUICAO AO SERVICO E PREENCHE A TABELA COM A RESPOSTA
		$.get("/logs/pesquisa/periodo/dataInicial/" + dataInicial + "/dataFinal/" + dataFinal, function (data) {

			$.each(data, function (i, logout) {
				$("#tabela-pesquisa").append(
					'<p class="media-body pb-3 mb-0 small lh-125 border-bottom border-gray">' +
					'<br />' +
					'<strong class="d-block text-gray-dark">' + logout.dataHoraCriacaoFormatado + '</strong>' +
					'<div class="row">' +
					'<div class="col-md-2">' +
					'Usuário:' +
					'<strong class="d-block text-gray-dark">' + logout.usuario + '</strong>' +
					'</div>' +
					'<div class="col-md-2">' +
					'Frente:' +
					'<strong class="d-block text-gray-dark">' + logout.frente + '</strong>' +
					'</div>' +
					'<div class="col-md-1">' +
					'Incidente:' +
					'<strong class="d-block text-gray-dark">' + logout.incidente + '</strong>' +
					'</div>' +
					'<div class="col-md-1">' +
					'Nível:' +
					'<strong class="d-block text-gray-dark">' + logout.nivel + '</strong>' +
					'</div>' +
					'<div class="col-md-5">' +
					'Job:' +
					'<strong class="d-block text-gray-dark">' + logout.job + '</strong>' +
					'</div>' +
					'</div>' +
					'<div class="row">' +
					'<div class="col-md-10">' +
					'Descrição:' + '<br/>' +
					'<strong class="d-block text-gray-dark">' + logout.descricao + '</strong>' +
					'</div>' +
					'<div class="col-md-2">' +
					'<a href="/editar/' + logout.id + '" class="btn btn-primary" style="float:right" role="buton" >Editar</a>' +
					'</div>' +
					'</div>' +
					'<br />' +
					'</p>'
				);
			})

		})

		$("#div-tabela").show();
		var target_offset = $("#div-tabela").offset();
		var target_top = target_offset.top;
		$('html, body').animate({
			scrollTop: target_top
		}, 400);

		//NÃO REDIRECIONAR
		return false;
	});
	
	$("#extrair-log-periodo").click(function () {
		
		//PEGA A DATA INICIAL E FINAL EM MILISEGUNDOS
		var d = $("#data-inicial").val();
		var dateParts = new Date((Number(d.split("-")[2])), (Number(d.split("-")[1]) - 1), (Number(d.split("-")[0])));
		var dataInicial = dateParts.getTime();

		d = $("#data-final").val();
		dateParts = new Date((Number(d.split("-")[2])), (Number(d.split("-")[1]) - 1), (Number(d.split("-")[0])));
		var dataFinal = dateParts.getTime();

		//ENVIA A REQUICAO AO SERVICO
		$.get("/logs/extrair/periodo/dataInicial/" + dataInicial + "/dataFinal/" + dataFinal, function (data) {

		})

		//NÃO REDIRECIONAR
		return false;
	});
	
	$("#gerar-relatorio").click(function () {

		$.post("/gerargrafico", $("#grafico").serialize(), function (data) {
			var datatable = [];
			datatable.push(['Data', 'Info', 'Warn', 'Erro', 'Total']);
			if (data.length == 0) {
				var curreg = [];
				curreg.push("0");
				curreg.push(0);
				curreg.push(0);
				curreg.push(0);
				curreg.push(0)
				datatable.push(curreg);
			} else {
				data.forEach(element => {
					var curreg = [];
					curreg.push(element["data"]);
					curreg.push(element["info"]);
					curreg.push(element["warn"]);
					curreg.push(element["erro"]);
					curreg.push(element["total"])
					datatable.push(curreg);
				});
			}

			var data = google.visualization.arrayToDataTable(datatable);


			// Set chart options
			var options = {
				title: 'Grafico de registros',
				vAxis: { title: 'Registros' },
				hAxis: { title: 'Data' },
				seriesType: 'bars',
				width: 1050,
				colors: ['#000099', '#ffcc00', '#cc0000', 'green'],
				height: 500,
				series: { 3: { type: 'line' } }
			};

			// Instantiate and draw our chart, passing in some options.
			var chart = new google.visualization.ComboChart(document.getElementById('chart_div'));
			chart.draw(data, options);

			$("#div-grafico").show();
			var target_offset = $("#div-grafico").offset();
			var target_top = target_offset.top;
			$('html, body').animate({
				scrollTop: target_top
			}, 400);
		});

		return false;
	});

	$("#pesquisa-log-malha").click(function () {
		//LIMPA A TABELA DE PESQUISA
		$("#tabela-pesquisa").text("");

		//PEGA A DATA EM MILISEGUNDOS
		var d = $("#data").val();
		var dateParts = new Date((Number(d.split("-")[2])), (Number(d.split("-")[1]) - 1), (Number(d.split("-")[0])));
		var data = dateParts.getTime();

		var incidente = 0;
		incidente = $("#incidente").val();
		var nivel = $("#nivel").val();

		if ($("#incidente").val() == "") {
			incidente = 0;
		}
		if ($("#data").val() == "") {
			data = 0;
		}


		$.get("/logs/pesquisa/periodo/data/" + data + "/incidente/" + incidente + "/nivel/" + nivel, function (data) {

			$.each(data, function (i, logout) {
				console.log(logout);
				$("#tabela-pesquisa").append(
					'<p class="media-body pb-3 mb-0 small lh-125 border-bottom border-gray">' +
					'<br />' +
					'<strong class="d-block text-gray-dark">' + logout.dataHoraCriacaoFormatado + '</strong>' +
					'<div class="row">' +
					'<div class="col-md-2">' +
					'Usuário:' +
					'<strong class="d-block text-gray-dark">' + logout.usuario + '</strong>' +
					'</div>' +
					'<div class="col-md-2">' +
					'Frente:' +
					'<strong class="d-block text-gray-dark">' + logout.frente + '</strong>' +
					'</div>' +
					'<div class="col-md-1">' +
					'Incidente:' +
					'<strong class="d-block text-gray-dark">' + logout.incidente + '</strong>' +
					'</div>' +
					'<div class="col-md-1">' +
					'Nível:' +
					'<strong class="d-block text-gray-dark">' + logout.nivel + '</strong>' +
					'</div>' +
					'<div class="col-md-5">' +
					'Job:' +
					'<strong class="d-block text-gray-dark">' + logout.job + '</strong>' +
					'</div>' +
					'</div>' +
					'<div class="row">' +
					'<div class="col-md-10">' +
					'Descrição:' + '<br/>' +
					'<strong class="d-block text-gray-dark">' + logout.descricao + '</strong>' +
					'</div>' +
					'<div class="col-md-2">' +
					'<a href="/editar/' + logout.id + '" class="btn btn-primary" style="float:right" role="buton" >Editar</a>' +
					'</div>' +
					'</div>' +
					'<br />' +
					'</p>'
				);
			})

		})

		$("#div-tabela").show();
		var target_offset = $("#div-tabela").offset();
		var target_top = target_offset.top;
		$('html, body').animate({
			scrollTop: target_top
		}, 400);

		//NÃO REDIRECIONAR
		return false;
	});

	$("#btnMalha").click(function () {
		$("#btnMalha").addClass('active');
		$("#btnPeriodo").removeClass('active');
		$("#btnGrafico").removeClass('active');

		$("#periodo").hide();
		$("#grafico").hide();
		$("#malha").show();

		$("#div-tabela").hide();
		$("#div-grafico").hide();

		//NÃO REDIRECIONAR
		return false;
	});

	$("#btnPeriodo").click(function () {
		$("#btnPeriodo").addClass('active');
		$("#btnMalha").removeClass('active');
		$("#btnGrafico").removeClass('active');

		$("#malha").hide();
		$("#grafico").hide();
		$("#periodo").show();

		$("#div-tabela").hide();
		$("#div-grafico").hide();

		//NÃO REDIRECIONAR
		return false;
	});

	$("#btnGrafico").click(function () {
		$("#btnPeriodo").removeClass('active');
		$("#btnMalha").removeClass('active');
		$("#btnGrafico").addClass('active');

		$("#malha").hide();
		$("#grafico").show();
		$("#periodo").hide();

		$("#div-tabela").hide();
		$("#div-grafico").hide();

		//NÃO REDIRECIONAR
		return false;
	});

});