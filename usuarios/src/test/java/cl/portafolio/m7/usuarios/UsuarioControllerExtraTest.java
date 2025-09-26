package cl.portafolio.m7.usuarios;

import cl.portafolio.m7.usuarios.domain.Usuario;
import cl.portafolio.m7.usuarios.service.DuplicateEmailException;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UsuarioController.class)
@Import(GlobalExceptionHandler.class)
class UsuarioControllerExtraTest {

  @Autowired
  private MockMvc mvc;

  @MockBean
  private UsuarioService usuarioService;

  @Test
  void list_ok() throws Exception {
    Mockito.when(usuarioService.list())
        .thenReturn(List.of(
            new Usuario(1L, "a@test.com", "A"),
            new Usuario(2L, "b@test.com", "B")
        ));

    mvc.perform(get("/usuarios"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(1))
        .andExpect(jsonPath("$[1].email").value("b@test.com"));
  }

  @Test
  void update_ok() throws Exception {
    Mockito.when(usuarioService.update(eq(5L), any(Usuario.class)))
        .thenReturn(new Usuario(5L, "nuevo@test.com", "Nuevo"));

    mvc.perform(put("/usuarios/{id}", 5)
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"email\":\"nuevo@test.com\",\"nombre\":\"Nuevo\"}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(5))
        .andExpect(jsonPath("$.email").value("nuevo@test.com"))
        .andExpect(jsonPath("$.nombre").value("Nuevo"));
  }

  @Test
  void update_notFound() throws Exception {
    Mockito.when(usuarioService.update(eq(404L), any(Usuario.class)))
        .thenThrow(new NotFoundException("no existe"));

    mvc.perform(put("/usuarios/{id}", 404)
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"email\":\"x@test.com\",\"nombre\":\"X\"}"))
        .andExpect(status().isNotFound());
  }

  @Test
  void delete_ok() throws Exception {
    mvc.perform(delete("/usuarios/{id}", 7))
        .andExpect(status().isNoContent());
    Mockito.verify(usuarioService).delete(7L);
  }

  @Test
  void delete_notFound() throws Exception {
    Mockito.doThrow(new NotFoundException("no existe"))
        .when(usuarioService).delete(123L);

    mvc.perform(delete("/usuarios/{id}", 123))
        .andExpect(status().isNotFound());
  }

  @Test
  void create_duplicateEmail() throws Exception {
    Mockito.when(usuarioService.create(any(Usuario.class)))
        .thenThrow(new DuplicateEmailException("admin@test.com"));

    mvc.perform(post("/usuarios")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"email\":\"admin@test.com\",\"nombre\":\"Admin\"}"))
        .andExpect(status().isConflict());
  }
}
