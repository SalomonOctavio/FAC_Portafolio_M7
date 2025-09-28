package cl.portafolio.m7.inventario.web;

import cl.portafolio.m7.inventario.domain.Producto;
import cl.portafolio.m7.inventario.service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;


import java.net.URI;
import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/productos")
public class ProductoController {

  private final ProductoService service;

  public ProductoController(ProductoService service) { this.service = service; }

  @PostMapping
  public ResponseEntity<Producto> create(@Valid @RequestBody Producto in) {
    var creado = service.create(in);
    return ResponseEntity.created(URI.create("/productos/" + creado.getId())).body(creado);
  }

  @GetMapping("/{id}")
  public Producto getById(@PathVariable("id") Long id) { return service.getById(id); }

  @GetMapping
  public List<Producto> list() { return service.list(); }

  @PutMapping("/{id}")
  public Producto update(@PathVariable("id") Long id, @Valid @RequestBody Producto in) {
    return service.update(id, in);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
    service.delete(id);
    return ResponseEntity.noContent().build();
  }
}