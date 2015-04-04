(function(){
	var botonFinalizarReunionExpertos = $('#boton-finalizar');
	var barraProgreso = $('#barra-progreso');
	var reloj = $('#reloj');   
	
	botonFinalizarReunionExpertos.click(function(){		
		console.log("FinReunionExpertos");
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
        	botonFinalizarReunionExpertos.trigger('click');
        }
	});

})();
/*
 * 
 <script type="text/javascript" src="@routes.Assets.at("js/jquery.countdown.js")" ></script>
 <script type="text/javascript" src="@routes.Assets.at("js/jquery.countdown-es.js")" ></script>
<script type="text/javascript" src="@routes.Assets.at("js/faseExpertos.js")" ></script>
 * 
 */