package cl.portafolio.m7.notificaciones.application;

import cl.portafolio.m7.notificaciones.application.dto.NotificacionDto;
import cl.portafolio.m7.notificaciones.domain.Notificacion;
import cl.portafolio.m7.notificaciones.domain.NotificacionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificacionServiceTest {

    @Mock
    NotificacionRepository repository;

    @InjectMocks
    NotificacionService service;

    @Test
    void listar_mapeaOk() {
        Notificacion e = new Notificacion();
        e.setId(1L);
        e.setAsunto("T");
        e.setMensaje("M");
        e.setLeida(true); // getter boolean: isLeida()

        when(repository.findAll()).thenReturn(List.of(e));

        var out = service.listar();
        assertEquals(1, out.size());
        assertEquals(1L, out.get(0).id());
        assertEquals(true, out.get(0).leida());
    }

    @Test
    void obtener_ok() {
        Notificacion e = new Notificacion();
        e.setId(2L); e.setAsunto("A"); e.setMensaje("B"); e.setLeida(false);

        when(repository.findById(2L)).thenReturn(Optional.of(e));

        var out = service.obtener(2L);
        assertTrue(out.isPresent());
        assertEquals(2L, out.get().id());
        assertFalse(out.get().leida());
    }

    @Test
    void crear_ok() {
        Notificacion e = new Notificacion();
        e.setId(10L); e.setAsunto("N"); e.setMensaje("X"); e.setLeida(false);

        when(repository.save(any(Notificacion.class))).thenReturn(e);

        var dto = new NotificacionDto(null, "N", "user@test.com", "X", false);
        var creado = service.crear(dto);

        assertEquals(10L, creado.id());
        assertEquals("N", creado.asunto());
    }

    @Test
    void actualizar_ok() {
        Notificacion e = new Notificacion();
        e.setId(3L); e.setAsunto("T"); e.setMensaje("M"); e.setLeida(false);

        when(repository.findById(3L)).thenReturn(Optional.of(e));
        when(repository.save(any(Notificacion.class))).thenAnswer(inv -> inv.getArgument(0));

        var dto = new NotificacionDto(null, "T2", "updated@test.com", "M2", true);
        var out = service.actualizar(3L, dto);

        assertTrue(out.isPresent());
        assertEquals(true, out.get().leida());
        assertEquals("T2", out.get().asunto());
    }
}
