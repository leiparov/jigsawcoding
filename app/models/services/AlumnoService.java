package models.services;

import java.util.List;

import models.entities.Alumno;

public interface AlumnoService {

    public abstract Alumno obtener(int dni);

    public abstract List<Alumno> buscarPorTexto(String texto);

    public abstract void guardarAlumno(Alumno alumno);

	

}