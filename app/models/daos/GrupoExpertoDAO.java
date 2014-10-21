package models.daos;

import models.entities.Docente;
import models.entities.GrupoExperto;
import play.db.ebean.Model.Finder;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Page;

public class GrupoExpertoDAO {

	public static Finder<Integer, GrupoExperto> find = new Finder<Integer, GrupoExperto>(
			Integer.class, GrupoExperto.class);

	public void guardarGrupoExperto(GrupoExperto grupoExperto) {
		Ebean.save(grupoExperto);
		guardarAlumnos(grupoExperto);

	}

	public void actualizarGrupoExperto(GrupoExperto grupoExperto) {
		Ebean.update(grupoExperto);

	}

	public GrupoExperto obtenerGrupoExperto(Integer id) {
		return EbeanUtils.findOrException(GrupoExperto.class, id);
	}

	public Page<GrupoExperto> page(int page, int pageSize, String sortBy,
			String order, String filter) {
		return find.where().ilike("nombre", "%" + filter + "%")
				.orderBy(sortBy + " " + order).findPagingList(pageSize)
				.setFetchAhead(false).getPage(page);
	}

	public Page<GrupoExperto> page(Docente docente, int page, int pageSize,
			String sortBy, String order, String filter) {
		return find.where().eq("docente_dni", docente.getDNI())
				.ilike("nombre", "%" + filter + "%")
				.orderBy(sortBy + " " + order).findPagingList(pageSize)
				.setFetchAhead(false).getPage(page);
	}

	public void eliminarGrupoExperto(Integer id) {
		Ebean.delete(obtenerGrupoExperto(id));
	}

	public void guardarAlumnos(GrupoExperto grupo) {
		Ebean.saveManyToManyAssociations(grupo, "alumnos");		
	}
	

}
