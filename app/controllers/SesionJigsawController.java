package controllers;

import java.util.LinkedList;
import java.util.List;

import models.entities.Alumno;
import models.entities.Docente;
import models.entities.EtapaSesionJigsaw;
import models.entities.GrupoExperto;
import models.entities.GrupoExpertoProblema;
import models.entities.Problema;
import models.entities.SesionJigsaw;
import models.services.GrupoExpertoService;
import models.services.Login;
import models.services.ProblemaService;
import models.services.SesionJigsawService;
import models.services.UsuarioService;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import utils.ExpresionDuracion;
import utils.FormatoFechaHora;

@Login.Requiere
public class SesionJigsawController extends Controller {

	private static UsuarioService usuarioService = new UsuarioService();
	private static SesionJigsawService sesionJigsawService = new SesionJigsawService();
	private static GrupoExpertoService grupoExpertoService = new GrupoExpertoService();
	private static ProblemaService problemaService =  new ProblemaService();

	private static Docente getDocente() {
		return usuarioService.obtener(Login.obtener(ctx()).getDNI(),
				Docente.class);
	}

	/*Módulo DOCENTE*/
	public static Result GO_HOME = redirect(routes.SesionJigsawController.list(0, "id",
			"asc", ""));

	public static Result list(int page, String sortBy, String order,
			String filter) {
		return ok(views.html.sesionesjigsaw.listaSesionesJigsaw.render(
				sesionJigsawService.page(getDocente(), page, 10, sortBy, order,
						filter), sortBy, order, filter));
	}

	public static Result index() {
		return GO_HOME;
	}

	public static Result interfazNuevo() {
		return ok(views.html.sesionesjigsaw.nuevaSesionJigsaw
				.render(new SesionJigsaw()));
	}

	public static Result registrarSesionJigsaw() {
		Form<SesionJigsawForm> form = Form.form(SesionJigsawForm.class)
				.bindFromRequest();
		try {
			SesionJigsaw s = form.get().entidad();
			if (form.get().actualizar(s) == null) {
				flash("error",
						"No se puede guardar la sesión jigsaw. Complete todos los campos");
				return interfazNuevo();
			} else {
				s = form.get().actualizar(s);				
				sesionJigsawService.guardarSesionJigsaw(getDocente(), s);
				flash("success", "SesionJigsaw registrada con éxito");
				return GO_HOME;
				// return asignarProblemas(id)
			}

		} catch (Exception e) {
			e.printStackTrace();
			flash("error", "No se pudo guardar la Sesión Jigsaw");
			return redirect(routes.SesionJigsawController.index());
		}
	}

	public static Result asignarProblemas(Integer id) {
		SesionJigsaw s = sesionJigsawService.obtenerSesionJigsaw(id);
		return ok(views.html.sesionesjigsaw.asignarProblemas.render(id, s));
	}

	public static Result guardarProblemas(Integer id) {
		
		Form<ParGrupoProblemaForm> form = Form.form(ParGrupoProblemaForm.class).bindFromRequest();
		try {
			SesionJigsaw s = sesionJigsawService.obtenerSesionJigsaw(id);
			//List<ParGrupoExpertoProblema> lista = form.get().getAsignacionProblemas(id);
			//s.setPares(lista);
			s = form.get().definirProblemas(s);
			sesionJigsawService.guardarProblemas(s);
			//sesionJigsawService.actualizarSesionJigsaw(getDocente(), s);
			flash("success", "Asignación de Problemas realizada con éxito");
			return GO_HOME;
		} catch (Exception e) {
			e.printStackTrace();
			flash("error", "No se pudo guardar la Asignación de Problemas");
			return redirect(routes.SesionJigsawController.index());
		}		
	}

	public static Result editarSesionJigsaw(Integer id) {
		SesionJigsaw s = sesionJigsawService.obtenerSesionJigsaw(id);
		return ok(views.html.sesionesjigsaw.editarSesionJigsaw.render(id, s));
	}

	public static Result actualizarSesionJigsaw(Integer id) {
		Form<SesionJigsawForm> form = Form.form(SesionJigsawForm.class)
				.bindFromRequest();
		try {
			SesionJigsaw s = sesionJigsawService.obtenerSesionJigsaw(id);
			if (form.get().actualizar(s) == null) {
				flash("error",
						"No se puede actualizar la sesión jigsaw. Complete todos los campos");
				return editarSesionJigsaw(id);
			} else {
				s = form.get().actualizar(s);
				sesionJigsawService.actualizarSesionJigsaw(getDocente(), s);
				flash("success", "SesionJigsaw actualizada con éxito");
				return GO_HOME;
			}

		} catch (Exception e) {
			e.printStackTrace();
			flash("error", "No se pudo guardar la Sesión Jigsaw");
			return redirect(routes.SesionJigsawController.index());
		}
	}

	public static Result eliminarSesionJigsaw(Integer id) {
		sesionJigsawService.eliminarSesionJigsaw(id);
		flash("success", "Sesión Jigsaw borrada con éxito");
		return GO_HOME;
	}

	/* Clases estaticas FOrm */
	public static class SesionJigsawForm {
		public String tema;

