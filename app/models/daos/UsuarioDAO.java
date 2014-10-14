package models.daos;

import java.util.List;

import models.entities.Usuario;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlQuery;
import com.avaje.ebean.SqlRow;
import com.avaje.ebean.SqlUpdate;

import exceptions.DAOException;

public class UsuarioDAO {

	public void guardar(Usuario u) {
		Ebean.save(u);
	}

	public Usuario obtener(int DNI) {
		Usuario resultado = Ebean.find(Usuario.class, DNI);
		if (resultado != null)
			return resultado;
		else
			throw new DAOException("Usuario no encontrado");
	}
	
	

	public Usuario obtenerLogin(String email, String password) {
		SqlQuery sql = Ebean
				.createSqlQuery("select dni from usuario where email like :email and password like :password");
		sql.setParameter("email", email);
		sql.setParameter("password", password);

		SqlRow resultado = sql.findUnique();
		if (resultado == null)
			throw new DAOException.FalloLoginException();
		else
			return obtener(resultado.getInteger("dni"));
	}

	public void cambiarPassword(int DNI, String nuevoPass) {
		SqlUpdate sql = Ebean
				.createSqlUpdate("update usuario set password = :password where dni = :dni");
		sql.setParameter("dni", DNI);
		sql.setParameter("password", nuevoPass);

		int cuenta = sql.execute();

		if (cuenta == 0)
			throw new DAOException("Usuario no encontrado");
	}

	public <T extends Usuario> T obtener(int dni, Class<T> claseUsuario) {
		T resultado = Ebean.find(claseUsuario, dni);
		if (resultado != null)
			return resultado;
		else
			throw new DAOException.NoEncontradoException(claseUsuario);
	}
	
	public <T extends Usuario> List<T> obtener(Class<T> claseUsuario) {
		@SuppressWarnings("unchecked")
		List<T> resultado = (List<T>)Ebean.find(claseUsuario);
		if (resultado != null)
			return resultado;
		else
			throw new DAOException.NoEncontradoException(claseUsuario);
	}
	

	public int obtener(String email) {
		int dni = 0;
		SqlQuery sql = Ebean
				.createSqlQuery("select dni from usuario where email like :email");
		sql.setParameter("email", email);
		List<SqlRow> resultado = sql.findList();
		if (resultado == null || resultado.isEmpty()) {
			throw new DAOException("Email no encontrado");
		} else {
			for (SqlRow row : resultado) {
				dni = row.getInteger("dni");
			}
		}
		return dni;
	}
	
}
