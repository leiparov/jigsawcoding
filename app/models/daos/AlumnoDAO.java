package models.daos;

import java.util.ArrayList;
import java.util.List;

import models.entities.Alumno;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Expr;
import com.avaje.ebean.Query;
import com.avaje.ebean.RawSql;
import com.avaje.ebean.RawSqlBuilder;
import com.avaje.ebean.SqlQuery;
import com.avaje.ebean.SqlRow;

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
				.add(Expr.like("upper(nombres)", criterio + '%'))
				.add(Expr.like("upper(nombres)", "% " + criterio + '%'))
				.add(Expr.like("upper(apellidoPaterno)", criterio + '%'))
				.add(Expr.like("upper(apellidoMaterno)", "% " + criterio + '%'))
				.setMaxRows(max);
		return query.findList();
	}

	public void guardar(Alumno alumno) {
		Ebean.save(alumno);
	}

	public List<Alumno> disponibles() {
		int dni = 0;
		String sql = "select u.dni from usuario u "
				+ "left join grupo_experto_usuario g on u.dni = g.usuario_dni "
				+ "where g.usuario_dni is null and dtype = 'ALUMNO' ";
		SqlQuery sqlQuery = Ebean.createSqlQuery(sql);
		List<Alumno> alumnosDisponibles = new ArrayList<>();
		List<SqlRow> resultado = sqlQuery.findList();
		if (resultado == null) {
			throw new DAOException("No existen alumnos disponibles");
		} else {
			for (SqlRow row : resultado) {
				dni = row.getInteger("dni");
				alumnosDisponibles.add(obtener(dni));
			}
		}
		System.out.println(alumnosDisponibles);
		return alumnosDisponibles;
	}
}
