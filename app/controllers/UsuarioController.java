package controllers;
import javax.persistence.PersistenceException;

import models.entities.Alumno;
import models.entities.Docente;
import models.entities.Sexo;
import models.entities.Usuario;
import models.services.AlumnoService;
import models.services.DocenteService;
import models.services.UsuarioService;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
//import views.html.usuarios.actualizarDocente;
import views.html.usuarios.nuevoUsuario;
import exceptions.DAOException;

public class UsuarioController extends Controller{   
   
   private static UsuarioService usuarioService = new UsuarioService();
   private static DocenteService docenteService = new DocenteService();
   private static AlumnoService alumnoService = new AlumnoService();
   private static final String carInvalidos = "|1234567890'!\"#$%&/()=?\\"
                                             +"@+{},.-<>;:_[]*^`~";
   
   public static Result interfazNuevo(String email) {
	   return ok(nuevoUsuario.render(email));	 
	   //return TODO;
   }
   public static Result nuevoUsuario(String email) {	   
	   return redirect(routes.UsuarioController.interfazNuevo(email));
	   //return ok(nuevoUsuario.render(email));	   
   }
   
   public static Result registrarUsuario(){
	   String email = "";
      try{
         Form<UsuarioForm> form = Form.form(UsuarioForm.class).bindFromRequest();
         int tipoUsuario = form.get().tipo;
         email = form.get().email;
         Usuario usuario;
         if (tipoUsuario == 0) {
        	 usuario = form.get().entidadDocenteNueva();
         }else{
        	 usuario = form.get().entidadAlumnoNueva();
         }
         if(!validarNombre(usuario.getNombreCompleto())){
            throw new DAOException("Caracteres inválidos en Nombres y/o Apellidos.");
         }
         if((usuario.getDNI()+"").length() != 8){
            throw new IllegalStateException();
         }
         
         try{
            usuarioService.obtener(usuario.getEmail());
         }catch(DAOException e){ 
        	if (tipoUsuario == 0) docenteService.guardar((Docente)usuario);
        	else alumnoService.guardarAlumno((Alumno) usuario);
            flash("success", "Su cuenta ha sido creada correctamente.");
            return redirect(routes.Application.interfazLogin());
         }
         throw new DAOException("Email ya registrado, por favor ingrese otro.");
      }catch(DAOException daoe){
         flash("error", daoe.getMessage());
         return redirect(routes.UsuarioController.interfazNuevo(email));
      }catch(PersistenceException pe){
         flash("error", "Error al registrar: DNI ya existente.");
         return redirect(routes.UsuarioController.interfazNuevo(email));
      }catch(IllegalStateException ise){
         flash("error", "Error en DNI: Por favor ingrese 8 digitos.");
         return redirect(routes.UsuarioController.interfazNuevo(email));
      }catch(Exception e){
         e.printStackTrace();
         flash("error", "Error desconocido: Posiblemente datos no validos.");
         return redirect(routes.UsuarioController.interfazNuevo(email));
      }
   }
   
   
  
   
   private static boolean validarNombre(String nombre){
      for (int i = 0; i < nombre.length(); i++) {
         if(carInvalidos.contains(nombre.charAt(i)+"")){
            return false;
         }
      }
      return true;
   }
   
   
   
   
   //clases estaticas para los formularios
   public static class UsuarioForm{
      public int dni;
      public String email;
      public String nombre;
      public String apellidoPaterno;
      public String apellidoMaterno;
      public Sexo sexo;
      public int tipo; //0: Docente, 1:Alumno
      
      public Docente entidadDocenteNueva() {
         Docente docente = new Docente();
         docente.setDNI(dni);
         docente.setEmail(email);
         docente.setNombres(nombre);
         docente.setApellidoPaterno(apellidoPaterno);
         docente.setApellidoMaterno(apellidoMaterno);
         docente.setSexo(sexo);         
         return docente;
      }
      
      public Alumno entidadAlumnoNueva() {
          Alumno alumno = new Alumno();
          alumno.setDNI(dni);
          alumno.setEmail(email);
          alumno.setNombres(nombre);
          alumno.setApellidoPaterno(apellidoPaterno);
          alumno.setApellidoMaterno(apellidoMaterno);
          alumno.setSexo(sexo);         
          return alumno;
       } 
      public void entidadModificada(Usuario usuario){
    	 usuario.setEmail(email);
    	 usuario.setNombres(nombre);
    	 usuario.setApellidoPaterno(apellidoPaterno);
    	 usuario.setApellidoMaterno(apellidoMaterno);
    	 usuario.setSexo(sexo);
      }
   }
	   
   
}