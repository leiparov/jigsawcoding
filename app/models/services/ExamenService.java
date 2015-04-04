package models.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import models.daos.ExamenDAO;
import models.daos.NotaAlumnoDAO;
import models.daos.RespuestasAlumnoDAO;
import models.entities.Alumno;
import models.entities.Docente;
import models.entities.Examen;
import models.entities.NotaAlumno;
import models.entities.ProblemaExamen;
import models.entities.RespuestaAlumno;
import models.services.ideone.IdeoneService;
import models.services.ideone.IdeoneSubmissionDetails;

import com.avaje.ebean.Page;

public class ExamenService {

	private static ExamenDAO examenDAO = new ExamenDAO();
	private static RespuestasAlumnoDAO respuestasAlumnoDAO = new RespuestasAlumnoDAO();
	private static IdeoneService ideoneService = new IdeoneService();
	private static NotaAlumnoDAO notaAlumnoDAO = new NotaAlumnoDAO();

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
	public boolean yaRindioExamen(Alumno a, Examen e){
		return examenDAO.yaRindioExamen(a,e);
	}
	
	public ProblemaExamen obtenerProblemaExamen(Integer id){
		return examenDAO.obtenerProblemaExamen(id);
	}
	
	public void finalizarExamen(Alumno alumno, List<String> respuestas){
		
		List<RespuestaAlumno> respuestasAlumno = new ArrayList<>();
		for (String rpta : respuestas){
			RespuestaAlumno ra = new RespuestaAlumno();
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
	public List<Examen> obtenerExamenes() {
		
		return examenDAO.obtenerExamenes();
	}
	public Map<RespuestaAlumno, IdeoneSubmissionDetails> obtenerRespuestas(Alumno a, Examen e) {
		Set<RespuestaAlumno> respuestasAlumno = a.getRespuestasAlumno();
		Iterator<RespuestaAlumno> it = respuestasAlumno.iterator();
		Map<RespuestaAlumno, IdeoneSubmissionDetails> misRespuestas = new HashMap<RespuestaAlumno, IdeoneSubmissionDetails>();
		
		while(it.hasNext()){
			RespuestaAlumno rpta =  it.next();
			ProblemaExamen pe = rpta.getProblemaExamen();
			Examen ex = pe.getExamen();
			if(e.getId() == ex.getId()){
				String link = rpta.getIdeoneLink();
				IdeoneSubmissionDetails isd = ideoneService.getSubmissionDetails(link, true, true, true, true, true);
				misRespuestas.put(rpta, isd);
			}
		}
		return misRespuestas;
	}
	public void guardarPuntaje(Integer respuestasAlumnoId,
			Integer puntajeObtenido) {
		RespuestaAlumno r = respuestasAlumnoDAO.obtener(respuestasAlumnoId);
		r.setPuntajeObtenido(puntajeObtenido);
		respuestasAlumnoDAO.actualizar(r);
		
	}
	public void calificarExamen(Alumno a, Examen e, Map<String, String> puntajes) {
		//r_3=10
		Integer respuestasAlumnoId;
		Integer puntajeObtenido;
		Integer nota = 0;
		Iterator<String> it = puntajes.keySet().iterator();
		while(it.hasNext()){
			String r_id = it.next();
			respuestasAlumnoId = Integer.parseInt(r_id.substring(2));
			puntajeObtenido = Integer.parseInt(puntajes.get(r_id));
			guardarPuntaje(respuestasAlumnoId, puntajeObtenido);
			nota += puntajeObtenido;
		}
		guardarNota(a, e, nota);
	}
	private void guardarNota(Alumno a, Examen e, Integer nota) {
		NotaAlumno notaAlumno = new NotaAlumno();
		notaAlumno.setAlumno(a);
		notaAlumno.setExamen(e);
		notaAlumno.setNota(nota);
		notaAlumnoDAO.guardar(notaAlumno);
	}

}
