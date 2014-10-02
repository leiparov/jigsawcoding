(function(){
	
	var fecha = $('input[name="fecha"]');
	var tiempoApertura = $('input[name="tiempoApertura"]');
	var tiempoClausura = $('input[name="tiempoClausura"]');
	
	function presente(campo){
		return campo.val() != undefined &&
				campo.val() != null &&
				campo.val().length != 0;
	}
	
	function algunoDeLosCamposVacio(){
		return !presente(fecha) || !presente(tiempoApertura) || !presente(tiempoClausura);
	}
	function todosLosCamposVacios(){
		return !presente(fecha) && !presente(tiempoApertura) && !presente(tiempoClausura);
	}
	
	fecha.on('change', function() {
		if(fecha.val().length == 0){
			tiempoApertura.val('');
			tiempoClausura.val('');
		}
	})
	
	$("#mainForm").on('submit', function() {
		if(algunoDeLosCamposVacio() && !todosLosCamposVacios()){
			//TODO: Custom HTML5 validation?
			alert("Para establecer una fecha, completar los 3 campos de fecha y rangos de inicio\n" +
					"Para indicar fecha indefinida, borre los 3 campos");
			return false;
		}
	});
})();