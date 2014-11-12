package models.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import models.daos.GrupoExpertoDAO;
import models.daos.SesionJigsawDAO;
import models.daos.UsuarioDAO;
import models.entities.Alumno;
import models.entities.Docente;
import models.entities.GrupoExperto;
import models.entities.Problema;
import models.entities.SesionJigsaw;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Page;

public class SesionJigsawService {

	private static UsuarioDAO usuarioDAO = new UsuarioDAO();
	private static SesionJigsawDAO sesionJigsawDAO = new SesionJigsawDAO();
	private static GrupoExpertoDAO grupoExpertoDAO = new GrupoExpertoDAO();

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

//		if (sesionJigsaw.getTotalGruposExpertos() != sesionJigsaw.getPares()
//				.size()) {
//			borrarPares(sesionJigsaw);
//		}

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

//	public void guardarProblemas(SesionJigsaw s) {
//		sesionJigsawDAO.guardarProblemas(s);
//	}
//
//	private void borrarPares(SesionJigsaw s) {
//		List<GrupoExpertoProblema> listaActual = s.getPares();
//		sesionJigsawDAO.borrarListaProblemasActual(listaActual);
//	}
//
	public GrupoExperto grupoExpertoDelAlumno(Alumno alumno, SesionJigsaw s) throws Exception {
		List<GrupoExperto> grupos = s.getGruposExpertos();
		GrupoExperto grupo = null;
		for (GrupoExperto ge: grupos){
			if(ge.getAlumnos().contains(alumno)){
				grupo = ge;
			}
		}
		if(grupo == null){
			throw new Exception("Alumno no registrado en esta sesion jigsaw");
		}
		return grupo;
	}

	public void guardarAlumnos(SesionJigsaw s, List<Integer> dnialumnos) {
		List<Alumno> alumnos = new ArrayList<>();
		for (Integer dni: dnialumnos){
			alumnos.add(usuarioDAO.obtener(dni, Alumno.class));
		}
		s.setAlumnos(alumnos);
		sesionJigsawDAO.guardarAlumnos(s);
		
	}

	public void generarGrupos(SesionJigsaw s) {
		//eliminarGruposExpertos(s.getId());
		
		/*Generacion de nuevos grupos expertos*/
		List<Alumno> alumnos = s.getAlumnos();
		int tge = s.getTotalGruposExpertos();
		int cantidadAlumnosPorGrupoExperto = alumnos.size()/tge;
		List<GrupoExperto> grupos = new ArrayList<>();
		for (int i = 0; i<tge; i++){
			GrupoExperto g = new GrupoExperto();
			List<Alumno> a = new ArrayList<>();
			String nombre = "GE_";
			for (int j = 0; j < cantidadAlumnosPorGrupoExperto; j++){
				int x = (int)Math.random()*(alumnos.size()-1);
				Alumno alu = alumnos.get(x);
				a.add(alu);
				alumnos.remove(x);
				if(j == cantidadAlumnosPorGrupoExperto-1){
					nombre += alu.getIniciales();
				}else{
					nombre += alu.getIniciales() + "_";
				}
			}
			g.setNombre(nombre);
			g.setSesionJigsaw(s);
			g.setAlumnos(a);
			grupos.add(g);
		}
		s.setGruposExpertos(grupos);
		sesionJigsawDAO.guardarGruposExpertos(s);
		
	}

	public void guardarProblemas(HashMap<GrupoExperto, Problema> pares) {
		Iterator<GrupoExperto> it = pares.keySet().iterator();
		while(it.hasNext()){
			GrupoExperto g = it.next();
			Problema p = pares.get(g);
			g.setProblema(p);
			Ebean.update(g);
		}
		
	}

	public void eliminarGruposExpertos(Integer id) {
		SesionJigsaw s = obtenerSesionJigsaw(id);
		for(GrupoExperto g : s.getGruposExpertos()){
			grupoExpertoDAO.eliminarGrupoExperto(g.getId());
		}
		sesionJigsawDAO.eliminarGruposExpertos(s);
		
	}

}
