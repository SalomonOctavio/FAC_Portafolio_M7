package cl.portafolio.m7.notificaciones.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class NotificacionRepositoryTest {

    @Autowired
    NotificacionRepository repo;

    @Test
    void contexto_carga_y_repo_inyectado() {
        assertThat(repo).isNotNull();
    }

    @Test
    void dataSql_cargada_hay_registros() {
        long count = repo.count(); // se apoya en data.sql
        assertThat(count).isGreaterThan(0);
    }

    @Test
    void primerElemento_tiene_id() {
        List<Notificacion> todos = repo.findAll();
        assertThat(todos).isNotEmpty();
        assertThat(todos.get(0).getId()).isNotNull();
    }
}
