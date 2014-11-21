package models.daos;

import models.entities.Examen;
import models.entities.RespuestaAlumno;
import play.db.ebean.Model.Finder;

import com.avaje.ebean.Ebean;

public class RespuestasAlumnoDAO {

	private static Finder<Integer, RespuestaAlumno> find = new Finder<Integer, RespuestaAlumno>(
			Integer.class, RespuestaAlumno.class);
	public RespuestaAlumno obtener(Integer integer) {
		return EbeanUtils.findOrException(RespuestaAlumno.class, integer);
	}
	
	public void guardar(Examen e) {
		Ebean.save(e);
	}

	public void actualizar(RespuestaAlumno r) {
		Ebean.update(r);		
	}

}
