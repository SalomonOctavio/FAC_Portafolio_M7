package cl.portafolio.m7.usuarios.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "usuarios", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class Usuario {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank(message = "El email es obligatorio")
  @Email(message = "El email no tiene formato válido")
  @Column(nullable = false, unique = true)
  private String email;

  @NotBlank(message = "El nombre es obligatorio")
  @Column(nullable = false)
  private String nombre;

  /** Constructor vacío requerido por JPA */
  protected Usuario() {}

  /** Constructor de conveniencia */
  public Usuario(Long id, String email, String nombre) {
    this.id = id;
    this.email = email;
    this.nombre = nombre;
  }

  // Getters / Setters clásicos (sin Lombok)
  public Long getId() { return id; }
  public String getEmail() { return email; }
  public String getNombre() { return nombre; }

  public void setEmail(String email) { this.email = email; }
  public void setNombre(String nombre) { this.nombre = nombre; }
}