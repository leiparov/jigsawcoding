@(firepadid: String, userid: String)

$(function(){
	
	var firepad;
	var botonRun = $('#boton-run');
	var botonVer = $('#boton-ver');
	
	function init() {
		// Initialize Firebase.
		// var firepadRef = getExampleRef();
		// TODO: Replace above line with:
		url = 'https://vivid-heat-5073.firebaseio.com/firepads/jc_' + '@firepadid';
		console.log(url);
		var firepadRef = new Firebase(url);

		// // Create CodeMirror (with line numbers and the Java mode).
		var codeMirror = CodeMirror(document.getElementById('firepad'), {
			lineNumbers : true,
			lineWrapping : true,
			mode : 'text/x-c++src'
		});

		// // Create Firepad.
		firepad = Firepad.fromCodeMirror(firepadRef, codeMirror, {
			defaultText : 'Hello, World!'
		});
		firepad.setUserId('@userid');
	}

	// Helper to get hash from end of URL or generate a random one.
	function getExampleRef() {
		var ref = new Firebase('https://firepad.firebaseio-demo.com');
		var hash = window.location.hash.replace(/#/g, '');
		if (hash) {
			ref = ref.child(hash);
		} else {
			ref = ref.push(); // generate unique location.
			window.location = window.location + '#' + ref.name(); // add it as
																	// a
																	// hash to
																	// the
																	// URL.
		}
		if (typeof console !== 'undefined')
			console.log('Firebase data: ', ref.toString());
		return ref;
	}
	init();
	
	/*Ejecutar codigo fuente*/
	function mostrarIdeoneSubmissionResults(data){
		console.log(data);
		console.log(data['status']);
		
		
		var myModal = $('#modalResultados');
		
		var input = $('#input');
		var output = $('#output');
		var result = $('#result');
		var time = $('#time');
		var memory = $('#memory');
		var link = $('#link');
		
		
		input.text(data['input']);
		output.text(data['output']);
		result.text(data['result']);
		time.text(data['time']);
		memory.text(data['memory'] + 'Kb');
		link.val(data['link']);
		
		myModal.modal('show');
	}
	
	function problemaRun (){
		var firepadText = firepad.getText();
		var inputStdinText = $('#input-stdin').val();
		console.log(firepadText);
		console.log(inputStdinText);
		var call = jsRoutes.controllers.ProblemaController.problemaRunJs(firepadText, inputStdinText);
		$.ajax({
			url: call.url,
			type: call.type,
			success: mostrarIdeoneSubmissionResults
		});
		
	}
	function verResultadosProblemaRun (){
		var link = $('#link');
		//console.log(link.val());
		if(link.val() != null){
			//console.log(link);
			var call = jsRoutes.controllers.ProblemaController.verResultadosProblemaRunJs(link.val());
			$.ajax({
				url: call.url,
				type: call.type,
				success: mostrarIdeoneSubmissionResults
			});
		}
		else{
			altert("Ejecute un problema");
		}
		
		
	}
	
	botonRun.on('click', function(e){
		e.preventDefault();
		var $btn = $(this).button('loading');
		problemaRun();
		$btn.button('reset');
		botonVer.popover('destroy');
	});
	
	botonVer.on('click', function(e){
		e.preventDefault();
		var linkText = $('#link').val();
		if(linkText == ""){
			console.log('Nada');
			//alert('Para ver Resultados debe primero Ejecutar un Programa presionando el botón Run');
			var options = {
					trigger: 'focus',
					title: 'Mensaje',
					content: 'Para ver resultados debe ejecutar primero un programa presionando el botón Run',
					placement: 'top'
			};
			$(this).popover(options);
			$(this).popover('show');
		}else{
			verResultadosProblemaRun();
		}
				
	});
})

