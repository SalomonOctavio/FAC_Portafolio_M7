package cl.portafolio.m7.notificaciones.application;

import cl.portafolio.m7.notificaciones.application.dto.NotificacionDto;
import cl.portafolio.m7.notificaciones.domain.Notificacion;
import cl.portafolio.m7.notificaciones.domain.NotificacionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NotificacionService {

    private final NotificacionRepository repository;

    public NotificacionService(NotificacionRepository repository) {
        this.repository = repository;
    }

    public List<NotificacionDto> listar() {
        return repository.findAll().stream().map(this::toDto).toList();
    }

    public Optional<NotificacionDto> obtener(Long id) {
        return repository.findById(id).map(this::toDto);
    }

    public NotificacionDto crear(NotificacionDto dto) {
        Notificacion entity = toEntity(dto);
        entity.setId(null);
        Notificacion saved = repository.save(entity);
        return toDto(saved);
    }

    public Optional<NotificacionDto> actualizar(Long id, NotificacionDto dto) {
        return repository.findById(id).map(existing -> {
            existing.setAsunto(dto.asunto());
            existing.setDestino(dto.destino());
            existing.setMensaje(dto.mensaje());
            existing.setLeida(Boolean.TRUE.equals(dto.leida()));
            return toDto(repository.save(existing));
        });
    }

    public void marcarLeida(Long id, boolean leida) {
        repository.findById(id).ifPresent(n -> {
            n.setLeida(leida);
            repository.save(n);
        });
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }

    // ---------- mapping ----------
    private NotificacionDto toDto(Notificacion n) {
        return new NotificacionDto(
                n.getId(),
                n.getAsunto(),
                n.getDestino(),
                n.getMensaje(),
                n.isLeida()
        );
    }

    private Notificacion toEntity(NotificacionDto dto) {
        Notificacion n = new Notificacion();
        n.setId(dto.id());
        n.setAsunto(dto.asunto());
        n.setDestino(dto.destino());
        n.setMensaje(dto.mensaje());
        n.setLeida(Boolean.TRUE.equals(dto.leida()));
        return n;
    }
}
