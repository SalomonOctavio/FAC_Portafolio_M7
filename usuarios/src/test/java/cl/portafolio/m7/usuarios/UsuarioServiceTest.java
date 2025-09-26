package cl.portafolio.m7.usuarios;

import cl.portafolio.m7.usuarios.domain.Usuario;
import cl.portafolio.m7.usuarios.domain.UsuarioRepository;
import cl.portafolio.m7.usuarios.service.DuplicateEmailException;
import cl.portafolio.m7.usuarios.service.NotFoundException;
import cl.portafolio.m7.usuarios.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsuarioServiceTest {

  UsuarioRepository repo;
  UsuarioService service;

  @BeforeEach
  void setup() {
    repo = mock(UsuarioRepository.class);
    service = new UsuarioService(repo);
  }

  @Test
  void create_duplicateEmail_throws() {
    when(repo.existsByEmail("ana@acme.com")).thenReturn(true);
    assertThrows(DuplicateEmailException.class,
        () -> service.create(new Usuario(null, "ana@acme.com", "Ana")));
  }

  @Test
  void getById_notFound_throws() {
    when(repo.findById(42L)).thenReturn(Optional.empty());
    assertThrows(NotFoundException.class, () -> service.getById(42L));
  }

  @Test
  void update_ok() {
    Usuario existing = new Usuario(1L, "ana@acme.com", "Ana");
    when(repo.findById(1L)).thenReturn(Optional.of(existing));
    when(repo.existsByEmail("ana@acme.com")).thenReturn(false);

    Usuario updated = service.update(1L, new Usuario(null, "ana@acme.com", "Ana María"));
    assertEquals("Ana María", updated.getNombre());
  }
}