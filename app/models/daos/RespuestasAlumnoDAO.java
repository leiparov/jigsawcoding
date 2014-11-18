package models.daos;

import models.entities.Examen;
import models.entities.RespuestasAlumno;
import play.db.ebean.Model.Finder;

import com.avaje.ebean.Ebean;

public class RespuestasAlumnoDAO {

	private static Finder<Integer, RespuestasAlumno> find = new Finder<Integer, RespuestasAlumno>(
			Integer.class, RespuestasAlumno.class);
	public RespuestasAlumno obtener(Integer integer) {
		return EbeanUtils.findOrException(RespuestasAlumno.class, integer);
	}
	
	public void guardar(Examen e) {
		Ebean.save(e);
	}

	public void actualizar(RespuestasAlumno r) {
		Ebean.update(r);		
	}

}
