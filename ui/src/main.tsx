import React from "react";
import ReactDOM from "react-dom/client";
import { createBrowserRouter, RouterProvider, Link } from "react-router-dom";
import UsuariosPage from "./usuarios/UsuariosPage";
import ProductosPage from "./inventario/ProductosPage";

const router = createBrowserRouter([
  { path: "/", element: <Home /> },
  { path: "/usuarios", element: <UsuariosPage /> },
  { path: "/inventario", element: <ProductosPage /> },
]);

function Home() {
  return (
    <div style={{ padding: 16 }}>
      <h1>FAC Portafolio</h1>
      <nav style={{ display: "flex", gap: 12 }}>
        <Link to="/usuarios">Usuarios</Link>
        <Link to="/inventario">Inventario</Link>
      </nav>
      <p>Selecciona un módulo en el menú.</p>
    </div>
  );
}

ReactDOM.createRoot(document.getElementById("root")!).render(
  <React.StrictMode>
    <RouterProvider router={router} />
  </React.StrictMode>
);