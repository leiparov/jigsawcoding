package controllers;

import models.entities.Docente;
import models.entities.GrupoExperto;
import models.services.GrupoExpertoService;
import models.services.UsuarioService;
import models.services.impl.GrupoExpertoServiceImpl;
import models.services.impl.UsuarioServiceImpl;
import static play.data.Form.*;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import utils.Login;
import views.html.gruposexpertos.nuevoGrupoExperto;
import views.html.gruposexpertos.listaGruposExpertos;
import views.html.gruposexpertos.editarGrupoExperto;

@Login.Requiere
public class GruposExpertos extends Controller {

	private static UsuarioService usuarioService = new UsuarioServiceImpl();
	private static GrupoExpertoService grupoExpertoService = new GrupoExpertoServiceImpl();

	private static Docente getDocente() {
		return usuarioService.obtener(Login.obtener(ctx()).getDNI(),
				Docente.class);
	}

	public static Result GO_HOME = redirect(routes.GruposExpertos.list(0,
			"grupoExpertoId", "asc", ""));

	/**
	 * Display the paginated list of grupoExpertos.
	 * 
	 * @param page
	 *            Current page number (starts from 0)
	 * @param sortBy
	 *            Column to be sorted
	 * @param order
	 *            Sort order (either asc or desc)
	 * @param filter
	 *            Filter applied on GrupoExperto names
	 */
	public static Result list(int page, String sortBy, String order,
			String filter) {
		return ok(listaGruposExpertos.render(grupoExpertoService.page(getDocente(),
				page, 10, sortBy, order, filter), sortBy, order, filter));
	}

	public static Result index() {
		return GO_HOME;
	}

	public static Result interfazNuevo() {
		Form<GrupoExperto> grupoExpertoForm = form(GrupoExperto.class).bindFromRequest();
		return ok(nuevoGrupoExperto.render(grupoExpertoForm));
	}

	public static Result registrarGrupoExperto() {
		Form<GrupoExperto> grupoExpertoForm = form(GrupoExperto.class).bindFromRequest();
		if (grupoExpertoForm.hasErrors()) {
			return badRequest(nuevoGrupoExperto.render(grupoExpertoForm));
		}
		try {
			GrupoExperto grupoExperto = grupoExpertoForm.get();
			grupoExperto.setDocente(getDocente());
			grupoExpertoService.guardarGrupoExperto(getDocente(), grupoExperto);
			flash("success", "GrupoExperto registrado con éxito");
			return GO_HOME;

		} catch (Exception e) {
			e.printStackTrace();
			flash("error", "No se pudo guardar el GrupoExperto");
			return redirect(routes.Application.index());
		}
	}

	public static Result editarGrupoExperto(Long id) {
		Form<GrupoExperto> grupoExpertoForm = form(GrupoExperto.class).fill(
				grupoExpertoService.obtenerGrupoExperto(id));
		return ok(editarGrupoExperto.render(id, grupoExpertoForm));
	}

	public static Result actualizarGrupoExperto(Long id) {
		Form<GrupoExperto> grupoExpertoForm = form(GrupoExperto.class).bindFromRequest();
		if (grupoExpertoForm.hasErrors()) {
			return badRequest(editarGrupoExperto.render(id, grupoExpertoForm));
		}
		try {
			GrupoExperto grupoExperto = grupoExpertoForm.get();
			grupoExperto.setDocente(getDocente());
			grupoExperto.setGrupoExpertoId(id);
			System.out.println(grupoExperto.toString());
			grupoExpertoService.actualizarGrupoExperto(getDocente(), grupoExperto);
			flash("success", "GrupoExperto actualizado con éxito");
			return GO_HOME;

		} catch (Exception e) {
			e.printStackTrace();
			flash("error", "No se pudo actualizar el GrupoExperto");
			return redirect(routes.Application.index());
		}
	}

	public static Result eliminarGrupoExperto(Long id) {
		grupoExpertoService.eliminarGrupoExperto(id);
		flash("success", "GrupoExperto borrado con éxito");
		return GO_HOME;
	}
}
