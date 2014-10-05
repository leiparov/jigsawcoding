package models.daos.impl;

import play.db.ebean.Model.Finder;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Page;

import models.daos.GrupoExpertoDAO;
import models.entities.Docente;
import models.entities.GrupoExperto;

public class GrupoExpertoDAOImp implements GrupoExpertoDAO {

	public static Finder<Integer, GrupoExperto> find = new Finder<Integer, GrupoExperto>(
			Integer.class, GrupoExperto.class);

	@Override
	public void guardarGrupoExperto(GrupoExperto grupoExperto) {
		Ebean.save(grupoExperto);

	}
	@Override
	public void actualizarGrupoExperto(GrupoExperto grupoExperto) {
		Ebean.update(grupoExperto);

	}

	@Override
	public GrupoExperto obtenerGrupoExperto(Long idGrupoExperto) {
		return EbeanUtils.findOrException(GrupoExperto.class, idGrupoExperto);
	}

	@Override
	public Page<GrupoExperto> page(int page, int pageSize, String sortBy,
			String order, String filter) {
		return find.where().ilike("nombre", "%" + filter + "%")
				.orderBy(sortBy + " " + order)
				.findPagingList(pageSize).setFetchAhead(false).getPage(page);
	}

	@Override
	public Page<GrupoExperto> page(Docente docente, int page, int pageSize,
			String sortBy, String order, String filter) {
		return find.where().eq("docente_dni", docente.getDNI()).ilike("nombre", "%" + filter + "%")
				.orderBy(sortBy + " " + order)
				.findPagingList(pageSize).setFetchAhead(false).getPage(page);
	}
	@Override
	public void eliminarGrupoExperto(Long id) {
		Ebean.delete(obtenerGrupoExperto(id));		
	}

}
