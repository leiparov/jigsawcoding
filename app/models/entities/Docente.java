package models.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity
@DiscriminatorValue("DOCENTE")
public class Docente extends Usuario {  
	
	@OneToMany(cascade = CascadeType.PERSIST)
	private List<Problema> problemas;
	
	@OneToMany(cascade = CascadeType.PERSIST)
	private List<GrupoExperto> gruposExpertos;
    
	@Override
	public String toString() {
		return "Docente [getDNI()=" + getDNI() + ", getNombreCompleto()="
				+ getNombreCompleto() + "]";
	}

	public List<Problema> getProblemas() {
		return problemas;
	}

	public void setProblemas(List<Problema> problemas) {
		this.problemas = problemas;
	}

	public List<GrupoExperto> getGruposExpertos() {
		return gruposExpertos;
	}

	public void setGruposExpertos(List<GrupoExperto> gruposExpertos) {
		this.gruposExpertos = gruposExpertos;
	}
	
	
	
}
