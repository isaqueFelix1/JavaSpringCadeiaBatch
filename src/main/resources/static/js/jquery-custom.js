		$(function () {
			$("#data-inicial").datepicker();
        });

        $(function () {
            $("#data-final").datepicker();
        });
		
		$(function () {
            $("#data").datepicker();
        });
		
		

        $(document).ready(function () {

			$("#div-tabela").hide();
            $("#periodo").hide();
            $("#malha").hide();
			
			$("#pesquisa-log-periodo").click(
                function () {
                    $("#div-tabela").show();
					var target_offset = $("#div-tabela").offset();
					var target_top = target_offset.top;
					$('html, body').animate({ scrollTop: target_top }, 400);
                }
            );
			
			$("#pesquisa-log-malha").click(
                function () {
                    $("#div-tabela").show();
					var target_offset = $("#div-tabela").offset();
					var target_top = target_offset.top;
					$('html, body').animate({ scrollTop: target_top }, 400);
                }
            );

            $("#btnMalha").click(
                function () {
                    $("#btnMalha").addClass('active');
                    $("#btnPeriodo").removeClass('active');

                    $("#periodo").hide();
                    $("#malha").show();
					
					$("#div-tabela").hide();
                }
            );

            $("#btnPeriodo").click(
                function () {
                    $("#btnPeriodo").addClass('active');
                    $("#btnMalha").removeClass('active');

                    $("#malha").hide();
                    $("#periodo").show();
					
					$("#div-tabela").hide();
                }
            );
			
        });