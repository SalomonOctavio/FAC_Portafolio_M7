import React from "react";
import ReactDOM from "react-dom/client";
import { BrowserRouter, Link, Route, Routes, Navigate } from "react-router-dom";
import UsuariosPage from "./usuarios/UsuariosPage";
import ProductosPage from "./inventario/ProductosPage";
import NotificacionesPage from "./notificaciones/NotificacionesPage";

function Nav() {
  const navStyle: React.CSSProperties = {
    display: "flex",
    gap: 12,
    padding: 12,
    borderBottom: "1px solid #ddd",
    marginBottom: 16,
  };
  return (
    <nav style={navStyle}>
      <Link to="/">Inicio</Link>
      <Link to="/usuarios">Usuarios</Link>
      <Link to="/inventario">Inventario</Link>
      <Link to="/notificaciones">Notificaciones</Link>
    </nav>
  );
}

function Home() {
  return (
    <div style={{ padding: 16 }}>
      <h1>FAC Portafolio M7 — UI</h1>
      <p>Selecciona un módulo en la navegación.</p>
    </div>
  );
}

ReactDOM.createRoot(document.getElementById("root")!).render(
  <React.StrictMode>
    <BrowserRouter>
      <Nav />
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/usuarios" element={<UsuariosPage />} />
        <Route path="/inventario" element={<ProductosPage />} />
        <Route path="/notificaciones" element={<NotificacionesPage />} />
        {/* compat: si alguien escribe /notifs lo mando a /notificaciones */}
        <Route path="/notifs" element={<Navigate to="/notificaciones" replace />} />
        <Route path="*" element={<div style={{ padding: 16 }}>Ruta no encontrada</div>} />
      </Routes>
    </BrowserRouter>
  </React.StrictMode>
);
