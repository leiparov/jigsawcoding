package controllers;

import models.entities.Docente;
import models.entities.Problema;
import models.services.ProblemaService;
import models.services.UsuarioService;
import models.services.impl.ProblemaServiceImpl;
import models.services.impl.UsuarioServiceImpl;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import utils.Login;
import views.html.problemas.nuevoProblema;

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
	
	public static Result index(){
		return interfazNuevo();
	}
	public static Result interfazNuevo(){
		return ok(nuevoProblema.render());
	}
	
	public static Result registrarProblema(){
		try {
			Form<ProblemaForm> form = Form.form(ProblemaForm.class).bindFromRequest();
			Problema problema = form.get().entidad();
			problemaService.guardarProblema(getDocente(), problema);
			flash("success", "Problema registrado con éxito");
			return redirect(routes.Problemas.index());
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		return noContent();
	}
	
}
