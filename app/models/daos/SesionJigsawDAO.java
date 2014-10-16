package models.daos;

import java.util.List;

import models.entities.Docente;
import models.entities.ParGrupoExpertoProblema;
import models.entities.SesionJigsaw;
import play.db.ebean.Model.Finder;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Page;

public class SesionJigsawDAO {
	public static Finder<Integer, SesionJigsaw> find = new Finder<Integer, SesionJigsaw>(
			Integer.class, SesionJigsaw.class);

	public void guardarSesionJigsaw(SesionJigsaw sesionJigsaw) {
		Ebean.save(sesionJigsaw);
	}

	public void actualizarSesionJigsaw(SesionJigsaw sesionJigsaw) {
		Ebean.update(sesionJigsaw);
	}

	public SesionJigsaw obtenerSesionJigsaw(int id) {
		return EbeanUtils.findOrException(SesionJigsaw.class, id);
	}

	public void eliminarSesionJigsaw(int id) {
		Ebean.delete(obtenerSesionJigsaw(id));
	}

	public Page<SesionJigsaw> page(Docente docente, int page, int pageSize,
			String sortBy, String order, String filter) {
		return find.where().eq("docente_dni", docente.getDNI())
				.orderBy(sortBy + " " + order).findPagingList(pageSize)
				.setFetchAhead(false).getPage(page);
	}

	public void guardarProblemas(SesionJigsaw s,
			List<ParGrupoExpertoProblema> lista) {
		Ebean.saveAssociation(s, "pares");
	}


	public void borrarListaProblemasActual(
			List<ParGrupoExpertoProblema> listaActual) {
		for(ParGrupoExpertoProblema p : listaActual){
			Ebean.delete(p);
		}
		
	}

}
