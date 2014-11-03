package models.services;

import java.util.List;

import models.daos.SesionJigsawDAO;
import models.daos.UsuarioDAO;
import models.entities.Alumno;
import models.entities.Docente;
import models.entities.GrupoExperto;
import models.entities.GrupoExpertoProblema;
import models.entities.Problema;
import models.entities.SesionJigsaw;

import com.avaje.ebean.Page;

public class SesionJigsawService {

	private static UsuarioDAO usuarioDAO = new UsuarioDAO();
	private static SesionJigsawDAO sesionJigsawDAO = new SesionJigsawDAO();

	public void guardarSesionJigsaw(Docente docente, SesionJigsaw sesionJigsaw) {
		sesionJigsawDAO.guardarSesionJigsaw(sesionJigsaw);
		int indiceActual = docente.getSesionesJigsaw().indexOf(sesionJigsaw);
		if (indiceActual == -1) {
			docente.getSesionesJigsaw().add(sesionJigsaw);
		} else {
			docente.getSesionesJigsaw().set(indiceActual, sesionJigsaw);
		}
		usuarioDAO.guardar(docente);
	}

	public void actualizarSesionJigsaw(Docente docente,
			SesionJigsaw sesionJigsaw) {

		if (sesionJigsaw.getTotalGruposExpertos() != sesionJigsaw.getPares()
				.size()) {
			borrarPares(sesionJigsaw);
		}

		sesionJigsawDAO.actualizarSesionJigsaw(sesionJigsaw);
		int indiceActual = docente.getSesionesJigsaw().indexOf(sesionJigsaw);
		if (indiceActual == -1) {
			docente.getSesionesJigsaw().add(sesionJigsaw);
		} else {
			docente.getSesionesJigsaw().set(indiceActual, sesionJigsaw);
		}
		usuarioDAO.guardar(docente);
	}

	public SesionJigsaw obtenerSesionJigsaw(int id) {
		return sesionJigsawDAO.obtenerSesionJigsaw(id);
	}

	public Page<SesionJigsaw> page(Docente docente, int page, int pageSize,
			String sortBy, String order, String filter) {
		return sesionJigsawDAO.page(docente, page, pageSize, sortBy, order,
				filter);
	}
	
	public Page<SesionJigsaw> pageForAlumno(Alumno alumno, int page, int pageSize,
			String sortBy, String order, String filter) {
		return sesionJigsawDAO.pageForAlumno(alumno, page, pageSize, sortBy, order,
				filter);
	}

	public void eliminarSesionJigsaw(int id) {
		sesionJigsawDAO.eliminarSesionJigsaw(id);
	}

	public void guardarProblemas(SesionJigsaw s) {
		sesionJigsawDAO.guardarProblemas(s);
	}

	private void borrarPares(SesionJigsaw s) {
		List<GrupoExpertoProblema> listaActual = s.getPares();
		sesionJigsawDAO.borrarListaProblemasActual(listaActual);
	}

	public Problema problemaAResolver(Alumno alumno, SesionJigsaw s) {
		List<GrupoExpertoProblema> pares = s.getPares();
		for (GrupoExpertoProblema gep: pares){
			GrupoExperto ge = gep.getGrupoExperto();
			if(ge.getAlumnos().contains(alumno)){
				return gep.getProblema();
			}
		}
		return null;
	}

}
