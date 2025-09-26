package cl.portafolio.m7.usuarios;

import cl.portafolio.m7.usuarios.domain.Usuario;
import cl.portafolio.m7.usuarios.domain.UsuarioRepository;
import cl.portafolio.m7.usuarios.service.DuplicateEmailException;
import cl.portafolio.m7.usuarios.service.NotFoundException;
import cl.portafolio.m7.usuarios.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsuarioServiceBranchTest {

  UsuarioRepository repo;
  UsuarioService service;

  @BeforeEach
  void setup() {
    repo = mock(UsuarioRepository.class);
    service = new UsuarioService(repo);
  }

  @Test
  void delete_notFound_throws() {
    when(repo.existsById(99L)).thenReturn(false);
    assertThrows(NotFoundException.class, () -> service.delete(99L));
    verify(repo, never()).deleteById(anyLong());
  }

  @Test
  void update_changingToExistingEmail_throwsDuplicate() {
    // existente con email A
    Usuario existente = new Usuario(1L, "a@test.com", "A");
    when(repo.findById(1L)).thenReturn(Optional.of(existente));
    // queremos cambiar a email B, pero ya existe otro con B
    when(repo.existsByEmail("b@test.com")).thenReturn(true);

    Usuario input = new Usuario(null, "b@test.com", "A2");
    assertThrows(DuplicateEmailException.class, () -> service.update(1L, input));
    // no debería persistir cambios
    assertEquals("a@test.com", existente.getEmail());
    assertEquals("A", existente.getNombre());
  }

  @Test
  void list_ok() {
    when(repo.findAll()).thenReturn(List.of(
        new Usuario(1L, "u1@test.com", "U1"),
        new Usuario(2L, "u2@test.com", "U2")
    ));
    var lista = service.list();
    assertEquals(2, lista.size());
  }
}