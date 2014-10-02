$(function(){
	$("#tipoPregunta").change(function(){
		var opcion = $("#tipoPregunta option:selected").val();
		if (opcion==1) {
			$(".preguntaverdaderofalso").css("display","block");
			$(".preguntaingresetexto").css("display","none");
			$(".preguntaopcionmultiple").css("display","none");
			$(".preguntacompletar").css("display","none");			
			$(".preguntaemparejamiento").css("display","none");
			
			console.log(opcion);
		}
		else if(opcion==2){
			$(".preguntaverdaderofalso").css("display","none");
			$(".preguntaingresetexto").css("display","block");
			$(".preguntaopcionmultiple").css("display","none");
			$(".preguntacompletar").css("display","none");			
			$(".preguntaemparejamiento").css("display","none");
			console.log(opcion);
		}
		else if(opcion==3){
			$(".preguntaverdaderofalso").css("display","none");
			$(".preguntaingresetexto").css("display","none");
			$(".preguntaopcionmultiple").css("display","block");
			$(".preguntacompletar").css("display","none");			
			$(".preguntaemparejamiento").css("display","none");
			console.log(opcion);
		}
		else if(opcion==4){
			$(".preguntaverdaderofalso").css("display","none");
			$(".preguntaingresetexto").css("display","none");
			$(".preguntaopcionmultiple").css("display","none");
			$(".preguntacompletar").css("display","block");			
			$(".preguntaemparejamiento").css("display","none");
			console.log(opcion);
		}else if(opcion==5){
			$(".preguntaverdaderofalso").css("display","none");
			$(".preguntaingresetexto").css("display","none");
			$(".preguntaopcionmultiple").css("display","none");
			$(".preguntacompletar").css("display","none");			
			$(".preguntaemparejamiento").css("display","block");
			console.log(opcion);
		}else{
			$(".preguntaverdaderofalso").css("display","none");
			$(".preguntaingresetexto").css("display","none");
			$(".preguntaopcionmultiple").css("display","none");
			$(".preguntacompletar").css("display","none");			
			$(".preguntaemparejamiento").css("display","none");
		}

	});
	
	$("#enunciadoParaCompletar").select(function(){		
		var textoSeleccionado = document.getSelection()+"";
		console.log(textoSeleccionado);
		$("#enunciadoOculto").val(textoSeleccionado);
	});
	
});



