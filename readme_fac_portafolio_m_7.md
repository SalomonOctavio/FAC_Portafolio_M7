# FAC_Portafolio_M7 – Microservicio Usuarios

Este repositorio contiene el **microservicio `usuarios`**, desarrollado con Spring Boot, JPA/Hibernate y H2 (modo memoria). Forma parte del portafolio del Módulo 7.

---

## 🚀 Características principales

* CRUD de usuarios (con validaciones de email y nombre).
* Arquitectura en capas: **domain**, **repository**, **service**, **web**.
* Manejo de excepciones centralizado (`GlobalExceptionHandler`).
* Tests unitarios y de integración con cobertura JaCoCo.
* Base de datos **H2 en memoria**, con consola web para inspección.
* Healthcheck y Actuator para monitoreo.

---

## 📂 Estructura del proyecto

```
usuarios/
 ├── src/main/java/cl/portafolio/m7/usuarios/
 │   ├── domain/              # Entidad Usuario
 │   ├── repository/          # UsuarioRepository (Spring Data JPA)
 │   ├── service/             # Lógica de negocio + excepciones
 │   └── web/                 # Controllers REST + ExceptionHandler
 │
 ├── src/main/resources/
 │   ├── application.yml      # Configuración (H2, Actuator, JPA)
 │   └── data.sql             # Datos iniciales (semilla)
 │
 ├── src/test/java/cl/portafolio/m7/usuarios/
 │   ├── UsuarioRepositoryTest
 │   ├── UsuarioServiceTest
 │   ├── UsuarioControllerTest
 │   └── UsuarioExtraTest     # Tests adicionales para cobertura
 │
 └── README.md
```

---

## ⚙️ Configuración

### application.yml

```yaml
server:
  port: 8081

spring:
  datasource:
    url: jdbc:h2:mem:usuariosdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
    driver-class-name: org.h2.Driver

  jpa:
    open-in-view: false
    hibernate:
      ddl-auto: update
    defer-datasource-initialization: true

  sql:
    init:
      mode: always

  h2:
    console:
      enabled: true
      path: /h2

management:
  endpoints:
    web:
      exposure:
        include: health,info
```

### Datos iniciales (`data.sql`)

```sql
INSERT INTO usuarios (email, nombre) VALUES ('admin@test.com', 'Admin') ON DUPLICATE KEY UPDATE email=email;
```

---

## 🧪 Tests & Cobertura

Ejecutar los tests y generar reporte JaCoCo:

```bash
mvn -pl usuarios clean verify
```

El reporte queda en:

```
usuarios/target/site/jacoco/index.html
```

Cobertura actual:

* `cl.portafolio.m7.usuarios.domain` → **100%**
* `cl.portafolio.m7.usuarios.repository` → **100%**
* `cl.portafolio.m7.usuarios.service` → **75%**
* `cl.portafolio.m7.usuarios.web` → **89%**

Total: **~83%**

---

## 🌐 Endpoints principales

### Healthcheck

```bash
curl -s http://localhost:8081/actuator/health
```

👉 Respuesta esperada:

```json
{"status":"UP"}
```

### CRUD Usuarios

* **POST /usuarios** → Crear un usuario
* **GET /usuarios/{id}** → Obtener un usuario
* **GET /usuarios** → Listar todos
* **PUT /usuarios/{id}** → Actualizar un usuario
* **DELETE /usuarios/{id}** → Eliminar un usuario

### Ejemplos cURL

🔹 Crear usuario válido:

```bash
curl -X POST http://localhost:8081/usuarios \
  -H "Content-Type: application/json" \
  -d '{"email":"test@test.com","nombre":"Test"}'
```

🔹 Intentar crear usuario inválido (400):

```bash
curl -X POST http://localhost:8081/usuarios \
  -H "Content-Type: application/json" \
  -d '{"email":"no-email","nombre":""}'
```

🔹 Obtener usuario por ID:

```bash
curl http://localhost:8081/usuarios/1
```

---

## 💻 Consola H2

Acceso a la consola web:

```
http://localhost:8081/h2
```

Configuración de conexión:

* **JDBC URL**: `jdbc:h2:mem:usuariosdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE`
* **User**: `sa`
* **Password**: *(vacío)*

Ejemplo query:

```sql
SELECT * FROM usuarios;
```

---

## 🚀 Arranque manual

Levantar el microservicio:

```bash
mvn -pl usuarios spring-boot:run
```

Acceder a:

* API: [http://localhost:8081/usuarios](http://localhost:8081/usuarios)
* Healthcheck: [http://localhost:8081/actuator/health](http://localhost:8081/actuator/health)
* H2 Console: [http://localhost:8081/h2](http://localhost:8081/h2)

---

## 📌 Pendientes / Mejoras futuras

* Subir cobertura en `service` al 90%+.
* Añadir Swagger/OpenAPI para documentación.
* Integración con otros microservicios del portafolio.

---

## ✅ Estado actual

* Código estable, todos los tests pasan.
* Cobertura JaCoCo generada correctamente.
* Datos semilla cargados en H2.
* Listo para commit final y despliegue local.
