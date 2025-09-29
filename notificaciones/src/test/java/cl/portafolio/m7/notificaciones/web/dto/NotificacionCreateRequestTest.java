package cl.portafolio.m7.notificaciones.web.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class NotificacionCreateRequestTest {

    private static Validator validator;

    @BeforeAll
    static void setupValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void gettersAndSetters_workCorrectly() {
        NotificacionCreateRequest req = new NotificacionCreateRequest();
        req.setAsunto("Prueba");
        req.setDestino("user@test.com");
        req.setMensaje("Hola mundo");
        req.setLeida(true);

        assertEquals("Prueba", req.getAsunto());
        assertEquals("user@test.com", req.getDestino());
        assertEquals("Hola mundo", req.getMensaje());
        assertTrue(req.isLeida());
    }

    @Test
    void validRequest_hasNoViolations() {
        NotificacionCreateRequest req = new NotificacionCreateRequest();
        req.setAsunto("Asunto válido");
        req.setDestino("valid@test.com");
        req.setMensaje("Mensaje válido");
        req.setLeida(false);
        Set<jakarta.validation.ConstraintViolation<NotificacionCreateRequest>> violations = validator.validate(req);
        assertTrue(violations.isEmpty(), "No debería haber errores de validación");
        assertTrue(violations.isEmpty(), "No debería haber errores de validación");
    }

    @Test
    void invalidRequest_detectsViolations() {
        NotificacionCreateRequest req = new NotificacionCreateRequest();
        req.setAsunto(""); // vacío
        req.setDestino("no-es-email"); // inválido
        req.setMensaje(""); // vacío
        Set<jakarta.validation.ConstraintViolation<NotificacionCreateRequest>> violations = validator.validate(req);
        assertFalse(violations.isEmpty(), "Debe haber errores de validación");
        assertFalse(violations.isEmpty(), "Debe haber errores de validación");
    }
}
