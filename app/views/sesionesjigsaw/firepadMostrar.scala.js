@(firepadid: String)

$(function(){
	
	var firepad;
	var botonRun = $('#boton-run');
	var botonVer = $('#boton-ver');
	var contenedorResultados = $('#ideoneResultados');
	var firepadresultados = $('#resultados');
	
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
		
		// // Create CodeMirror (with line numbers and the Java mode).
		//codeMirror = getCodeMirror(language.val());
		modeLanguage = getMode(language.val());
		codeMirror = CodeMirror(document.getElementById('firepad'), {
			lineNumbers : true,
			lineWrapping : true,
			styleActiveLine: true,
			matchBrackets: true,
			mode : getMode(language.val()),
			theme: 'monokai'
		});

		// // Create Firepad.
		firepad = Firepad.fromCodeMirror(firepadRef, codeMirror, {
			defaultText : '// your code goes here'
		});
		firepad.setUserId('Docente');
	}

	init();
	
	/* Ejecutar codigo fuente */
	function mostrarIdeoneSubmissionResults(data){
		contenedorResultados.empty();
		for(i in data){
			var elemento = $.parseHTML(data[i]);
			contenedorResultados.append(elemento);
		}
		ocultarLoading();
	}
	
	function mostrarLoading(){
		$('#ajaxloader').html('<img src="@routes.Assets.at("images/loading.gif")"/>');
		$('#panelResultados').hide();
	}
	function ocultarLoading(){
		$('#ajaxloader').empty();
		$('#panelResultados').show();
	}
	function problemaRun (){
		var firepadText = firepad.getText();
		var inputStdinText = $('#input-stdin').val();
		var languageId = language.val();
		fpadid = ''+'@firepadid';
		console.log(firepadText);
		console.log(inputStdinText);
		
		mostrarLoading();
		
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
		botonVer.popover('destroy');
	});
		
})

