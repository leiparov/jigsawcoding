package controllers;

import static play.data.Form.form;
import models.entities.Docente;
import models.entities.SesionJigsaw;
import models.services.ProblemaService;
import models.services.SesionJigsawService;
import models.services.UsuarioService;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import utils.Login;

@Login.Requiere
public class SesionesJigsaw extends Controller {

	private static UsuarioService usuarioService = new UsuarioService();
	private static SesionJigsawService sesionJigsawService = new SesionJigsawService();

	private static Docente getDocente() {
		return usuarioService.obtener(Login.obtener(ctx()).getDNI(),
				Docente.class);
	}

	public static Result GO_HOME = redirect(routes.SesionesJigsaw.list(0, "id",
			"asc", ""));

	public static Result list(int page, String sortBy, String order,
			String filter) {
		return ok(views.html.sesionesjigsaw.listaSesionesJigsaw.render(sesionJigsawService.page(
				getDocente(), page, 10, sortBy, order, filter), sortBy, order,
				filter));
	}

	public static Result index() {
		return GO_HOME;
	}

	public static Result interfazNuevo() {
		Form<SesionJigsaw> sesionJigsawForm = form(SesionJigsaw.class)
				.bindFromRequest();
		return ok(views.html.sesionesjigsaw.nuevaSesionJigsaw.render(sesionJigsawForm));
	}

	public static Result registrarSesionJigsaw() {
		Form<SesionJigsaw> sesionJigsawForm = form(SesionJigsaw.class)
				.bindFromRequest();
		if (sesionJigsawForm.hasErrors()) {
			return badRequest(views.html.sesionesjigsaw.nuevaSesionJigsaw.render(sesionJigsawForm));
		}
		try {
			// Problema problema = problemaForm.get();
			// problema.setDocente(getDocente());
			// problemaService.guardarProblema(getDocente(), problema);
			flash("success", "SesionJigsaw registrada con éxito");
			return GO_HOME;

		} catch (Exception e) {
			e.printStackTrace();
			flash("error", "No se pudo guardar la Sesión Jigsaw");
			return redirect(routes.SesionesJigsaw.index());
		}
	}

}
