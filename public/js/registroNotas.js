//Funcion para limpiar el dropdown
function LimpiarSelect(idControl) {
	var mySelect = document.getElementById(idControl);
	mySelect.options.length = 0;
}
// En el evento OnChange capturamos el value de la opción seleccionada
$(document).on(
		'change',
		'#grupos',
		function() {
			// limpiamos el dropdown de resultados
			LimpiarSelect('examenes');
			// usamos el enrutador para acceder al método examenesGrupo y le
			// pasamos el valor seleccionado
			jsRoutes.controllers.RegistroNotas.examenesGrupo($(this).val())
					.ajax(myAjax);
		});
// Si la función AJAX devuelve la data correcta poblamos el dropdown
var successFn = function(data) {
	$("#examenes").html(data);
	console.debug("Success:");
	console.debug(data);
};
// Si hay error mostramos el error en consola
var errorFn = function(err) {
	console.debug("Error:");
	console.debug(err);
}
myAjax = {
	success : successFn,
	error : errorFn
}