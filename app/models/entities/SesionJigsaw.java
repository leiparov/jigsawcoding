package models.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class SesionJigsaw {

	@Id
	private int id;

	private String tema;
	
	private Date inicioReunionExpertos;
	private Integer duracionReunionExpertos;
	
	private Date inicioReunionJigsaw;
	private Integer duracionReunionJigsaw;

	private Integer totalGruposExpertos;
	
	@ManyToOne(cascade = CascadeType.PERSIST)
	private Docente docente;
	
	@OneToMany(cascade = CascadeType.PERSIST)
	private List<ParGrupoExpertoProblema> pares;
	
	/**/
	
	public String mostrarPares(){
		String cad = "";
		for (ParGrupoExpertoProblema p : pares){
			cad += p + " ";
		}
		return cad;
	}

	/* Getters and Setters */
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getTema() {
		return tema;
	}

	public void setTema(String tema) {
		this.tema = tema;
	}

	public Date getInicioReunionExpertos() {
		return inicioReunionExpertos;
	}

	public void setInicioReunionExpertos(Date inicioReunionExpertos) {
		this.inicioReunionExpertos = inicioReunionExpertos;
	}

	public Integer getDuracionReunionExpertos() {
		return duracionReunionExpertos;
	}

	public void setDuracionReunionExpertos(Integer duracionReunionExpertos) {
		this.duracionReunionExpertos = duracionReunionExpertos;
	}

	public Date getInicioReunionJigsaw() {
		return inicioReunionJigsaw;
	}

	public void setInicioReunionJigsaw(Date inicioReunionJigsaw) {
		this.inicioReunionJigsaw = inicioReunionJigsaw;
	}

	public Integer getDuracionReunionJigsaw() {
		return duracionReunionJigsaw;
	}

	public void setDuracionReunionJigsaw(Integer duracionReunionJigsaw) {
		this.duracionReunionJigsaw = duracionReunionJigsaw;
	}

	public Integer getTotalGruposExpertos() {
		return totalGruposExpertos;
	}

	public void setTotalGruposExpertos(Integer totalGruposExpertos) {
		this.totalGruposExpertos = totalGruposExpertos;
	}

	public Docente getDocente() {
		return docente;
	}

	public void setDocente(Docente docente) {
		this.docente = docente;
	}

	public List<ParGrupoExpertoProblema> getPares() {
		return pares;
	}

	public void setPares(List<ParGrupoExpertoProblema> pares) {
		this.pares = pares;
	}

}
