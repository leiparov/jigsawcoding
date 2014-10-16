package models.daos;

import java.util.HashSet;
import java.util.Set;

import models.entities.Docente;
import models.entities.Examen;
import play.db.ebean.Model.Finder;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Page;

public class ExamenDAO {

	private static Finder<Long, Examen> find = new Finder<Long, Examen>(
			Long.class, Examen.class);
	public Examen obtener(Long id) {
		return EbeanUtils.findOrException(Examen.class, id);
	}
	public void actualizarDatosHorario(Examen examen) {
		Set<String> camposHorario = new HashSet<String>();

		camposHorario.add("tiempoApertura");
		camposHorario.add("tiempoClausura");
		camposHorario.add("duracion");
		camposHorario.add("grupo");

		Ebean.update(examen, camposHorario);
	}

	public void guardar(Examen e) {
		Ebean.save(e);
	}

	public void modificar(Examen e) {

	}

	public void eliminar(Long id) {
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
