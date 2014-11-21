package models.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import models.daos.NotaAlumnoDAO;

@Entity
@DiscriminatorValue("ALUMNO")
public class Alumno extends Usuario {

	@ManyToMany(cascade = CascadeType.PERSIST, mappedBy = "alumnos")
	private List<SesionJigsaw> sesionesJigsaw = new ArrayList<>();

	@OneToMany(cascade = CascadeType.PERSIST)
	private Set<RespuestaAlumno> respuestasAlumno;

	@OneToMany(cascade = CascadeType.PERSIST)
    private List<NotaAlumno> notas;

	/* GEtters and Setters */

	public List<SesionJigsaw> getSesionesJigsaw() {
		return sesionesJigsaw;
	}

	public void setSesionesJigsaw(List<SesionJigsaw> sesionesJigsaw) {
		this.sesionesJigsaw = sesionesJigsaw;
	}

	public Set<RespuestaAlumno> getRespuestasAlumno() {
		return respuestasAlumno;
	}

	public void setRespuestasAlumno(Set<RespuestaAlumno> respuestaAlumno) {
		this.respuestasAlumno = respuestaAlumno;
	}

	public List<NotaAlumno> getNotas() {
		return notas;
	}

	public void setNotas(List<NotaAlumno> notas) {
		this.notas = notas;
	}

	public String getIniciales() {
		return getNombres().toUpperCase().substring(0, 1)
				+ getApellidoPaterno().toUpperCase().substring(0, 1)
				+ getApellidoMaterno().toUpperCase().substring(0, 1);
	}

	@Override
	public String toString() {
		return "Alumno [getDNI()="
				+ getDNI()
				+ ", "
				+ (getNombreCompleto() != null ? "getNombreCompleto()="
						+ getNombreCompleto() : "") + "]";
	}
	
	public String getNotaExamen(Integer examenId){
		NotaAlumnoDAO notaAlumnoDAO = new NotaAlumnoDAO();
		Integer nota = notaAlumnoDAO.buscarNota(examenId, getDNI());
		if(nota != null)
			return nota.toString();
		else 
			return "";
	}

}
