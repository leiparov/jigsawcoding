package models.entities;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
@DiscriminatorValue("ALUMNO")
public class Alumno extends Usuario {
	
	@ManyToOne(cascade = CascadeType.PERSIST)
	private GrupoExperto grupoExperto;

	public GrupoExperto getGrupoExperto() {
		return grupoExperto;
	}

	public void setGrupoExperto(GrupoExperto grupoExperto) {
		this.grupoExperto = grupoExperto;
	}

	
	
	

}
