package cl.portafolio.m7.pagos.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pagos")
public class Pago {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 50)
  private String ordenId;

  @Column(nullable = false, precision = 12, scale = 2)
  private BigDecimal monto;

  @Column(nullable = false, length = 20)
  private String estado; // PENDIENTE, APROBADO, RECHAZADO

  @Column(nullable = false)
  private LocalDateTime creadoEn = LocalDateTime.now();

  public Pago() {}
  public Pago(String ordenId, BigDecimal monto, String estado) {
    this.ordenId = ordenId; this.monto = monto; this.estado = estado;
  }

  public Long getId() { return id; }
  public String getOrdenId() { return ordenId; }
  public void setOrdenId(String ordenId) { this.ordenId = ordenId; }
  public BigDecimal getMonto() { return monto; }
  public void setMonto(BigDecimal monto) { this.monto = monto; }
  public String getEstado() { return estado; }
  public void setEstado(String estado) { this.estado = estado; }
  public LocalDateTime getCreadoEn() { return creadoEn; }
  public void setCreadoEn(LocalDateTime creadoEn) { this.creadoEn = creadoEn; }
}
