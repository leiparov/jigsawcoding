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
	
	@ManyToOne(cascade = CascadeType.PERSIST)
	private GrupoExperto grupoExperto;

	@ManyToMany(cascade = CascadeType.PERSIST, mappedBy ="alumnos")
    private List<SesionJigsaw> sesionesJigsaw = new ArrayList<>();
	
	/*GEtters and Setters*/
	public GrupoExperto getGrupoExperto() {
		return grupoExperto;
	}

	public void setGrupoExperto(GrupoExperto grupoExperto) {
		this.grupoExperto = grupoExperto;
	}

	public List<SesionJigsaw> getSesionesJigsaw() {
		return sesionesJigsaw;
	}

	public void setSesionesJigsaw(List<SesionJigsaw> sesionesJigsaw) {
		this.sesionesJigsaw = sesionesJigsaw;
	}

	
	
	

}
