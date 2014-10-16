package models.entities;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class ParGrupoExpertoProblema {

	@Id
	private Integer parId;
	@ManyToOne(cascade = CascadeType.PERSIST)
	private GrupoExperto grupoExperto;
	@ManyToOne(cascade = CascadeType.PERSIST)
	private Problema problema;
	
	@ManyToOne(cascade = CascadeType.PERSIST)
	private SesionJigsaw sesionJigsaw;

	
	
	@Override
	public String toString() {
		return "(GE"+grupoExperto.getGrupoExpertoId() + ",P"+problema.getProblemaId()+")";
	}

	/*Getters and Setters*/
	public Integer getParId() {
		return parId;
	}

	public void setParId(Integer parId) {
		this.parId = parId;
	}

	public GrupoExperto getGrupoExperto() {
		return grupoExperto;
	}

	public void setGrupoExperto(GrupoExperto grupoExperto) {
		this.grupoExperto = grupoExperto;
	}

	public Problema getProblema() {
		return problema;
	}

	public void setProblema(Problema problema) {
		this.problema = problema;
	}

	public SesionJigsaw getSesionJigsaw() {
		return sesionJigsaw;
	}

	public void setSesionJigsaw(SesionJigsaw sesionJigsaw) {
		this.sesionJigsaw = sesionJigsaw;
	}
	
	
	
}
