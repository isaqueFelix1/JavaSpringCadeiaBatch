$(document).ready(function() {

	$("#myForm").submit(function(event) {
		event.preventDefault();
		ajaxPost();
	});

	function ajaxPost() {
		
		console.log();
		
		var formData = {
			usuario : null,
			incidente : $("#incidente").val(),
			nivel : $("#nivel").val(),
			job : $("#job").val(),
			descricao : $("#descricao").val(),
			tipo: $('input[name=tipo]:checked', '#myForm').val()
		}
		
		console.log(formData);

		$.ajax({
			type : "POST",
			contentType : "application/json",
			url : "/logs/salvar",
			data : JSON.stringify(formData),
			success : function(result) {
				$( "div.success" ).fadeIn( 300 ).delay( 2000 ).fadeOut( 400 );
				resetData();
			},
			error : function(e) {
				console.log("ERRO: ", e);
				$( "div.failure" ).fadeIn( 300 ).delay( 1500 ).fadeOut( 400 );
			}
		});

	}

	function resetData() {
		$("#descricao").val("");
	}
})