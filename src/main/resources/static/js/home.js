$(document).ready(

	$.get("logs/recentes", function (data) {
		$.each(data, function (i, logout) {
			$("#execRecentes").append(

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
				'Chamado:' +
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
				// PRÓXIMAS ENTREGAS - 
				//'<div class="text-right">' +
				//    '<a href="#">Ver detalhes</a>'+
				//'</div>'+
				'</p>'

			);
		})
		/*
		 * 
		 * //console.log(data.descricao);
		 * 
		 * $.each(data, function(i, logs) {
		 * 
		 * 
		 * 
		 * <p> class="media-body pb-3 mb-0 small lh-125 border-bottom border-gray">
		 * <br /> <strong class="d-block text-gray-dark">10/05/2018</strong> <div
		 * class="row"> <div class="col"> Total de Logs: <strong class="d-block
		 * text-gray-dark">23</strong> </div> <div class="col"> Quantidade de Info:
		 * <strong class="d-block text-gray-dark">10</strong> </div> <div
		 * class="col"> Quantidade de Warnings: <strong class="d-block
		 * text-gray-dark">5</strong> </div> <div class="col"> Quantidade de Error:
		 * <strong class="d-block text-gray-dark">5</strong> </div> </div>
		 * 
		 * <br /> <div class="text-right"> <a href="#">Ver detalhes</a> </div>
		 * 
		 * </p>
		 *  })
		 */

	})

	/*
	 * $.ajax({ type : "GET", dataType : "json", url :
	 * "http://localhost:8888/logs/mostrar", success : function(data) {
	 * console.log(data.descricao); } });
	 */
	
	$.get("/nivelUsuario", function(data){
		console.log(data);
	})

);