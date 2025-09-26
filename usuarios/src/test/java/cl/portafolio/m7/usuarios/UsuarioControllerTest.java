package cl.portafolio.m7.usuarios;

import cl.portafolio.m7.usuarios.domain.Usuario;
import cl.portafolio.m7.usuarios.service.NotFoundException;
import cl.portafolio.m7.usuarios.service.UsuarioService;
import cl.portafolio.m7.usuarios.web.UsuarioController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UsuarioController.class)
class UsuarioControllerTest {

  @Autowired MockMvc mvc;
  @MockBean UsuarioService service;

  @Test
  void getById_ok() throws Exception {
    when(service.getById(1L)).thenReturn(new Usuario(1L,"ana@acme.com","Ana"));
    mvc.perform(get("/usuarios/1").accept(MediaType.APPLICATION_JSON))
       .andExpect(status().isOk())
       .andExpect(jsonPath("$.email").value("ana@acme.com"));
  }

  @Test
  void getById_notFound() throws Exception {
    when(service.getById(99L)).thenThrow(new NotFoundException("Usuario 99 no encontrado"));
    mvc.perform(get("/usuarios/99"))
       .andExpect(status().isNotFound());
  }

  @Test
  void create_validationError() throws Exception {
    // email inválido y nombre vacío -> 400
    String body = """
      {"email":"no-email","nombre":""}
      """;
    mvc.perform(post("/usuarios").contentType(MediaType.APPLICATION_JSON).content(body))
       .andExpect(status().isBadRequest());
  }

  @Test
  void create_ok() throws Exception {
    when(service.create(any())).thenReturn(new Usuario(2L,"bob@acme.com","Bob"));
    String body = """
      {"email":"bob@acme.com","nombre":"Bob"}
      """;
    mvc.perform(post("/usuarios").contentType(MediaType.APPLICATION_JSON).content(body))
       .andExpect(status().isCreated())
       .andExpect(jsonPath("$.id").value(2))
       .andExpect(jsonPath("$.email").value("bob@acme.com"));
  }
}
