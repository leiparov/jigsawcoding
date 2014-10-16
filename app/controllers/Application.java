package controllers;

import exceptions.DAOException;
import models.entities.Alumno;
import models.entities.Usuario;
import models.services.Login;
import models.services.UsuarioService;
import play.Routes;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
//import views.html.usuarios.*;

public class Application extends Controller {

    private static UsuarioService usuarioService = new UsuarioService();

    @Login.Requiere
    public static Result index() {
        Login login = Login.obtener(ctx());
        if (login.isTipo(Alumno.class)) {
            return ok(views.html.indexAlumno.render());
        } else {
            return ok(views.html.indexDocente.render());
        }
    }

    public static Result interfazLogin() {
        return ok(views.html.login.render());
    }

    @Login.Requiere
    public static Result interfazPerfilUsuario() {
        return noContent();
    }

    /*@Login.Requiere
    public static Result interfazModificarUsuario() {
        Login login = Login.obtener(ctx());
        if (login.isTipo(Alumno.class)) {
            Usuario usuario = new Usuario();
            UsuarioDAOImpl udi = new UsuarioDAOImpl(); 
            usuario = udi.obtener(login.getDNI());
            return ok(actualizarAlumno.render(usuario));
        } else {
            Docente docente = usuarioService.obtener(login.getDNI(), Docente.class);
            return ok(actualizarDocente.render(docente));
        }
    }*/

    public static Result interfazCambiarContrasenia() {
    	Login login = Login.obtener(ctx());
        if (login.isTipo(Alumno.class))
            return ok(views.html.alumnos.cambiarContrasenia.render());
        else 
            return ok(views.html.docentes.cambiarContrasenia.render());
    }

    public static Result logout() {
        Login.obtener(ctx()).deslogearSesion();
        return redirect(routes.Application.interfazLogin());
    }

    public static Result autenticar() {
        try {
            Form<Login.Form> loginForm = Form.form(Login.Form.class).bindFromRequest();
            Usuario logueado = usuarioService.obtenerLogin(loginForm.get().email, loginForm.get().password);
            Login.obtener(ctx()).logearSesion(logueado);
            return redirect(routes.Application.index());
        } catch (DAOException.FalloLoginException e) {
            flash("error", "Usuario y/o Password incorrectos");
            return badRequest(views.html.login.render());
        }
    }
    
    public static Result recuperarContrasenia() {
    	Form<contraseniaForm> popupForm = Form.form(contraseniaForm.class).bindFromRequest();
    	String email = popupForm.get().emailRecuperar;
    	if(email != ""){
    		if(usuarioService.obtener(email) != 0){
    			usuarioService.recuperarContrasenia(email);
    			flash("successRecuperar", "Su nueva contraseña ha sido enviada a su correo.");
    		}
    		else
    			flash("errorRecuperar", "El correo ingresado no pertenece a un usuario.");
    	}
    	else{
    		flash("errorRecuperar", "No ha ingresado su correo para recuperar su contraseña.");
    	}
        return ok(views.html.login.render());
    }
    
    public static Result jsRoutes(){
        response().setContentType("text/javascript");
        return ok(Routes.javascriptRouter("jsRoutes", 
                    controllers.routes.javascript.Alumnos.buscarAlumnos(),
                    controllers.routes.javascript.Alumnos.disponibles(),
                    controllers.routes.javascript.Problemas.buscarProblemas(),
                    controllers.routes.javascript.Examenes.renderPreguntaEdicion()
                ));
    }

    //clases estaticas para los formularios
    public static class contraseniaForm{
        public String emailRecuperar; 
    }
}
