@(firepadid: String, userid: String)

$(function(){
	
	var firepad;
	var botonRun = $('#@firepadid-boton-run');
	var botonVer = $('#@firepadid-boton-ver');
	var contenedorResultados = $('#@firepadid-ideoneResultados');
	var language = $('#language');
	var codeMirror;
	
	language.change(function(){
		var number = $(this).val();
		mode = getMode(number);
		console.log(mode);
		codeMirror.setOption("mode", mode);
		console.log(codeMirror);
	});
	
	function getMode(languageid){
		divfpadid = ''+'@firepadid';
		var modeLanguage;
		
		switch(languageid){
			case '1': modeLanguage = 'text/x-c++src'; break;
			case '4': modeLanguage = 'text/x-python'; break;
			case '55': modeLanguage = 'text/x-java'; break;
			default: modeLanguage = 'text/x-c++src';
		}		
		return modeLanguage;
	}
	
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
		codeMirror = CodeMirror(document.getElementById(divfpadid), {
			lineNumbers : true,
			lineWrapping : true,
			styleActiveLine: true,
			matchBrackets: true,
			mode : getMode(language.val()),
			theme: 'monokai'
		});

		// // Create Firepad.
		firepad = Firepad.fromCodeMirror(firepadRef, codeMirror, {
			defaultText : 'print "Hola mundo"'
		});
		firepad.setUserId('@userid');
	}
	init();
	
	/* Ejecutar codigo fuente */
	function mostrarIdeoneSubmissionResults(data){
//		console.log(data);
//		console.log(data['status']);
		
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
		var languageId = language.val();
		fpadid = ''+'@firepadid';
		console.log(firepadText);
		console.log(inputStdinText);
		var call = jsRoutes.controllers.ProblemaController.problemaRunJs(firepadText, inputStdinText, languageId, fpadid);
		$.ajax({
			url: call.url,
			type: call.type,
			success: mostrarIdeoneSubmissionResults
		});
		
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

