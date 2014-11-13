package models.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

@Entity
@DiscriminatorValue("ALUMNO")
public class Alumno extends Usuario {

	@ManyToMany(cascade = CascadeType.PERSIST, mappedBy = "alumnos")
	private List<SesionJigsaw> sesionesJigsaw = new ArrayList<>();

	@OneToMany(cascade = CascadeType.PERSIST)
	private Set<RespuestasAlumno> respuestasAlumno;

	@OneToMany(cascade = CascadeType.PERSIST)
    private List<NotaAlumno> notas;

	/* GEtters and Setters */

	public List<SesionJigsaw> getSesionesJigsaw() {
		return sesionesJigsaw;
	}

	public void setSesionesJigsaw(List<SesionJigsaw> sesionesJigsaw) {
		this.sesionesJigsaw = sesionesJigsaw;
	}

	public Set<RespuestasAlumno> getRespuestasAlumno() {
		return respuestasAlumno;
	}

	public void setRespuestasAlumno(Set<RespuestasAlumno> respuestasAlumno) {
		this.respuestasAlumno = respuestasAlumno;
	}

	public List<NotaAlumno> getNotas() {
		return notas;
	}

	public void setNotas(List<NotaAlumno> notas) {
		this.notas = notas;
	}

	public String getIniciales() {
		return getNombres().toUpperCase().substring(0, 1)
				+ getApellidoPaterno().toUpperCase().substring(0, 1)
				+ getApellidoMaterno().toUpperCase().substring(0, 1);
	}

}
