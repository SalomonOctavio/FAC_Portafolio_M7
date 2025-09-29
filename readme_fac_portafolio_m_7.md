# FAC_Portafolio_M7 – Microservicios Usuarios + Inventario + Notificaciones + UI

Este repositorio contiene el **portafolio del Módulo 7**, con tres microservicios desarrollados en Spring Boot y una SPA (Single Page Application) hecha con React + Vite.

---

## 🚀 Microservicios

### 📌 `usuarios/`

* CRUD de usuarios (con validaciones de email y nombre).
* Arquitectura en capas: **domain**, **repository**, **service**, **web**.
* Manejo de excepciones centralizado (`GlobalExceptionHandler`).
* Tests unitarios e integración con JaCoCo (cobertura >80%).
* Base de datos H2 en memoria (semilla vía `data.sql`).

👉 Puerto: **8081**

---

### 📌 `inventario/`

* CRUD de productos (atributos: `id`, `sku`, `nombre`, `stock`).
* Validaciones de stock >= 0 y SKU único.
* Arquitectura en capas similar a `usuarios`.
* Manejo centralizado de errores (`GlobalExceptionHandler`).
* Tests unitarios e integración con JaCoCo (cobertura >85%).
* Base de datos H2 en memoria (semilla vía `data.sql`).

👉 Puerto: **8082**

---

### 📌 `notificaciones/`

* CRUD de notificaciones (`id`, `asunto`, `destino`, `mensaje`, `leida`).
* Validaciones con `jakarta.validation` (`@NotBlank`, `@Email`).
* Arquitectura en capas: **domain**, **repository**, **service**, **web**, **dto**.
* Tests unitarios y de integración con JaCoCo (cobertura >85%).
* Base de datos H2 en memoria con datos semilla (`data.sql`).
* Configuración de CORS para permitir integración con la UI (`http://localhost:5173`).

👉 Puerto: **8083**

---

## 🌐 UI (SPA React + Vite)

La carpeta `ui/` contiene una **Single Page Application** que consume los endpoints REST de los microservicios.  

### 📂 Estructura

```
ui/
 ├── src/
 │   ├── api.ts                 # Cliente Axios (usuariosApi, inventarioApi, notificacionesApi)
 │   ├── main.tsx               # Router principal
 │   ├── usuarios/UsuariosPage.tsx
 │   ├── inventario/ProductosPage.tsx
 │   └── notificaciones/NotificacionesPage.tsx
 ├── .env                       # Configuración de endpoints
 └── package.json
```

### 🔗 Endpoints configurados en `.env`

```env
VITE_API_USUARIOS=http://localhost:8081
VITE_API_INVENTARIO=http://localhost:8082
VITE_API_NOTIFICACIONES=http://localhost:8083
```

### Funcionalidades actuales

* Listar usuarios, productos y notificaciones.
* Crear nuevos usuarios, productos y notificaciones.
* Actualizar y eliminar productos (CRUD completo).
* Notificaciones: listar, crear, actualizar estado (marcar como leída) y eliminar.
* Usuarios: crear y listar (pendiente update/delete en UI).

👉 Puerto dev: **5173**

---

## ⚙️ Cómo ejecutar todo

### 1. Levantar backends

```bash
mvn -pl usuarios spring-boot:run
mvn -pl inventario spring-boot:run
mvn -pl notificaciones spring-boot:run
```

### 2. Levantar UI

```bash
cd ui
npm install
npm run dev
```

### 3. Acceder

* Usuarios API → [http://localhost:8081/usuarios](http://localhost:8081/usuarios)  
* Inventario API → [http://localhost:8082/productos](http://localhost:8082/productos)  
* Notificaciones API → [http://localhost:8083/notificaciones](http://localhost:8083/notificaciones)  
* UI → [http://localhost:5173](http://localhost:5173)  

---

## 🧪 Tests y cobertura

### Ejecutar con Maven

```bash
mvn -pl usuarios clean verify
mvn -pl inventario clean verify
mvn -pl notificaciones clean verify
```

### Reportes JaCoCo

* `usuarios/target/site/jacoco/index.html`
* `inventario/target/site/jacoco/index.html`
* `notificaciones/target/site/jacoco/index.html`

---

## 📌 Pendientes / Mejoras futuras

* **Usuarios UI**: implementar update/delete.
* Añadir **Swagger/OpenAPI** para documentación automática.
* Subir cobertura de controladores web >90%.
* Dockerizar microservicios y SPA.
* CI/CD básico con GitHub Actions.

---

## ✅ Estado actual

* Usuarios, Inventario y Notificaciones levantan correctamente con datos semilla.
* UI lista y conectada a los tres backends.
* CRUD completo en Productos y Notificaciones, parcial en Usuarios.
* Código estable, todos los tests pasan.
* Cobertura JaCoCo total ≈87%, con varios paquetes al 100%.

