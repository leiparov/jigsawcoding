package models.daos;

import models.entities.Docente;

import com.avaje.ebean.Ebean;

public class DocenteDAO {
	public static UsuarioDAO usuarioDAO = new UsuarioDAO();

	public Docente obtener(int dni) {
		return usuarioDAO.obtener(dni, Docente.class);
	}

	public void guardar(Docente docente) {
		Ebean.save(docente);
	}

	public void eliminar(Docente docente) {
		docente.setInhabilitado(true);
		guardar(docente);
	}
}