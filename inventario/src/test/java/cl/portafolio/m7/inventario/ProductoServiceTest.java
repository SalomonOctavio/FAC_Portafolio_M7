package cl.portafolio.m7.inventario;

import cl.portafolio.m7.inventario.domain.Producto;
import cl.portafolio.m7.inventario.domain.ProductoRepository;
import cl.portafolio.m7.inventario.service.DuplicateSkuException;
import cl.portafolio.m7.inventario.service.NotFoundException;
import cl.portafolio.m7.inventario.service.ProductoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

  @Mock
  ProductoRepository repo;

  @InjectMocks
  ProductoService service;

  @Test
  void create_ok() {
    Producto in = new Producto(null, "SKU-1", "Lapiz", 10);
    when(repo.existsBySku("SKU-1")).thenReturn(false);
    when(repo.save(any(Producto.class))).thenAnswer(i -> {
      Producto p = i.getArgument(0);
      return new Producto(1L, p.getSku(), p.getNombre(), p.getStock());
    });

    Producto out = service.create(in);

    assertNotNull(out.getId());
    assertEquals("SKU-1", out.getSku());
    assertEquals("Lapiz", out.getNombre());
    assertEquals(10, out.getStock());

    ArgumentCaptor<Producto> cap = ArgumentCaptor.forClass(Producto.class);
    verify(repo).save(cap.capture());
    assertNull(cap.getValue().getId()); // se crea con id nulo
  }

  @Test
  void create_duplicateSku() {
    Producto in = new Producto(null, "SKU-1", "Lapiz", 10);
    when(repo.existsBySku("SKU-1")).thenReturn(true);

    assertThrows(DuplicateSkuException.class, () -> service.create(in));
    verify(repo, never()).save(any());
  }

  @Test
  void create_validacionesBasicas() {
    // null -> IAE
    assertThrows(IllegalArgumentException.class, () -> service.create(null));
    // sku vacío
    assertThrows(IllegalArgumentException.class, () -> service.create(new Producto(null, "", "a", 1)));
    // nombre vacío
    assertThrows(IllegalArgumentException.class, () -> service.create(new Producto(null, "S", "", 1)));
    // stock negativo
    assertThrows(IllegalArgumentException.class, () -> service.create(new Producto(null, "S", "a", -1)));
  }

  @Test
  void getById_ok() {
    when(repo.findById(7L)).thenReturn(Optional.of(new Producto(7L, "SKU-7", "Borrador", 3)));

    Producto p = service.getById(7L);

    assertEquals(7L, p.getId());
    assertEquals("SKU-7", p.getSku());
    assertEquals("Borrador", p.getNombre());
    assertEquals(3, p.getStock());
  }

  @Test
  void getById_notFound() {
    when(repo.findById(99L)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> service.getById(99L));
  }

  @Test
  void list_ok() {
    when(repo.findAll()).thenReturn(List.of(
        new Producto(1L, "A", "A1", 1),
        new Producto(2L, "B", "B1", 2)
    ));

    var lst = service.list();
    assertEquals(2, lst.size());
  }

  @Test
  void update_ok_cambiaTodoSinConflicto() {
    when(repo.findById(5L)).thenReturn(Optional.of(new Producto(5L, "SKU-5", "Viejo", 1)));
    when(repo.existsBySku("SKU-NEW")).thenReturn(false);

    Producto in = new Producto(null, "SKU-NEW", "Nuevo", 7);
    Producto out = service.update(5L, in);

    assertEquals(5L, out.getId());
    assertEquals("SKU-NEW", out.getSku());
    assertEquals("Nuevo", out.getNombre());
    assertEquals(7, out.getStock());
  }

  @Test
  void update_conflictoSku() {
    when(repo.findById(5L)).thenReturn(Optional.of(new Producto(5L, "SKU-5", "Viejo", 1)));
    when(repo.existsBySku("SKU-OTRO")).thenReturn(true);

    Producto in = new Producto(null, "SKU-OTRO", "Nuevo", 7);
    assertThrows(DuplicateSkuException.class, () -> service.update(5L, in));
  }

  @Test
  void update_validaciones() {
    // No stubs: con inputs inválidos la validación falla antes de tocar el repo
    assertThrows(IllegalArgumentException.class, () -> service.update(5L, new Producto(null, "", "n", 1)));
    assertThrows(IllegalArgumentException.class, () -> service.update(5L, new Producto(null, "S", "", 1)));
    assertThrows(IllegalArgumentException.class, () -> service.update(5L, new Producto(null, "S", "n", -1)));
  }

  @Test
  void delete_ok() {
    when(repo.existsById(3L)).thenReturn(true);

    service.delete(3L);

    verify(repo).deleteById(3L);
  }

  @Test
  void delete_notFound() {
    when(repo.existsById(3L)).thenReturn(false);

    assertThrows(NotFoundException.class, () -> service.delete(3L));
    verify(repo, never()).deleteById(anyLong());
  }
}
