package controllers;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import javax.persistence.PersistenceException;
import models.daos.DAOException;
import models.entities.Docente;
import models.entities.Sexo;
import models.entities.Usuario;
import models.services.DocenteService;
import models.services.UsuarioService;
import models.services.impl.DocenteServiceImpl;
import models.services.impl.UsuarioServiceImpl;
import play.data.Form;
/*import views.html.docentes.nuevoDocente;
import views.html.usuarios.actualizarDocente;*/
import utils.Login;

public class Docentes extends Controller{   
   
   private static DocenteService docenteService = new DocenteServiceImpl();
   private static UsuarioService usuarioService = new UsuarioServiceImpl();
   private static final String carInvalidos = "|1234567890'!\"#$%&/()=?\\"
                                             +"@+{},.-<>;:_[]*^`~";
   private static final String ruta = "public/photos/";
   
   public static Result interfazNuevo() {
      //return ok(nuevoDocente.render());
	   return noContent();
   }
   
   /*public static Result registrarDocente(){
      try{
         Form<DocenteForm> form = Form.form(DocenteForm.class).bindFromRequest();
         Docente docente = form.get().entidadNueva();
         
         if(!validarNombre(docente.getNombreCompleto())){
            throw new DAOException("Caracteres inválidos en Nombres y/o Apellidos.");
         }
         if((docente.getDNI()+"").length() != 8){
            throw new IllegalStateException();
         }
         if(!docente.getPassword().equals(form.get().contrasena2)){
            throw new DAOException("Las contraseñas no coinciden.");
         }
         try{
            usuarioService.obtener(docente.getEmail());
         }catch(DAOException e){
            if(guardarFoto(docente.getDNI())){
               docente.setFoto(true);
            }
            docenteService.guardar(docente);
            flash("success", "Su cuenta ha sido creada correctamente.");
            return redirect(routes.Application.interfazLogin());
         }
         throw new DAOException("Email ya registrado, por favor ingrese otro.");
      }catch(DAOException daoe){
         flash("error", daoe.getMessage());
         return redirect(routes.Docentes.interfazNuevo());
      }catch(PersistenceException pe){
         flash("error", "Error al registrar: DNI ya existente.");
         return redirect(routes.Docentes.interfazNuevo());
      }catch(IllegalStateException ise){
         flash("error", "Error en DNI: Por favor ingrese 8 digitos.");
         return redirect(routes.Docentes.interfazNuevo());
      }catch(Exception e){
         e.printStackTrace();
         flash("error", "Error desconocido: Posiblemente datos no validos.");
         return redirect(routes.Docentes.interfazNuevo());
      }
   }*/
   /*@Login.Requiere
   public static Result actualizarDocente(){
      try{
         Form<DocenteForm> form = Form.form(DocenteForm.class).bindFromRequest();
         Docente docente = getDocente();
         String emailAntiguo = docente.getEmail();
         
         form.get().entidadModificada(docente);
         if(!validarNombre(docente.getNombreCompleto())){
            throw new DAOException("Caracteres inválidos en Nombres y/o Apellidos.");
         }
         try{
            if(emailAntiguo.equals(docente.getEmail())){
               throw new DAOException();
            }
            usuarioService.obtener(docente.getEmail());
         }catch(DAOException e){
            if(guardarFoto(docente.getDNI())){
               docente.setFoto(true);
            }
            docenteService.guardar(docente);
            // No encuentro otra forma de actualizar los datos en la vista
            Login.obtener(ctx()).logearSesion(docente);
            return redirect(routes.Application.index());
         }
         throw new DAOException("Email ya registrado, porfavor ingrese otro.");
      }catch(DAOException daoe) {
         flash("error", "Error al registrar: " + daoe.getMessage());
         return redirect(routes.Application.interfazModificarUsuario());
      }catch(PersistenceException pe){
         flash("error", "Error al actualizar.");
         return redirect(routes.Application.interfazModificarUsuario());
      }catch(Exception e){
         flash("error", "Error desconocido: Posiblemente datos no validos.");
         return redirect(routes.Application.interfazModificarUsuario());
      }
   }*/
   /*@Login.Requiere
   public static Result eliminarDocente(){
      try{
         Form<DocenteForm> form = Form.form(DocenteForm.class).bindFromRequest();
         
         if(!form.get().contrasena.equals(getDocente().getPassword())){
            flash("error", "Contraseña invalida.");
            return badRequest(actualizarDocente.render(getDocente()));
         }
         docenteService.eliminar(getDocente());
         return redirect(routes.Application.logout());
      }catch(DAOException daoe) {
         flash("error", daoe.getMessage());
         return redirect(routes.Application.interfazModificarUsuario());
      }catch(PersistenceException pe){
         flash("error", pe.getMessage());
         return redirect(routes.Application.interfazModificarUsuario());
      }
   }*/
   
