package models.daos.impl;

import com.avaje.ebean.Ebean;

import models.daos.DocenteDAO;
import models.daos.UsuarioDAO;
import models.entities.Docente;

public class DocenteDAOImpl implements DocenteDAO {
    public static UsuarioDAO usuarioDAO = new UsuarioDAOImpl();
    @Override
    public Docente obtener(int dni){
       return usuarioDAO.obtener(dni, Docente.class);
    }
    @Override
    public void guardar(Docente docente){
       Ebean.save(docente);
    }
    @Override
    public void eliminar(Docente docente){
       docente.setInhabilitado(true);
       guardar(docente);
    }
}