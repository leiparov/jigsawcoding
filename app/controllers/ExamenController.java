package controllers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import models.entities.Alumno;
import models.entities.Docente;
import models.entities.Examen;
import models.entities.Problema;
import models.entities.ProblemaExamen;
import models.entities.SesionJigsaw;
import models.services.AlumnoService;
import models.services.DocenteService;
import models.services.ExamenService;
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
public class ExamenController extends Controller {

	private static UsuarioService usuarioService = new UsuarioService();
	private static DocenteService docenteService = new DocenteService();
	private static AlumnoService alumnoService = new AlumnoService();
	private static ExamenService examenService = new ExamenService();
	private static ProblemaService problemaService = new ProblemaService();
	private static SesionJigsawService sesionJigsawService = new SesionJigsawService();

	private static Docente getDocente() {
		return usuarioService.obtener(Login.obtener(ctx()).getDNI(),
				Docente.class);
	}
	public static Result GO_HOME = redirect(routes.ExamenController.list(0,
			"id", "asc", ""));
	public static Result list(int page, String sortBy, String order,
			String filter) {
		return ok(views.html.examenes.indexExamenes.render(examenService.page(
				getDocente(), page, 10, sortBy, order, filter), sortBy, order,
				filter));
	}
	public static Result index() {
		Login login = Login.obtener(ctx());
		if (login.isTipo(Alumno.class)) {
			return redirect(routes.ExamenController.indexAlumno());
		} else {
			return GO_HOME;
		}

	}
	public static Result interfazNuevo() {
		return ok(views.html.examenes.nuevoExamen.render(null));
	}
	public static Result interfazEditar(Integer id) {
		Examen e = examenService.obtener(id);
		return ok(views.html.examenes.nuevoExamen.render(e));
	}
	public static Result interfazDefinirHorario(Integer id) {
		Docente d = getDocente();
		Examen e = examenService.obtener(id);
		List<SesionJigsaw> sesionesJigsaw = d.getSesionesJigsaw();
		return ok(views.html.examenes.definirHorarioExamen.render(e,
				sesionesJigsaw));

	}

	public static Result definirHorario(Integer id) {
		Form<HorarioForm> form = Form.form(HorarioForm.class).bindFromRequest();
		Examen e = examenService.obtener(id);
		examenService.actualizarHorario(form.get().actualizar(e));
		flash("success", "Cambios guardados con éxito");
		return SesionJigsawController.GO_HOME;
	}

	public static Result crearExamen() {
		try {
			Form<ExamenForm> form = Form.form(ExamenForm.class)
					.bindFromRequest();
			Login login = Login.obtener(ctx());
			Docente d = docenteService.obtener(login.getDNI());
			Examen e = form.get().obtenerExamen();
			e.setDocente(d);
			examenService.crear(e);
			flash("success", e.getTitulo() + " creado con éxito");
			return redirect(routes.ExamenController.index());
		} catch (ExamenForm.Exception e) {
			flash("error", e.getMessage());
			return redirect(routes.ExamenController.index());
		}
	}
	public static Result guardarExamen(Integer id) {
		try {
			Form<ExamenForm> form = Form.form(ExamenForm.class)
					.bindFromRequest();
			Examen e = examenService.obtener(id);
			e = form.get().modificarExamen(e);
			examenService.modificar(e);
			flash("success", e.getTitulo() + " modificado con éxito");
			return redirect(routes.ExamenController.index());
		} catch (ExamenForm.Exception e) {
			flash("error", e.getMessage());
			return redirect(routes.ExamenController.index());
		}
	}

	public static Result eliminarExamen(Integer id) {
		examenService.eliminar(id);
		flash("success", "Examen borrado con éxito");
		return GO_HOME;
	}

	public static Result renderPreguntaEdicion(Integer idPregunta) {
		Problema p = problemaService.obtenerProblema(idPregunta);
		ProblemaExamen pe = new ProblemaExamen();
		pe.setProblema(p);
		return ok(views.html.examenes.contenedorPreguntaEdicion.render(pe));
	}

	public static class ExamenForm {
		public String titulo;
		public List<Integer> idPregunta;// guarda los id de los problemas que
										// son
										// asignados al examen
		public List<Integer> puntajeCorrecto;
		public List<Integer> puntajeIncorrecto;
		public class Exception extends RuntimeException {

			private static final long serialVersionUID = 1L;

			public Exception(String message) {
				super("Error al procesar: " + message);
			}
		}
		public Examen obtenerExamen() {
			Examen e = new Examen();
			String newstring = new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date());
			titulo = "Examen "+ newstring;
			e.setTitulo(getTitulo());
			e.setProblemas(getProblemas());
			return e;
		}

		public Examen modificarExamen(Examen e) {
			e.setTitulo(getTitulo());
			e.setProblemas(getProblemas());
			return e;
		}

		public String getTitulo() {
			if (titulo != null && !titulo.isEmpty())
				return titulo;
			else
				throw new Exception("Título vacio");
		}

