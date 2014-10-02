package models.daos;

import models.entities.Problema;

public interface ProblemaDAO {
	
	void guardarProblema(Problema problema);

	Problema obtenerProblema(int idProblema);
}
