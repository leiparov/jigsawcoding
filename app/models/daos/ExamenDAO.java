package models.daos;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import models.entities.Docente;
import models.entities.Examen;
import models.entities.ProblemaExamen;
import play.db.ebean.Model.Finder;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Page;

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

}
