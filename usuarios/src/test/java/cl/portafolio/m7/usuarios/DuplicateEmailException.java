package cl.portafolio.m7.usuarios;

public class DuplicateEmailException extends RuntimeException {
  public DuplicateEmailException(String email) { super("El email ya existe: " + email); }
}
