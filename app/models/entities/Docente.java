package models.entities;

import java.util.ArrayList;
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
	private List<SesionJigsaw> sesionesJigsaw;
    
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

	public List<SesionJigsaw> getSesionesJigsaw() {
		return sesionesJigsaw;
	}

	public void setSesionesJigsaw(List<SesionJigsaw> sesionesJigsaw) {
		this.sesionesJigsaw = sesionesJigsaw;
	}
	
	public List<Grupo> getGrupos(){
		List<Grupo> grupos = new ArrayList<>();
		for (SesionJigsaw s : getSesionesJigsaw()){
			grupos.addAll(s.getGruposExpertos());
			grupos.addAll(s.getGruposJigsaw());
		}
		return grupos;
	}
	
}
