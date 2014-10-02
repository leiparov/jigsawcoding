package models.services.impl;

import models.daos.DocenteDAO;
import models.daos.impl.DocenteDAOImpl;
import models.entities.Docente;
import models.services.DocenteService;

public class DocenteServiceImpl implements DocenteService{
    
   private static DocenteDAO docenteDAO = new DocenteDAOImpl();
    @Override
    public Docente obtener(int dni){
        return docenteDAO.obtener(dni);
    }
    @Override
    public void guardar(Docente docente){
       docenteDAO.guardar(docente);
    }
    @Override
    public void eliminar(Docente docente){
       docenteDAO.eliminar(docente);
    }
    
}