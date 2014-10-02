package models.services.impl;

import java.util.LinkedList;
import java.util.List;

import models.daos.AlumnoDAO;
import models.daos.impl.AlumnoDAOImpl;
import models.entities.Alumno;
import models.services.AlumnoService;

public class AlumnoServiceImpl implements AlumnoService {
    
    private static AlumnoDAO alumnoDAO = new AlumnoDAOImpl();
    private static final int MAX_BUSQUEDA = 50;
    private static final String ESTRUCTURA_DNI = "\\d{8}";
    
    @Override
    public Alumno obtener(int dni){
        return alumnoDAO.obtener(dni);
    }
    
    @Override
    public List<Alumno> buscarPorTexto(String texto){
        if(texto.matches(ESTRUCTURA_DNI)){
            List<Alumno> resultado = new LinkedList<Alumno>();
            resultado.add(alumnoDAO.obtener(Integer.parseInt(texto)));
            return resultado;
        }else{
            return alumnoDAO.buscarAlumno(texto, MAX_BUSQUEDA);
        }
    }

    @Override
    public void guardarAlumno(Alumno alumno) {
        alumnoDAO.guardar(alumno);
    }

	

}