package models.daos;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import models.entities.Alumno;
import models.entities.Docente;
import models.entities.Examen;
import models.entities.ProblemaExamen;
import models.entities.RespuestasAlumno;
import models.entities.SesionJigsaw;
import play.db.ebean.Model.Finder;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Page;
import com.avaje.ebean.SqlQuery;
import com.avaje.ebean.SqlRow;

public class ExamenDAO {

	private static Finder<Integer, Examen> find = new Finder<Integer, Examen>(
			Integer.class, Examen.class);
	public Examen obtener(Integer integer) {
		return EbeanUtils.findOrException(Examen.class, integer);
	}
	public void actualizarDatosHorario(Examen examen) {
		Set<String> camposHorario = new HashSet<String>();

		camposHorario.add("tiempoApertura");
		camposHorario.add("tiempoClausura");
		camposHorario.add("duracion");
		camposHorario.add("sesionJigsaw");

		Ebean.update(examen, camposHorario);
		SesionJigsaw s = examen.getSesionJigsaw();
		s.setExamen(examen);
		Ebean.update(s);
	}

	public void guardar(Examen e) {
		Ebean.save(e);
	}

	public void modificar(Examen e) {
		Examen examenExistente = obtener(e.getId());
		// Modificados
		for (ProblemaExamen pregunta : e.getProblemas()) {
			ProblemaExamen relacionExistente = buscarEn(
					examenExistente.getProblemas(), pregunta.getProblema()
							.getId());
			if (relacionExistente != null) {
				pregunta.setId(relacionExistente.getId());
				Ebean.update(pregunta);
			}
		}
		// Eliminados
		for (ProblemaExamen existente : examenExistente.getProblemas()) {
			ProblemaExamen relacionActual = buscarEn(e.getProblemas(),
					existente.getProblema().getId());
			if (relacionActual == null) {
				Ebean.delete(existente);
			}
		}
	}
	private ProblemaExamen buscarEn(List<ProblemaExamen> lista,
			Integer idPregunta) {
		for (ProblemaExamen pe : lista) {
			if (pe.getProblema().getId() == idPregunta)
				return pe;
		}
		return null;
	}

	public void eliminar(Integer id) {
		Ebean.delete(obtener(id));
	}

	public Page<Examen> page(Docente docente, int page, int pageSize,
			String sortBy, String order, String filter) {
		return find.where().eq("docente_dni", docente.getDNI())
				.ilike("titulo", "%" + filter + "%")
				.orderBy(sortBy + " " + order).findPagingList(pageSize)
				.setFetchAhead(false).getPage(page);
	}
	public boolean existeNotaExamen(Alumno a, Examen e) {
		SqlQuery sql = Ebean
				.createSqlQuery("select * from nota_alumno where alumno_dni like :dni and examen_id like :examenid");
		sql.setParameter("dni", a.getDNI());
		sql.setParameter("examenid", e.getId());
		SqlRow resultado = sql.findUnique();
		if (resultado == null)
			return false;
		else
			return true;

	}
	public ProblemaExamen obtenerProblemaExamen(Integer id) {
		return Ebean.find(ProblemaExamen.class, id);
	}
	public void guardarRespuestasAlumno(List<RespuestasAlumno> respuestasAlumno) {
		Ebean.save(respuestasAlumno);

	}
	public boolean yaRindioExamen(Alumno a, Examen e) {
		SqlQuery sql = Ebean
				.createSqlQuery("select pe.examen_id from respuestas_alumno ra "
						+ "left join problema_examen pe on ra.problema_examen_id = pe.id "
						+ "where pe.examen_id = :examenid and ra.alumno_dni = :dni group by pe.examen_id");
		sql.setParameter("dni", a.getDNI());
		sql.setParameter("examenid", e.getId());
		SqlRow resultado = sql.findUnique();
		if (resultado == null)
			return false;
		else
			return true;
	}

}
