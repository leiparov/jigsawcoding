package controllers;

import static play.data.Form.form;

import java.util.List;

import models.entities.Alumno;
import models.entities.Docente;
import models.entities.Problema;
import models.services.Login;
import models.services.ProblemaService;
import models.services.UsuarioService;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.problemas.editarProblema;
import views.html.problemas.listaProblemas;
import views.html.problemas.nuevoProblema;

import com.fasterxml.jackson.databind.JsonNode;

import exceptions.DAOException.NoEncontradoException;

@Login.Requiere
public class ProblemaController extends Controller {

	private static UsuarioService usuarioService = new UsuarioService();
	private static ProblemaService problemaService = new ProblemaService();

	private static Docente getDocente() {
		return usuarioService.obtener(Login.obtener(ctx()).getDNI(),
				Docente.class);
	}
	
	public static Result index() {
		Login login = Login.obtener(ctx());
		if(login.isTipo(Alumno.class)){
			return indexAlumno();
		}else if (login.isTipo(Docente.class)){
			return indexDocente();
		}else{
			return redirect(routes.Application.interfazLogin());
		}		
	}
	
	//Modulo DOCENTES
	public static Result GO_HOME_PROBLEMAS = redirect(routes.ProblemaController
			.list(0, "id", "asc", ""));

	public static Result list(int page, String sortBy, String order,
			String filter) {
		return ok(listaProblemas.render(problemaService.page(getDocente(),
				page, 10, sortBy, order, filter), sortBy, order, filter));
	}

	public static Result indexDocente() {
		return GO_HOME_PROBLEMAS;
	}

	public static Result interfazNuevo() {
		Form<Problema> problemaForm = form(Problema.class).bindFromRequest();
		return ok(nuevoProblema.render(problemaForm));
	}

	public static Result registrarProblema() {
		Form<Problema> problemaForm = form(Problema.class).bindFromRequest();
		if (problemaForm.hasErrors()) {
			return badRequest(nuevoProblema.render(problemaForm));
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
		Form<Problema> problemaForm = form(Problema.class).fill(
				problemaService.obtenerProblema(id));
		return ok(editarProblema.render(id, problemaForm));
	}

	public static Result actualizarProblema(Integer id) {
		Form<Problema> problemaForm = form(Problema.class).bindFromRequest();
		if (problemaForm.hasErrors()) {
			return badRequest(editarProblema.render(id, problemaForm));
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
	
	//Módulo ALUMNOS
	public static Result indexAlumno() {
		return redirect(routes.AlumnoController.indexAlumno());
	}

	public static Result interfazResolver(Integer id) {
		try {
			Problema p = problemaService.obtenerProblema(id);
			return ok(views.html.problemas.resolverProblema.render(p));
		} catch (NoEncontradoException e) {
			flash("error", "No existe el problema " + e.getMessage());
			return indexAlumno();
		} catch (Exception e) {
			e.printStackTrace();
			flash("error", e.getMessage());
			return indexAlumno();
		}

	}
}
