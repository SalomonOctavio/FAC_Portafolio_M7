import React, { useEffect, useState } from "react";
import { notificacionesApi } from "../api";

type Notificacion = {
  id?: number;
  // ajusta estos campos según tu entidad real.
  // si tu Notificacion tiene otros nombres, cámbialos acá y en el backend.
  asunto?: string;
  mensaje?: string;
  destino?: string; // email/usuario/etc
  leida?: boolean;
};

export default function NotificacionesPage() {
  const [items, setItems] = useState<Notificacion[]>([]);
  const [loading, setLoading] = useState(false);
  const [err, setErr] = useState<string | null>(null);

  const [form, setForm] = useState<Notificacion>({
    asunto: "",
    mensaje: "",
    destino: "",
    leida: false,
  });

  async function load() {
    setLoading(true);
    setErr(null);
    try {
      const { data } = await notificacionesApi.get<Notificacion[]>("/notificaciones");
      setItems(data);
    } catch (e: any) {
      setErr(e?.message ?? "Error cargando notificaciones");
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    load();
  }, []);

  async function crear(e: React.FormEvent) {
  e.preventDefault();
  setErr(null);

  // Validación mínima en el cliente para evitar 400/500 del backend
  const asunto = (form.asunto ?? "").trim();
  const destino = (form.destino ?? "").trim();
  const mensaje = (form.mensaje ?? "").trim();

  if (!asunto || !destino || !mensaje) {
    setErr("Asunto, Destino y Mensaje son obligatorios.");
    return;
  }

  try {
    await notificacionesApi.post("/notificaciones", {
      asunto,
      destino,
      mensaje,
      leida: !!form.leida,
    });
    setForm({ asunto: "", mensaje: "", destino: "", leida: false });
    await load();
  } catch (e: any) {
    // Muestra el detalle de error del backend si viene en el body
    const msg =
      e?.response?.data?.message ||
      e?.response?.data?.error ||
      e?.message ||
      "Error creando notificación";
    setErr(msg);
    }
  }
  async function actualizar(n: Notificacion) {
    if (!n.id) return;
    setErr(null);
    try {
      await notificacionesApi.put(`/notificaciones/${n.id}`, n);
      await load();
    } catch (e: any) {
      setErr(e?.message ?? "Error actualizando notificación");
    }
  }

  async function eliminar(id: number) {
    setErr(null);
    try {
      await notificacionesApi.delete(`/notificaciones/${id}`);
      await load();
    } catch (e: any) {
      setErr(e?.message ?? "Error eliminando notificación");
    }
  }

  async function limpiar() {
    setErr(null);
    try {
      await notificacionesApi.delete(`/notificaciones`);
      await load();
    } catch (e: any) {
      setErr(e?.message ?? "Error limpiando notificaciones");
    }
  }

  return (
    <div style={{ padding: 16 }}>
      <h1>Notificaciones</h1>

      <form onSubmit={crear} style={{ display: "grid", gap: 8, maxWidth: 520, marginBottom: 24 }}>
        <input
          placeholder="Asunto"
          value={form.asunto ?? ""}
          onChange={(e) => setForm((f) => ({ ...f, asunto: e.target.value }))}
        />
        <input
          placeholder="Destino (email/usuario)"
          value={form.destino ?? ""}
          onChange={(e) => setForm((f) => ({ ...f, destino: e.target.value }))}
        />
        <textarea
          placeholder="Mensaje"
          value={form.mensaje ?? ""}
          onChange={(e) => setForm((f) => ({ ...f, mensaje: e.target.value }))}
          rows={3}
        />
        <label style={{ display: "flex", gap: 8, alignItems: "center" }}>
          <input
            type="checkbox"
            checked={!!form.leida}
            onChange={(e) => setForm((f) => ({ ...f, leida: e.target.checked }))}
          />
          Marcar como leída
        </label>
        <button type="submit">Crear</button>
      </form>

      <div style={{ display: "flex", gap: 8, alignItems: "center", marginBottom: 8 }}>
        <button onClick={load} disabled={loading}>Refrescar</button>
        <button onClick={limpiar} style={{ background: "#f33", color: "#fff" }}>Limpiar todo</button>
        {loading && <span>Cargando…</span>}
        {err && <span style={{ color: "crimson" }}>{err}</span>}
      </div>

      <div style={{ overflowX: "auto" }}>
        <table border={1} cellPadding={6} style={{ borderCollapse: "collapse", minWidth: 700 }}>
          <thead>
            <tr>
              <th>ID</th>
              <th>Asunto</th>
              <th>Destino</th>
              <th>Mensaje</th>
              <th>Leída</th>
              <th>Acciones</th>
            </tr>
          </thead>
          <tbody>
            {items.map((n) => (
              <tr key={n.id}>
                <td>{n.id}</td>
                <td>
                  <input
                    value={n.asunto ?? ""}
                    onChange={(e) => setItems((prev) =>
                      prev.map((x) => (x.id === n.id ? { ...x, asunto: e.target.value } : x))
                    )}
                  />
                </td>
                <td>
                  <input
                    value={n.destino ?? ""}
                    onChange={(e) => setItems((prev) =>
                      prev.map((x) => (x.id === n.id ? { ...x, destino: e.target.value } : x))
                    )}
                  />
                </td>
                <td>
                  <input
                    value={n.mensaje ?? ""}
                    onChange={(e) => setItems((prev) =>
                      prev.map((x) => (x.id === n.id ? { ...x, mensaje: e.target.value } : x))
                    )}
                  />
                </td>
                <td style={{ textAlign: "center" }}>
                  <input
                    type="checkbox"
                    checked={!!n.leida}
                    onChange={(e) =>
                      setItems((prev) =>
                        prev.map((x) => (x.id === n.id ? { ...x, leida: e.target.checked } : x))
                      )
                    }
                  />
                </td>
                <td>
                  <button onClick={() => actualizar(n)}>Guardar</button>{" "}
                  {typeof n.id === "number" && (
                    <button onClick={() => eliminar(n.id!)} style={{ background: "#f33", color: "#fff" }}>
                      Eliminar
                    </button>
                  )}
                </td>
              </tr>
            ))}
            {items.length === 0 && !loading && (
              <tr>
                <td colSpan={6} style={{ textAlign: "center", color: "#666" }}>
                  Sin notificaciones
                </td>
              </tr>
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
}