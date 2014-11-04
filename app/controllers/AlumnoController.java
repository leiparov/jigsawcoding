package controllers;

import static play.data.Form.form;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;


/*import views.html.alumnos.indexAlumnos;
 import views.html.alumnos.nuevoAlumno;*/
import javax.persistence.PersistenceException;

import models.entities.Alumno;
import models.entities.Docente;
import models.entities.Sexo;
import models.entities.Usuario;
import models.services.AlumnoService;
import models.services.DocenteService;
import models.services.Login;
import models.services.UsuarioService;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;

import com.fasterxml.jackson.databind.JsonNode;

import exceptions.DAOException;

@Login.Requiere
public class AlumnoController extends Controller {

	public static class AlumnoForm {
		public String nombres;
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
			nuevo.setNombres(nombres);
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
		public String nombreCompleto;
	}

	private static AlumnoService alumnoService = new AlumnoService();
	private static DocenteService docenteService = new DocenteService();
	private static UsuarioService usuarioService = new UsuarioService();

	private static final String carInvalidos = "|1234567890'!\"#$%&/()=?\\"
			+ "@+{},.-<>;:_[]*^`~";
	private static final String ruta = "public/photos/";

	/* Módulo DOCENTE */
	public static Result GO_HOME_DOCENTE = redirect(routes.AlumnoController.list(0, "dni",
			"asc", ""));

	public static Result list(int page, String sortBy, String order,
			String filter) {
		return ok(views.html.alumnos.listaAlumnos.render(
				alumnoService.page(page, 10, sortBy, order, filter), sortBy,
				order, filter));
	}
	
	public static Result indexDocente(){
		return GO_HOME_DOCENTE;
	}

	public static Result index() {
		Login login = Login.obtener(ctx());
		if(login.isTipo(Alumno.class)){
			return indexAlumno();
		}else if (login.isTipo(Docente.class)){
			return indexDocente();
		}else{
			return redirect(routes.Application.interfazLogin());
		}
		
	}

	// Para Profesor
	/*
	 * public static Result index() { Login login = Login.obtener(ctx());
	 * Docente usuario = docenteService.obtener(login.getDNI()); //
	 * List<Tuple2<Alumno, List<Grupo>>> alumnos = //
	 * docenteService.obtenerAlumnosDeSusGrupos(usuario); // return
	 * ok(indexAlumnos.render(alumnos)); return noContent(); }
	 */

	public static Result interfazNuevo() {
		Login login = Login.obtener(ctx());
		Docente usuario = docenteService.obtener(login.getDNI());
		return ok(views.html.alumnos.nuevoAlumno.render(usuario));
		// return TODO;
	}

	public static Result registrarAlumno() {
		try {
			Form<AlumnoForm> form = Form.form(AlumnoForm.class)
					.bindFromRequest();
			Alumno nuevoa = form.get().entidad();

			if (!validarNombre(nuevoa.getNombreCompleto())) {
				throw new DAOException(
						"Caracteres inválidos en Nombres y/o Apellidos.");
			}
			if ((nuevoa.getDNI() + "").length() != 8) {
				throw new NumberFormatException();
			}
			if (!nuevoa.getPassword().equals(form.get().repcontrasena)) {
				throw new DAOException("Las contraseñas no coinciden.");
			}
			try {
				usuarioService.obtener(nuevoa.getEmail());
			} catch (DAOException daoe) {
				alumnoService.guardarAlumno(nuevoa);
				flash("success", "El alumno fue creado correctamente");
				return redirect(routes.AlumnoController.index());
			}
			throw new DAOException(
					"Email ya registrado, porfavor ingrese otro.");
		} catch (DAOException daoe) {
			flash("error", "Error al registrar: " + daoe.getMessage());
			return redirect(routes.AlumnoController.interfazNuevo());
		} catch (NumberFormatException nfe) {
			flash("error", "Error en DNI: Por favor ingrese 8 digitos.");
			return redirect(routes.AlumnoController.interfazNuevo());
		} catch (PersistenceException pe) {
			flash("error", "Error al registrar: DNI ya existente.");
			return redirect(routes.AlumnoController.interfazNuevo());
		} catch (Exception e) {
			e.printStackTrace();
			flash("error", "Error desconocido: " + e.getMessage());
			return redirect(routes.AlumnoController.interfazNuevo());
		}
	}

