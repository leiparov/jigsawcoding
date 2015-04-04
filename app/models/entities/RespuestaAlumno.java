package models.entities;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class RespuestaAlumno {
	@Id
	private int id;
	@ManyToOne(cascade = CascadeType.PERSIST)
	private ProblemaExamen problemaExamen;
	@ManyToOne(cascade = CascadeType.PERSIST)
	private Alumno alumno;

	private String ideoneLink;
	private int puntajeObtenido;

	/* GEtters and Setters */
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ProblemaExamen getProblemaExamen() {
		return problemaExamen;
	}

	public void setProblemaExamen(ProblemaExamen problemaExamen) {
		this.problemaExamen = problemaExamen;
	}

	public Alumno getAlumno() {
		return alumno;
	}

	public void setAlumno(Alumno alumno) {
		this.alumno = alumno;
	}

	public String getIdeoneLink() {
		return ideoneLink;
	}

	public void setIdeoneLink(String ideoneLink) {
		this.ideoneLink = ideoneLink;
	}

	public int getPuntajeObtenido() {
		return puntajeObtenido;
	}

	public void setPuntajeObtenido(int puntajeObtenido) {
		this.puntajeObtenido = puntajeObtenido;
	}

}
