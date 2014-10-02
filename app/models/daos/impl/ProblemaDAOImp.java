package models.daos.impl;

import play.db.ebean.Model.Finder;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Page;

import models.daos.ProblemaDAO;
import models.entities.Docente;
import models.entities.Problema;

public class ProblemaDAOImp implements ProblemaDAO {

	public static Finder<Integer, Problema> find = new Finder<Integer, Problema>(
			Integer.class, Problema.class);

	@Override
	public void guardarProblema(Problema problema) {
		Ebean.save(problema);

	}

	@Override
	public Problema obtenerProblema(int idProblema) {
		return EbeanUtils.findOrException(Problema.class, idProblema);
	}

	@Override
	public Page<Problema> page(int page, int pageSize, String sortBy,
			String order, String filter) {
		return find.where().ilike("titulo", "%" + filter + "%")
				.orderBy(sortBy + " " + order)
				.findPagingList(pageSize).setFetchAhead(false).getPage(page);
	}

	@Override
	public Page<Problema> page(Docente docente, int page, int pageSize,
			String sortBy, String order, String filter) {
		return find.where().eq("docente_dni", docente.getDNI()).ilike("titulo", "%" + filter + "%")
				.orderBy(sortBy + " " + order)
				.findPagingList(pageSize).setFetchAhead(false).getPage(page);
	}

}
