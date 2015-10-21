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

	public Usuario obtener(String id) {
		Usuario resultado = Ebean.find(Usuario.class, id);
		if (resultado != null)
			return resultado;
		else
			//throw new DAOException("Usuario no encontrado");
			return null;
	}
//	public Usuario obtenerPorEmail (String email) {
//		Usuario resultado = Ebean.find(Usuario.class, email);
//		if (resultado != null)
//			return resultado;
//		else
//			throw new DAOException("Usuario no encontrado");
//	}
	

	public Usuario obtenerLogin(String email, String password) {
		SqlQuery sql = Ebean
				.createSqlQuery("select id from usuario where email like :email and password like :password");
		sql.setParameter("email", email);
		sql.setParameter("password", password);

		SqlRow resultado = sql.findUnique();
		if (resultado == null)
			throw new DAOException.FalloLoginException();
		else
			return obtener(resultado.getString("id"));
	}
	
	public Usuario obtenerLogin(String email) {
		SqlQuery sql = Ebean
				.createSqlQuery("select id from usuario where email like :email");
		sql.setParameter("email", email);
		

		SqlRow resultado = sql.findUnique();
		if (resultado == null)
			//throw new DAOException.FalloLoginException();
			return null;
		else
			return obtener(resultado.getString("id"));
	}

	public void cambiarPassword(String dni, String nuevoPass) {
		SqlUpdate sql = Ebean
				.createSqlUpdate("update usuario set password = :password where dni = :dni");
		sql.setParameter("dni", dni);
		sql.setParameter("password", nuevoPass);

		int cuenta = sql.execute();

		if (cuenta == 0)
			throw new DAOException("Usuario no encontrado");
	}

	public <T extends Usuario> T obtener(String dni, Class<T> claseUsuario) {
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
	

	public String obtenerPorEmail(String email) {
		String dni = "";
		SqlQuery sql = Ebean
				.createSqlQuery("select dni from usuario where email like :email");
		sql.setParameter("email", email);
		List<SqlRow> resultado = sql.findList();
		if (resultado == null || resultado.isEmpty()) {
			throw new DAOException("Email no encontrado");
		} else {
			for (SqlRow row : resultado) {
				dni = row.getString("dni");
			}
		}
		return dni;
	}
	
}
