package models.daos;

import java.util.List;

import models.entities.Docente;
import models.entities.ParGrupoExpertoProblema;
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
		for (ParGrupoExpertoProblema par: s.getPares()){
			ParGrupoExpertoProblema relacionExistente = buscarEn(sesionExistente.getPares(), par.getGrupoExperto().getGrupoExpertoId());
			if (relacionExistente != null){
				par.setParId(relacionExistente.getParId());
				Ebean.update(par);
			}
		}
		//Eliminados
		for(ParGrupoExpertoProblema existente: sesionExistente.getPares()){
			ParGrupoExpertoProblema relacionActual = buscarEn(s.getPares(), existente.getGrupoExperto().getGrupoExpertoId());
			if(relacionActual == null){
				Ebean.delete(existente);
			}
		}
		Ebean.update(s);
		
	}
	private ParGrupoExpertoProblema buscarEn(List<ParGrupoExpertoProblema> lista, Long idGrupo){
		for (ParGrupoExpertoProblema p: lista){
			if (p.getGrupoExperto().getGrupoExpertoId() == idGrupo) return p;
		}
		return null;
	}


	public void borrarListaProblemasActual(
			List<ParGrupoExpertoProblema> listaActual) {
		for(ParGrupoExpertoProblema p : listaActual){
			Ebean.delete(p);
		}
		
	}

}
