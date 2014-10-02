package models.services;

import models.entities.*;

public interface UsuarioService {

    public abstract Usuario obtenerLogin(String email, String password);

    public abstract <T extends Usuario> T obtener(int dni, Class<T> claseUsuario);

	public abstract void recuperarContrasenia(String email);

	public abstract Usuario obtener(int dni);

	public abstract void cambiarContrasenia(int dni, String nuevo);
    
        public abstract int obtener(String email);
}
