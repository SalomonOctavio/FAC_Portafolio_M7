package cl.portafolio.m7.usuarios;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UsuariosExceptionsTest {

    @Test
    void duplicateEmailException_message() {
        DuplicateEmailException ex = new DuplicateEmailException("correo@test.com");
        assertTrue(ex.getMessage().contains("correo@test.com"));
    }

    @Test
    void notFoundException_message() {
        NotFoundException ex = new NotFoundException("Usuario no existe");
        assertEquals("Usuario no existe", ex.getMessage());
    }
}
