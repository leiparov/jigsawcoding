package models.services;

import java.util.List;

import models.daos.ProblemaDAO;
import models.daos.UsuarioDAO;
import models.entities.Docente;
import models.entities.Problema;

import com.avaje.ebean.Page;

public class ProblemaService {

	private static ProblemaDAO problemaDAO = new ProblemaDAO();
	private static UsuarioDAO usuarioDAO = new UsuarioDAO();
	 private static final int MAX_PREGUNTAS_BUSQUEDA = 30;

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

	public void actualizarProblema(Docente docente, Problema problema) {
		/* Agrega nueva pregunta o actualiza la pregunta en la lista del docente */
		problemaDAO.actualizarProblema(problema);
		int indiceActual = docente.getProblemas().indexOf(problema);
		if (indiceActual == -1) {
			docente.getProblemas().add(problema);
		} else {
			docente.getProblemas().set(indiceActual, problema);
		}
		usuarioDAO.guardar(docente);
	}

	public Problema obtenerProblema(Integer idProblema) {
		return problemaDAO.obtenerProblema(idProblema);
	}

	public Page<Problema> page(int page, int pageSize, String sortBy,
			String order, String filter) {
		return problemaDAO.page(page, pageSize, sortBy, order, filter);
	}

	public Page<Problema> page(Docente docente, int page, int pageSize,
			String sortBy, String order, String filter) {
		return problemaDAO.page(docente, page, pageSize, sortBy, order, filter);
	}

	public void eliminarProblema(Integer id) {
		problemaDAO.eliminarProblema(id);
	}

	public List<Problema> buscarPorTexto(String q) {
		return problemaDAO.buscarProblema(q, MAX_PREGUNTAS_BUSQUEDA);
	}
	
	public List<Problema> all () {
		return problemaDAO.all();
	}

}
