package models.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class ProblemaExamen {

    @Id
    private long genId;
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Problema problema;
    // Hacer similar con Examen si se requiere relacion bidireccional
    private int puntajeFavor;
    private int puntajeContra;
	public long getGenId() {
		return genId;
	}
	public void setGenId(long genId) {
		this.genId = genId;
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

    

   

    

    

}
