package controllers;

import java.util.LinkedList;
import java.util.List;

import models.entities.Docente;
import models.entities.Examen;
import models.entities.Problema;
import models.entities.ProblemaExamen;
import models.services.DocenteService;
import models.services.ExamenService;
import models.services.Login;
import models.services.ProblemaService;
import models.services.UsuarioService;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import controllers.Examenes.ExamenForm;

public class Examenes extends Controller {

	private static UsuarioService usuarioService = new UsuarioService();
	private static DocenteService docenteService = new DocenteService();
	private static ExamenService examenService = new ExamenService();
	private static ProblemaService problemaService = new ProblemaService();
	
	private static Docente getDocente() {
		return usuarioService.obtener(Login.obtener(ctx()).getDNI(),
				Docente.class);
	}
	public static Result GO_HOME = redirect(routes.Examenes.list(0, "id",
			"asc", ""));
	public static Result list(int page, String sortBy, String order,
			String filter) {
		return ok(views.html.examenes.indexExamenes.render(examenService.page(getDocente(), page,
				10, sortBy, order, filter), sortBy, order, filter));
	}
	public static Result index() {
		return GO_HOME;
	}
	public static Result interfazNuevo() {
		return ok(views.html.examenes.nuevoExamen.render(null));
	}
	public static Result interfazEditar(Long id) {
		Examen e = examenService.obtener(id);
		return ok(views.html.examenes.nuevoExamen.render(e));
	}
	public static Result interfazDefinirHorario(Long id) {
		return TODO;
	}
	public static Result crearExamen(){
		try {
            Form<ExamenForm> form = Form.form(ExamenForm.class).bindFromRequest();
            Login login = Login.obtener(ctx());
            Docente d = docenteService.obtener(login.getDNI());
            Examen e = form.get().obtenerExamen();
            e.setDocente(d);
            examenService.crear(e);
            flash("success", e.getTitulo() + " creado con éxito");
            return redirect(routes.Examenes.index());
        } catch (ExamenForm.Exception e) {
            flash("error", e.getMessage());
            return redirect(routes.Examenes.index());
        }
	}
	public static Result guardarExamen(Long id){
		try {
			Form<ExamenForm> form = Form.form(ExamenForm.class).bindFromRequest();
			Examen e = examenService.obtener(id);
			e = form.get().modificarExamen(e);
			examenService.modificar(e);
			flash("success", e.getTitulo() + " modificado con éxito");
            return redirect(routes.Examenes.index());
		} catch (ExamenForm.Exception e) {
			flash("error", e.getMessage());
            return redirect(routes.Examenes.index());
		}
	}
	public static Result renderPreguntaEdicion(Long idPregunta) {
		Problema p = problemaService.obtenerProblema(idPregunta);
		//Pregunta p = preguntaService.obtener(idPregunta);
        ProblemaExamen pe = new ProblemaExamen();
        pe.setProblema(p);
        return ok(views.html.examenes.contenedorPreguntaEdicion.render(pe));
    }
	
	public static class ExamenForm{
		public String titulo;
		public List<Long> idPregunta;//guarda los id de los problemas que son asignados al examen
		public List<Integer> puntajeCorrecto;
        public List<Integer> puntajeIncorrecto;
        public class Exception extends RuntimeException {
            public Exception(String message) {
                super("Error al procesar: " + message);
            }
        }
        public Examen obtenerExamen(){
        	Examen e = new Examen();
        	e.setTitulo(getTitulo());
        	e.setProblemas(getProblemas());
        	return e;
        }
        
        public Examen modificarExamen(Examen e){
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
                // TODO: lanzar excepcion si vacios
            	ProblemaExamen p = new ProblemaExamen();
                p.setProblema(getProblema(i));
            	
                p.setPuntajeFavor(getPuntajeFavor(i));
                p.setPuntajeContra(getPuntajeContra(i));
                preguntas.add(p);
            }
            return preguntas;
        }
        
        public Problema getProblema(int id){
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
                throw new Exception("Puntaje en contra mayor al puntaje a favor");
        }
	}

}
