package models.entities;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import play.data.format.Formats;

@Entity
public class SesionJigsaw {

	@Id
	private int id;
	
	@ManyToOne(cascade = CascadeType.PERSIST)
	private Curso curso;
	
	@ManyToOne(cascade = CascadeType.PERSIST)
	private Tema tema;

	@Formats.DateTime(pattern = "yyyy-MM-dd")
	private Date inicioReunionExpertos;
	private int duracionReunionExpertos;
	@Formats.DateTime(pattern = "yyyy-MM-dd")
	private Date inicioReunionJigsaw;
	private int duracionReunionJigsaw;

	private int totalGruposExpertos;

	
	/*Getters and Setters*/
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Curso getCurso() {
		return curso;
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
	}

	public Tema getTema() {
		return tema;
	}

	public void setTema(Tema tema) {
		this.tema = tema;
	}

	public Date getInicioReunionExpertos() {
		return inicioReunionExpertos;
	}

	public void setInicioReunionExpertos(Date inicioReunionExpertos) {
		this.inicioReunionExpertos = inicioReunionExpertos;
	}

	public int getDuracionReunionExpertos() {
		return duracionReunionExpertos;
	}

	public void setDuracionReunionExpertos(int duracionReunionExpertos) {
		this.duracionReunionExpertos = duracionReunionExpertos;
	}

	public Date getInicioReunionJigsaw() {
		return inicioReunionJigsaw;
	}

	public void setInicioReunionJigsaw(Date inicioReunionJigsaw) {
		this.inicioReunionJigsaw = inicioReunionJigsaw;
	}

	public int getDuracionReunionJigsaw() {
		return duracionReunionJigsaw;
	}

	public void setDuracionReunionJigsaw(int duracionReunionJigsaw) {
		this.duracionReunionJigsaw = duracionReunionJigsaw;
	}

	public int getTotalGruposExpertos() {
		return totalGruposExpertos;
	}

	public void setTotalGruposExpertos(int totalGruposExpertos) {
		this.totalGruposExpertos = totalGruposExpertos;
	}

}
