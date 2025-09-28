package cl.portafolio.m7.inventario.service;

import cl.portafolio.m7.inventario.domain.Producto;
import cl.portafolio.m7.inventario.domain.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProductoService {

  private final ProductoRepository repo;

  public ProductoService(ProductoRepository repo) {
    this.repo = repo;
  }

  public Producto create(Producto in) {
    validar(in);
    if (repo.existsBySku(in.getSku())) {
      throw new DuplicateSkuException(in.getSku());
    }
    // aseguramos que el id vaya null al crear
    return repo.save(new Producto(null, in.getSku(), in.getNombre(), in.getStock()));
  }

  @Transactional(readOnly = true)
  public Producto getById(Long id) {
    return repo.findById(id).orElseThrow(() ->
        new NotFoundException("Producto " + id + " no encontrado"));
  }

  @Transactional(readOnly = true)
  public List<Producto> list() {
    return repo.findAll();
  }

  public Producto update(Long id, Producto in) {
    // Primero validar inputs para evitar stubs innecesarios en tests
    validar(in);

    Producto actual = repo.findById(id).orElseThrow(() ->
        new NotFoundException("Producto " + id + " no encontrado"));

    String nuevoSku = in.getSku();
    if (!actual.getSku().equals(nuevoSku) && repo.existsBySku(nuevoSku)) {
      throw new DuplicateSkuException(nuevoSku);
    }

    actual.setSku(nuevoSku);
    actual.setNombre(in.getNombre());
    actual.setStock(in.getStock());
    return actual;
  }

  public void delete(Long id) {
    if (!repo.existsById(id)) {
      throw new NotFoundException("Producto " + id + " no encontrado");
    }
    repo.deleteById(id);
  }

  private void validar(Producto in) {
    if (in == null) {
      throw new IllegalArgumentException("producto requerido");
    }
    if (in.getSku() == null || in.getSku().isBlank()) {
      throw new IllegalArgumentException("sku requerido");
    }
    if (in.getNombre() == null || in.getNombre().isBlank()) {
      throw new IllegalArgumentException("nombre requerido");
    }
    if (in.getStock() < 0) {
      throw new IllegalArgumentException("stock no puede ser negativo");
    }
  }
}