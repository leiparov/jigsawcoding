package models.services;

import java.util.LinkedList;
import java.util.List;

import models.daos.AlumnoDAO;
import models.entities.Alumno;
import models.entities.SesionJigsaw;

import com.avaje.ebean.Page;

public class AlumnoService {

	private static AlumnoDAO alumnoDAO = new AlumnoDAO();
	private static final int MAX_BUSQUEDA = 50;
	private static final String ESTRUCTURA_DNI = "\\d{8}";

	public Alumno obtener(int dni) {
		return alumnoDAO.obtener(dni);
	}

	public List<Alumno> buscarPorTexto(String texto) {
		if (texto.matches(ESTRUCTURA_DNI)) {
			List<Alumno> resultado = new LinkedList<Alumno>();
			resultado.add(alumnoDAO.obtener(Integer.parseInt(texto)));
			return resultado;
		} else {
			return alumnoDAO.buscarAlumno(texto, MAX_BUSQUEDA);
		}
	}
	
	public List<Alumno> disponibles(){
		return alumnoDAO.disponibles();
	}

	public void guardarAlumno(Alumno alumno) {
		alumnoDAO.guardar(alumno);
	}
	
	public Page<Alumno> page(int page, int pageSize, String sortBy,
			String order, String filter) {
		return alumnoDAO.page(page, pageSize, sortBy, order, filter);
	}
	
	public List<SesionJigsaw> sesionesActivas(Alumno alumno){
		return null;
	}

}