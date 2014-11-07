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

	public <T extends Usuario> T obtener(int dni, Class<T> claseUsuario) {
		return usuarioDAO.obtener(dni, claseUsuario);
	}

	public void recuperarContrasenia(String email) {
		String nuevaContrasenia = generarContrasenia();
		usuarioDAO.cambiarPassword(usuarioDAO.obtener(email), nuevaContrasenia);
		mail.enviarContrasenia(email, nuevaContrasenia);
	}

	public Usuario obtener(int dni) {
		return usuarioDAO.obtener(dni);
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

	public void cambiarContrasenia(int dni, String nuevo) {
		usuarioDAO.cambiarPassword(dni, nuevo);
	}

	public int obtener(String email) {
		return usuarioDAO.obtener(email);
	}
	
	
	
	
}
