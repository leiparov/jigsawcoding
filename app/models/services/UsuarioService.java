package models.services;

import java.util.Random;

import models.daos.UsuarioDAO;
import models.entities.Usuario;

public class UsuarioService {

	private static UsuarioDAO usuarioDAO = new UsuarioDAO();
	private static Mail mail = new Mail();

	public Usuario obtenerLogin(String email, String password) {
		return usuarioDAO.obtenerLogin(email, password);
	}
	public Usuario obtenerLogin(String email) {
		return usuarioDAO.obtenerLogin(email);
	}

	public <T extends Usuario> T obtener(String dni, Class<T> claseUsuario) {
		return usuarioDAO.obtener(dni, claseUsuario);
	}

	public void recuperarContrasenia(String email) {
		String nuevaContrasenia = generarContrasenia();
		usuarioDAO.cambiarPassword(usuarioDAO.obtenerPorEmail(email), nuevaContrasenia);
		mail.enviarContrasenia(email, nuevaContrasenia);
	}

	public Usuario obtener(String id) {
		return usuarioDAO.obtener(id);
	}

	private String generarContrasenia() {
		Random rnd = new Random();
		int numero;
		char caracter;
		String nuevocontrasenia = "";

		for (int i = 0; i < 8; i++) {
			if ((int) (rnd.nextDouble() * 2.0) == 0) {
				numero = (int) (rnd.nextDouble() * 10.0);
				nuevocontrasenia = nuevocontrasenia + numero;
			} else {
				caracter = (char) (rnd.nextDouble() * 25.0 + 97);
				nuevocontrasenia = nuevocontrasenia + caracter;
			}
		}
		return nuevocontrasenia;
	}

	public void cambiarContrasenia(String dni, String nuevo) {
		usuarioDAO.cambiarPassword(dni, nuevo);
	}

	public String obtenerPorEmail(String email) {
		return usuarioDAO.obtenerPorEmail(email);
	}
	
	
	
	
}