	public static Result interfazEditar(int dni) {
		Form<Alumno> alumnoForm = form(Alumno.class).fill(
				alumnoService.obtener(dni));
		// return ok(editarAlumno.render(dni, alumnoForm));
		return TODO;
	}

	public static Result actualizarAlumno(int dni) {
		/*
		 * Form<Alumno> problemaForm = form(Alumno.class).bindFromRequest(); if
		 * (problemaForm.hasErrors()) { return
		 * badRequest(editarAlumno.render(id, problemaForm)); } try { Alumno
		 * problema = problemaForm.get(); problema.setDocente(getDocente());
		 * problema.setAlumnoId(id); System.out.println(problema.toString());
		 * problemaService.actualizarAlumno(getDocente(), problema);
		 * flash("success", "Alumno actualizado con éxito"); return GO_HOME;
		 * 
		 * } catch (Exception e) { e.printStackTrace(); flash("error",
		 * "No se pudo actualizar el Alumno"); return
		 * redirect(routes.Application.index()); }
		 */
		return TODO;
	}

	private static Alumno getAlumno() {
		return usuarioService.obtener(Login.obtener(ctx()).getDNI(),
				Alumno.class);
	}

	public static Result buscarAlumnos(String q) {
		List<Alumno> alumnos = alumnoService.buscarPorTexto(q);
		JsonNode respuesta = convertirListaAlumnosAJson(alumnos);
		return ok(respuesta);
	}

	public static Result disponibles() {
		/* Alumnos disponibles que aun no han sido asignados a un grupo experto */
		List<Alumno> alumnos = alumnoService.disponibles();
		play.Logger.debug("asdasdas");
		JsonNode respuesta = convertirListaAlumnosAJson(alumnos);
		System.out.println(respuesta);
		play.Logger.debug(respuesta.asText());
		return ok(respuesta);
	}

	private static JsonNode convertirListaAlumnosAJson(List<Alumno> lista) {
		ResultadoBusqueda[] array = new ResultadoBusqueda[lista.size()];
		int i = 0;
		for (Alumno alumno : lista) {
			ResultadoBusqueda resultado = new ResultadoBusqueda();
			resultado.dni = alumno.getDNI();
			resultado.nombreCompleto = alumno.getNombreCompleto();
			array[i++] = resultado;
		}
		return Json.toJson(array);
	}

	private static boolean guardarFoto(int DNI) {
		try {
			MultipartFormData cuerpo = request().body().asMultipartFormData();
			FilePart foto = cuerpo.getFile("imagen");

			if (foto == null) {
				return false;
			}
			// Se abre el fichero original para lectura
			FileInputStream fileInput = new FileInputStream(foto.getFile());
			BufferedInputStream bufferedInput = new BufferedInputStream(
					fileInput);
			// Se abre el fichero donde se hara la copia
			FileOutputStream fotoCopia = new FileOutputStream(ruta + DNI);
			BufferedOutputStream bufferedOutput = new BufferedOutputStream(
					fotoCopia);
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
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	private static boolean validarNombre(String nombre) {
		for (int i = 0; i < nombre.length(); i++) {
			if (carInvalidos.contains(nombre.charAt(i) + "")) {
				return false;
			}
		}
		return true;
	}

	public static Result cambiarContrasenia() {
		try {
			Form<nuevaContraseniaForm> form = Form.form(
					nuevaContraseniaForm.class).bindFromRequest();
			if ((form.get().nuevo).equals(form.get().repitenuevo)) {
				Login login = Login.obtener(ctx());
				Usuario usuario = usuarioService.obtener(login.getDNI());
				if (usuario.getPassword().equals(form.get().actual)) {
					usuarioService.cambiarContrasenia(usuario.getDNI(),
							form.get().nuevo);
					flash("success",
							"Su contraseña ha sido cambiada correctamente.");
				} else {
					flash("error", "Su contraseña actual no es correcta.");
				}
			} else {
				flash("error",
						"La contraseña nueva no coincide en ambos campos.");
			}
		} catch (Exception e) {
			flash("error", "Error desconocido: " + e.getMessage());
		}
		return redirect(routes.Application.interfazCambiarContrasenia());
	}

	/*Módulo ALUMNO*/
	
	public static Result indexAlumno(){
		Alumno a = getAlumno();
		return ok(views.html.perfilalumno.indexAlumno.render(a));
	}
}