   private static boolean guardarFoto(int DNI){
      try{
         MultipartFormData cuerpo = request().body().asMultipartFormData();
         FilePart foto = cuerpo.getFile("imagen");

         if(foto == null){
            return false;
         }
         // Se abre el fichero original para lectura
         FileInputStream fileInput = new FileInputStream(foto.getFile());
         BufferedInputStream bufferedInput = new BufferedInputStream(fileInput);
         // Se abre el fichero donde se hara la copia
         FileOutputStream fotoCopia = new FileOutputStream(ruta + DNI);
         BufferedOutputStream bufferedOutput = new BufferedOutputStream(fotoCopia);
         // Bucle para leer de un fichero y escribir en el otro.
         byte[] array = new byte[1000];
         int leidos = bufferedInput.read(array);
         while (leidos > 0) {
            bufferedOutput.write(array, 0, leidos);
            leidos = bufferedInput.read(array);
         }
         // Cierre de los ficheros
         bufferedInput.close();
         bufferedOutput.close();
         return true;
      }catch(Exception e){
         e.printStackTrace();
         return false;
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
   
   private static Docente getDocente(){
      return usuarioService.obtener(Login.obtener(ctx()).getDNI(), Docente.class);
   }
   /*@Login.Requiere
   public static Result cambiarContrasenia() {
      try {
         Form<nuevaContraseniaForm> form = Form.form(nuevaContraseniaForm.class)
                 .bindFromRequest();
         if((form.get().nuevo).equals(form.get().repitenuevo)){
            Login login = Login.obtener(ctx());
            Usuario usuario = usuarioService.obtener(login.getDNI());
            if (usuario.getPassword().equals(form.get().actual)) {
               usuarioService.cambiarContrasenia(usuario.getDNI(), form.get().nuevo);
               flash("success", "Su contraseña ha sido cambiada correctamente.");
            } else {
               flash("error", "Su contraseña actual no es correcta.");
            }
         } else {
            flash("error", "La contraseña nueva no coincide en ambos campos.");
         }
      }catch(Exception e){
         flash("error", "Error desconocido: " + e.getMessage());
      }
      return redirect(routes.Application.interfazCambiarContrasenia());
   }*/
   
   //clases estaticas para los formularios
   public static class DocenteForm{
      public int dni;
      public String email;
      public String nombre;
      public String apellido;
      public Sexo sexo;
      public String contrasena;
      public String contrasena2;
      
      public Docente entidadNueva() {
         Docente docente = new Docente();
         docente.setDNI(dni);
         docente.setEmail(email);
         docente.setNombre(nombre);
         docente.setApellido(apellido);
         docente.setSexo(sexo);
         docente.setPassword(contrasena);
         return docente;
      }
      public void entidadModificada(Docente docente){
         docente.setEmail(email);
         docente.setNombre(nombre);
         docente.setApellido(apellido);
         docente.setSexo(sexo);
      }
   }
	   
   public static class nuevaContraseniaForm{
      public String actual;
      public String nuevo;
      public String repitenuevo;
   }
}