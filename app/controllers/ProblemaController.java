package controllers;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import models.entities.Alumno;
import models.entities.Docente;
import models.entities.Problema;
import models.services.Login;
import models.services.ProblemaService;
import models.services.UsuarioService;
import models.services.ideone.IdeoneRun;
import models.services.ideone.IdeoneService;
import models.services.ideone.IdeoneSubmissionDetails;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import exceptions.DAOException.NoEncontradoException;

@Login.Requiere
public class ProblemaController extends Controller {

	private static UsuarioService usuarioService = new UsuarioService();
	private static ProblemaService problemaService = new ProblemaService();
	private static IdeoneService ideoneService = new IdeoneService();

	private static Docente getDocente() {
		return usuarioService.obtener(Login.obtener(ctx()).getDNI(),
				Docente.class);
	}

	public static Result index() {
		Login login = Login.obtener(ctx());
		if (login.isTipo(Alumno.class)) {
			return indexAlumno();
		} else if (login.isTipo(Docente.class)) {
			return indexDocente();
		} else {
			return redirect(routes.Application.interfazLogin());
		}
	}

	// Modulo DOCENTES
	public static Result GO_HOME_PROBLEMAS = redirect(routes.ProblemaController
			.list(0, "id", "asc", ""));

	public static Result list(int page, String sortBy, String order,
			String filter) {
		return ok(views.html.problemas.listaProblemas.render(problemaService.page(getDocente(),
				page, 10, sortBy, order, filter), sortBy, order, filter));
	}

	public static Result indexDocente() {
		return GO_HOME_PROBLEMAS;
	}

	public static Result interfazNuevo() {
		Form<Problema> problemaForm = Form.form(Problema.class).bindFromRequest();
		return ok(views.html.problemas.nuevoProblema.render(problemaForm));
	}

	public static Result registrarProblema() {
		Form<Problema> problemaForm = Form.form(Problema.class).bindFromRequest();
		if (problemaForm.hasErrors()) {
			return badRequest(views.html.problemas.nuevoProblema.render(problemaForm));
		}
		try {
			Problema problema = problemaForm.get();
			problema.setDocente(getDocente());
			problemaService.guardarProblema(getDocente(), problema);
			flash("success", "Problema registrado con éxito");
			return GO_HOME_PROBLEMAS;

		} catch (Exception e) {
			e.printStackTrace();
			flash("error", "No se pudo guardar el Problema");
			return redirect(routes.ProblemaController.index());
		}
	}

	public static Result interfazEditar(Integer id) {
		Form<Problema> problemaForm = Form.form(Problema.class).fill(
				problemaService.obtenerProblema(id));
		return ok(views.html.problemas.editarProblema.render(id, problemaForm));
	}

	public static Result actualizarProblema(Integer id) {
		Form<Problema> problemaForm = Form.form(Problema.class).bindFromRequest();
		if (problemaForm.hasErrors()) {
			return badRequest(views.html.problemas.editarProblema.render(id, problemaForm));
		}
		try {
			Problema problema = problemaForm.get();
			problema.setDocente(getDocente());
			problema.setId(id);
			System.out.println(problema.toString());
			problemaService.actualizarProblema(getDocente(), problema);
			flash("success", "Problema actualizado con éxito");
			return GO_HOME_PROBLEMAS;

		} catch (Exception e) {
			e.printStackTrace();
			flash("error", "No se pudo actualizar el Problema");
			return redirect(routes.ProblemaController.index());
		}
	}

	public static Result eliminarProblema(Integer id) {
		problemaService.eliminarProblema(id);
		flash("success", "Problema borrado con éxito");
		return GO_HOME_PROBLEMAS;
	}

	public static Result buscarProblemas(String q) {
		List<Problema> problemas = problemaService.buscarPorTexto(q);
		JsonNode respuesta = convertirListaPreguntasAJson(problemas);
		return ok(respuesta);
	}
	private static JsonNode convertirListaPreguntasAJson(List<Problema> lista) {
		String[] array = new String[lista.size()];
		int i = 0;
		for (Problema pregunta : lista) {
			array[i++] = views.html.examenes.resultadoBusquedaPregunta
					.render(pregunta).body().trim();
		}
		return Json.toJson(array);
	}

	// Módulo ALUMNOS
	public static Result indexAlumno() {
		return redirect(routes.AlumnoController.indexAlumno());
	}

//	public static Result interfazResolver(Integer id) {
//		try {
//			Problema p = problemaService.obtenerProblema(id);
//			return ok(views.html.perfilalumno.resolverProblema.render(p));
//		} catch (NoEncontradoException e) {
//			flash("error", "No existe el problema " + e.getMessage());
//			return indexAlumno();
//		} catch (Exception e) {
//			e.printStackTrace();
//			flash("error", e.getMessage());
//			return indexAlumno();
//		}
//	}

	public static Result problemaRunJs(String source, String input, Integer languageId, String firepadid) {
			
        final ExecutorService service;
        final Future<String> task;
        play.Logger.info("SOURCE" + source);
        service = Executors.newFixedThreadPool(1);
        task = service.submit(new IdeoneRun(source, input, languageId));
        
        //ObjectNode resultado = Json.newObject();
        //resultado = test("hACTit");
        String link = "";
        try {
        	link = task.get();
        	play.Logger.info("IDEONE Link: " + link);
            
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        service.shutdown();
        return verResultadosProblemaRunJs(link, firepadid);
		
	}
	
	public static Result verResultadosProblemaRunJs(String link, String firepadid){
		IdeoneSubmissionDetails isd = ideoneService.getSubmissionDetails(
				link, true, true, true, true, true);
		
		String[] array = new String[1];		
		array[0] = views.html.perfilalumno.resultadosIdeoneRun.render(isd, firepadid).body().trim();
		
		return ok(Json.toJson(array));
	}
	
	
	private static ObjectNode test (String link){
		ObjectNode resultado = Json.newObject();
		//String link = "9Q7GON";
        
        IdeoneSubmissionDetails details = ideoneService.getSubmissionDetails(
				link, true, true, true, true, true);
        if(details.getError().equals("OK")){
        	resultado.put("status", IdeoneService.translateStatus(details.getStatus()));
    		resultado.put("result", IdeoneService.translateResult(details.getResult()));
    		resultado.put("input", details.getInput());
    		resultado.put("output", details.getOutput());
    		resultado.put("date", details.getDate());
    		resultado.put("time", details.getTime());
    		resultado.put("memory", details.getMemory());
    		resultado.put("error", details.getError());
    		resultado.put("link", link);
    		resultado.put("cmpinfo", details.getCmpinfo());
        }else{
        	resultado.put("status", "");
    		resultado.put("result", "");
    		resultado.put("input", "");
    		resultado.put("output", "");
    		resultado.put("date", "");
    		resultado.put("time", "");
    		resultado.put("memory", "");
    		resultado.put("error", "");
    		resultado.put("link", link);
    		resultado.put("cmpinfo", "");
        }
        return resultado;
        
        
	}
}
