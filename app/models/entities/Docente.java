package models.entities;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("DOCENTE")
public class Docente extends Usuario {   

    
    
	@Override
	public String toString() {
		return "Docente [getDNI()=" + getDNI() + ", getNombreCompleto()="
				+ getNombreCompleto() + "]";
	}
}
