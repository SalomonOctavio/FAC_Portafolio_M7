package cl.portafolio.m7.inventario;

import cl.portafolio.m7.inventario.domain.Producto;
import cl.portafolio.m7.inventario.service.DuplicateSkuException;
import cl.portafolio.m7.inventario.service.NotFoundException;
import cl.portafolio.m7.inventario.service.ProductoService;
import cl.portafolio.m7.inventario.web.ProductoController;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ProductoController.class)
class ProductoControllerExtraTest {

  @Autowired
  MockMvc mvc;

  @MockBean
  ProductoService service;

  @Test
  void post_producto_invalido_400() throws Exception {
    // nombre en blanco, sku vacío, stock negativo -> debería fallar validación (400)
    String body = """
      {"nombre":"","sku":"","stock":-1}
      """;
    mvc.perform(post("/productos")
        .contentType(MediaType.APPLICATION_JSON)
        .content(body))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.mensaje", not(emptyOrNullString())));
  }

  @Test
  void post_sku_duplicado_409() throws Exception {
    Mockito.when(service.create(any(Producto.class)))
      .thenThrow(new DuplicateSkuException("SKU-DUP"));

    String body = """
      {"nombre":"P1","sku":"SKU-DUP","stock":5}
      """;
    mvc.perform(post("/productos")
        .contentType(MediaType.APPLICATION_JSON)
        .content(body))
      .andExpect(status().isConflict())
      .andExpect(jsonPath("$.mensaje", containsString("SKU")));
  }

  @Test
  void put_not_found_404() throws Exception {
    Mockito.when(service.update(eq(99L), any(Producto.class)))
      .thenThrow(new NotFoundException("no existe"));

    String body = """
      {"nombre":"Nuevo","sku":"SKU-NEW","stock":3}
      """;
    mvc.perform(put("/productos/99")
        .contentType(MediaType.APPLICATION_JSON)
        .content(body))
      .andExpect(status().isNotFound())
      .andExpect(jsonPath("$.mensaje", containsString("no existe")));
  }

  @Test
  void put_invalido_400() throws Exception {
    // Violamos validación del DTO (nombre/suku vacíos o stock negativo)
    String body = """
      {"nombre":"","sku":"","stock":-5}
      """;
    mvc.perform(put("/productos/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(body))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.mensaje", not(emptyOrNullString())));
  }

  @Test
  void delete_not_found_404() throws Exception {
    Mockito.doThrow(new NotFoundException("no existe")).when(service).delete(77L);

    mvc.perform(delete("/productos/77"))
      .andExpect(status().isNotFound())
      .andExpect(jsonPath("$.mensaje", containsString("no existe")));
  }

  @Test
  void get_list_vacio_200() throws Exception {
    Mockito.when(service.list()).thenReturn(Collections.emptyList());

    mvc.perform(get("/productos"))
      .andExpect(status().isOk())
      .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$", hasSize(0)));
  }

  @Test
  void get_by_id_not_found_404() throws Exception {
    Mockito.when(service.getById(123L)).thenThrow(new NotFoundException("x"));

    mvc.perform(get("/productos/123"))
      .andExpect(status().isNotFound())
      .andExpect(jsonPath("$.mensaje", not(emptyOrNullString())));
  }
}