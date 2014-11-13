@(firepadid: String, userid: String)

$(function(){
	
	var firepad;
	var botonRun = $('#@firepadid-boton-run');
	var botonVer = $('#@firepadid-boton-ver');
	var contenedorResultados = $('#@firepadid-ideoneResultados');
	
	function init() {
		// Initialize Firebase.
		// var firepadRef = getExampleRef();
		// TODO: Replace above line with:
		url = 'https://vivid-heat-5073.firebaseio.com/firepads/jc_' + '@firepadid';
		console.log(url);
		var firepadRef = new Firebase(url);
		console.log('here');
		console.log(firepadRef);
		// // Create CodeMirror (with line numbers and the Java mode).
		divfpadid = ''+'@firepadid';
		console.log(divfpadid);
		var codeMirror = CodeMirror(document.getElementById(divfpadid), {
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
		
		//var myModal = $('#modalResultados');		
		//myModal.modal('show');
		alert('Ejecución de problema exitosa!!!');
	}
	
	function problemaRun (){
		var firepadText = firepad.getText();
		var inputStdinText = $('#@firepadid-input-stdin').val();
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
		var $btn = $(this).button('loading');
		problemaRun();
		$btn.button('reset');
		//botonVer.popover('destroy');
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
	
	
})

