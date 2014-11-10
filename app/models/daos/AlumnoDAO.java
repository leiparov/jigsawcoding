package models.daos;

import java.util.ArrayList;
import java.util.List;

import play.db.ebean.Model.Finder;
import models.entities.Alumno;
import models.entities.Problema;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Expr;
import com.avaje.ebean.Page;
import com.avaje.ebean.Query;
import com.avaje.ebean.RawSql;
import com.avaje.ebean.RawSqlBuilder;
import com.avaje.ebean.SqlQuery;
import com.avaje.ebean.SqlRow;

import exceptions.DAOException;

public class AlumnoDAO {

	public static UsuarioDAO usuarioDAO = new UsuarioDAO();

	public Alumno obtener(int dni) {
		return usuarioDAO.obtener(dni, Alumno.class);
	}

	public List<Alumno> buscarAlumno(String criterio, int max) {
		criterio = criterio.toUpperCase();
		Query<Alumno> query = Ebean
				.find(Alumno.class)
				.where()
				.disjunction()
				.add(Expr.like("upper(nombres)", '%' + criterio + '%'))
				.add(Expr.like("upper(nombres)", "%" + criterio + '%'))
				.add(Expr.like("upper(apellidoPaterno)", '%' + criterio + '%'))
				.add(Expr.like("upper(apellidoMaterno)", "%" + criterio + '%'))
				.setMaxRows(max);
		return query.findList();
	}

	public void guardar(Alumno alumno) {
		Ebean.save(alumno);
	}

	public List<Alumno> disponibles() {
		int dni = 0;
//		String sql = "select u.dni from usuario u "
//				+ "where u.grupo_experto_id is null and u.tipo = 'ALUMNO' ";
		String sql = "select u.dni from usuario u where u.tipo = 'ALUMNO'";
		SqlQuery sqlQuery = Ebean.createSqlQuery(sql);
		List<Alumno> alumnosDisponibles = new ArrayList<>();
		List<SqlRow> resultado = sqlQuery.findList();
		if (resultado == null) {
			throw new DAOException("No existen alumnos disponibles");
		} else {
			for (SqlRow row : resultado) {
				dni = row.getInteger("dni");
				System.out.println(obtener(dni).getNombreCompleto());
				alumnosDisponibles.add(obtener(dni));
			}
		}
		System.out.println(alumnosDisponibles);
		return alumnosDisponibles;
	}

	private static Finder<Integer, Alumno> find = new Finder<Integer, Alumno>(
			Integer.class, Alumno.class);

	public Page<Alumno> page(int page, int pageSize, String sortBy,
			String order, String filter) {
		return find.where()
				.disjunction()
				.add(Expr.ilike("dni", "%" + filter + "%"))
				.add(Expr.ilike("nombres", "%" + filter + "%"))
				.add(Expr.ilike("apellido_paterno", "%" + filter + "%"))
				.add(Expr.ilike("apellido_materno", "%" + filter + "%"))
				.orderBy(sortBy + " " + order).findPagingList(pageSize)
				.setFetchAhead(false).getPage(page);
	}

	public List<Alumno> todos() {
		return find.all();
	}

}
