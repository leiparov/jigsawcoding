package models.daos;

import models.entities.Docente;
import models.entities.GrupoExperto;

import com.avaje.ebean.Page;

public interface GrupoExpertoDAO {
	
	void guardarGrupoExperto(GrupoExperto grupoExperto);
	
	void actualizarGrupoExperto(GrupoExperto grupoExperto);

	GrupoExperto obtenerGrupoExperto(Long idGrupoExperto);

	Page<GrupoExperto> page(int page, int pageSize, String sortBy, String order,
			String filter);
	
	Page<GrupoExperto> page(Docente docente, int page, int pageSize, String sortBy, String order,
			String filter);

	void eliminarGrupoExperto(Long id);
}
