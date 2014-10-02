package models.daos.impl;

import com.avaje.ebean.Ebean;

import models.daos.ProblemaDAO;
import models.entities.Problema;

public class ProblemaDAOImp implements ProblemaDAO {

	@Override
	public void guardarProblema(Problema problema) {
		Ebean.save(problema);

	}

	@Override
	public Problema obtenerProblema(int idProblema) {
		return EbeanUtils.findOrException(Problema.class, idProblema);
	}

}
