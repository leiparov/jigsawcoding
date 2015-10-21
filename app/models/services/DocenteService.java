package models.services;

import models.daos.DocenteDAO;
import models.entities.Docente;

public class DocenteService {

	private static DocenteDAO docenteDAO = new DocenteDAO();

	public Docente obtener(int dni) {
		return docenteDAO.obtener(dni);
	}

	public void guardar(Docente docente) {
		docenteDAO.guardar(docente);
	}

	public void eliminar(Docente docente) {
		docenteDAO.eliminar(docente);
	}

}