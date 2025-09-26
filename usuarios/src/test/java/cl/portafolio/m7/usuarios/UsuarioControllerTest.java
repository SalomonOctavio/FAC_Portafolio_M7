package cl.portafolio.m7.usuarios;

import cl.portafolio.m7.usuarios.domain.Usuario;
import cl.portafolio.m7.usuarios.service.NotFoundException;
import cl.portafolio.m7.usuarios.service.UsuarioService;
import cl.portafolio.m7.usuarios.web.GlobalExceptionHandler;
import cl.portafolio.m7.usuarios.web.UsuarioController;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UsuarioController.class)
@Import(GlobalExceptionHandler.class)
class UsuarioControllerTest {

  @Autowired
  private MockMvc mvc;

  @MockBean
  private UsuarioService usuarioService;

  @Test
  void getById_ok() throws Exception {
    Mockito.when(usuarioService.getById(1L))
        .thenReturn(new Usuario(1L, "uno@test.com", "Uno"));

    mvc.perform(get("/usuarios/{id}", 1))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.email").value("uno@test.com"))
        .andExpect(jsonPath("$.nombre").value("Uno"));
  }

  @Test
  void getById_notFound() throws Exception {
    Mockito.when(usuarioService.getById(99L))
        .thenThrow(new NotFoundException("nope"));

    mvc.perform(get("/usuarios/{id}", 99))
        .andExpect(status().isNotFound());
  }

  @Test
  void create_validationError() throws Exception {
    // email inválido y nombre en blanco => 400
    mvc.perform(post("/usuarios")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"email\":\"no-email\",\"nombre\":\"\"}"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void create_ok() throws Exception {
    Mockito.when(usuarioService.create(any(Usuario.class)))
        .thenReturn(new Usuario(10L, "ok@test.com", "OK"));

    mvc.perform(post("/usuarios")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"email\":\"ok@test.com\",\"nombre\":\"OK\"}"))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(10))
        .andExpect(jsonPath("$.email").value("ok@test.com"))
        .andExpect(jsonPath("$.nombre").value("OK"));
  }
}
