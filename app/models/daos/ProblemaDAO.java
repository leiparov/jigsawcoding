package models.daos;

import java.util.List;

import models.entities.Docente;
import models.entities.Problema;
import play.db.ebean.Model.Finder;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Page;
import com.avaje.ebean.Query;
import com.avaje.ebean.SqlQuery;
import com.avaje.ebean.SqlRow;

public class ProblemaDAO {

	private static Finder<Integer, Problema> find = new Finder<Integer, Problema>(
			Integer.class, Problema.class);

	private Integer generarId() {
		SqlQuery sql = Ebean
				.createSqlQuery("select case when max(id) is null then 0 else max(id) end as maxid from problema");
		SqlRow resultado = sql.findUnique();
		return resultado.getInteger("maxid") + 1;
	}
	public void guardarProblema(Problema problema) {
		problema.setId(generarId());
		Ebean.save(problema);
	}

	public void actualizarProblema(Problema problema) {
		Ebean.update(problema);

	}

	public Problema obtenerProblema(Integer id) {
		return EbeanUtils.findOrException(Problema.class, id);
	}

	public Page<Problema> page(int page, int pageSize, String sortBy,
			String order, String filter) {
		return find.where().ilike("titulo", "%" + filter + "%")
				.orderBy(sortBy + " " + order).findPagingList(pageSize)
				.setFetchAhead(false).getPage(page);
	}

	public Page<Problema> page(Docente docente, int page, int pageSize,
			String sortBy, String order, String filter) {
		return find.where().eq("docente_dni", docente.getDNI())
				.ilike("titulo", "%" + filter + "%")
				.orderBy(sortBy + " " + order).findPagingList(pageSize)
				.setFetchAhead(false).getPage(page);
	}

	public void eliminarProblema(Integer id) {
		Ebean.delete(obtenerProblema(id));
	}

	public List<Problema> buscarProblema(String q, int maxPreguntasBusqueda) {
		q = q.toUpperCase();
		Query<Problema> consulta = Ebean.find(Problema.class).where()
				.like("upper(titulo)", '%' + q + '%').query();
		return consulta.findList();
	}
	public List<Problema> all() {
		return find.all();
	}

}
