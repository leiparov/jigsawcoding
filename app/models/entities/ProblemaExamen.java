package models.entities;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class ProblemaExamen {

	@Id
	private int id;
	@ManyToOne(cascade = CascadeType.PERSIST)
	private Problema problema;
	@ManyToOne(cascade = CascadeType.PERSIST)
	private Examen examen;
	
	private int puntajeFavor;
	private int puntajeContra;
	

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Problema getProblema() {
		return problema;
	}
	public void setProblema(Problema problema) {
		this.problema = problema;
	}
	public int getPuntajeFavor() {
		return puntajeFavor;
	}
	public void setPuntajeFavor(int puntajeFavor) {
		this.puntajeFavor = puntajeFavor;
	}
	public int getPuntajeContra() {
		return puntajeContra;
	}
	public void setPuntajeContra(int puntajeContra) {
		this.puntajeContra = puntajeContra;
	}
	public Examen getExamen() {
		return examen;
	}
	public void setExamen(Examen examen) {
		this.examen = examen;
	}

}
