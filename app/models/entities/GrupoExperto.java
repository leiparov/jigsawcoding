package models.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Entity
public class GrupoExperto {
	@Id
	private Long grupoExpertoId;
	private String nombre;
	private Integer maximoIntegrantes;
	
	@ManyToMany(cascade = CascadeType.ALL)
	private List<Alumno> integrantes;
	
	@ManyToOne(cascade = CascadeType.PERSIST)
	private Docente docente;

	
	/*Getters and Setters*/
	
	

	public String getNombre() {
		return nombre;
	}

	public Long getGrupoExpertoId() {
		return grupoExpertoId;
	}

	public void setGrupoExpertoId(Long grupoExpertoId) {
		this.grupoExpertoId = grupoExpertoId;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Integer getMaximoIntegrantes() {
		return maximoIntegrantes;
	}

	public void setMaximoIntegrantes(Integer maximoIntegrantes) {
		this.maximoIntegrantes = maximoIntegrantes;
	}

	public List<Alumno> getIntegrantes() {
		return integrantes;
	}

	public void setIntegrantes(List<Alumno> integrantes) {
		this.integrantes = integrantes;
	}

	public Docente getDocente() {
		return docente;
	}

	public void setDocente(Docente docente) {
		this.docente = docente;
	}
	
	
	
}
