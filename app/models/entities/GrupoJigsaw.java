package models.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Entity
@DiscriminatorValue("GRUPOJIGSAW")
public class GrupoJigsaw extends Grupo {
	@ManyToMany(cascade = CascadeType.ALL)
	private List<Alumno> alumnos;
	
	@ManyToOne(cascade = CascadeType.PERSIST)
	private SesionJigsaw sesionJigsaw;
	
	@ManyToMany(cascade = CascadeType.ALL)
	private List<Problema> problemas;

	public List<Alumno> getAlumnos() {
		return alumnos;
	}

	public void setAlumnos(List<Alumno> alumnos) {
		this.alumnos = alumnos;
	}

	public SesionJigsaw getSesionJigsaw() {
		return sesionJigsaw;
	}

	public void setSesionJigsaw(SesionJigsaw sesionJigsaw) {
		this.sesionJigsaw = sesionJigsaw;
	}

	public List<Problema> getProblemas() {
		return problemas;
	}

	public void setProblemas(List<Problema> problemas) {
		this.problemas = problemas;
	}
	
	
}
