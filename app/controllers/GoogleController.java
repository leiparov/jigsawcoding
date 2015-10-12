package controllers;

import com.fasterxml.jackson.databind.JsonNode;

import models.entities.Usuario;
import models.services.UsuarioService;
import play.Routes;
import play.api.libs.json.JsValue;
import play.data.Form;
import play.libs.F.Promise;
import play.libs.WS;
import play.libs.WS.Response;
import play.libs.WS.WSRequestHolder;
import play.mvc.Controller;
import play.mvc.Result;

public class GoogleController extends Controller {
  
  private static String clientId = "1012976681806-gq056951j0hc78ccv8jopndteng1n57g.apps.googleusercontent.com";
  private static String clientSecret = "I4BMnyS5UEF7BdoFED_E2IoA";
  private static String redirectUrl = "http://localhost:9000/google/oauth2callback";
  private static UsuarioService usuarioService = new UsuarioService();

  public static Result loginGoogle() {
    String scope = "profile";
    return redirect("https://accounts.google.com/o/oauth2/auth?client_id=" + 
    clientId + "&redirect_uri=" + redirectUrl + "&response_type=code&scope=" + scope);
  }

  public static Result oauth2callback( String code) {
    String postBody = "code=" + code + "&client_id=" + clientId + "&client_secret=" + clientSecret + "&redirect_uri=" + 
  redirectUrl + "&grant_type=authorization_code";
    Promise<Response> body = WS.url("https://accounts.google.com/o/oauth2/token").
    		setHeader("Content-Type", "application/x-www-form-urlencoded").post(postBody);
    play.Logger.info(body.toString());
    
    //JsonNode accessToken = jsonPromise.get(OK).get("access_token");
    //String accessJson = body. await.get.json;
    //String accessToken = strip((accessJson \ "access_token").toString);
    //String userJson = WS.url("https://www.googleapis.com/oauth2/v1/userinfo?access_token=" + accessToken).get.await.get.json;
    //Usuario user = getOrCreateUser(userJson);
    //return redirect(routes.Application.index()).withSession("connected", user.getEmail());
    return TODO;
  }
  
  
  
  
  public Usuario getExistingUser(String email) {
	      return usuarioService.obtenerLogin(email);
	    }
  
  public Usuario getOrCreateUser(JsValue googleUser) {
    
    String email = "";//strip((googleUser \ "email").toString());
    Usuario existingUser = getExistingUser(email);
    if (existingUser == null) {
      String fullName = "";//strip((googleUser \ "name").toString());
      //usuarioService.create(User(email, fullName));
      return getExistingUser(email);
    } else {
      return existingUser;
    }
  }
  
  public static void main( String args[]) {
	  
	  
  }
  
}
