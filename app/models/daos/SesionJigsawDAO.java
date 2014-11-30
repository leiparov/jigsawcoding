package models.daos;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import models.entities.Alumno;
import models.entities.Docente;
import models.entities.GrupoExperto;
import models.entities.SesionJigsaw;
import play.db.ebean.Model.Finder;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Page;
import com.avaje.ebean.SqlQuery;
import com.avaje.ebean.SqlRow;

public class SesionJigsawDAO {
	public static Finder<Integer, SesionJigsaw> find = new Finder<Integer, SesionJigsaw>(
			Integer.class, SesionJigsaw.class);

	private Integer generarId() {
		SqlQuery sql = Ebean
				.createSqlQuery("select case when max(id) is null then "
						+ "0 else max(id) end as maxid from sesion_jigsaw");
		SqlRow resultado = sql.findUnique();
		return resultado.getInteger("maxid") + 1;
	}
	public void guardarSesionJigsaw(SesionJigsaw sesionJigsaw) {
		sesionJigsaw.setId(generarId());
		Ebean.save(sesionJigsaw);
	}

	public void actualizarSesionJigsaw(SesionJigsaw sesionJigsaw) {
		Ebean.update(sesionJigsaw);	
		//Ebean.saveManyToManyAssociations(sesionJigsaw, "alumnos");
	}

	public SesionJigsaw obtenerSesionJigsaw(int id) {
		return EbeanUtils.findOrException(SesionJigsaw.class, id);
	}

	public void eliminarSesionJigsaw(int id) {
		Ebean.delete(obtenerSesionJigsaw(id));
	}

	public Page<SesionJigsaw> page(Docente docente, int page, int pageSize,
			String sortBy, String order, String filter) {
		return find.where().eq("docente_dni", docente.getDNI())
				.ilike("tema", "%" + filter + "%")
				.orderBy(sortBy + " " + order).findPagingList(pageSize)
				.setFetchAhead(false).getPage(page);
	}
	public Page<SesionJigsaw> pageForAlumno(Alumno alumno, int page,
			int pageSize, String sortBy, String order, String filter) {
		SqlQuery sql = Ebean
				.createSqlQuery("select sesion_jigsaw_id from sesion_jigsaw_usuario where usuario_dni = :dni");
		sql.setParameter("dni", alumno.getDNI());
		List<SqlRow> resultado = sql.findList();
		List<Integer> listaIDSesionesJigsaw = new ArrayList<Integer>();
		for (SqlRow row : resultado){
			listaIDSesionesJigsaw.add(row.getInteger("sesion_jigsaw_id"));
		}		
		return find.where().in("id", listaIDSesionesJigsaw)
				.ilike("tema", "%" + filter + "%")
				.orderBy(sortBy + " " + order).findPagingList(pageSize)
				.setFetchAhead(false).getPage(page);
	}

//	public void guardarProblemas(SesionJigsaw s) {
//		SesionJigsaw sesionExistente = obtenerSesionJigsaw(s.getId());
//		// Modificados
//		for (GrupoExpertoProblema par : s.getPares()) {
//			GrupoExpertoProblema relacionExistente = buscarEn(
//					sesionExistente.getPares(), par.getGrupoExperto().getId());
//			if (relacionExistente != null) {
//				par.setId(relacionExistente.getId());
//				Ebean.update(par);
//			}
//		}
//		// Eliminados
//		for (GrupoExpertoProblema existente : sesionExistente.getPares()) {
//			GrupoExpertoProblema relacionActual = buscarEn(s.getPares(),
//					existente.getGrupoExperto().getId());
//			if (relacionActual == null) {
//				Ebean.delete(existente);
//			}
//		}
//		Ebean.update(s);
//
//	}
//	private GrupoExpertoProblema buscarEn(List<GrupoExpertoProblema> lista,
//			Integer idGrupo) {
//		for (GrupoExpertoProblema p : lista) {
//			if (p.getGrupoExperto().getId() == idGrupo)
//				return p;
//		}
//		return null;
//	}
//
//	public void borrarListaProblemasActual(
//			List<GrupoExpertoProblema> listaActual) {
//		for (GrupoExpertoProblema p : listaActual) {
//			Ebean.delete(p);
//		}
//
//	}
	public void guardarAlumnos(SesionJigsaw s) {
		Ebean.update(s);
		Ebean.saveManyToManyAssociations(s, "alumnos");
	}
	public void guardarGruposExpertos(SesionJigsaw s) {
		Ebean.saveAssociation(s, "gruposExpertos");
		//Ebean.save(s);	
	}
	public void eliminarGruposExpertos(SesionJigsaw s) {
		s.setGruposExpertos(null);
		Ebean.update(s);
		Ebean.saveAssociation(s, "gruposExpertos");		
	}
	public void guardarGruposJigsaw(SesionJigsaw s) {
		Ebean.saveAssociation(s, "gruposJigsaw");
	}
	public void eliminarGruposJigsaw(SesionJigsaw s) {
		s.setGruposJigsaw(null);
		Ebean.update(s);
		Ebean.saveAssociation(s, "gruposJigsaw");
		
	}
	public List<SesionJigsaw> all() {
		return find.all();
	}

}
