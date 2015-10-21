package models.daos;

import models.entities.NotaAlumno;
import play.db.ebean.Model.Finder;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Expr;

public class NotaAlumnoDAO {

	private static Finder<Integer, NotaAlumno> find = new Finder<Integer, NotaAlumno>(
			Integer.class, NotaAlumno.class);

	public NotaAlumno obtener(Integer integer) {
		return EbeanUtils.findOrException(NotaAlumno.class, integer);
	}

	public void guardar(NotaAlumno e) {
		Ebean.save(e);
	}

	public void actualizar(NotaAlumno r) {
		Ebean.update(r);
	}

	public Integer buscarNota(Integer examenid, String dni) {
		NotaAlumno na = find.where().conjunction()
				.add(Expr.eq("alumno_dni", dni))
				.add(Expr.eq("examen_id", examenid)).findUnique();
		if(na != null)
			return na.getNota();
		else
			return null;
	}

}
