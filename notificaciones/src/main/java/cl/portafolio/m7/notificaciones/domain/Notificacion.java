package cl.portafolio.m7.notificaciones.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "notificaciones")
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String asunto;

    @Column(nullable = false)
    private String destino;

    @Column(nullable = false, length = 2000)
    private String mensaje;

    @Column(nullable = false)
    private boolean leida = false;

    // --- getters y setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getAsunto() { return asunto; }
    public void setAsunto(String asunto) { this.asunto = asunto; }

    public String getDestino() { return destino; }
    public void setDestino(String destino) { this.destino = destino; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }

    public boolean isLeida() { return leida; }
    public void setLeida(boolean leida) { this.leida = leida; }
}