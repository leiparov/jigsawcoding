package models.entities;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class GrupoExpertoProblema {

	@Id
	private Integer id;
	@ManyToOne(cascade = CascadeType.PERSIST)
	private GrupoExperto grupoExperto;
	@ManyToOne(cascade = CascadeType.PERSIST)
	private Problema problema;

	@ManyToOne(cascade = CascadeType.PERSIST)
	private SesionJigsaw sesionJigsaw;

	@Override
	public String toString() {
		return "(GE" + grupoExperto.getId() + ",P" + problema.getId() + ")";
	}

	/* Getters and Setters */

	public GrupoExperto getGrupoExperto() {
		return grupoExperto;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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
