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
	private Integer id;
	private String nombre;
	private String descripcion;
	private Integer maximoAlumnos;

	@ManyToMany(cascade = CascadeType.PERSIST)
	private List<Alumno> alumnos;

	@ManyToOne(cascade = CascadeType.PERSIST)
	private Docente docente;

	/* Getters and Setters */

	public String getNombre() {
		return nombre.toUpperCase();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre.toUpperCase();
	}

	public Integer getMaximoAlumnos() {
		return maximoAlumnos;
	}

	public void setMaximoAlumnos(Integer maximoAlumnos) {
		this.maximoAlumnos = maximoAlumnos;
	}

	public List<Alumno> getAlumnos() {
		return alumnos;
	}

	public void setAlumnos(List<Alumno> alumnos) {
		this.alumnos = alumnos;
	}

	public Docente getDocente() {
		return docente;
	}

	public void setDocente(Docente docente) {
		this.docente = docente;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	public String integrantes (){
		String cad = "";
		for (Alumno alumno: alumnos){
			cad += "* " + alumno.getNombres() + "\n";			
		}
		return cad;
	}

}