		private List<ProblemaExamen> getProblemas() {
			List<ProblemaExamen> preguntas = new LinkedList<ProblemaExamen>();
			for (int i = 0; i < idPregunta.size(); i++) {
				ProblemaExamen p = new ProblemaExamen();
				p.setProblema(getProblema(i));

				p.setPuntajeFavor(getPuntajeFavor(i));
				p.setPuntajeContra(getPuntajeContra(i));
				preguntas.add(p);
			}
			return preguntas;
		}

		public Problema getProblema(int id) {
			return problemaService.obtenerProblema(idPregunta.get(id));
		}
		public int getPuntajeFavor(int i) {
			Integer puntaje = puntajeCorrecto.get(i);
			if (puntaje != null && puntaje > 0)
				return puntaje;
			else if (puntaje == null)
				throw new Exception("Puntaje a favor no especificado");
			else
				throw new Exception("Puntaje inválido, debe ser mayor a cero");
		}
		public int getPuntajeContra(int i) {
			Integer puntaje = puntajeIncorrecto.get(i);
			if (puntaje != null && puntaje < getPuntajeFavor(i))
				return puntaje;
			else if (puntaje == null)
				return 0;
			else
				throw new Exception(
						"Puntaje en contra mayor al puntaje a favor");
		}
	}

	public static class HorarioForm {
		public String fecha;
		public String tiempoApertura;
		public String tiempoClausura;
		public String duracionHoras;
		public String duracionMinutos;
		public Integer sesionJigsaw;

		public Examen actualizar(Examen e) {
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

				e.setTiempoApertura(tiempoAbsolutoApertura.toDate());
				e.setTiempoClausura(tiempoAbsolutoClausura.toDate());
			} else {
				e.setTiempoApertura(null);
				e.setTiempoClausura(null);
			}

			ExpresionDuracion exp = new ExpresionDuracion(duracionHoras,
					duracionMinutos);
			if (exp.toMinutos() != 0) {
				e.setDuracion(exp.toMinutos());
			} else {
				e.setDuracion(null);
			}
			return e;
		}

		public boolean existenDatosFecha() {
			return fecha != null && fecha.length() != 0
					&& tiempoApertura != null && tiempoApertura.length() != 0
					&& tiempoClausura != null && tiempoClausura.length() != 0;
		}
	}

	/* Módulo Alumno */
	private static Alumno getAlumno() {
		return usuarioService.obtener(Login.obtener(ctx()).getDNI(),
				Alumno.class);
	}

	public static Result indexAlumno() {
		Alumno a = getAlumno();
		play.Logger.info(a.toString());
		List<Examen> examenes = alumnoService.obtenerExamenes(a);
		return ok(views.html.perfilalumno.indexExamenesAlumno.render(examenes,
				a));
	}

	public static Result interfazRendir(Integer id) {
		try {
			Examen e = examenService.obtener(id);
			Alumno a = getAlumno();
			boolean existeNotaExamen = examenService.existeNotaExamen(a, e);
			boolean rindioExamen = examenService.yaRindioExamen(a, e);
			if (!rindioExamen) {
				return ok(views.html.examenes.rendirExamen.render(new SesionJigsaw(), e, a));
			} else {
				if (!existeNotaExamen) {
					flash("success", "Su examen aún no ha sido evaluado");
					return redirect(routes.ExamenController.indexAlumno());
				} else {
					flash("success", "Usted ya rindió este examen.");
					return interfazResultados(id);
				}
			}
		} catch (Exception e) {
			flash("error", "Resultados: " + e.getMessage());
			return indexAlumno();
		}		
	}

	public static Result interfazResultados(Integer id) {
		try {
			// Examen e = examenService.obtener(id);
			// Alumno a = getAlumno();
			// NotaAlumno n = examenService.obtenerNotaAlumno(a.getDNI(),
			// e.getId());
			// List<RespuestasAlumno> respuestas =
			// examenService.obtenerRespuestasExamen(n);
			// return ok(views.html.examenes.mostrarResultados.render(n,
			// respuestas));
			return TODO;
		} catch (Exception e) {
			flash("error", "Resultados: " + e.getMessage());
			return indexAlumno();
		}
	}
	public static Result finalizarExamen(Integer examenid, Integer dnialumno) {
		try {
			Form<RendirExamenForm> form = Form.form(RendirExamenForm.class)
					.bindFromRequest();
			List<String> respuestas = form.get().getRespuestas();
			play.Logger.info("FinalizarExamen : " + respuestas.toString());
			Alumno a = getAlumno();
			examenService.finalizarExamen(a, respuestas);
			flash("success", "Su examen ha sido guardado correctamente: ");
			return indexAlumno();
		} catch (Exception e) {
			e.printStackTrace();
			flash("error", "Error en Finalizar examen: " + e.getMessage());
			return indexAlumno();
		}
	}
	public static Result firepadExamenJs(String firepadid, String userid) {
		return ok(views.js.examenes.firepadExamen.render(firepadid, userid));
	}
	public static class RendirExamenForm {
		public List<String> respuestas = new ArrayList<>();
		public List<String> getRespuestas() {
			return respuestas;
		}
	}
}
