@(firepadid: String, userid: String)

$(function(){
	
	var firepad;
	var botonRun = $('#@firepadid-boton-run');
	var botonVer = $('#@firepadid-boton-ver');
	var firepadstdin = $('#@firepadid-stdin');
	var contenedorResultados = $('#@firepadid-ideoneResultados');
	//var botonChat = $(".toggleup");
	var firepadresultados = $('#@firepadid-resultados');
	var language = $('#@firepadid-language');
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
		divfpadid = ''+'@firepadid';
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
			defaultText : '// your code goes here'
		});
		firepad.setUserId('@userid');
	}

	init();
	
	function mostrarLoading(){
		$('#ajaxloader@firepadid').html('<img src="@routes.Assets.at("images/loading.gif")"/>');
		$('#panelResultados@firepadid').hide();
	}
	function ocultarLoading(){
		$('#ajaxloader@firepadid').empty();
		$('#panelResultados@firepadid').show();
	}
	
	/* Ejecutar codigo fuente */
	function mostrarIdeoneSubmissionResults(data){
//		console.log(data);
//		console.log(data['status']);
		
		contenedorResultados.empty();
		for(i in data){
			var elemento = $.parseHTML(data[i]);
			contenedorResultados.append(elemento);
		}
		
//		var myModal = $('#modalResultados');		
//		myModal.modal('show');
		//alert('Ejecución de problema exitosa!!!');
		ocultarLoading();
	}
	
	function problemaRun (){
		var firepadText = firepad.getText();
		var inputStdinText = $('#@firepadid-input-stdin').val();
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
	
//	botonVer.on('click', function(e){
//		e.preventDefault();
//		verResultadosProblemaRun();
//	});
//	botonChat.click(function(){
//		$("#chat").slideToggle("slow"); 
//		$(this).toggleClass("toggledown");
//		firepadstdin.collapse('hide');
//		firepadresultados.collapse('hide');
//		return false;
//	});
	
	botonVer.click(function(){
		$("#chat").hide(); 		
	});
	var botonStdin = $('#@firepadid-boton-stdin');
	botonStdin.click(function(){
		$("#chat").hide();
	});
	
	
})

