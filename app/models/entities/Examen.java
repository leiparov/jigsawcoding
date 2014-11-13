package models.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

@Entity
public class Examen {
	@Id
	private Integer id;
	private String titulo;
	private Date tiempoApertura;
	private Date tiempoClausura;
	private Date tiempoCreacion;
	private Integer duracion;

	@OneToOne(cascade = CascadeType.PERSIST)
	private SesionJigsaw sesionJigsaw;

	@ManyToOne
	private Docente docente;

	@OneToMany(cascade = CascadeType.ALL)
	private List<ProblemaExamen> problemas;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public Date getTiempoApertura() {
		return tiempoApertura;
	}

	public void setTiempoApertura(Date tiempoApertura) {
		this.tiempoApertura = tiempoApertura;
	}

	public Date getTiempoClausura() {
		return tiempoClausura;
	}

	public void setTiempoClausura(Date tiempoClausura) {
		this.tiempoClausura = tiempoClausura;
	}

	public Date getTiempoCreacion() {
		return tiempoCreacion;
	}

	public void setTiempoCreacion(Date tiempoCreacion) {
		this.tiempoCreacion = tiempoCreacion;
	}

	public Integer getDuracion() {
		return duracion;
	}

	public void setDuracion(Integer duracion) {
		this.duracion = duracion;
	}

	public Docente getDocente() {
		return docente;
	}

	public void setDocente(Docente docente) {
		this.docente = docente;
	}

	public List<ProblemaExamen> getProblemas() {
		return problemas;
	}

	public void setProblemas(List<ProblemaExamen> problemas) {
		this.problemas = problemas;
	}
	@Transient
	public int getDuracionEnSegundos() {
		return this.duracion * 60;
	}

	public SesionJigsaw getSesionJigsaw() {
		return sesionJigsaw;
	}

	public void setSesionJigsaw(SesionJigsaw sesionJigsaw) {
		this.sesionJigsaw = sesionJigsaw;
	}

	@Override
	public String toString() {
		return "Examen ["
				+ (id != null ? "id=" + id + ", " : "")
				+ (titulo != null ? "titulo=" + titulo + ", " : "")
				+ (tiempoApertura != null ? "tiempoApertura=" + tiempoApertura
						+ ", " : "")
				+ (tiempoClausura != null ? "tiempoClausura=" + tiempoClausura
						+ ", " : "")
				+ (tiempoCreacion != null ? "tiempoCreacion=" + tiempoCreacion
						+ ", " : "")
				+ (duracion != null ? "duracion=" + duracion : "") + "]";
	}

}
