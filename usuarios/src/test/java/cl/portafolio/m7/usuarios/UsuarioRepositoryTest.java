package cl.portafolio.m7.usuarios;

import cl.portafolio.m7.usuarios.domain.Usuario;
import cl.portafolio.m7.usuarios.domain.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UsuarioRepositoryTest {

  @Autowired
  UsuarioRepository repo;

  @Test
  void save_and_findByEmail_ok() {
    Usuario u = new Usuario(null, "ana@acme.com", "Ana");
    repo.save(u);

    var found = repo.findByEmail("ana@acme.com");
    assertThat(found).isPresent();
    assertThat(found.get().getNombre()).isEqualTo("Ana");
    assertThat(found.get().getId()).isNotNull();
  }
}