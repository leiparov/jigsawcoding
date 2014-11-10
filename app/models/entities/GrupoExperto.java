package models.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity
@DiscriminatorValue("GRUPOEXPERTO")
public class GrupoExperto extends Grupo {

	@OneToMany(cascade = CascadeType.ALL)
	private List<Alumno> alumnos;
	
	@OneToMany
	private List<GrupoExpertoProblema> gruposExpertosProblema;

	/*Getters and Setters*/
	public List<Alumno> getAlumnos() {
		return alumnos;
	}

	public void setAlumnos(List<Alumno> alumnos) {
		this.alumnos = alumnos;
	}
	public List<GrupoExpertoProblema> getGruposExpertosProblema() {
		return gruposExpertosProblema;
	}

	public void setGruposExpertosProblema(
			List<GrupoExpertoProblema> gruposExpertosProblema) {
		this.gruposExpertosProblema = gruposExpertosProblema;
	}
	
	public String getChatId (){
		return "GE" + getId();
	}
	
	public String integrantes() {
		String cad = "";
		for (Alumno alumno : alumnos) {
			cad += "* " + alumno.getNombres() + "\n";
		}
		return cad;
	}

}
