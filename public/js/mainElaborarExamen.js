(function(){
    
    var contenedorExamen = $('#examen-contenedor');
    var contenedorPreguntas = contenedorExamen.find('.examen-preguntas');
    var formularioExamen = contenedorExamen.find('form');
    var conteoGeneral = formularioExamen.children('.conteo-nota');
    var botonListo = contenedorExamen.find(':submit');
    var inputBuscar = $('#input-buscar');
    var botonBuscar = $('#boton-buscar');
    var contenedorResultadosBusqueda = $('#busqueda-preguntas .busqueda-resultados');
    
    // Mostrar resultados de busqueda de preguntas
    
    function avisar(mensaje){
        inputBuscar.attr('data-content', mensaje);
        inputBuscar.popover('show');
    }
    
    (function(){
        var lastTimeout;
        
        inputBuscar.popover({
            placement: 'top',
            trigger: 'manual',
        });
        
        inputBuscar.on('shown.bs.popover', function(){
            clearTimeout(lastTimeout);
            lastTimeout = setTimeout(function(){
                inputBuscar.popover('hide');
            }, 1500);
        });
    })();
    
    function mostrarResultadosBusqueda(data){
        if(data.length == 0) return avisar("La búsqueda no retornó resultados");
        
        var preguntasExistentes = contenedorPreguntas.find('input[name="idPregunta[]"]');
        var agregadoUno = false;
        
        contenedorResultadosBusqueda.empty();
        for(i in data){
            var elemento = $.parseHTML(data[i]);
            var id = $(elemento).data('idPregunta');
            var presenteEnExamen = preguntasExistentes.is(function() {
                return $(this).val() == id;
            });
            if(!presenteEnExamen){
                contenedorResultadosBusqueda.append(elemento);
                agregadoUno = true;
            }
        }
        
        if(!agregadoUno) return avisar("La búsqueda no retornó nuevos resultados");
    }
    
    function obtenerResultados(){
        if(inputBuscar.val().length == 0) return avisar("Escriba para buscar");
        var call = jsRoutes.controllers.Preguntas.buscarPreguntas(inputBuscar.val());
        $.ajax({
            url: call.url,
            type: call.type,
            success: mostrarResultadosBusqueda
        });
    }

    botonBuscar.on('click', function(e){
        e.preventDefault();
        obtenerResultados();
    });
    inputBuscar.on('keypress', function(e){
        if(e.which == 13){
            e.preventDefault();
            obtenerResultados();
        }
    });
    
    // Agregar resultado de busqueda a examen
    
    var preguntaEnProceso = null;
    
    function escribirPunto(numero){
        if(numero == 1) return "punto";
        else return "puntos";
    }
    function actualizarParrafoConteoPregunta(contenedor){
        var conteoPregunta = contenedor.find('.conteo-nota');
        var puntajeFavor = contenedor.find('input[name="puntajeCorrecto[]"]').val();
        var puntajeContra = contenedor.find('input[name="puntajeIncorrecto[]"]').val();
        
        puntajeFavor = (puntajeFavor == "" ? 0 : parseInt(puntajeFavor));
        puntajeContra = (puntajeContra == "" ? 0 : parseInt(puntajeContra));
        
        var textoConteo = "";
        if(puntajeFavor != 0){
            textoConteo = puntajeFavor + " " + escribirPunto(puntajeFavor) + " a favor";
            if(puntajeContra != 0){
                textoConteo += ", " + (-puntajeContra) + " " + escribirPunto(-puntajeContra) + " en contra";
            }
        }
        conteoPregunta.text(textoConteo);
    }
    function actualizarParrafoConteoGeneral() {
        var sumaTotal = 0;
        contenedorPreguntas.find('input[name="puntajeCorrecto[]"]').each(function() {
            var val = $(this).val();
            val = (val == "" ? 0 : parseInt(val));
            sumaTotal += val;
        });
        if(sumaTotal != 0) conteoGeneral.text("Puntaje total actual: " + sumaTotal);
        else conteoGeneral.text("");
        if(sumaTotal == 20){
            conteoGeneral.addClass('success').removeClass('danger');
        }else{
            conteoGeneral.addClass('danger').removeClass('success');
        }
    }
    function actualizarTodo(){
        contenedorPreguntas.find('.pregunta-contenedor-edicion').each(function() {
            actualizarParrafoConteoPregunta($(this));
        });
        actualizarParrafoConteoGeneral();
    }
    
    contenedorExamen.
    on('click', 'button.pregunta-agregar', function(){
        $(this).closest('.pregunta-contenedor-edicion').removeClass('pregunta-por-agregar');
        //Este boton solo se muestra bajo pregunta-por-agregar
        preguntaEnProceso.remove();
        botonListo.removeAttr('disabled');
        preguntaEnProceso = null;
    }).
    on('click', 'button.close', function(){
        var contenedor = $(this).closest('.pregunta-contenedor-edicion');
        if(contenedor.hasClass('pregunta-por-agregar')){
            preguntaEnProceso.removeClass('inactive');
            botonListo.removeAttr('disabled');
            preguntaEnProceso = null;
        }
        contenedor.remove();
        actualizarParrafoConteoGeneral();
    }).
    on('click', '.pregunta-contenedor-edicion:not(.pregunta-por-agregar)', function(e){
        var that = $(this);
        that.find('button.close').toggle();
        that.find('.pregunta-opciones').toggle();
        that.find('.conteo-nota').toggle();
    }).
    on('click', 'input', function(e){
        e.stopPropagation();
    }).
    on('change', 'input[name="puntajeCorrecto[]"], input[name="puntajeIncorrecto[]"]', function(){
        var contenedor = $(this).closest('.pregunta-contenedor-edicion');
        actualizarParrafoConteoPregunta(contenedor);
        actualizarParrafoConteoGeneral();
    });
    
    function agregarPreguntaEdicion(objetoResultado){
        return function(data) {
            var nuevo = $($.parseHTML(data));
            nuevo.addClass('pregunta-por-agregar');
            contenedorPreguntas.append(nuevo);
            objetoResultado.addClass('inactive');
            preguntaEnProceso = objetoResultado;
            botonListo.attr('disabled', 'disabled');
        }
    }

    $('#busqueda-preguntas').
    on('click', '.resultado-busqueda', function(e){
        e.preventDefault();
        if(preguntaEnProceso != null) return false;
        var that = $(this);
        if(that.hasClass('inactive')) return false;
        var call = jsRoutes.controllers.Examenes.renderPreguntaEdicion(that.data('idPregunta'));
        $.ajax({
            url: call.url,
            type: call.type,
            success: agregarPreguntaEdicion(that)
        });
    });
    
    // Verificacion de formulario
    function avisoYSalir(mensaje){
        alert(mensaje);
        return false;
    }
    formularioExamen.on('submit', function(e) {
        try {
            var titulo = formularioExamen.find('input[name="titulo"]').val();
            if (titulo == undefined || titulo.length == 0)
                return avisoYSalir("Título no puede estar vacio");
            var hayUnElemento = false;
            var notaMaxima = 0;
            var errorDentro;
            contenedorExamen.find('.pregunta-contenedor-edicion').each(function() {
                hayUnElemento = true;
                var that = $(this);
                var puntajeFavor = that.find('input[name="puntajeCorrecto[]"]').val();
                var puntajeContra = that.find('input[name="puntajeIncorrecto[]"]').val();

                if (puntajeFavor == "" || puntajeFavor == 0){
                    errorDentro = "Puntaje no puede ser nulo";
                }else if(puntajeContra != "" && parseInt(puntajeContra) >= puntajeFavor){
                    errorDentro = "Puntaje en contra no puede ser mayor o igual al puntaje a favor";
                }

                notaMaxima += parseInt(puntajeFavor);
            });
            if(errorDentro != undefined)
                return avisoYSalir(errorDentro);
            if (!hayUnElemento)
                return avisoYSalir("No hay preguntas en el examen");
            if (notaMaxima != 20)
                return avisoYSalir("La suma de notas debe sumar 20");
        } catch (e) {
            console.log(e);
            return avisoYSalir("Error desconocido");
        }
    });
    
    window.preguntaEnProceso = function() {
        return preguntaEnProceso;
    }
    
    //Inicializacion
    actualizarTodo();

})();
