package models.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Entity
@DiscriminatorValue("GRUPOEXPERTO")
public class GrupoExperto extends Grupo {

	@ManyToMany(cascade = CascadeType.ALL)
	private List<Alumno> alumnos;
	
	@ManyToOne(cascade = CascadeType.PERSIST)
	private SesionJigsaw sesionJigsaw;
	
	@ManyToOne(cascade = CascadeType.PERSIST)
	private Problema problema;

	/*Getters and Setters*/
	public List<Alumno> getAlumnos() {
		return alumnos;
	}

	public void setAlumnos(List<Alumno> alumnos) {
		this.alumnos = alumnos;
	}
	
	public String integrantes() {
		String cad = "";
		for (Alumno alumno : alumnos) {
			cad += "* " + alumno.getNombres() + "\n";
		}
		return cad;
	}

	public SesionJigsaw getSesionJigsaw() {
		return sesionJigsaw;
	}

	public void setSesionJigsaw(SesionJigsaw sesionJigsaw) {
		this.sesionJigsaw = sesionJigsaw;
	}

	public Problema getProblema() {
		return problema;
	}

	public void setProblema(Problema problema) {
		this.problema = problema;
	}
	
	public String mostrarGrupoExpertoProblema (){
		if(problema != null){
			return ("(" + this.getNombre() + " - " + getProblema().getTitulo() + ")");
		}else{
			return ("(" + this.getNombre() + " - <asignar problema>)");
		}
		
	}

	@Override
	public String toString() {
		return "GrupoExperto [alumnos=" + alumnos + ", sesionJigsaw="
				+ sesionJigsaw + ", problema=" + problema + ", getNombre()="
				+ getNombre() + ", getId()=" + getId()
				+ ", getMaximoAlumnos()=" + getMaximoAlumnos()
				+ ", getDescripcion()=" + getDescripcion() + ", getChatId()="
				+ getChatId() + ", getClass()=" + getClass() + ", hashCode()="
				+ hashCode() + ", toString()=" + super.toString() + "]";
	}

}
