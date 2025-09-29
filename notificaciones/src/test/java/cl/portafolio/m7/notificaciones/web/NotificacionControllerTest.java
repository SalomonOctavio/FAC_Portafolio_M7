package cl.portafolio.m7.notificaciones.web;

import cl.portafolio.m7.notificaciones.application.NotificacionService;
import cl.portafolio.m7.notificaciones.application.dto.NotificacionDto;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = NotificacionController.class)
// Si tienes Spring Security en el classpath, esto evita fallos 401/403 en los tests:
@AutoConfigureMockMvc(addFilters = false)
class NotificacionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificacionService notificacionService;

    @Test
    void listar_ok() throws Exception {
        var n1 = new NotificacionDto(1L, "Asunto 1", "user1@test.com", "Mensaje 1", false);
        var n2 = new NotificacionDto(2L, "Asunto 2", "user2@test.com", "Mensaje 2", true);

        when(notificacionService.listar()).thenReturn(List.of(n1, n2));

        mockMvc.perform(get("/notificaciones"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].asunto", is("Asunto 1")))
                .andExpect(jsonPath("$[0].destino", is("user1@test.com")))
                .andExpect(jsonPath("$[0].mensaje", is("Mensaje 1")))
                .andExpect(jsonPath("$[0].leida", is(false)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].leida", is(true)));
    }

    @Test
    void obtener_found() throws Exception {
        var n = new NotificacionDto(10L, "Asunto X", "dest@test.com", "Contenido", true);
        when(notificacionService.obtener(10L)).thenReturn(Optional.of(n));

        mockMvc.perform(get("/notificaciones/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(10)))
                .andExpect(jsonPath("$.asunto", is("Asunto X")))
                .andExpect(jsonPath("$.destino", is("dest@test.com")))
                .andExpect(jsonPath("$.mensaje", is("Contenido")))
                .andExpect(jsonPath("$.leida", is(true)));
    }

    @Test
    void obtener_notFound() throws Exception {
        when(notificacionService.obtener(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/notificaciones/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void crear_created() throws Exception {
        var creado = new NotificacionDto(5L, "Nuevo", "n@test.com", "Hola", false);
        when(notificacionService.crear(any())).thenReturn(creado);

        String body = """
            {"asunto":"Nuevo","destino":"n@test.com","mensaje":"Hola","leida":false}
            """;

        mockMvc.perform(post("/notificaciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/notificaciones/5"))
                .andExpect(jsonPath("$.id", is(5)))
                .andExpect(jsonPath("$.asunto", is("Nuevo")));
    }

    @Test
    void actualizar_ok() throws Exception {
        var actualizado = new NotificacionDto(3L, "Upd", "upd@test.com", "Mensaje upd", true);
        when(notificacionService.actualizar(eq(3L), any())).thenReturn(Optional.of(actualizado));

        String body = """
            {"id":3,"asunto":"Upd","destino":"upd@test.com","mensaje":"Mensaje upd","leida":true}
            """;

        mockMvc.perform(put("/notificaciones/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.leida", is(true)));
    }

    @Test
    void actualizar_notFound() throws Exception {
        when(notificacionService.actualizar(eq(77L), any())).thenReturn(Optional.empty());

        String body = """
            {"id":77,"asunto":"X","destino":"x@test.com","mensaje":"M","leida":false}
            """;

        mockMvc.perform(put("/notificaciones/77")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isNotFound());
    }

    @Test
    void marcarLeida_noContent() throws Exception {
        mockMvc.perform(patch("/notificaciones/4/leida?leida=true"))
                .andExpect(status().isNoContent());

        Mockito.verify(notificacionService).marcarLeida(4L, true);
    }

    @Test
    void eliminar_noContent() throws Exception {
        mockMvc.perform(delete("/notificaciones/8"))
                .andExpect(status().isNoContent());

        Mockito.verify(notificacionService).eliminar(8L);
    }
}