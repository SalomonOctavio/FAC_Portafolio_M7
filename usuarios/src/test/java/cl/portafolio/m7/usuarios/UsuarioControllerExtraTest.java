package cl.portafolio.m7.usuarios;

import cl.portafolio.m7.usuarios.service.UsuarioService;
import cl.portafolio.m7.usuarios.web.UsuarioController;
import cl.portafolio.m7.usuarios.domain.Usuario;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UsuarioController.class)
class UsuarioExtraTest {

  @Autowired
  private MockMvc mvc;

  @MockBean
  private UsuarioService usuarioService;

  @Test
  void create_ok() throws Exception {
    Usuario creado = new Usuario(10L, "ok@test.com", "OK");
    Mockito.when(usuarioService.create(Mockito.any(Usuario.class))).thenReturn(creado);

    mvc.perform(post("/usuarios")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"email\":\"ok@test.com\",\"nombre\":\"OK\"}"))
      .andExpect(status().isCreated())
      .andExpect(jsonPath("$.id").value(10))
      .andExpect(jsonPath("$.email").value("ok@test.com"))
      .andExpect(jsonPath("$.nombre").value("OK"));
  }

  @Test
  void create_duplicateEmail() throws Exception {
    Mockito.when(usuarioService.create(Mockito.any(Usuario.class)))
      .thenThrow(new cl.portafolio.m7.usuarios.service.DuplicateEmailException("Duplicado"));

    mvc.perform(post("/usuarios")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"email\":\"admin@test.com\",\"nombre\":\"Admin\"}"))
      .andExpect(status().isConflict());
  }
}
