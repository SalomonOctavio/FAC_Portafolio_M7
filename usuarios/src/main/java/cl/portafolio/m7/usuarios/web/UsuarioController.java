package cl.portafolio.m7.usuarios.web;

import cl.portafolio.m7.usuarios.domain.Usuario;
import cl.portafolio.m7.usuarios.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

  private final UsuarioService service;

  public UsuarioController(UsuarioService service) {
    this.service = service;
  }

  @PostMapping
  public ResponseEntity<Usuario> create(@Valid @RequestBody Usuario request) {
    Usuario creado = service.create(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(creado);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Usuario> getById(@PathVariable("id") Long id) {
    return ResponseEntity.ok(service.getById(id));
  }

  @GetMapping
  public ResponseEntity<List<Usuario>> list() {
    return ResponseEntity.ok(service.list());
  }

  @PutMapping("/{id}")
  public ResponseEntity<Usuario> update(@PathVariable("id") Long id, @Valid @RequestBody Usuario request) {
    return ResponseEntity.ok(service.update(id, request));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
    service.delete(id);
    return ResponseEntity.noContent().build();
  }
}