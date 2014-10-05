package models.services;

import models.daos.GrupoExpertoDAO;
import models.daos.UsuarioDAO;
import models.entities.Docente;
import models.entities.GrupoExperto;

import com.avaje.ebean.Page;

public class GrupoExpertoService  {

	private static GrupoExpertoDAO grupoExpertoDAO = new GrupoExpertoDAO();
	private static UsuarioDAO usuarioDAO = new UsuarioDAO();

	
	public void guardarGrupoExperto(Docente docente, GrupoExperto grupoExperto) {
		/* Agrega nueva pregunta o actualiza la pregunta en la lista del docente */
		grupoExpertoDAO.guardarGrupoExperto(grupoExperto);
		int indiceActual = docente.getGruposExpertos().indexOf(grupoExperto);
		if (indiceActual == -1) {
			docente.getGruposExpertos().add(grupoExperto);
		} else {
			docente.getGruposExpertos().set(indiceActual, grupoExperto);
		}
		usuarioDAO.guardar(docente);
	}
	
	public void actualizarGrupoExperto(Docente docente, GrupoExperto grupoExperto) {
		/* Agrega nueva pregunta o actualiza la pregunta en la lista del docente */
		grupoExpertoDAO.actualizarGrupoExperto(grupoExperto);
		int indiceActual = docente.getGruposExpertos().indexOf(grupoExperto);
		if (indiceActual == -1) {
			docente.getGruposExpertos().add(grupoExperto);
		} else {
			docente.getGruposExpertos().set(indiceActual, grupoExperto);
		}
		usuarioDAO.guardar(docente);
	}

	
	public GrupoExperto obtenerGrupoExperto(Long idGrupoExperto) {
		return grupoExpertoDAO.obtenerGrupoExperto(idGrupoExperto);
	}

	
	public Page<GrupoExperto> page(int page, int pageSize, String sortBy,
			String order, String filter) {
		return grupoExpertoDAO.page(page, pageSize, sortBy, order, filter);
	}

	
	public Page<GrupoExperto> page(Docente docente, int page, int pageSize,
			String sortBy, String order, String filter) {
		return grupoExpertoDAO.page(docente, page, pageSize, sortBy, order, filter);
	}
	
	public void eliminarGrupoExperto(Long id) {
		grupoExpertoDAO.eliminarGrupoExperto(id);
	}

}
