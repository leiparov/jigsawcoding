package models.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

@Entity
public class SesionJigsaw {

	@Id
	private Integer id;

	private String tema;

	private Date inicioReunionExpertos;
	private Integer duracionReunionExpertos;

	private Date inicioReunionJigsaw;
	private Integer duracionReunionJigsaw;

	/*Evaluacion*/
	private Date tiempoAperturaExamen;
	private Date tiempoClausuraExamen;
	private Integer duracionExamen;
	
	private Integer totalGruposExpertos;

	private EtapaSesionJigsaw etapa;

	@ManyToOne(cascade = CascadeType.PERSIST)
	private Docente docente;

	@OneToMany(cascade = CascadeType.ALL)
	private List<GrupoExperto> gruposExpertos;
	
	@OneToMany(cascade = CascadeType.ALL)
	private List<GrupoJigsaw> gruposJigsaw;
	
	@ManyToOne
	private Examen examen;
	
	@ManyToMany( cascade = CascadeType.PERSIST)
	private List<Alumno> alumnos;;

	/**/

	public List<String> mostrarPares() {
		List<String> cad = new ArrayList<>();
		for (GrupoExperto p : gruposExpertos) {
			cad.add(p.mostrarGrupoExpertoProblema());
		}
		return cad;
	}

	/* Getters and Setters */
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
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

	

	public String getNombre() {
		return "SesionJigsaw_" + this.id;
	}

	public EtapaSesionJigsaw getEtapa() {
		return etapa;
	}

	public void setEtapa(EtapaSesionJigsaw etapa) {
		this.etapa = etapa;
	}
	
	
	public Examen getExamen() {
		return examen;
	}

	public void setExamen(Examen examen) {
		this.examen = examen;
	}

	public List<Alumno> getAlumnos() {
		return alumnos;
	}

	public void setAlumnos(List<Alumno> alumnos) {
		this.alumnos = alumnos;
	}

	@Transient
    public int getDuracionReunionExpertosEnSegundos(){
        return this.duracionReunionExpertos * 60;
    }
	@Transient
    public int getDuracionReunionJigsawEnSegundos(){
        return this.duracionReunionJigsaw * 60;
    }
	@Transient
    public int getDuracionExamenEnSegundos(){
        return this.duracionExamen * 60;
    }

	public List<GrupoExperto> getGruposExpertos() {
		return gruposExpertos;
	}

	public void setGruposExpertos(List<GrupoExperto> gruposExpertos) {
		this.gruposExpertos = gruposExpertos;
	}

	public Date getTiempoAperturaExamen() {
		return tiempoAperturaExamen;
	}

	public void setTiempoAperturaExamen(Date tiempoAperturaExamen) {
		this.tiempoAperturaExamen = tiempoAperturaExamen;
	}

	public Date getTiempoClausuraExamen() {
		return tiempoClausuraExamen;
	}

	public void setTiempoClausuraExamen(Date tiempoClausuraExamen) {
		this.tiempoClausuraExamen = tiempoClausuraExamen;
	}

	public Integer getDuracionExamen() {
		return duracionExamen;
	}

	public void setDuracionExamen(Integer duracionExamen) {
		this.duracionExamen = duracionExamen;
	}

	public List<GrupoJigsaw> getGruposJigsaw() {
		return gruposJigsaw;
	}

	public void setGruposJigsaw(List<GrupoJigsaw> gruposJigsaw) {
		this.gruposJigsaw = gruposJigsaw;
	}

	

}
