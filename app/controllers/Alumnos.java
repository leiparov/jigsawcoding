package controllers;

import java.util.List;
import java.util.LinkedList;

import models.daos.DAOException;
import models.entities.*;
import models.services.*;
import models.services.impl.*;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import scala.Tuple2;
import utils.Login;
/*import views.html.alumnos.indexAlumnos;
import views.html.alumnos.nuevoAlumno;*/

import com.fasterxml.jackson.databind.JsonNode;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;

import javax.persistence.PersistenceException;

@Login.Requiere
public class Alumnos extends Controller {

   public static class AlumnoForm {
      public String nombre;
      public String apellidoPaterno;
      public String apellidoMaterno;
      public String dni;
      public Sexo sexo;
      public String email;
      public String contrasena;
      public String repcontrasena;
      public int grupo;

      public Alumno entidad() {
         Alumno nuevo = new Alumno();
         nuevo.setPassword(contrasena);
         nuevo.setNombres(nombre);
         nuevo.setApellidoPaterno(apellidoPaterno);
         nuevo.setApellidoMaterno(apellidoMaterno);
         nuevo.setDNI(Integer.parseInt(dni));
         nuevo.setSexo(sexo);
         nuevo.setEmail(email);

         return nuevo;
      }

      public void entidadModificada(Alumno alumno) {
         alumno.setEmail(email);
      }
   }

   public static class nuevaContraseniaForm {
      public String actual;
      public String nuevo;
      public String repitenuevo;
   }

   private static class ResultadoBusqueda {
      public int dni;
      public String nombre;
   }

   private static AlumnoService alumnoService = new AlumnoServiceImpl();
   private static DocenteService docenteService = new DocenteServiceImpl();
   private static UsuarioService usuarioService = new UsuarioServiceImpl();
   
   private static final String carInvalidos = "|1234567890'!\"#$%&/()=?\\"
                                             +"@+{},.-<>;:_[]*^`~";
   private static final String ruta = "public/photos/";

   // Para Profesor
   public static Result index() {
      Login login = Login.obtener(ctx());
      Docente usuario = docenteService.obtener(login.getDNI());
      //List<Tuple2<Alumno, List<Grupo>>> alumnos = docenteService.obtenerAlumnosDeSusGrupos(usuario);
      //return ok(indexAlumnos.render(alumnos));
      return noContent();
   }

   /*public static Result interfazNuevo() {
      Login login = Login.obtener(ctx());
      Docente usuario = docenteService.obtener(login.getDNI());
      return ok(nuevoAlumno.render(usuario));
   }*/

   /*public static Result registrarAlumno() {
      try{
         Form<AlumnoForm> form = Form.form(AlumnoForm.class).bindFromRequest();
         Alumno nuevoa = form.get().entidad();
         
         if(!validarNombre(nuevoa.getNombreCompleto())){
            throw new DAOException("Caracteres inválidos en Nombres y/o Apellidos.");
         }
         if((nuevoa.getDNI()+"").length() != 8){
            throw new NumberFormatException();
         }
         if(!nuevoa.getPassword().equals(form.get().repcontrasena)){
            throw new DAOException("Las contraseñas no coinciden.");
         }         
         try{
            usuarioService.obtener(nuevoa.getEmail());
         }catch(DAOException daoe){
            if(form.get().grupo != -1){
               List grupos = new LinkedList();
               grupos.add(grupoService.obtener(form.get().grupo));
               nuevoa.setGrupos(grupos);
            }
            if (guardarFoto(nuevoa.getDNI())) {
               nuevoa.setFoto(true);
            }
            alumnoService.guardarAlumno(nuevoa);
            flash("success", "El alumno fue creado correctamente");
            return redirect(routes.Alumnos.index());
         }
         throw new DAOException("Email ya registrado, porfavor ingrese otro.");
      }catch(DAOException daoe){
         flash("error", "Error al registrar: " + daoe.getMessage());
         return redirect(routes.Alumnos.interfazNuevo());
      }catch(NumberFormatException nfe){
         flash("error", "Error en DNI: Por favor ingrese 8 digitos.");
         return redirect(routes.Alumnos.interfazNuevo());
      }catch(PersistenceException pe){
         flash("error", "Error al registrar: DNI ya existente.");
         return redirect(routes.Alumnos.interfazNuevo());
      }catch(Exception e){
         e.printStackTrace();
         flash("error", "Error desconocido: " + e.getMessage());
         return redirect(routes.Alumnos.interfazNuevo());
      }
   }*/

   /*public static Result actualizarAlumno() {
      try{
         Form<AlumnoForm> form = Form.form(AlumnoForm.class).bindFromRequest();
         Alumno alumno = getAlumno();
         String emailAntiguo = alumno.getEmail();

         form.get().entidadModificada(alumno);
         try{
            if(emailAntiguo.equals(alumno.getEmail())){
               throw new DAOException();
            }
            usuarioService.obtener(alumno.getEmail());
         }catch(DAOException daoe){
            if (guardarFoto(alumno.getDNI())) {
               alumno.setFoto(true);
            }
            alumnoService.guardarAlumno(alumno);
            Login.obtener(ctx()).logearSesion(alumno);
            return redirect(routes.Application.index());
         }
         throw new DAOException("Email ya registrado, por favor ingrese otro.");
      }catch(DAOException daoe){
         flash("error", "Error al registrar: " + daoe.getMessage());
         return redirect(routes.Application.interfazModificarUsuario());
      }catch(Exception e){
         e.printStackTrace();
         flash("error", "Error desconocido: " + e.getMessage());
         return redirect(routes.Application.interfazModificarUsuario());
      }
   }*/

   private static Alumno getAlumno() {
      return usuarioService.obtener(Login.obtener(ctx()).getDNI(), Alumno.class);
   }

   public static Result buscarAlumnos(String q) {
      List<Alumno> alumnos = alumnoService.buscarPorTexto(q);
      JsonNode respuesta = convertirListaAlumnosAJson(alumnos);
      return ok(respuesta);
   }

   private static JsonNode convertirListaAlumnosAJson(List<Alumno> lista) {
      ResultadoBusqueda[] array = new ResultadoBusqueda[lista.size()];
      int i = 0;
      for(Alumno alumno : lista){
         ResultadoBusqueda resultado = new ResultadoBusqueda();
         resultado.dni = alumno.getDNI();
         resultado.nombre = alumno.getNombreCompleto();
         array[i++] = resultado;
      }
      return Json.toJson(array);
   }

   private static boolean guardarFoto(int DNI) {
      try{
         MultipartFormData cuerpo = request().body().asMultipartFormData();
         FilePart foto = cuerpo.getFile("imagen");

         if (foto == null) {
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
   
   /*public static Result cambiarContrasenia() {
      try {
         Form<nuevaContraseniaForm> form = Form.form(nuevaContraseniaForm.class)
                 .bindFromRequest();
         if ((form.get().nuevo).equals(form.get().repitenuevo)) {
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
      } catch (Exception e) {
         flash("error", "Error desconocido: " + e.getMessage());
      }
      return redirect(routes.Application.interfazCambiarContrasenia());
   }*/
}
