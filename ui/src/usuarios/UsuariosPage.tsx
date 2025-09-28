import { useEffect, useState } from "react";
import { usuariosApi } from "../api";

type Usuario = {
  id: number;
  email: string;
  nombre: string;
};

export default function UsuariosPage() {
  const [items, setItems] = useState<Usuario[]>([]);
  const [loading, setLoading] = useState(false);
  const [err, setErr] = useState<string | null>(null);

  // crear
  const [email, setEmail] = useState("");
  const [nombre, setNombre] = useState("");

  // editar
  const [editId, setEditId] = useState<number | null>(null);
  const [editEmail, setEditEmail] = useState("");
  const [editNombre, setEditNombre] = useState("");

  const load = async () => {
    setLoading(true);
    setErr(null);
    try {
      const { data } = await usuariosApi.get<Usuario[]>("/usuarios");
      setItems(data);
    } catch (e: any) {
      setErr(e?.message || "Error al cargar usuarios");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    load();
  }, []);

  const crear = async (e: React.FormEvent) => {
    e.preventDefault();
    setErr(null);
    try {
      await usuariosApi.post("/usuarios", { email, nombre });
      setEmail("");
      setNombre("");
      await load();
    } catch (e: any) {
      setErr(e?.response?.data?.message || e?.message || "Error al crear");
    }
  };

  const startEdit = (u: Usuario) => {
    setEditId(u.id);
    setEditEmail(u.email);
    setEditNombre(u.nombre);
  };

  const cancelEdit = () => {
    setEditId(null);
    setEditEmail("");
    setEditNombre("");
  };

  const saveEdit = async (id: number) => {
    setErr(null);
    try {
      await usuariosApi.put(`/usuarios/${id}`, {
        email: editEmail,
        nombre: editNombre,
      });
      cancelEdit();
      await load();
    } catch (e: any) {
      setErr(e?.response?.data?.message || e?.message || "Error al actualizar");
    }
  };

  const eliminar = async (id: number) => {
    setErr(null);
    try {
      await usuariosApi.delete(`/usuarios/${id}`);
      await load();
    } catch (e: any) {
      setErr(e?.response?.data?.message || e?.message || "Error al eliminar");
    }
  };

  return (
    <div style={{ padding: 16 }}>
      <h1>Usuarios</h1>

      <form onSubmit={crear} style={{ display: "grid", gap: 8, maxWidth: 420, marginBottom: 16 }}>
        <h3>Crear</h3>
        <input
          placeholder="Email"
          type="email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          required
        />
        <input
          placeholder="Nombre"
          value={nombre}
          onChange={(e) => setNombre(e.target.value)}
          required
        />
        <button type="submit">Crear</button>
      </form>

      {err && <p style={{ color: "crimson" }}>{err}</p>}
      {loading && <p>Cargando...</p>}

      <table border={1} cellPadding={6} style={{ borderCollapse: "collapse", width: "100%" }}>
        <thead>
          <tr>
            <th>ID</th>
            <th>Email</th>
            <th>Nombre</th>
            <th style={{ width: 220 }}>Acciones</th>
          </tr>
        </thead>
        <tbody>
          {items.map((u) => (
            <tr key={u.id}>
              <td>{u.id}</td>

              {editId === u.id ? (
                <>
                  <td>
                    <input
                      type="email"
                      value={editEmail}
                      onChange={(e) => setEditEmail(e.target.value)}
                    />
                  </td>
                  <td>
                    <input
                      value={editNombre}
                      onChange={(e) => setEditNombre(e.target.value)}
                    />
                  </td>
                  <td>
                    <button onClick={() => saveEdit(u.id)}>Guardar</button>{" "}
                    <button onClick={cancelEdit}>Cancelar</button>
                  </td>
                </>
              ) : (
                <>
                  <td>{u.email}</td>
                  <td>{u.nombre}</td>
                  <td>
                    <button onClick={() => startEdit(u)}>Editar</button>{" "}
                    <button onClick={() => eliminar(u.id)}>Eliminar</button>
                  </td>
                </>
              )}
            </tr>
          ))}
          {items.length === 0 && !loading && (
            <tr>
              <td colSpan={4} style={{ textAlign: "center" }}>
                Sin usuarios
              </td>
            </tr>
          )}
        </tbody>
      </table>
    </div>
  );
}
