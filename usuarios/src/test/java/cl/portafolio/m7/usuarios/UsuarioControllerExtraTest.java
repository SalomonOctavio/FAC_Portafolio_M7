package cl.portafolio.m7.usuarios;

import cl.portafolio.m7.usuarios.domain.Usuario;
import cl.portafolio.m7.usuarios.service.DuplicateEmailException;
import cl.portafolio.m7.usuarios.service.UsuarioService;
import cl.portafolio.m7.usuarios.web.UsuarioController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UsuarioController.class)
class UsuarioControllerExtraTest {

  @Autowired MockMvc mvc;
  @Autowired ObjectMapper om;

  @MockBean UsuarioService service;

  @Test
  void list_ok() throws Exception {
    List<Usuario> data = List.of(
        new Usuario(1L, "a@test.com", "A"),
        new Usuario(2L, "b@test.com", "B")
    );
    Mockito.when(service.list()).thenReturn(data);

    mvc.perform(get("/usuarios"))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].email", is("a@test.com")));
  }

  @Test
  void update_ok() throws Exception {
    Usuario req = new Usuario(null, "a@test.com", "A2");
    Usuario resp = new Usuario(1L, "a@test.com", "A2");
    Mockito.when(service.update(eq(1L), any(Usuario.class))).thenReturn(resp);

    mvc.perform(put("/usuarios/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(om.writeValueAsString(req)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.nombre", is("A2")));
  }

  @Test
  void delete_noContent() throws Exception {
    mvc.perform(delete("/usuarios/1"))
        .andExpect(status().isNoContent());
    Mockito.verify(service).delete(1L);
  }

  @Test
  void create_duplicateEmail_conflict() throws Exception {
    Usuario req = new Usuario(null, "dup@test.com", "Dup");
    Mockito.when(service.create(any(Usuario.class)))
        .thenThrow(new DuplicateEmailException("dup@test.com"));

    mvc.perform(post("/usuarios")
            .contentType(MediaType.APPLICATION_JSON)
            .content(om.writeValueAsString(req)))
        .andExpect(status().isConflict());
  }
}