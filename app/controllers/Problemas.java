package controllers;

import models.entities.Docente;
import models.entities.Problema;
import models.services.ProblemaService;
import models.services.UsuarioService;
import models.services.impl.ProblemaServiceImpl;
import models.services.impl.UsuarioServiceImpl;
import static play.data.Form.*;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import utils.Login;
import views.html.problemas.indexProblemas;
import views.html.problemas.nuevoProblema;
import views.html.problemas.listaProblemas;
import views.html.problemas.editarProblema;

@Login.Requiere
public class Problemas extends Controller{
	
	private static UsuarioService usuarioService = new UsuarioServiceImpl();
	private static ProblemaService problemaService = new ProblemaServiceImpl();
	
	public static class ProblemaForm {
		public String titulo;
		public String enunciado;
		
		public Problema entidad(){
			Problema nuevo = new Problema();
			nuevo.setTitulo(titulo);
			nuevo.setEnunciado(enunciado);
			nuevo.setDocente(getDocente());
			
			return nuevo;
		}
		
	}
	
	private static Docente getDocente() {
        return usuarioService.obtener(Login.obtener(ctx()).getDNI(), Docente.class);
    }
	
	public static Result GO_HOME = redirect(routes.Problemas.list(0, "problemaId", "asc", ""));
	/**
     * Display the paginated list of problemas.
     * 
     * @param page
     *            Current page number (starts from 0)
     * @param sortBy
     *            Column to be sorted
     * @param order
     *            Sort order (either asc or desc)
     * @param filter
     *            Filter applied on Problema names
     */
    public static Result list(int page, String sortBy, String order, String filter) {
        return ok(listaProblemas.render(problemaService.page(getDocente(), page, 10, sortBy, order, filter), sortBy, order, filter));
    }
	
	
	public static Result index(){
		//return ok(indexProblemas.render());
		return GO_HOME;
	}
	public static Result interfazNuevo(){
		Form<Problema> problemaForm = form(Problema.class).bindFromRequest();
		return ok(nuevoProblema.render(problemaForm));
	}
	
	public static Result registrarProblema(){
		Form<Problema> problemaForm = form(Problema.class).bindFromRequest();
		if(problemaForm.hasErrors()){
			return badRequest(nuevoProblema.render(problemaForm));
		}
		try {
			//Form<ProblemaForm> form = Form.form(ProblemaForm.class).bindFromRequest();
			
			//Problema problema = form.get().entidad();
			Problema problema = problemaForm.get();
			problema.setDocente(getDocente());
			problemaService.guardarProblema(getDocente(), problema);
			flash("success", "Problema registrado con éxito");
			return GO_HOME;
			
		} catch (Exception e) {
			e.printStackTrace();
			flash("error", "No se pudo guardar el Problema");
			return redirect(routes.Application.index());
		}
		
	}
	
	public static Result editarProblema(Long id){
		Form<Problema> problemaForm = form(Problema.class).fill(problemaService.obtenerProblema(id));
		return ok(editarProblema.render(id, problemaForm));
	}
	
	public static Result actualizarProblema(Long id){
		Form<Problema> problemaForm = form(Problema.class).bindFromRequest();
		if(problemaForm.hasErrors()){
			return badRequest(editarProblema.render(id, problemaForm));
		}
		try {
			//Form<ProblemaForm> form = Form.form(ProblemaForm.class).bindFromRequest();			
			//Problema problema = form.get().entidad();
			Problema problema = problemaForm.get();
			problema.setDocente(getDocente());
			problema.setProblemaId(id);
			System.out.println(problema.toString());
			problemaService.actualizarProblema(getDocente(), problema);
			flash("success", "Problema actualizado con éxito");
			return GO_HOME;
			
		} catch (Exception e) {
			e.printStackTrace();
			flash("error", "No se pudo actualizar el Problema");
			return redirect(routes.Application.index());
		}
		//return noContent();
	}
	
	public static Result eliminarProblema(Long id){
		problemaService.eliminarProblema(id);
		flash("success", "Problema borrado con éxito");		
		return GO_HOME;
	}
	
	
	
}
