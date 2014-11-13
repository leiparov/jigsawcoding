@(firepadid: String, userid: String)

$(function(){
	
	var firepad;
	var botonRun = $('#boton-run');
	var botonVer = $('#boton-ver');
	var contenedorResultados = $('#ideoneResultados');
	
	function init() {
		// Initialize Firebase.
		// var firepadRef = getExampleRef();
		// TODO: Replace above line with:
		url = 'https://vivid-heat-5073.firebaseio.com/firepads/jc_' + '@firepadid';
		console.log(url);
		var firepadRef = new Firebase(url);
		console.log(firepadRef);
		// // Create CodeMirror (with line numbers and the Java mode).
		var codeMirror = CodeMirror(document.getElementById('firepad'), {
			lineNumbers : true,
			lineWrapping : true,
			mode : 'text/x-c++src'
		});

		// // Create Firepad.
		firepad = Firepad.fromCodeMirror(firepadRef, codeMirror, {
			defaultText : '#include <iostream> using namespace std;	int main() { // your code goes here	return 0;}'
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
	
	/* Ejecutar codigo fuente */
	function mostrarIdeoneSubmissionResults(data){
		console.log(data);
		console.log(data['status']);
		
		contenedorResultados.empty();
		for(i in data){
			var elemento = $.parseHTML(data[i]);
			contenedorResultados.append(elemento);
		}
		
		var myModal = $('#modalResultados');		
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
		// console.log(link.val());
		if(link.val() != null){
			// console.log(link);
			var call = jsRoutes.controllers.ProblemaController.verResultadosProblemaRunJs(link.val());
			$.ajax({
				url: call.url,
				type: call.type,
				success: mostrarIdeoneSubmissionResults
			});
		}
		else{
			avisar("Ejecute un problema");
		}
		
		
	}
	
	botonRun.on('click', function(e){
		e.preventDefault();
		$('#divajaxloader').show();
		var $btn = $(this).button('loading');
		problemaRun();
		$btn.button('reset');
		botonVer.popover('destroy');
		$('#divajaxloader').hide();
	});
	
	function avisar(mensaje){
        botonVer.attr('data-content', mensaje);
        botonVer.popover('show');
    }
	(function(){
        var lastTimeout;
        
        botonVer.popover({
            placement: 'top',
            trigger: 'manual',
        });
        
        botonVer.on('shown.bs.popover', function(){
            clearTimeout(lastTimeout);
            lastTimeout = setTimeout(function(){
            	botonVer.popover('hide');
            }, 1500);
        });
    })();
	
	botonVer.on('click', function(e){
		e.preventDefault();
		verResultadosProblemaRun();
	});
})

