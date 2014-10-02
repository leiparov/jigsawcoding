package models.daos;

import models.entities.Docente;
import models.entities.Problema;

import com.avaje.ebean.Page;

public interface ProblemaDAO {
	
	void guardarProblema(Problema problema);

	Problema obtenerProblema(int idProblema);

	Page<Problema> page(int page, int pageSize, String sortBy, String order,
			String filter);
	
	Page<Problema> page(Docente docente, int page, int pageSize, String sortBy, String order,
			String filter);
}
