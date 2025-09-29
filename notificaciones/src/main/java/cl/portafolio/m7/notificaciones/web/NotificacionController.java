package cl.portafolio.m7.notificaciones.web;

import cl.portafolio.m7.notificaciones.application.NotificacionService;
import cl.portafolio.m7.notificaciones.application.dto.NotificacionDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/notificaciones")
public class NotificacionController {

    private final NotificacionService notificacionService;

    public NotificacionController(NotificacionService notificacionService) {
        this.notificacionService = notificacionService;
    }

    @GetMapping
    public List<NotificacionDto> listar() {
        return notificacionService.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificacionDto> obtener(@PathVariable Long id) {
        return notificacionService.obtener(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<NotificacionDto> crear(@RequestBody NotificacionDto dto) {
        NotificacionDto creado = notificacionService.crear(dto);
        return ResponseEntity.created(URI.create("/notificaciones/" + creado.id())).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<NotificacionDto> actualizar(@PathVariable Long id, @RequestBody NotificacionDto dto) {
        return notificacionService.actualizar(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/leida")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void marcarLeida(@PathVariable Long id, @RequestParam boolean leida) {
        notificacionService.marcarLeida(id, leida);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        notificacionService.eliminar(id);
    }
}
