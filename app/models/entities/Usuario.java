package models.entities;

import javax.persistence.*;

import play.data.validation.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorValue("USUARIO")
public class Usuario {

   @Id
   private int DNI;
   @Constraints.Required
   private String email;
   @SuppressWarnings("unused")
   private String password;
   private boolean inhabilitado = false;
   private String nombre;
   private String apellido;
   private Sexo sexo;

   public int getDNI() {
      return DNI;
   }
   public void setDNI(int dNI) {
      DNI = dNI;
   }
   public String getEmail() {
      return email;
   }
   public void setEmail(String email) {
      this.email = email;
   }
   public boolean isInhabilitado() {
      return inhabilitado;
   }
   public void setInhabilitado(boolean inhabilitado) {
      this.inhabilitado = inhabilitado;
   }
   public String getNombre() {
      return nombre;
   }
   public void setNombre(String nombre) {
      this.nombre = nombre;
   }
   public String getApellido() {
      return apellido;
   }
   public void setApellido(String apellido) {
      this.apellido = apellido;
   }
   public Sexo getSexo() {
      return sexo;
   }
   public void setSexo(Sexo sexo) {
      this.sexo = sexo;
   }
   public String getNombreCompleto() {
      return nombre + " " + apellido;
   }
   public String getPassword() {
      return password;
   }
   public void setPassword(String psw) {
      this.password = psw;
   }
}
