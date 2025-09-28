# FAC_Portafolio_M7 – Microservicios Usuarios + Inventario + UI

Este repositorio contiene el **portafolio del Módulo 7**, con dos microservicios desarrollados en Spring Boot y una SPA (Single Page Application) hecha con React + Vite.

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

## 🌐 UI (SPA React + Vite)

La carpeta `ui/` contiene una **Single Page Application** que consume los endpoints REST de los microservicios.  

### 📂 Estructura

```
ui/
 ├── src/
 │   ├── api.ts                 # Cliente Axios (usuariosApi, inventarioApi)
 │   ├── main.tsx               # Router principal
 │   ├── usuarios/UsuariosPage.tsx
 │   └── inventario/ProductosPage.tsx
 ├── .env                       # Configuración de endpoints
 └── package.json
```

### 🔗 Endpoints configurados en `.env`

```env
VITE_API_USUARIOS=http://localhost:8081
VITE_API_INVENTARIO=http://localhost:8082
```

### Funcionalidades actuales

* Listar usuarios y productos.
* Crear nuevos usuarios y productos.
* Actualizar y eliminar productos (CRUD completo).
* Usuarios: crear y listar (pendiente update/delete en UI).

👉 Puerto dev: **5173**

---

## ⚙️ Cómo ejecutar todo

### 1. Levantar backends

```bash
mvn -pl usuarios spring-boot:run
mvn -pl inventario spring-boot:run
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
* UI → [http://localhost:5173](http://localhost:5173)  

---

## 🧪 Tests y cobertura

### Ejecutar con Maven

```bash
mvn -pl usuarios clean verify
mvn -pl inventario clean verify
```

### Reportes JaCoCo

* `usuarios/target/site/jacoco/index.html`
* `inventario/target/site/jacoco/index.html`

---

## 📌 Pendientes / Mejoras futuras

* **Usuarios UI**: implementar update/delete.
* Añadir **Swagger/OpenAPI** para documentación automática.
* Subir cobertura de `service` >90%.
* Dockerizar microservicios y SPA.
* CI/CD básico con GitHub Actions.

---

## ✅ Estado actual

* Usuarios e Inventario levantan correctamente con datos semilla.
* UI lista y conectada a backends.
* CRUD completo en Productos, parcial en Usuarios.
* Código estable, todos los tests pasan.
* Cobertura JaCoCo >80%.
