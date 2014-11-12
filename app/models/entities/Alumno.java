package models.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;

@Entity
@DiscriminatorValue("ALUMNO")
public class Alumno extends Usuario {

	@ManyToMany(cascade = CascadeType.PERSIST, mappedBy = "alumnos")
	private List<SesionJigsaw> sesionesJigsaw = new ArrayList<>();

	

	/* GEtters and Setters */

	public List<SesionJigsaw> getSesionesJigsaw() {
		return sesionesJigsaw;
	}

	public void setSesionesJigsaw(List<SesionJigsaw> sesionesJigsaw) {
		this.sesionesJigsaw = sesionesJigsaw;
	}



	public String getIniciales() {
		return getNombres().toUpperCase().substring(0, 1)
				+ getApellidoPaterno().toUpperCase().substring(0, 1)
				+ getApellidoMaterno().toUpperCase().substring(0, 1);
	}

}
