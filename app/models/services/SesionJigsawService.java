package models.services;

import models.daos.SesionJigsawDAO;
import models.daos.UsuarioDAO;
import models.entities.Docente;
import models.entities.SesionJigsaw;

import com.avaje.ebean.Page;

public class SesionJigsawService {

	private static UsuarioDAO usuarioDAO = new UsuarioDAO();
	private static SesionJigsawDAO sesionJigsawDAO = new SesionJigsawDAO();

	public void guardarSesionJigsaw(Docente docente, SesionJigsaw sesionJigsaw) {

		sesionJigsawDAO.guardarSesionJigsaw(sesionJigsaw);
		int indiceActual = docente.getSesionesJigsaw().indexOf(sesionJigsaw);
		if (indiceActual == -1) {
			docente.getSesionesJigsaw().add(sesionJigsaw);
		} else {
			docente.getSesionesJigsaw().set(indiceActual, sesionJigsaw);
		}
		usuarioDAO.guardar(docente);
	}

	public void actualizarSesionJigsaw(Docente docente,
			SesionJigsaw sesionJigsaw) {

		sesionJigsawDAO.actualizarSesionJigsaw(sesionJigsaw);
		int indiceActual = docente.getSesionesJigsaw().indexOf(sesionJigsaw);
		if (indiceActual == -1) {
			docente.getSesionesJigsaw().add(sesionJigsaw);
		} else {
			docente.getSesionesJigsaw().set(indiceActual, sesionJigsaw);
		}
		usuarioDAO.guardar(docente);
	}

	public SesionJigsaw obtenerSesionJigsaw(int id) {
		return sesionJigsawDAO.obtenerSesionJigsaw(id);
	}

	public Page<SesionJigsaw> page(Docente docente, int page, int pageSize,
			String sortBy, String order, String filter) {
		return sesionJigsawDAO.page(docente, page, pageSize, sortBy, order,
				filter);
	}

	public void eliminarSesionJigsaw(int id) {
		sesionJigsawDAO.eliminarSesionJigsaw(id);
	}

}
