package models.services.impl;

import com.avaje.ebean.Page;

import models.daos.GrupoExpertoDAO;
import models.daos.UsuarioDAO;
import models.daos.impl.GrupoExpertoDAOImp;
import models.daos.impl.UsuarioDAOImpl;
import models.entities.Docente;
import models.entities.GrupoExperto;
import models.services.GrupoExpertoService;

public class GrupoExpertoServiceImpl implements GrupoExpertoService {

	private static GrupoExpertoDAO grupoExpertoDAO = new GrupoExpertoDAOImp();
	private static UsuarioDAO usuarioDAO = new UsuarioDAOImpl();

	@Override
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
	@Override
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

	@Override
	public GrupoExperto obtenerGrupoExperto(Long idGrupoExperto) {
		return grupoExpertoDAO.obtenerGrupoExperto(idGrupoExperto);
	}

	@Override
	public Page<GrupoExperto> page(int page, int pageSize, String sortBy,
			String order, String filter) {
		return grupoExpertoDAO.page(page, pageSize, sortBy, order, filter);
	}

	@Override
	public Page<GrupoExperto> page(Docente docente, int page, int pageSize,
			String sortBy, String order, String filter) {
		return grupoExpertoDAO.page(docente, page, pageSize, sortBy, order, filter);
	}
	@Override
	public void eliminarGrupoExperto(Long id) {
		grupoExpertoDAO.eliminarGrupoExperto(id);
	}

}
