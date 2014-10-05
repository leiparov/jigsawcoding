package models.entities;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("ALUMNO")
public class Alumno extends Usuario {  
    

}
