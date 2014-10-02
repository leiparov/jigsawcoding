package models.services.impl;

import com.avaje.ebean.Page;

import models.daos.ProblemaDAO;
import models.daos.UsuarioDAO;
import models.daos.impl.ProblemaDAOImp;
import models.daos.impl.UsuarioDAOImpl;
import models.entities.Docente;
import models.entities.Problema;
import models.services.ProblemaService;

public class ProblemaServiceImpl implements ProblemaService {

	private static ProblemaDAO problemaDAO = new ProblemaDAOImp();
	private static UsuarioDAO usuarioDAO = new UsuarioDAOImpl();

	@Override
	public void guardarProblema(Docente docente, Problema problema) {
		/* Agrega nueva pregunta o actualiza la pregunta en la lista del docente */
		problemaDAO.guardarProblema(problema);
		int indiceActual = docente.getProblemas().indexOf(problema);
		if (indiceActual == -1) {
			docente.getProblemas().add(problema);
		} else {
			docente.getProblemas().set(indiceActual, problema);
		}
		usuarioDAO.guardar(docente);
	}

	@Override
	public Problema obtenerProblema(int idProblema) {
		return problemaDAO.obtenerProblema(idProblema);
	}

	@Override
	public Page<Problema> page(int page, int pageSize, String sortBy,
			String order, String filter) {
		return problemaDAO.page(page, pageSize, sortBy, order, filter);
	}

	@Override
	public Page<Problema> page(Docente docente, int page, int pageSize,
			String sortBy, String order, String filter) {
		return problemaDAO.page(docente, page, pageSize, sortBy, order, filter);
	}

}
