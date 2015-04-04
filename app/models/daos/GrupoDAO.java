package models.daos;

import java.util.List;

import models.entities.Docente;
import models.entities.Grupo;
import models.entities.GrupoExperto;
import models.entities.GrupoJigsaw;
import play.db.ebean.Model.Finder;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Page;

public class GrupoDAO {

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
	public GrupoJigsaw obtenerGrupoJigsaw(Integer id) {
		return EbeanUtils.findOrException(GrupoJigsaw.class, id);
	}

	public Page<GrupoExperto> page(int page, int pageSize, String sortBy,
			String order, String filter) {
		return find.where().ilike("nombre", "%" + filter + "%")
				.orderBy(sortBy + " " + order).findPagingList(pageSize)
				.setFetchAhead(false).getPage(page);
	}

	public Page<GrupoExperto> page(Docente docente, int page, int pageSize,
			String sortBy, String order, String filter) {
		int dni = docente.getDNI();
		play.Logger.info("dni" + dni);
		return find.where()
				.eq("sesionJigsaw.docente.DNI", dni)
				.ilike("nombre", "%" + filter + "%")
				.orderBy(sortBy + " " + order).findPagingList(pageSize)
				.setFetchAhead(false).getPage(page);
	}

	public void eliminarGrupoExperto(Integer id) {
		Ebean.delete(obtenerGrupoExperto(id));
	}

	public void guardarAlumnos(GrupoExperto grupo) {
		Ebean.update(grupo);
		Ebean.saveAssociation(grupo, "alumnos");
	}

	public void eliminarGrupoJigsaw(Integer id) {
		Ebean.delete(obtenerGrupoJigsaw(id));
	}

	public void actualizarGrupoJigsaw(GrupoJigsaw gj) {
		Ebean.update(gj);
		Ebean.saveAssociation(gj, "problemas");
	}

	public static Finder<Integer, Grupo> findGrupo = new Finder<Integer, Grupo>(
			Integer.class, Grupo.class);
	public List<Grupo> all() {
		return findGrupo.all();
	}

	public Grupo obtenerGrupo(Integer id) {
		return EbeanUtils.findOrException(Grupo.class, id);
	}

}
