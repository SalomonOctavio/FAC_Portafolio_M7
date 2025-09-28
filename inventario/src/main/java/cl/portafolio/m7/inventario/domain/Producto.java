package cl.portafolio.m7.inventario.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "productos", uniqueConstraints = @UniqueConstraint(columnNames = "sku"))
public class Producto {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  @NotBlank(message = "El SKU es obligatorio")
  private String sku;

  @Column(nullable = false)
  @NotBlank(message = "El nombre es obligatorio")
  private String nombre;

  @NotNull @Min(value = 0, message = "El stock no puede ser negativo")
  private Integer stock;

  public Producto() {}

  public Producto(Long id, String sku, String nombre, Integer stock) {
    this.id = id; this.sku = sku; this.nombre = nombre; this.stock = stock;
  }

  public Long getId() { return id; }
  public String getSku() { return sku; }
  public String getNombre() { return nombre; }
  public Integer getStock() { return stock; }

  public void setSku(String sku) { this.sku = sku; }
  public void setNombre(String nombre) { this.nombre = nombre; }
  public void setStock(Integer stock) { this.stock = stock; }
}