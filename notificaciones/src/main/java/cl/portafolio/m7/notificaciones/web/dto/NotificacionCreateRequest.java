package cl.portafolio.m7.notificaciones.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class NotificacionCreateRequest {

    @NotBlank(message = "El asunto es obligatorio")
    private String asunto;

    @NotBlank(message = "El destino es obligatorio")
    @Email(message = "El destino debe ser un email válido")
    private String destino;

    @NotBlank(message = "El mensaje es obligatorio")
    private String mensaje;

    private boolean leida = false;

    public String getAsunto() { return asunto; }
    public void setAsunto(String asunto) { this.asunto = asunto; }

    public String getDestino() { return destino; }
    public void setDestino(String destino) { this.destino = destino; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }

    public boolean isLeida() { return leida; }
    public void setLeida(boolean leida) { this.leida = leida; }
}
