package models.daos;

import java.util.List;

import models.entities.Docente;
import models.entities.GrupoExpertoProblema;
import models.entities.SesionJigsaw;
import play.db.ebean.Model.Finder;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Page;
import com.avaje.ebean.SqlQuery;
import com.avaje.ebean.SqlRow;

public class SesionJigsawDAO {
	public static Finder<Integer, SesionJigsaw> find = new Finder<Integer, SesionJigsaw>(
			Integer.class, SesionJigsaw.class);

	private Integer generarId(){
		SqlQuery sql = Ebean.createSqlQuery("select case when max(id) is null then 0 else max(id) end as maxid from sesion_jigsaw");
		SqlRow resultado = sql.findUnique();
		return resultado.getInteger("maxid")+1;
	}
	public void guardarSesionJigsaw(SesionJigsaw sesionJigsaw) {
		sesionJigsaw.setId(generarId());
		Ebean.save(sesionJigsaw);
	}

	public void actualizarSesionJigsaw(SesionJigsaw sesionJigsaw) {
		Ebean.update(sesionJigsaw);
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
				.orderBy(sortBy + " " + order).findPagingList(pageSize)
				.setFetchAhead(false).getPage(page);
	}

	public void guardarProblemas(SesionJigsaw s) {		
		SesionJigsaw sesionExistente = obtenerSesionJigsaw(s.getId());
		//Modificados
		for (GrupoExpertoProblema par: s.getPares()){
			GrupoExpertoProblema relacionExistente = buscarEn(sesionExistente.getPares(), par.getGrupoExperto().getId());
			if (relacionExistente != null){
				par.setId(relacionExistente.getId());
				Ebean.update(par);
			}
		}
		//Eliminados
		for(GrupoExpertoProblema existente: sesionExistente.getPares()){
			GrupoExpertoProblema relacionActual = buscarEn(s.getPares(), existente.getGrupoExperto().getId());
			if(relacionActual == null){
				Ebean.delete(existente);
			}
		}
		Ebean.update(s);
		
	}
	private GrupoExpertoProblema buscarEn(List<GrupoExpertoProblema> lista, Integer idGrupo){
		for (GrupoExpertoProblema p: lista){
			if (p.getGrupoExperto().getId() == idGrupo) return p;
		}
		return null;
	}


	public void borrarListaProblemasActual(
			List<GrupoExpertoProblema> listaActual) {
		for(GrupoExpertoProblema p : listaActual){
			Ebean.delete(p);
		}
		
	}

}
