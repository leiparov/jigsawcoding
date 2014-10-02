package models.entities;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Problema {
	
	@Id
	private Long problemaId;
	private String titulo;
	private String enunciado;
	private Date fechaCreacion;
	
	@ManyToOne(cascade = CascadeType.PERSIST)
	private Docente docente;

	/*Getters and Setters*/
	
	

	public String getTitulo() {
		return titulo;
	}

	public Long getProblemaId() {
		return problemaId;
	}

	public void setProblemaId(Long problemaId) {
		this.problemaId = problemaId;
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
	
	
	
	
	
	
}