		public String fechaInicioRE;
		public String horaInicioRE;
		public String duracionREHoras;
		public String duracionREMinutos;

		public String fechaInicioRJ;
		public String horaInicioRJ;
		public String duracionRJHoras;
		public String duracionRJMinutos;

		public String totalGruposExpertos;

		public SesionJigsaw entidad() {
			SesionJigsaw s = new SesionJigsaw();
			s.setDocente(getDocente());
			s.setEtapa(EtapaSesionJigsaw.REUNIONEXPERTOS);
			return s;
		}

		public SesionJigsaw actualizar(SesionJigsaw s) {

			s.setTotalGruposExpertos(Integer.parseInt(totalGruposExpertos));
			s.setTema(tema);
			s.setEtapa(EtapaSesionJigsaw.REUNIONEXPERTOS);

			ExpresionDuracion expRE = new ExpresionDuracion(duracionREHoras,
					duracionREMinutos);
			ExpresionDuracion expRJ = new ExpresionDuracion(duracionRJHoras,
					duracionRJMinutos);

			if (existenDatosFechaRE() && existenDatosFechaRJ()
					&& expRE.toMinutos() != 0 && expRJ.toMinutos() != 0) {
				LocalDate fechaBase_RE = FormatoFechaHora
						.obtenerFecha(fechaInicioRE);
				LocalTime horaInicio_RE = FormatoFechaHora
						.obtenerHora(horaInicioRE);
				DateTime tiempoAbsoluto_RE = fechaBase_RE.toDateTime(
						horaInicio_RE, FormatoFechaHora.ZONA_PERU);
				s.setInicioReunionExpertos(tiempoAbsoluto_RE.toDate());
				s.setDuracionReunionExpertos(expRE.toMinutos());

				LocalDate fechaBase_RJ = FormatoFechaHora
						.obtenerFecha(fechaInicioRJ);
				LocalTime horaInicio_RJ = FormatoFechaHora
						.obtenerHora(horaInicioRJ);
				DateTime tiempoAbsoluto_RJ = fechaBase_RJ.toDateTime(
						horaInicio_RJ, FormatoFechaHora.ZONA_PERU);
				s.setInicioReunionJigsaw(tiempoAbsoluto_RJ.toDate());
				s.setDuracionReunionJigsaw(expRJ.toMinutos());
				return s;
			} else {
				s.setInicioReunionExpertos(null);
				s.setInicioReunionJigsaw(null);
				s.setDuracionReunionExpertos(null);
				s.setDuracionReunionJigsaw(null);
				return null;
			}

		}

		public boolean existenDatosFechaRE() {
			return fechaInicioRE != null && fechaInicioRE.length() != 0
					&& horaInicioRE != null && horaInicioRE.length() != 0;
		}

		public boolean existenDatosFechaRJ() {
			return fechaInicioRJ != null && fechaInicioRJ.length() != 0
					&& horaInicioRJ != null && horaInicioRJ.length() != 0;
		}
	}

	public static class ParGrupoProblemaForm{
		/*Lista de IDs de grupos y problemas*/
		public List<Integer> grupos;
		public List<Integer> problemas;
		
		public SesionJigsaw definirProblemas (SesionJigsaw s){			
			s.setPares(getParesGruposProblemas());
			return s;			
		}
		
		private List<GrupoExpertoProblema> getParesGruposProblemas(){
			List<GrupoExpertoProblema> pares = new LinkedList<GrupoExpertoProblema>();
			for (int i=0; i<grupos.size(); i++){
				GrupoExpertoProblema par = new GrupoExpertoProblema();
				par.setGrupoExperto(getGrupoExperto(i));
				par.setProblema(getProblema(i));
				pares.add(par);
			}
			return pares;			
		}
		private Problema getProblema(int i){
			return problemaService.obtenerProblema(problemas.get(i));
		}
		private GrupoExperto getGrupoExperto(int i){
			return grupoExpertoService.obtenerGrupoExperto(grupos.get(i));
		}
	}
	
	/*Módulo ALUMNO*/
	private static Alumno getAlumno() {
		return usuarioService.obtener(Login.obtener(ctx()).getDNI(),
				Alumno.class);
	}
	
	public static Result GO_HOME_ALUMNO = redirect(routes.SesionJigsawController.listForAlumno(0, "id",
			"asc", ""));
	
	public static Result listForAlumno(int page, String sortBy, String order,
			String filter) {
		return ok(views.html.perfilalumno.listaSesionesJigsaw.render(
				sesionJigsawService.pageForAlumno(getAlumno(), page, 10, sortBy, order,
						filter), sortBy, order, filter));
	}
	public static Result indexAlumno(){
		return GO_HOME_ALUMNO;
	}
	
	public static Result interfazFaseExpertos(Integer id){
		SesionJigsaw s = sesionJigsawService.obtenerSesionJigsaw(id);
		Problema p = sesionJigsawService.problemaAResolver(getAlumno(), s);
		if (p != null){
			return ok(views.html.perfilalumno.faseExpertos.render(p));
		}else{
			flash("error", "Ud. no se encuentra asignado a esta sesión jigsaw");
			return GO_HOME_ALUMNO;
		}
		
	}
}










