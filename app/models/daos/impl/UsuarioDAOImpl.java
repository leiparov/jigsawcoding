package models.daos.impl;

import java.util.List;

import models.daos.DAOException;
import models.daos.UsuarioDAO;
import models.entities.*;

import com.avaje.ebean.*;

public class UsuarioDAOImpl implements UsuarioDAO {

    @Override
    public void guardar(Usuario u) {
        Ebean.save(u);
    }

    @Override
    public Usuario obtener(int DNI) {
        Usuario resultado = Ebean.find(Usuario.class, DNI);
        if (resultado != null)
            return resultado;
        else
            throw new DAOException("Usuario no encontrado");
    }

    @Override
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

    @Override
    public void cambiarPassword(int DNI, String nuevoPass) {
        SqlUpdate sql = Ebean.createSqlUpdate("update usuario set password = :password where dni = :dni");
        sql.setParameter("dni", DNI);
        sql.setParameter("password", nuevoPass);

        int cuenta = sql.execute();

        if (cuenta == 0)
            throw new DAOException("Usuario no encontrado");
    }

    @Override
    public <T extends Usuario> T obtener(int dni, Class<T> claseUsuario) {
        T resultado = Ebean.find(claseUsuario, dni);
        if (resultado != null)
            return resultado;
        else
            throw new DAOException.NoEncontradoException(claseUsuario);
    }

    @Override
    public int obtener(String email) {
       int dni = 0;
       SqlQuery sql = Ebean.createSqlQuery("select dni from usuario where email like :email");
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
