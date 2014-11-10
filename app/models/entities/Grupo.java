package models.entities;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo")
@DiscriminatorValue("GRUPO")
public class Grupo {
	@Id
	private Integer id;
	private String nombre;
	private String descripcion;
	private Integer maximoAlumnos;

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

	

}
