package models.services;

import models.entities.Docente;
import models.entities.Problema;

public interface ProblemaService {
	
	public void guardarProblema (Docente docente, Problema problema);
	
	public Problema obtenerProblema(int idProblema);

}
