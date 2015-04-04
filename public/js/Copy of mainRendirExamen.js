(function(){
	var nextButton = $("#next");
	var prevButton = $("#prev");
	var preguntas = $('.examen .pregunta');
	var botonFinalizarExamen = $('#boton-finalizar');
	var barraProgreso = $('#barra-progreso');
	var reloj = $('#clock');
    
    function enableOrDisablePagers() {
        var current = preguntas.filter(':visible');
        nextButton.parent().toggleClass('disabled', current.next().length == 0);
        prevButton.parent().toggleClass('disabled', current.prev().length == 0);
    };
	
    nextButton.click(function(){
        var next = preguntas.filter(':visible').next();
        if (next.length != 0){
            next.show().prev().hide();
            enableOrDisablePagers();
        }
        return false;
    });

    prevButton.click(function(){
        var prev = preguntas.filter(':visible').prev();
        if (prev.length != 0){
            prev.show().next().hide();
            enableOrDisablePagers();
        }
        return false;
    });
	
	botonFinalizarExamen.click(function(){
		var inputValues = new Array();
		$('.respuesta').each(function(){
		    inputValues[$(this) .attr('name')] = $(this).val();
		});
		console.log(inputValues);
	});
	
	//Comportamiento de las opciones de emparejamiento
	$('.pregunta .emparejamiento input[type="text"]').change(function() {
	    var that = $(this);
        var contenedorEmparejamiento = that.closest('.emparejamiento'); 
        
        var enunciados  = contenedorEmparejamiento.find('.emparejamiento-enunciado li');
        var enunciados2 = contenedorEmparejamiento.find('.emparejamiento-respuesta li');
        var respuestas  = contenedorEmparejamiento.find('.emparejamiento-respuesta input');
        var respuestaCodificada = contenedorEmparejamiento.find('input[type="hidden"]');
        
        var codigo = "";
        for(var i = 0; i < enunciados.size(); i++){
            if(i != 0) codigo += ";";
            codigo += enunciados.eq(i).attr('data-id') + ',' + 
                        enunciados2.eq(i).attr('data-id') + ',' +
                        respuestas.eq(i).val();
        }
        
        respuestaCodificada.val(codigo);
    });

    //At start
	var tiempoExamen = reloj.attr('data-duracion');
    reloj.countdown({
        format: 'hmS',
        layout: "{h<}{hn} {hl}, {h>}{m<}{mn} {ml}, {m>}{sn} {sl}",
		until : tiempoExamen,
		onTick : function(periodos) {
            var segundos = periodos[6] + 60*periodos[5] + 3600*periodos[4];
            var proporcion = (segundos * 100)/tiempoExamen;
            barraProgreso.attr('aria-valuenow', proporcion);
            barraProgreso.width(proporcion + '%');
        },
        onExpiry: function() {
            botonFinalizarExamen.trigger('click');
        }
	});
	
	//carousel de divs
	preguntas.each(function(e) {
        if (e != 0) $(this).hide();
    });

    enableOrDisablePagers();

})();
