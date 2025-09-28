package cl.portafolio.m7.inventario.service;

public class DuplicateSkuException extends RuntimeException {
  public DuplicateSkuException(String sku) { super("El SKU ya existe: " + sku); }
}
