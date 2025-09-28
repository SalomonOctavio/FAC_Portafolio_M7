package cl.portafolio.m7.inventario;

import cl.portafolio.m7.inventario.domain.Producto;
import cl.portafolio.m7.inventario.domain.ProductoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import jakarta.validation.ConstraintViolationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
class ProductoRepositoryTest {

    @Autowired
    private ProductoRepository productoRepository;

    @Test
    void guardaProducto_ok() {
        Producto p = new Producto();
        p.setNombre("Producto 1");
        p.setSku("SKU-123");
        p.setStock(10);

        Producto saved = productoRepository.save(p);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getSku()).isEqualTo("SKU-123");
    }

    @Test
    void sku_noPuedeSerNulo() {
        Producto p = new Producto();
        p.setNombre("Producto sin sku");
        p.setSku(null);
        p.setStock(5);

        assertThrows(ConstraintViolationException.class, () -> {
            productoRepository.saveAndFlush(p);
        });
    }

    @Test
    void stock_no_puede_ser_negativo() {
        Producto p = new Producto();
        p.setNombre("Producto con stock negativo");
        p.setSku("SKU-NEG");
        p.setStock(-5);

        assertThrows(ConstraintViolationException.class, () -> {
            productoRepository.saveAndFlush(p);
        });
    }
}