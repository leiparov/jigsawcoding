package models.daos;

import java.util.List;

import models.entities.Alumno;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Expr;
import com.avaje.ebean.Query;

public class AlumnoDAO {

	public static UsuarioDAO usuarioDAO = new UsuarioDAO();

	public Alumno obtener(int dni) {
		return usuarioDAO.obtener(dni, Alumno.class);
	}

	public List<Alumno> buscarAlumno(String criterio, int max) {
		criterio = criterio.toUpperCase();
		Query<Alumno> query = Ebean.find(Alumno.class).where().disjunction()
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

	public List<Alumno> listaDeAlumnos() {
		return usuarioDAO.obtener(Alumno.class);
	}
}
