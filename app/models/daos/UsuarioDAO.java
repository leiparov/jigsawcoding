package models.daos;

import models.entities.*;

public interface UsuarioDAO {

	public abstract void guardar(Usuario u);

	public abstract int obtener(String email); 
	
	public abstract void cambiarPassword(int DNI, String nuevoPass);

	public abstract Usuario obtenerLogin(String email, String password);

	public abstract Usuario obtener(int DNI);

	public <T extends Usuario> T obtener(int dni, Class<T> claseUsuario);
	
}