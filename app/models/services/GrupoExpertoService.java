package models.services;

import models.entities.Docente;
import models.entities.GrupoExperto;

import com.avaje.ebean.Page;

public interface GrupoExpertoService {
	
	public void guardarGrupoExperto (Docente docente, GrupoExperto grupoExperto);
	public void actualizarGrupoExperto (Docente docente, GrupoExperto grupoExperto);
	public void eliminarGrupoExperto(Long id);
	
	public GrupoExperto obtenerGrupoExperto(Long idGrupoExperto);
	
	public Page<GrupoExperto> page(int page, int pageSize, String sortBy, String order, String filter);
	
	public Page<GrupoExperto> page(Docente docente, int page, int pageSize, String sortBy, String order, String filter);

}
