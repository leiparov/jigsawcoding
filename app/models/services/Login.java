package models.services;

import play.libs.F;
import play.mvc.*;
import play.mvc.Http.*;

import java.lang.annotation.*;

import models.entities.Usuario;
import models.entities.Sexo;

public class Login {

	// Anotacion para asegurar
	@With(AccionAutenticada.class)
	@Target({ ElementType.TYPE, ElementType.METHOD })
	@Retention(RetentionPolicy.RUNTIME)
	public @interface Requiere {
	}

	public static class AccionAutenticada extends Action<Login.Requiere> {
		// Tomado del codigo original del play

		private static Result RESULTADO_RECHAZADO = Results
				.redirect(controllers.routes.Application.interfazLogin());
		private Login info;

		public F.Promise<SimpleResult> call(Context ctx) {
			try {
				return autenticarAccion(ctx);
			} catch (RuntimeException e) {
				throw e;
			} catch (Throwable t) {
				throw new RuntimeException(t);
			}
		}

		private F.Promise<SimpleResult> autenticarAccion(Context ctx) {
			info = Login.obtener(ctx);
			if (info.hayUsuarioConectado()) {
				return accionAceptada(ctx);
			} else {
				return accionRechazada();
			}
		}

		private F.Promise<SimpleResult> accionAceptada(Context ctx) {
			try {
				ctx.args.put(CLAVE_ARGS, info);
				return delegate.call(ctx);
			} catch (Throwable t) {
				throw new RuntimeException(t);
			} finally {
				ctx.args.put(CLAVE_ARGS, null);
			}
		}

		@SuppressWarnings("deprecation")
		private F.Promise<SimpleResult> accionRechazada() {
			if (RESULTADO_RECHAZADO instanceof AsyncResult) {
				return ((AsyncResult) RESULTADO_RECHAZADO).getPromise();
			} else {
				return F.Promise.pure((SimpleResult) RESULTADO_RECHAZADO);
			}
		}
	}

	public static class Form {
		public String email;
		public String password;
	}

	public static class HeaderData {
		public String nombre;
		public String dni;
		public boolean hasFoto;
		public boolean isMan;

		HeaderData() {
			// Data por defecto
			nombre = "Debug-User";
			hasFoto = false;
			isMan = true;
		}
	}

	static String CLAVE_ARGS = "jigsawcoding-login";

	private String dni;
	private String nombre;
	private String tipo;

	private String sexo;
	private Context ctx;

	private Login(Context ctx, String dni, String nombre, String sexo,
			String tipo) {
		this.ctx = ctx;
		this.dni = dni;
		this.nombre = nombre;

		this.sexo = sexo;
		this.tipo = tipo;
	}

	public static Login obtener(Context ctx) {
		Login existente = (Login) ctx.args.get(CLAVE_ARGS);
		if (existente != null) {
			return existente;
		} else {
			return new Login(ctx, ctx.session().get("dni"), ctx.session().get(
					"nombre"), ctx.session().get("sexo"), ctx.session().get(
					"tipo"));
		}
	}

	public boolean hayUsuarioConectado() {
		return dni != null;
	}

	public int getDNI() {
		return Integer.parseInt(dni);
	}

	public String getNombre() {
		return nombre;
	}

	public String getTipo() {
		return tipo;
	}

	public boolean isMan() {
		return sexo.equals("true");
	}

	@Deprecated
	public boolean isTipo(String tipo) {
		return this.tipo.equals(tipo);
	}

	public boolean isTipo(Class<?> tipo) {
		return this.tipo.equals(tipo.getName());
	}

	public void deslogearSesion() {
		ctx.session().clear();
	}

	public void logearSesion(Usuario u) {
		ctx.session().clear();
		ctx.session().put("dni", Integer.toString(u.getDNI()));
		ctx.session().put("nombre", u.getNombreCompleto());
		ctx.session().put("sexo", u.getSexo().equals(Sexo.MASCULINO) + "");
		ctx.session().put("tipo", u.getClass().getName());
	}

	public HeaderData getHeaderData() {
		HeaderData data = new HeaderData();
		if (hayUsuarioConectado()) {
			data.nombre = this.nombre;
			data.dni = this.dni;
			data.isMan = this.sexo.equals("true");
		}
		return data;
	}

}
