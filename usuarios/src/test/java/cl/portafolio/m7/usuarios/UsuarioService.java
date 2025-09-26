package cl.portafolio.m7.usuarios.service;

import cl.portafolio.m7.usuarios.domain.Usuario;
import cl.portafolio.m7.usuarios.domain.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UsuarioService {

  private final UsuarioRepository repo;

  public UsuarioService(UsuarioRepository repo) {
    this.repo = repo;
  }

  public Usuario create(Usuario input) {
    if (input.getEmail() == null || input.getEmail().isBlank())
      throw new IllegalArgumentException("email requerido");
    if (input.getNombre() == null || input.getNombre().isBlank())
      throw new IllegalArgumentException("nombre requerido");
    if (repo.existsByEmail(input.getEmail()))
      throw new DuplicateEmailException(input.getEmail());
    return repo.save(new Usuario(null, input.getEmail(), input.getNombre()));
  }

  @Transactional(readOnly = true)
  public Usuario getById(Long id) {
    return repo.findById(id).orElseThrow(() -> new NotFoundException("Usuario " + id + " no encontrado"));
  }

  @Transactional(readOnly = true)
  public List<Usuario> list() {
    return repo.findAll();
  }

  public Usuario update(Long id, Usuario input) {
    Usuario u = repo.findById(id).orElseThrow(() -> new NotFoundException("Usuario " + id + " no encontrado"));
    String nuevoEmail = input.getEmail();
    String nuevoNombre = input.getNombre();
    if (nuevoEmail == null || nuevoEmail.isBlank())
      throw new IllegalArgumentException("email requerido");
    if (nuevoNombre == null || nuevoNombre.isBlank())
      throw new IllegalArgumentException("nombre requerido");
    if (!u.getEmail().equals(nuevoEmail) && repo.existsByEmail(nuevoEmail))
      throw new DuplicateEmailException(nuevoEmail);
    u.setEmail(nuevoEmail);
    u.setNombre(nuevoNombre);
    return u;
  }

  public void delete(Long id) {
    if (!repo.existsById(id))
      throw new NotFoundException("Usuario " + id + " no encontrado");
    repo.deleteById(id);
  }
}