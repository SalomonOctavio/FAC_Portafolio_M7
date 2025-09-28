package cl.portafolio.m7.inventario;

import cl.portafolio.m7.inventario.domain.Producto;
import cl.portafolio.m7.inventario.service.NotFoundException;
import cl.portafolio.m7.inventario.service.ProductoService;
import cl.portafolio.m7.inventario.web.ProductoController;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ProductoController.class)
class ProductoControllerTest {

  @Autowired
  private MockMvc mvc;

  @MockBean
  private ProductoService service;

  @Test
  void create_ok() throws Exception {
    var creado = new Producto(1L, "SKU-1", "Lapiz", 10);
    Mockito.when(service.create(any(Producto.class))).thenReturn(creado);

    mvc.perform(post("/productos")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"sku\":\"SKU-1\",\"nombre\":\"Lapiz\",\"stock\":10}"))
      .andExpect(status().isCreated())
      .andExpect(header().string("Location", "/productos/1"))
      .andExpect(jsonPath("$.id").value(1))
      .andExpect(jsonPath("$.sku").value("SKU-1"))
      .andExpect(jsonPath("$.nombre").value("Lapiz"))
      .andExpect(jsonPath("$.stock").value(10));
  }

  @Test
  void create_validationError() throws Exception {
    mvc.perform(post("/productos")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"sku\":\"\",\"nombre\":\"\",\"stock\":-5}"))
      .andExpect(status().isBadRequest());
  }

  @Test
  void getById_ok() throws Exception {
    Mockito.when(service.getById(5L))
      .thenReturn(new Producto(5L, "SKU-5", "Borrador", 3));

    mvc.perform(get("/productos/5"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id").value(5))
      .andExpect(jsonPath("$.sku").value("SKU-5"))
      .andExpect(jsonPath("$.nombre").value("Borrador"))
      .andExpect(jsonPath("$.stock").value(3));
  }

  @Test
  void getById_notFound() throws Exception {
    Mockito.when(service.getById(99L)).thenThrow(new NotFoundException("no"));

    mvc.perform(get("/productos/99"))
      .andExpect(status().isNotFound());
  }
}


