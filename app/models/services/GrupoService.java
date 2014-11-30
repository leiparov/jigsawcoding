package models.services;

import java.util.List;

import models.daos.AlumnoDAO;
import models.daos.GrupoDAO;
import models.daos.UsuarioDAO;
import models.entities.Docente;
import models.entities.Grupo;
import models.entities.GrupoExperto;

import com.avaje.ebean.Page;

public class GrupoService  {

	private static GrupoDAO grupoDAO = new GrupoDAO();
	private static UsuarioDAO usuarioDAO = new UsuarioDAO();
	private static AlumnoDAO alumnoDAO = new AlumnoDAO();

	
	public void guardarGrupoExperto(Docente docente, GrupoExperto grupoExperto) {
		/* Agrega nueva pregunta o actualiza la pregunta en la lista del docente */
//		grupoExpertoDAO.guardarGrupoExperto(grupoExperto);
//		int indiceActual = docente.getGruposExpertos().indexOf(grupoExperto);
//		if (indiceActual == -1) {
//			docente.getGruposExpertos().add(grupoExperto);
//		} else {
//			docente.getGruposExpertos().set(indiceActual, grupoExperto);
//		}
//		usuarioDAO.guardar(docente);
	}
	
	public void actualizarGrupoExperto(Docente docente, GrupoExperto grupoExperto) {
		/* Agrega nueva pregunta o actualiza la pregunta en la lista del docente */
//		grupoExpertoDAO.actualizarGrupoExperto(grupoExperto);
//		int indiceActual = docente.getGruposExpertos().indexOf(grupoExperto);
//		if (indiceActual == -1) {
//			docente.getGruposExpertos().add(grupoExperto);
//		} else {
//			docente.getGruposExpertos().set(indiceActual, grupoExperto);
//		}
//		usuarioDAO.guardar(docente);
	}

	
	public GrupoExperto obtenerGrupoExperto(Integer id) {
		return grupoDAO.obtenerGrupoExperto(id);
	}
	public Grupo obtenerGrupo(Integer id) {
		return grupoDAO.obtenerGrupo(id);
	}
	
	public Page<GrupoExperto> page(int page, int pageSize, String sortBy,
			String order, String filter) {
		return grupoDAO.page(page, pageSize, sortBy, order, filter);
	}

	
	public Page<GrupoExperto> page(Docente docente, int page, int pageSize,
			String sortBy, String order, String filter) {
		return grupoDAO.page(docente, page, pageSize, sortBy, order, filter);
	}
	
	public void eliminarGrupoExperto(Integer id) {
		grupoDAO.eliminarGrupoExperto(id);
	}
	
    public void actualizarAlumnos(GrupoExperto grupo, List<Integer> dnialumnos) {
        
        
//        List<Alumno> alumnosAnteriores = grupo.getAlumnos();
//        for (Alumno a : alumnosAnteriores){
//        	a.setGrupoExperto(null);
//        	usuarioDAO.guardar(a);
//        }
//        
//        List<Alumno> alumnosFuturos = new LinkedList<Alumno>();
//        for(Integer dniAlumno : dnialumnos){
//        	Alumno a = alumnoDAO.obtener(dniAlumno);
//        	a.setGrupoExperto(grupo);
//        	usuarioDAO.guardar(a);
//            alumnosFuturos.add(a);
//        }        
//        grupo.setAlumnos(alumnosFuturos);
//        //Logger.info(grupo.getAlumnos().get(0).toString());
//        grupoExpertoDAO.guardarAlumnos(grupo);
    }
    
    public List<Grupo> all(){
    	return grupoDAO.all();
    }

}
