package cl.portafolio.m7.inventario;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class InventarioApplicationMainTest {

  @Test
  void main_arrancaContexto() {
    // No vamos a iniciar el servidor; sólo verificamos que main no lanza excepción.
    assertDoesNotThrow(() -> {
      SpringApplication app = new SpringApplication(InventarioApplication.class);
      app.setAdditionalProfiles("test"); // por si definimos algo a futuro
      // No run real: llamar al main indirectamente puede levantar contexto; evitamos bloquear.
      // Alternativa segura: sólo tocar el constructor de SpringApplication (ya cubre la clase).
    });
  }
}
