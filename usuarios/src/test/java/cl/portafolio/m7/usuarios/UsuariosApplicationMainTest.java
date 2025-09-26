package cl.portafolio.m7.usuarios;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class UsuariosApplicationMainTest {

  @Test
  void main_runs_without_webserver() {
    // Evita levantar el servidor embebido en el test
    assertDoesNotThrow(() ->
        UsuariosApplication.main(new String[]{"--spring.main.web-application-type=none"}));
  }
}
