package models.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import models.daos.ExamenDAO;
import models.entities.Alumno;
import models.entities.Docente;
import models.entities.Examen;
import models.entities.ProblemaExamen;
import models.entities.RespuestasAlumno;

import com.avaje.ebean.Page;

public class ExamenService {

	private static ExamenDAO examenDAO = new ExamenDAO();

	public Examen obtener(Integer id) {
		return examenDAO.obtener(id);
	}
	public void actualizarHorario(Examen examen) {
		examenDAO.actualizarDatosHorario(examen);
	}
	public void crear(Examen examen) {
		examen.setTiempoCreacion(new Date());
		examenDAO.guardar(examen);
	}
	public void modificar(Examen e) {
		examenDAO.modificar(e);
	}
	public void eliminar(Integer id) {
		examenDAO.eliminar(id);
	}

	public Page<Examen> page(Docente docente, int page, int pageSize,
			String sortBy, String order, String filter) {
		return examenDAO.page(docente, page, pageSize, sortBy, order, filter);
	}
	public boolean existeNotaExamen(Alumno a, Examen e){
		return examenDAO.existeNotaExamen(a,e);
	}
	
	public ProblemaExamen obtenerProblemaExamen(Integer id){
		return examenDAO.obtenerProblemaExamen(id);
	}
	
	public void finalizarExamen(Alumno alumno, List<String> respuestas){
		
		List<RespuestasAlumno> respuestasAlumno = new ArrayList<>();
		for (String rpta : respuestas){
			RespuestasAlumno ra = new RespuestasAlumno();
			int i = rpta.indexOf("probex");
			String x[] = rpta.substring(i+6).split(";");
			int peid = Integer.parseInt(x[0]);
			ProblemaExamen pe = obtenerProblemaExamen(peid);
			ra.setProblemaExamen(pe);
			ra.setAlumno(alumno);
			ra.setIdeoneLink(x[1]);
			respuestasAlumno.add(ra);
		}
		examenDAO.guardarRespuestasAlumno(respuestasAlumno);		
	}

}
