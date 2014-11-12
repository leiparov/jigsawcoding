package models.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Entity
@DiscriminatorValue("ALUMNO")
public class Alumno extends Usuario {

	@ManyToMany(cascade = CascadeType.PERSIST)
	private List<GrupoExperto> gruposExpertos;

	@ManyToMany(cascade = CascadeType.PERSIST, mappedBy = "alumnos")
	private List<SesionJigsaw> sesionesJigsaw = new ArrayList<>();

	@ManyToOne
	private Grupo grupo;

	/* GEtters and Setters */

	public List<SesionJigsaw> getSesionesJigsaw() {
		return sesionesJigsaw;
	}

	public List<GrupoExperto> getGruposExpertos() {
		return gruposExpertos;
	}

	public void setGruposExpertos(List<GrupoExperto> gruposExpertos) {
		this.gruposExpertos = gruposExpertos;
	}

	public void setSesionesJigsaw(List<SesionJigsaw> sesionesJigsaw) {
		this.sesionesJigsaw = sesionesJigsaw;
	}

	public Grupo getGrupo() {
		return grupo;
	}

	public void setGrupo(Grupo grupo) {
		this.grupo = grupo;
	}

	public String getIniciales() {
		return getNombres().toUpperCase().substring(0, 1)
				+ getApellidoPaterno().toUpperCase().substring(0, 1)
				+ getApellidoMaterno().toUpperCase().substring(0, 1);
	}

}
