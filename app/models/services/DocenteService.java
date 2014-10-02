package models.services;

import models.entities.Docente;

public interface DocenteService {
   public abstract Docente obtener(int dni);
   public abstract void guardar(Docente docente);
   public abstract void eliminar(Docente docente);
   
}