package models.daos;

import models.entities.Docente;

public interface DocenteDAO {
   public abstract Docente obtener(int dni);
   public abstract void guardar(Docente docente);
   public abstract void eliminar(Docente docente);
}