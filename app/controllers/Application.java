package controllers;

import org.apache.http.HttpRequest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import oauth.signpost.http.HttpResponse;
import exceptions.DAOException;
import models.entities.Alumno;
import models.entities.Usuario;
import models.services.Login;
import models.services.UsuarioService;
import play.Routes;
import play.data.Form;
import play.libs.F.Promise;
import play.libs.WS;
import play.libs.WS.*;
import play.mvc.Controller;
import play.mvc.Result;
import play.libs.F.Function;
//import views.html.usuarios.*;
import utils.HttpUtils;

public class Application extends Controller {

	private static String clientId = "1012976681806-gq056951j0hc78ccv8jopndteng1n57g.apps.googleusercontent.com";
	private static String clientSecret = "I4BMnyS5UEF7BdoFED_E2IoA";
	private static String redirectUrl = "http://localhost:9000/google/oauth2callback";

	private static UsuarioService usuarioService = new UsuarioService();

	/* Login Google */
	public static Result loginGoogle() {
		String scope = "https://www.googleapis.com/auth/userinfo.email";
		return redirect("https://accounts.google.com/o/oauth2/auth?client_id="
				+ clientId + "&redirect_uri=" + redirectUrl
				+ "&response_type=code&scope=" + scope);
	}

	public static class GoogleForm {
		public String idtoken;
	}

	public static Result validarToken() {
		Form<GoogleForm> googleForm = Form.form(GoogleForm.class)
				.bindFromRequest();
		String idToken = googleForm.get().idtoken;
		String url = "https://www.googleapis.com/oauth2/v3/tokeninfo?id_token="
				+ idToken;
		play.Logger.info(idToken);

		JsonObject result = HttpUtils.callURL(url);

		String email = result.get("email").toString().replace("\"", "");

		play.Logger.info(result.toString());

		if (email == null) {
			return badRequest("Missing parameter [email]");
		} else {
			// return ok("Hello " + email);
			Usuario logueado = usuarioService.obtenerLogin(email);
			if (logueado == null) {
				return redirect(routes.UsuarioController.interfazNuevo(email));
			} else {
				Login.obtener(ctx()).logearSesion(logueado);
				//return index();
				return ok("DNI" + logueado.getDNI());
			}

		}

	}

	public static Result oauth2callback(String code) {
		String postBody = "code=" + code + "&client_id=" + clientId
				+ "&client_secret=" + clientSecret + "&redirect_uri="
				+ redirectUrl + "&grant_type=authorization_code";
		// HttpResponse response =
		// WS.url("https://accounts.google.com/o/oauth2/token")
		// .setHeader("Content-Type", "application/x-www-form-urlencoded")
		// .post(postBody);

		// play.Logger.info(response.get(0).asJson().asText());

		// WSRequestHolder request =
		// WS.url("https://accounts.google.com/o/oauth2/token");
		// request.setHeader("Content-Type",
		// "application/x-www-form-urlencoded");
		//
		// Promise<WS.Response> response = request.post(postBody);
		//
		// response.map(resp -> (Result) ok(resp.asJson()));

		// play.Logger.info(jsonPromise);

		// JsonNode accessToken = jsonPromise.get(OK).get("access_token");
		// String accessJson = body. await.get.json;
		// String accessToken = strip((accessJson \ "access_token").toString);
		// String userJson =
		// WS.url("https://www.googleapis.com/oauth2/v1/userinfo?access_token="
		// + accessToken).get.await.get.json;
		// Usuario user = getOrCreateUser(userJson);
		// return redirect(routes.Application.index()).withSession("connected",
		// user.getEmail());
		return TODO;
	}

	/* Fin Login Google */

	private static Alumno getAlumno() {
		return usuarioService.obtener(Login.obtener(ctx()).getDNI(),
				Alumno.class);
	}

	@Login.Requiere
	public static Result index() {
		Login login = Login.obtener(ctx());
		if (login.isTipo(Alumno.class)) {
			// return
			// ok(views.html.perfilalumno.indexAlumno.render(getAlumno()));
			return redirect(routes.AlumnoController.indexAlumno());
		} else {
			return ok(views.html.perfildocente.indexDocente.render());
		}
	}

	public static Result interfazLogin() {
		return ok(views.html.loginGoogle.render());
	}

	@Login.Requiere
	public static Result interfazPerfilUsuario() {
		return noContent();
	}

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
			Form<Login.Form> loginForm = Form.form(Login.Form.class)
					.bindFromRequest();
			Usuario logueado = usuarioService
					.obtenerLogin(loginForm.get().email);
			Login.obtener(ctx()).logearSesion(logueado);
			return redirect(routes.Application.index());
		} catch (DAOException.FalloLoginException e) {
			flash("error", "Usuario y/o Password incorrectos");
			return badRequest(views.html.login.render());
		}
	}

	public static Result recuperarContrasenia() {
		Form<contraseniaForm> popupForm = Form.form(contraseniaForm.class)
				.bindFromRequest();
		String email = popupForm.get().emailRecuperar;
		if (email != "") {
			if (usuarioService.obtener(email) != 0) {
				usuarioService.recuperarContrasenia(email);
				flash("successRecuperar",
						"Su nueva contraseña ha sido enviada a su correo.");
			} else
				flash("errorRecuperar",
						"El correo ingresado no pertenece a un usuario.");
		} else {
			flash("errorRecuperar",
					"No ha ingresado su correo para recuperar su contraseña.");
		}
		return ok(views.html.login.render());
	}

	public static Result jsRoutes() {
		response().setContentType("text/javascript");
		return ok(Routes.javascriptRouter("jsRoutes",
				controllers.routes.javascript.AlumnoController.buscarAlumnos(),
				controllers.routes.javascript.AlumnoController.disponibles(),
				controllers.routes.javascript.ProblemaController
						.buscarProblemas(),
				controllers.routes.javascript.ExamenController
						.renderPreguntaEdicion(),
				controllers.routes.javascript.ProblemaController
						.problemaRunJs(),
				controllers.routes.javascript.ProblemaController
						.verResultadosProblemaRunJs()));
	}

	// clases estaticas para los formularios
	public static class contraseniaForm {
		public String emailRecuperar;
	}
}
