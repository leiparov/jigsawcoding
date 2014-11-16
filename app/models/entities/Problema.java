package models.entities;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Problema {

	@Id
	private Integer id;
	private String titulo;
	private String enunciado;
	private Date fechaCreacion;

	@ManyToOne(cascade = CascadeType.PERSIST)
	private Docente docente;

	/* Getters and Setters */

	public String getTitulo() {
		return titulo;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getEnunciado() {
		return enunciado;
	}

	public void setEnunciado(String enunciado) {
		this.enunciado = enunciado;
	}

	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public Docente getDocente() {
		return docente;
	}

	public void setDocente(Docente docente) {
		this.docente = docente;
	}

	@Override
	public String toString() {
		return "Problema ["
				+ (id != null ? "id=" + id + ", " : "")
				+ (titulo != null ? "titulo=" + titulo + ", " : "")
				+ (enunciado != null ? "enunciado=" + enunciado + ", " : "")
				+ (fechaCreacion != null ? "fechaCreacion=" + fechaCreacion
						+ ", " : "")
				+ (docente != null ? "docente=" + docente.getDNI() : "") + "]";
	}

}
