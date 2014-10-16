package models.services;

import java.util.Date;

import models.daos.ExamenDAO;
import models.daos.ProblemaDAO;
import models.entities.Docente;
import models.entities.Examen;

import com.avaje.ebean.Page;

public class ExamenService {

	private static ExamenDAO examenDAO = new ExamenDAO();
	private static ProblemaDAO problemaDAO = new ProblemaDAO();

	public Examen obtener(Long id) {
		return examenDAO.obtener(id);
	}
	public void actualizarHorario(Examen examen) {
		examenDAO.actualizarDatosHorario(examen);
	}
	public void crear(Examen examen) {
		examen.setTiempoCreacion(new Date());
		examenDAO.guardar(examen);
	}
	public void modificar(Examen e) {
		examenDAO.modificar(e);
	}
	public void eliminar(Long id) {
		examenDAO.eliminar(id);
	}

	public Page<Examen> page(Docente docente, int page, int pageSize,
			String sortBy, String order, String filter) {
		return examenDAO.page(docente, page, pageSize, sortBy, order, filter);
	}

}
