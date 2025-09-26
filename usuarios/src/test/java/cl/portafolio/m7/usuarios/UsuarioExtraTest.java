package cl.portafolio.m7.usuarios;

import cl.portafolio.m7.usuarios.domain.Usuario;
import cl.portafolio.m7.usuarios.service.DuplicateEmailException;
import cl.portafolio.m7.usuarios.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UsuarioExtraTest {

  @Autowired
  private UsuarioService service;

  @Test
  void create_ok() {
    Usuario u = new Usuario(null, "nuevo@test.com", "Nuevo");
    Usuario saved = service.create(u);
    assertNotNull(saved.getId());
    assertEquals("nuevo@test.com", saved.getEmail());
  }

  @Test
  void create_duplicateEmail() {
    Usuario u1 = new Usuario(null, "dup@test.com", "A");
    Usuario saved = service.create(u1);
    assertNotNull(saved.getId());

    Usuario u2 = new Usuario(null, "dup@test.com", "B");
    assertThrows(DuplicateEmailException.class, () -> service.create(u2));
  }
}
