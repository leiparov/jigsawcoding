package models.daos;

import java.util.List;

import models.entities.Alumno;

public interface AlumnoDAO {

    public abstract Alumno obtener(int dni);

    public abstract List<Alumno> buscarAlumno(String criterio, int max);

    public abstract void guardar(Alumno alumno);

}