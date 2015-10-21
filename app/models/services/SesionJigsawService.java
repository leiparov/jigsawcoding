package models.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import models.daos.GrupoDAO;
import models.daos.SesionJigsawDAO;
import models.daos.UsuarioDAO;
import models.entities.Alumno;
import models.entities.Docente;
import models.entities.GrupoExperto;
import models.entities.GrupoJigsaw;
import models.entities.Problema;
import models.entities.SesionJigsaw;

import com.avaje.ebean.Page;

public class SesionJigsawService {

	private static UsuarioDAO usuarioDAO = new UsuarioDAO();
	private static SesionJigsawDAO sesionJigsawDAO = new SesionJigsawDAO();
	private static GrupoDAO grupoDAO = new GrupoDAO();

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
	
	public List<SesionJigsaw> all (){
		return sesionJigsawDAO.all();
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
	public GrupoJigsaw grupoJigsawDelAlumno(Alumno alumno, SesionJigsaw s) throws Exception {
		List<GrupoJigsaw> grupos = s.getGruposJigsaw();
		GrupoJigsaw grupo = null;
		for (GrupoJigsaw gj: grupos){
			if(gj.getAlumnos().contains(alumno)){
				grupo = gj;
			}
		}
		if(grupo == null){
			throw new Exception("Alumno no registrado en esta sesion jigsaw");
		}
		return grupo;
	}

	public void guardarAlumnos(SesionJigsaw s, List<String> dnialumnos) {
		List<Alumno> alumnos = new ArrayList<>();
		for (String dni: dnialumnos){
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
		List<GrupoExperto> gruposExpertos = new ArrayList<>();
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
			gruposExpertos.add(g);
		}
		s.setGruposExpertos(gruposExpertos);
		/*Generacion de nuevos grupos jigsaw*/
		List<GrupoJigsaw> gruposJigsaw = new ArrayList<>();
		for (int i=0; i< cantidadAlumnosPorGrupoExperto; i++){
			GrupoJigsaw gj = new GrupoJigsaw();
			List<Alumno> agj = new ArrayList<>();
			String nombre = "GJ_";
			for(int j = 0; j < tge; j++){
				GrupoExperto ge = gruposExpertos.get(j);
				Alumno alu = ge.getAlumnos().get(i);
				agj.add(alu);
				if(j == tge-1){
					nombre += alu.getIniciales();
				}else{
					nombre += alu.getIniciales() + "_";
				}
			}
			gj.setNombre(nombre);
			gj.setSesionJigsaw(s);
			gj.setAlumnos(agj);
			gruposJigsaw.add(gj);
		}
		s.setGruposJigsaw(gruposJigsaw);
		sesionJigsawDAO.guardarGruposExpertos(s);
		sesionJigsawDAO.guardarGruposJigsaw(s);
	}

	public void guardarProblemas(SesionJigsaw s, HashMap<GrupoExperto, Problema> pares) {
		Iterator<GrupoExperto> it = pares.keySet().iterator();
		List<Problema> problemas = new ArrayList<>();
		while(it.hasNext()){
			GrupoExperto g = it.next();
			Problema p = pares.get(g);
			g.setProblema(p);
			problemas.add(p);
			//Ebean.update(g);
			grupoDAO.actualizarGrupoExperto(g);
		}
		for (GrupoJigsaw gj : s.getGruposJigsaw()){
			gj.setProblemas(problemas);
			grupoDAO.actualizarGrupoJigsaw(gj);
		}
		sesionJigsawDAO.actualizarSesionJigsaw(s);
	}

	public void eliminarGruposExpertos(Integer id) {
		SesionJigsaw s = obtenerSesionJigsaw(id);
		for(GrupoExperto g : s.getGruposExpertos()){
			grupoDAO.eliminarGrupoExperto(g.getId());
		}
		sesionJigsawDAO.eliminarGruposExpertos(s);
	}
	public void eliminarGruposJigsaw(Integer id) {
		SesionJigsaw s = obtenerSesionJigsaw(id);
		for(GrupoJigsaw g : s.getGruposJigsaw()){
			grupoDAO.eliminarGrupoJigsaw(g.getId());
		}
		sesionJigsawDAO.eliminarGruposJigsaw(s);
	}

}
