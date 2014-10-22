(function() {

	var inputBuscar = $('#input-buscar');
	var botonBuscar = $('#boton-buscar');
	var botonBuscarAll = $('#boton-buscar-all');

	var dualListbox = $('[name="alumnos[]"]');
	//var dualListbox = $('.demo');

	function avisar(mensaje) {
		inputBuscar.attr('data-content', mensaje);
		inputBuscar.popover('show');
	}

	function filtrarAlumnosPresentes(lista) {
		var hijos = dualListbox.children(':selected');
		return lista.filter(function(nuevo) {
			return !hijos.is(function() {
				return $(this).val() == nuevo.dni;
			});
		});
	}

	function mostrarResultadosBusqueda(data) {
		if (data.length == 0)
			return avisar("La búsqueda no retornó resultados");

		var dataMenosPresentes = filtrarAlumnosPresentes(data);
		if (dataMenosPresentes.length == 0)
			return avisar("La búsqueda no retornó nuevos resultados");

		dualListbox.children(':not(:selected)').remove();
		for (i in dataMenosPresentes) {
			dualListbox.append($("<option></option>").val(data[i].dni).text(
					data[i].nombreCompleto));
		}
		dualListbox.trigger('bootstrapduallistbox.refresh');
	}

	function obtenerResultados() {
		if (inputBuscar.val().length == 0) {
			return avisar("Escriba para buscar");
		}
		var call = jsRoutes.controllers.AlumnoController.buscarAlumnos(inputBuscar.val());
		$.ajax({
			url : call.url,
			type : call.type,
			success : mostrarResultadosBusqueda
		});
	}
	
	function disponibles() {
		var call2 = jsRoutes.controllers.AlumnoController.disponibles();
		$.ajax({
			url : call2.url,
			type : call2.type,
			success : mostrarResultadosBusqueda
		});
	}

	(function() {
		var lastTimeout;

		inputBuscar.popover({
			placement : 'top',
			trigger : 'manual',
		});

		inputBuscar.on('shown.bs.popover', function() {
			clearTimeout(lastTimeout);
			lastTimeout = setTimeout(function() {
				inputBuscar.popover('hide');
			}, 1500);
		});
	})();

	botonBuscar.on('click', function(e) {
		e.preventDefault();
		obtenerResultados();
	});
	
	botonBuscarAll.on('click', function(e) {
		e.preventDefault();
		disponibles();
	});
	
	
	inputBuscar.on('keypress', function(e) {
		if (e.which == 13) {
			e.preventDefault();
			obtenerResultados();
		}
	});

})();
