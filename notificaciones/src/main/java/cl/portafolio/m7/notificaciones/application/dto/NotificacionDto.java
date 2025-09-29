package cl.portafolio.m7.notificaciones.application.dto;

public record NotificacionDto(
        Long id,
        String asunto,
        String destino,
        String mensaje,
        Boolean leida
) {}
