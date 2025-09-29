package cl.portafolio.m7.notificaciones.application;

import cl.portafolio.m7.notificaciones.application.dto.NotificacionDto;
import cl.portafolio.m7.notificaciones.domain.Notificacion;
import cl.portafolio.m7.notificaciones.domain.NotificacionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificacionServiceEdgeCasesTest {

    @Mock
    NotificacionRepository repository;

    @InjectMocks
    NotificacionService service;

    @Test
    void eliminar_llamaDeleteById() {
        service.eliminar(99L);
        verify(repository).deleteById(99L);
    }

    @Test
    void marcarLeida_noHaceNada_siNoExiste() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        service.marcarLeida(1L, true);
        verify(repository, never()).save(any());
    }

    @Test
    void marcarLeida_actualiza_siExiste() {
        Notificacion n = new Notificacion();
        n.setId(2L);
        n.setLeida(false);
        when(repository.findById(2L)).thenReturn(Optional.of(n));

        service.marcarLeida(2L, true);

        assertTrue(n.isLeida());
        verify(repository).save(n);
    }

    @Test
    void actualizar_notFound_devuelveEmpty() {
        when(repository.findById(50L)).thenReturn(Optional.empty());
        var out = service.actualizar(50L, new NotificacionDto(50L, "A", "b@test.com", "M", true));
        assertTrue(out.isEmpty());
        verify(repository, never()).save(any());
    }

    @Test
    void crear_normalizaLeidaNullAFalse() {
        Notificacion guardada = new Notificacion();
        guardada.setId(123L);
        guardada.setAsunto("X");
        guardada.setDestino("x@test.com");
        guardada.setMensaje("M");
        guardada.setLeida(false);

        // capturamos lo que se guarda
        ArgumentCaptor<Notificacion> captor = ArgumentCaptor.forClass(Notificacion.class);
        when(repository.save(any(Notificacion.class))).thenReturn(guardada);

        var creado = service.crear(new NotificacionDto(null, "X", "x@test.com", "M", null));

        verify(repository).save(captor.capture());
        assertFalse(captor.getValue().isLeida(), "leida null debe normalizarse a false");
        assertEquals(123L, creado.id());
    }
}
