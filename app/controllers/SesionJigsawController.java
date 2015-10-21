package controllers;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import models.entities.Alumno;
import models.entities.Docente;
import models.entities.Examen;
import models.entities.Grupo;
import models.entities.GrupoExperto;
import models.entities.GrupoJigsaw;
import models.entities.Problema;
import models.entities.RespuestaAlumno;
import models.entities.SesionJigsaw;
import models.services.ExamenService;
import models.services.GrupoService;
import models.services.Login;
import models.services.ProblemaService;
import models.services.SesionJigsawService;
import models.services.UsuarioService;
import models.services.ideone.IdeoneSubmissionDetails;

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
	private static GrupoService grupoService = new GrupoService();
	private static ProblemaService problemaService = new ProblemaService();
	private static ExamenService examenService = new ExamenService();

	private static Docente getDocente() {
		return usuarioService.obtener(Login.obtener(ctx()).getDNI(),
				Docente.class);
	}

	/* Módulo DOCENTE */
	public static Result GO_HOME = redirect(routes.SesionJigsawController.list(
			0, "id", "asc", ""));

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

			sesionJigsawService.guardarSesionJigsaw(getDocente(), s);
			flash("success", "SesionJigsaw registrada con éxito");
			return GO_HOME;
			// return asignarProblemas(id)

		} catch (Exception e) {
			e.printStackTrace();
			flash("error", "No se pudo guardar la Sesión Jigsaw");
			return redirect(routes.SesionJigsawController.index());
		}
	}
	/* Asisgnar Alumnos a Sesion Jigsaw */
	public static Result interfazAsignarAlumnos(Integer id) {
		SesionJigsaw s = sesionJigsawService.obtenerSesionJigsaw(id);
		return ok(views.html.sesionesjigsaw.asignarAlumnos.render(s));
	}
	public static Result guardarAlumnos(Integer id) {
		try {
			Form<AsignarAlumnosForm> form = Form.form(AsignarAlumnosForm.class)
					.bindFromRequest();
			SesionJigsaw s = sesionJigsawService.obtenerSesionJigsaw(id);
			List<String> dnialumnos = form.get().alumnos;
			sesionJigsawService.guardarAlumnos(s, dnialumnos);
			flash("success", "Alumnos asignados con éxito");
			return GO_HOME;
		} catch (Exception e) {
			flash("error", "Error: " + e.getMessage());
			return interfazAsignarAlumnos(id);
		}
	}
	public static class AsignarAlumnosForm {
		public List<String> alumnos = new LinkedList<String>();
	}

	/* Generar Grupos */
	public static Result generarGrupos(Integer id) {
		try {
			SesionJigsaw s = sesionJigsawService.obtenerSesionJigsaw(id);
			sesionJigsawService.generarGrupos(s);

			flash("success", "Grupos generados exitosamente");
			return verGrupos(id);
			// views.html.sesionesjigsaw.gruposExpertos.render(s));
		} catch (Exception e) {
			flash("error", "Error: " + e.getMessage());
			return GO_HOME;
		}
	}
	public static Result verGrupos(Integer id) {
		SesionJigsaw s = sesionJigsawService.obtenerSesionJigsaw(id);
		return ok(views.html.sesionesjigsaw.grupos.render(s));
	}
	public static Result eliminarGrupos(Integer id) {
		sesionJigsawService.eliminarGruposExpertos(id);
		sesionJigsawService.eliminarGruposJigsaw(id);
		flash("success", "Grupos eliminados correctamente");
		return GO_HOME;
	}

	/* Asignar problemas */
	public static Result asignarProblemas(Integer id) {
		SesionJigsaw s = sesionJigsawService.obtenerSesionJigsaw(id);
		return ok(views.html.sesionesjigsaw.asignarProblemas.render(id, s));
	}

	public static Result guardarProblemas(Integer id) {

		Form<ParGrupoProblemaForm> form = Form.form(ParGrupoProblemaForm.class)
				.bindFromRequest();
		try {
			SesionJigsaw s = sesionJigsawService.obtenerSesionJigsaw(id);
			// List<ParGrupoExpertoProblema> lista =
			// form.get().getAsignacionProblemas(id);
			// s.setPares(lista);
			HashMap<GrupoExperto, Problema> pares = form.get()
					.asignacionProblemas();
			sesionJigsawService.guardarProblemas(s, pares);
			// sesionJigsawService.actualizarSesionJigsaw(getDocente(), s);
			flash("success", "Asignación de Problemas realizada con éxito");
			return GO_HOME;
		} catch (Exception e) {
			e.printStackTrace();
			flash("error", "No se pudo guardar la Asignación de Problemas");
			return redirect(routes.SesionJigsawController.index());
		}
		// return TODO;
	}

	public static Result editarSesionJigsaw(Integer id) {
		SesionJigsaw s = sesionJigsawService.obtenerSesionJigsaw(id);
		List<Examen> examenes = examenService.obtenerExamenes();
		play.Logger.info("Editar sesion jigsaw - examenes " + examenes.size());
		return ok(views.html.sesionesjigsaw.editarSesionJigsaw.render(id, s,
				examenes));
	}

	public static Result definirHorarioReunionExpertos(Integer id) {
		SesionJigsaw s = sesionJigsawService.obtenerSesionJigsaw(id);
		return ok(views.html.sesionesjigsaw.definirHorarioReunionExpertos
				.render(s));
	}
	public static Result definirHorarioReunionJigsaw(Integer id) {
		SesionJigsaw s = sesionJigsawService.obtenerSesionJigsaw(id);
		return ok(views.html.sesionesjigsaw.definirHorarioReunionJigsaw
				.render(s));
	}
	public static Result definirHorarioExamen(Integer id) {
		SesionJigsaw s = sesionJigsawService.obtenerSesionJigsaw(id);
		return ok(views.html.sesionesjigsaw.definirHorarioExamen.render(s));
	}

	public static Result actualizarSesionJigsaw(Integer id) {
		Form<SesionJigsawForm> form = Form.form(SesionJigsawForm.class)
				.bindFromRequest();
		try {
			SesionJigsaw s = sesionJigsawService.obtenerSesionJigsaw(id);
			s = form.get().editar(s);
			sesionJigsawService.actualizarSesionJigsaw(getDocente(), s);
			flash("success", "SesionJigsaw actualizada con éxito");
			return GO_HOME;

		} catch (Exception e) {
			e.printStackTrace();
			flash("error", "No se pudo guardar la Sesión Jigsaw");
			return GO_HOME;
		}
	}
	public static Result guardarHorarioReunionExpertos(Integer id) {
		Form<SesionJigsawForm> form = Form.form(SesionJigsawForm.class)
				.bindFromRequest();
		try {
			SesionJigsaw s = sesionJigsawService.obtenerSesionJigsaw(id);
			s = form.get().definirHorarioReunionExpertos(s);
			sesionJigsawService.actualizarSesionJigsaw(getDocente(), s);
			flash("success", "SesionJigsaw actualizada con éxito");
			return GO_HOME;

		} catch (Exception e) {
			e.printStackTrace();
			flash("error", "No se pudo guardar la Sesión Jigsaw");
			return GO_HOME;
		}
	}
	public static Result guardarHorarioReunionJigsaw(Integer id) {
		Form<SesionJigsawForm> form = Form.form(SesionJigsawForm.class)
				.bindFromRequest();
		try {
			SesionJigsaw s = sesionJigsawService.obtenerSesionJigsaw(id);
			s = form.get().definirHorarioReunionJigsaw(s);
			sesionJigsawService.actualizarSesionJigsaw(getDocente(), s);
			flash("success", "SesionJigsaw actualizada con éxito");
			return GO_HOME;

		} catch (Exception e) {
			e.printStackTrace();
			flash("error", "No se pudo guardar la Sesión Jigsaw");
			return GO_HOME;
		}
	}
	public static Result guardarHorarioExamen(Integer id) {
		Form<SesionJigsawForm> form = Form.form(SesionJigsawForm.class)
				.bindFromRequest();
		try {
			SesionJigsaw s = sesionJigsawService.obtenerSesionJigsaw(id);
			s = form.get().definirHorarioExamen(s);
			sesionJigsawService.actualizarSesionJigsaw(getDocente(), s);
			flash("success", "SesionJigsaw actualizada con éxito");
			return GO_HOME;

		} catch (Exception e) {
			e.printStackTrace();
			flash("error", "No se pudo guardar la Sesión Jigsaw");
			return GO_HOME;
		}
	}

	public static Result eliminarSesionJigsaw(Integer id) {
		try {
			sesionJigsawService.eliminarSesionJigsaw(id);
			flash("success", "Sesión Jigsaw borrada con éxito");
			return GO_HOME;
		} catch (Exception e) {
			e.printStackTrace();
			flash("error",
					"No se pudo eliminar la Sesión Jigsaw - " + e.getMessage());

			return GO_HOME;
		}
	}
	
	/*Evaluar examen*/
	public static Result corregirExamenes(Integer sesionjigsawid){
		try {
			SesionJigsaw s = sesionJigsawService.obtenerSesionJigsaw(sesionjigsawid);
			List<Alumno> alumnos = s.getAlumnos();
			return ok(views.html.sesionesjigsaw.corregirExamenes.render(s, alumnos));
		} catch (Exception e) {
			e.printStackTrace();
			flash("error", "Error " + e.getMessage());
			return GO_HOME;
		}		
	}
	
	public static Result corregirExamenAlumno(Integer sesionid, String dni, Integer examenid ){
		try {
			Alumno a = usuarioService.obtener(dni, Alumno.class);
			Examen e = examenService.obtener(examenid);
			SesionJigsaw s = sesionJigsawService.obtenerSesionJigsaw(sesionid);
			Map<RespuestaAlumno, IdeoneSubmissionDetails> respuestas = examenService.obtenerRespuestas(a,e);
			//response().setContentType("application/javascript");
			return ok(views.html.sesionesjigsaw.corregirExamenAlumno.render(s, a, e, respuestas));
		} catch (Exception e) {
			e.printStackTrace();
			flash("error", "Error " + e.getMessage());
			return GO_HOME;
		}		
	}
	public static Result guardarPuntaje(String dni, Integer examenid, Integer respuestasAlumnoId){
		try {
			Form<PuntajeForm> form = Form.form(PuntajeForm.class)
					.bindFromRequest();
			Integer puntajeObtenido = form.get().puntajeObtenido;
			
			examenService.guardarPuntaje(respuestasAlumnoId, puntajeObtenido);
			
			Alumno a = usuarioService.obtener(dni, Alumno.class);
			Examen e = examenService.obtener(examenid);
			Map<RespuestaAlumno, IdeoneSubmissionDetails> respuestas = examenService.obtenerRespuestas(a,e);
			//return ok(views.html.sesionesjigsaw.corregirExamenAlumno.render(a, e, respuestas));
			return noContent();
		} catch (Exception e) {
			e.printStackTrace();
			flash("error", "Error " + e.getMessage());
			return GO_HOME;
		}		
	}
	public static Result calificarExamen(Integer sid, String dni, Integer examenid){
		try {
			Form<CalificarExamenForm> form = Form.form(CalificarExamenForm.class)
					.bindFromRequest();
			Map<String, String> puntajes = form.data();
			Alumno a = usuarioService.obtener(dni, Alumno.class);
			Examen e = examenService.obtener(examenid);
			//SesionJigsaw s = sesionJigsawService.obtenerSesionJigsaw(sid);
			play.Logger.info("calificarExamen" + puntajes.toString());
			examenService.calificarExamen(a, e, puntajes);
			flash("success", "Examen calificado exitosamente");
			return redirect(routes.SesionJigsawController.corregirExamenes(sid));
			//return corregirExamenes(sid);
			//return ok(views.html.sesionesjigsaw.corregirExamenAlumno.render(a, e, respuestas));
		} catch (Exception e) {
			e.printStackTrace();
			flash("error", "Error " + e.getMessage());
			return GO_HOME;
		}		
	}

	/* Clases estaticas FOrm */
	public static class PuntajeForm{
		public Integer puntajeObtenido;
	}
	public static class CalificarExamenForm{
		
	}
	
	public static class SesionJigsawForm {
		public String tema;
		public String totalGruposExpertos;

		public String fechaInicioRE;
		public String horaInicioRE;
		public String duracionREHoras;
		public String duracionREMinutos;

		public String fechaInicioRJ;
		public String horaInicioRJ;
		public String duracionRJHoras;
		public String duracionRJMinutos;

		public String examenid;
		public String fecha;
		public String tiempoApertura;
		public String tiempoClausura;
		public String duracionHoras;
		public String duracionMinutos;

		public SesionJigsaw entidad() {
			SesionJigsaw s = new SesionJigsaw();
			s.setDocente(getDocente());
			s.setTema(tema);
			s.setTotalGruposExpertos(Integer.parseInt(totalGruposExpertos));
			return s;
		}

		public SesionJigsaw definirHorarioReunionExpertos(SesionJigsaw s) {
			ExpresionDuracion expRE = new ExpresionDuracion(duracionREHoras,
					duracionREMinutos);
			if (existenDatosFechaRE() && expRE.toMinutos() != 0) {
				LocalDate fechaBase_RE = FormatoFechaHora
						.obtenerFecha(fechaInicioRE);
				LocalTime horaInicio_RE = FormatoFechaHora
						.obtenerHora(horaInicioRE);
				DateTime tiempoAbsoluto_RE = fechaBase_RE.toDateTime(
						horaInicio_RE, FormatoFechaHora.ZONA_PERU);
				s.setInicioReunionExpertos(tiempoAbsoluto_RE.toDate());
				s.setDuracionReunionExpertos(expRE.toMinutos());
			} else {
				s.setInicioReunionExpertos(null);
				s.setDuracionReunionExpertos(null);
			}
			return s;
		}

		public SesionJigsaw definirHorarioReunionJigsaw(SesionJigsaw s) {
			ExpresionDuracion expRJ = new ExpresionDuracion(duracionRJHoras,
					duracionRJMinutos);
			if (existenDatosFechaRJ() && expRJ.toMinutos() != 0) {

				LocalDate fechaBase_RJ = FormatoFechaHora
						.obtenerFecha(fechaInicioRJ);
				LocalTime horaInicio_RJ = FormatoFechaHora
						.obtenerHora(horaInicioRJ);
				DateTime tiempoAbsoluto_RJ = fechaBase_RJ.toDateTime(
						horaInicio_RJ, FormatoFechaHora.ZONA_PERU);
				s.setInicioReunionJigsaw(tiempoAbsoluto_RJ.toDate());
				s.setDuracionReunionJigsaw(expRJ.toMinutos());
			} else {
				s.setInicioReunionJigsaw(null);
				s.setDuracionReunionJigsaw(null);
			}
			return s;
		}

		public SesionJigsaw definirHorarioExamen(SesionJigsaw s) {
			if (existenDatosFecha()) {
				LocalDate fechaBase = FormatoFechaHora.obtenerFecha(fecha);
				LocalTime apertura = FormatoFechaHora
						.obtenerHora(tiempoApertura);
				LocalTime clausura = FormatoFechaHora
						.obtenerHora(tiempoClausura);

				DateTime tiempoAbsolutoApertura = fechaBase.toDateTime(
						apertura, FormatoFechaHora.ZONA_PERU);
				DateTime tiempoAbsolutoClausura = fechaBase.toDateTime(
						clausura, FormatoFechaHora.ZONA_PERU);
				if (clausura.isBefore(apertura))
					tiempoAbsolutoClausura = tiempoAbsolutoClausura.plusDays(1);

				s.setTiempoAperturaExamen(tiempoAbsolutoApertura.toDate());
				s.setTiempoClausuraExamen(tiempoAbsolutoClausura.toDate());
			} else {
				s.setTiempoAperturaExamen(null);
				s.setTiempoClausuraExamen(null);
			}

			ExpresionDuracion exp = new ExpresionDuracion(duracionHoras,
					duracionMinutos);
			if (exp.toMinutos() != 0) {
				s.setDuracionExamen(exp.toMinutos());
			} else {
				s.setDuracionExamen(null);
			}
			return s;
		}

		public SesionJigsaw editar(SesionJigsaw s) {
			s.setTema(tema);
			s.setTotalGruposExpertos(Integer.parseInt(totalGruposExpertos));
			if (examenid != null && examenid != "") {
				Integer id = Integer.parseInt(examenid);
				if (id > 0) {
					Examen e = examenService.obtener(id);
					s.setExamen(e);
				} else {
					s.setExamen(null);
				}
			}
			return s;
		}
		public boolean existenDatosFecha() {
			return fecha != null && fecha.length() != 0
					&& tiempoApertura != null && tiempoApertura.length() != 0
					&& tiempoClausura != null && tiempoClausura.length() != 0;
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

	public static class ParGrupoProblemaForm {
		/* Lista de IDs de grupos y problemas */
		public List<Integer> grupos;
		public List<Integer> problemas;

		public HashMap<GrupoExperto, Problema> asignacionProblemas() {
			HashMap<GrupoExperto, Problema> pares = new HashMap<>();
			for (int i = 0; i < grupos.size(); i++) {
				pares.put(getGrupoExperto(i), getProblema(i));
			}
			return pares;
		}
		private Problema getProblema(int i) {
			return problemaService.obtenerProblema(problemas.get(i));
		}
		private GrupoExperto getGrupoExperto(int i) {
			return grupoService.obtenerGrupoExperto(grupos.get(i));
		}
	}

	/* Módulo ALUMNO */
	private static Alumno getAlumno() {
		return usuarioService.obtener(Login.obtener(ctx()).getDNI(),
				Alumno.class);
	}

	public static Result GO_HOME_ALUMNO = redirect(routes.SesionJigsawController
			.listForAlumno(0, "id", "asc", ""));

	public static Result listForAlumno(int page, String sortBy, String order,
			String filter) {
		return ok(views.html.perfilalumno.indexSesionesJigsaw.render(
				sesionJigsawService.pageForAlumno(getAlumno(), page, 10,
						sortBy, order, filter), sortBy, order, filter));
	}
	public static Result indexAlumno() {
		return GO_HOME_ALUMNO;
	}
	
	private static Date sumarSegundos(Date fecha, int segundos){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(fecha);
		calendar.add(Calendar.SECOND, segundos);
		return calendar.getTime();
	}

	public static Result interfazFaseExpertos(Integer id) {
		try {
			Date fechaActual = new Date();
			SesionJigsaw s = sesionJigsawService.obtenerSesionJigsaw(id);
			Date inicio = s.getInicioReunionExpertos();
			Date fin = sumarSegundos(inicio, s.getDuracionReunionExpertosEnSegundos());
			
			if(fechaActual.before(inicio) || fechaActual.after(fin)){
				String mensaje = "Ud. no puede ingresar a la reunión de Expertos. La reunión aún no empieza o ya ha finalizado";
				return ok(views.html.errors.paginaRestringida.render(mensaje));
			}
			
			
			GrupoExperto gep = sesionJigsawService.grupoExpertoDelAlumno(
					getAlumno(), s);
			play.Logger.info(gep.toString());

			String firepadID = "sj" + s.getId() + "ge" + gep.getId() + "p"
					+ gep.getProblema().getId();
			return ok(views.html.perfilalumno.faseExpertos.render(getAlumno(),
					gep, s));
		} catch (Exception e) {
			e.printStackTrace();
			flash("error", "Error: " + e.getMessage());
			return GO_HOME_ALUMNO;
		}

	}

	public static Result firepadFaseExpertosJs(String firepadid, String userid) {
		return ok(views.js.perfilalumno.firepadFaseExpertos.render(firepadid, userid));
	}
	public static Result firepadFaseJigsawJs(String firepadid, String userid) {
		return ok(views.js.perfilalumno.firepadFaseJigsaw.render(firepadid, userid));
	}
	
	public static Result interfazFaseJigsaw (Integer id){
		try {
			SesionJigsaw s = sesionJigsawService.obtenerSesionJigsaw(id);
			
			Date fechaActual = new Date();
			Date inicio = s.getInicioReunionJigsaw();
			Date fin = sumarSegundos(inicio, s.getDuracionReunionJigsawEnSegundos());
			if(fechaActual.before(inicio) || fechaActual.after(fin)){
				String mensaje = "Ud. no puede ingresar a la reunión Jigsaw. La reunión aún no empieza o ya ha finalizado";
				return ok(views.html.errors.paginaRestringida.render(mensaje));
			}
			GrupoJigsaw gj = sesionJigsawService.grupoJigsawDelAlumno(getAlumno(), s);
			play.Logger.info(gj.toString());
			//String firepadID = "sj" + s.getId() + "gj" + gj.getId();
			return ok(views.html.perfilalumno.faseJigsaw.render(getAlumno(), gj, s));
		} catch (Exception e) {
			e.printStackTrace();
			flash("error", "Error: " + e.getMessage());
			return GO_HOME_ALUMNO;
		}
	}
	public static Result interfazEvaluacion(Integer id){
		try {
			SesionJigsaw s = sesionJigsawService.obtenerSesionJigsaw(id);
			
			Date fechaActual = new Date();
			if(fechaActual.before(s.getTiempoAperturaExamen()) || fechaActual.after(s.getTiempoClausuraExamen())){
				String mensaje = "Acceso al examen restringido. Verifique el horario permitido para rendir la evaluación";
				return ok(views.html.errors.paginaRestringida.render(mensaje));
			}
			
			Examen e = s.getExamen();
			Alumno a = getAlumno();
			boolean existeNotaExamen = examenService.existeNotaExamen(a, e);
			boolean rindioExamen = examenService.yaRindioExamen(a, e);
			if (!rindioExamen) {
				return ok(views.html.examenes.rendirExamen.render(s, e, a));
			} else {
				if (!existeNotaExamen) {
					flash("success", "Su examen aún no ha sido evaluado");
					return GO_HOME_ALUMNO;
				} else {
					flash("success", "Usted ya rindió este examen.");
					return GO_HOME_ALUMNO;
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			flash("error", "Error: " + e.getMessage());
			return GO_HOME_ALUMNO;
		}
	}
	
	public static Result revisarSoluciones(){
		List<SesionJigsaw> sesionesJigsaw = sesionJigsawService.all();
		List<Grupo> grupos = grupoService.all();
		List<Problema> problemas = problemaService.all();
		return ok(views.html.sesionesjigsaw.revisarSoluciones.render(sesionesJigsaw, grupos, problemas));
	}
	public static class SolucionFirepadForm {
		public Integer sesionjigsawid;
		public Integer grupoid;
		public Integer problemaid;
		
		public String getFirepadID (){
			Grupo g = grupoService.obtenerGrupo(grupoid);
			String firepadid ="";
			if(g instanceof GrupoExperto){
				firepadid = "sj"+sesionjigsawid+"ge"+grupoid+"p"+problemaid;
			}
			if(g instanceof GrupoJigsaw){
				firepadid = "sj"+sesionjigsawid+"gj"+grupoid+"p"+problemaid;
			}
			return firepadid;
		}
	}
	public static Result mostrarSolucionFirepad(){
		try {
			Form<SolucionFirepadForm> form = Form.form(SolucionFirepadForm.class)
					.bindFromRequest();
			String firepadid = form.get().getFirepadID();
			return ok(views.html.sesionesjigsaw.mostrarSolucionFirepad.render(firepadid));
		} catch (Exception e) {
			flash("error", "Error");
			return revisarSoluciones();
		}
	}
	public static Result firepadMostrarJs(String url) {
		return ok(views.js.sesionesjigsaw.firepadMostrar.render(url));
	}
}
