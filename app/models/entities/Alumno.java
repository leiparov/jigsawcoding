package models.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Entity
@DiscriminatorValue("ALUMNO")
public class Alumno extends Usuario {
	@ManyToMany(mappedBy = "alumnos", cascade = CascadeType.PERSIST)
	private List<GrupoExperto> gruposExpertos;
	
	@ManyToOne
	private GrupoExperto grupoActual;

	public List<GrupoExperto> getGruposExpertos() {
		return gruposExpertos;
	}

	public void setGruposExpertos(List<GrupoExperto> gruposExpertos) {
		this.gruposExpertos = gruposExpertos;
	}

	public GrupoExperto getGrupoActual() {
		return grupoActual;
	}

	public void setGrupoActual(GrupoExperto grupoActual) {
		this.grupoActual = grupoActual;
	}
	
	

}
