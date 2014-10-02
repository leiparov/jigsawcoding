package models.daos;

import com.avaje.ebean.Page;

import models.entities.Problema;

public interface ProblemaDAO {
	
	void guardarProblema(Problema problema);

	Problema obtenerProblema(int idProblema);

	Page<Problema> page(int page, int pageSize, String sortBy, String order,
			String filter);
}
