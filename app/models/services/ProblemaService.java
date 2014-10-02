package models.services;

import models.entities.Docente;
import models.entities.Problema;

import com.avaje.ebean.Page;

public interface ProblemaService {
	
	public void guardarProblema (Docente docente, Problema problema);
	
	public Problema obtenerProblema(int idProblema);
	
	public Page<Problema> page(int page, int pageSize, String sortBy, String order, String filter);

}
