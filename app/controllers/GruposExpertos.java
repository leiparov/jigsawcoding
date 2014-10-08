package controllers;

import static play.data.Form.form;

import java.util.LinkedList;
import java.util.List;

import models.daos.DAOException;
import models.entities.Alumno;
import models.entities.Docente;
import models.entities.GrupoExperto;
import models.services.GrupoExpertoService;
import models.services.UsuarioService;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import utils.Login;
import views.html.gruposexpertos.*;

@Login.Requiere
public class GruposExpertos extends Controller {

	private static UsuarioService usuarioService = new UsuarioService();
	private static GrupoExpertoService grupoExpertoService = new GrupoExpertoService();

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
	
	public static Result interfazAsignar(Long id) {
        GrupoExperto grupo = grupoExpertoService.obtenerGrupoExperto(id);        
        return ok(views.html.gruposexpertos.alumnoToGrupo.render(grupo));
    }
	
	public static Result definirAlumnos(Long id){
		//return TODO;
        try {
            return tryDefinirAlumnos(id);
        } catch (DAOException.NoEncontradoException nee) {
            if(nee.esClase(GrupoExperto.class)){
                flash("error", "El grupo experto no existe");
                return redirect(routes.GruposExpertos.index());
            }else if(nee.esClase(Alumno.class)){
                flash("error", "Un alumno no existe");
                return redirect(routes.GruposExpertos.interfazAsignar(id));
            }else{
                throw nee;
            }
        } catch (Exception e) {
            flash("error", "Error desconocido: " + e.getMessage());
            return redirect(routes.GruposExpertos.interfazAsignar(id));
        }
    }
	private static Result tryDefinirAlumnos(Long id) {
        Form<AsignarAlumnosForm> form = Form.form(AsignarAlumnosForm.class).bindFromRequest();
        GrupoExperto grupo = grupoExpertoService.obtenerGrupoExperto(id);
        List<Integer> dnialumnos = form.get().alumnos;
        if(grupo.getMaximoAlumnos() < dnialumnos.size()){
        	flash("error", "El grupo experto debe tener " + grupo.getMaximoAlumnos() + " integrantes");
        	return redirect(routes.GruposExpertos.interfazAsignar(id));
        }	
        grupoExpertoService.actualizarAlumnos(grupo, dnialumnos);
        flash("success", "Alumnos asignados con éxito");
        return redirect(routes.GruposExpertos.index());
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
		return ok(views.html.gruposexpertos.editarGrupoExperto.render(id, grupoExpertoForm));
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
	
	public static class AsignarAlumnosForm{
        public List<Integer> alumnos = new LinkedList<Integer>();
    }
}
