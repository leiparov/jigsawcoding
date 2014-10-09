package models.entities;

import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;

@Entity
@DiscriminatorValue("ALUMNO")
public class Alumno extends Usuario {
	@ManyToMany(mappedBy = "alumnos")
	private List<GrupoExperto> gruposExpertos;
}
