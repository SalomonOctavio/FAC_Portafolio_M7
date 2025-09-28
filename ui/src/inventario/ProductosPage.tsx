import { useEffect, useState } from "react";
import { inventarioApi } from "../api";

type Producto = {
  id: number;
  sku: string;
  nombre: string;
  stock: number;
};

export default function ProductosPage() {
  const [items, setItems] = useState<Producto[]>([]);
  const [loading, setLoading] = useState(false);
  const [err, setErr] = useState<string | null>(null);

  // crear
  const [sku, setSku] = useState("");
  const [nombre, setNombre] = useState("");
  const [stock, setStock] = useState<number>(0);

  // editar
  const [editId, setEditId] = useState<number | null>(null);
  const [editSku, setEditSku] = useState("");
  const [editNombre, setEditNombre] = useState("");
  const [editStock, setEditStock] = useState<number>(0);

  const load = async () => {
    setLoading(true);
    setErr(null);
    try {
      const { data } = await inventarioApi.get<Producto[]>("/productos");
      setItems(data);
    } catch (e: any) {
      setErr(e?.message || "Error al cargar productos");
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
      await inventarioApi.post("/productos", { sku, nombre, stock: Number(stock) });
      setSku("");
      setNombre("");
      setStock(0);
      await load();
    } catch (e: any) {
      setErr(e?.response?.data?.message || e?.message || "Error al crear");
    }
  };

  const startEdit = (p: Producto) => {
    setEditId(p.id);
    setEditSku(p.sku);
    setEditNombre(p.nombre);
    setEditStock(p.stock);
  };

  const cancelEdit = () => {
    setEditId(null);
    setEditSku("");
    setEditNombre("");
    setEditStock(0);
  };

  const saveEdit = async (id: number) => {
    setErr(null);
    try {
      await inventarioApi.put(`/productos/${id}`, {
        sku: editSku,
        nombre: editNombre,
        stock: Number(editStock),
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
      await inventarioApi.delete(`/productos/${id}`);
      await load();
    } catch (e: any) {
      setErr(e?.response?.data?.message || e?.message || "Error al eliminar");
    }
  };

  return (
    <div style={{ padding: 16 }}>
      <h1>Inventario — Productos</h1>

      <form onSubmit={crear} style={{ display: "grid", gap: 8, maxWidth: 420, marginBottom: 16 }}>
        <h3>Crear</h3>
        <input
          placeholder="SKU"
          value={sku}
          onChange={(e) => setSku(e.target.value)}
          required
        />
        <input
          placeholder="Nombre"
          value={nombre}
          onChange={(e) => setNombre(e.target.value)}
          required
        />
        <input
          placeholder="Stock"
          type="number"
          min={0}
          value={stock}
          onChange={(e) => setStock(Number(e.target.value))}
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
            <th>SKU</th>
            <th>Nombre</th>
            <th>Stock</th>
            <th style={{ width: 220 }}>Acciones</th>
          </tr>
        </thead>
        <tbody>
          {items.map((p) => (
            <tr key={p.id}>
              <td>{p.id}</td>

              {editId === p.id ? (
                <>
                  <td>
                    <input value={editSku} onChange={(e) => setEditSku(e.target.value)} />
                  </td>
                  <td>
                    <input value={editNombre} onChange={(e) => setEditNombre(e.target.value)} />
                  </td>
                  <td>
                    <input
                      type="number"
                      min={0}
                      value={editStock}
                      onChange={(e) => setEditStock(Number(e.target.value))}
                    />
                  </td>
                  <td>
                    <button onClick={() => saveEdit(p.id)}>Guardar</button>{" "}
                    <button onClick={cancelEdit}>Cancelar</button>
                  </td>
                </>
              ) : (
                <>
                  <td>{p.sku}</td>
                  <td>{p.nombre}</td>
                  <td>{p.stock}</td>
                  <td>
                    <button onClick={() => startEdit(p)}>Editar</button>{" "}
                    <button onClick={() => eliminar(p.id)}>Eliminar</button>
                  </td>
                </>
              )}
            </tr>
          ))}
          {items.length === 0 && !loading && (
            <tr>
              <td colSpan={5} style={{ textAlign: "center" }}>
                Sin productos
              </td>
            </tr>
          )}
        </tbody>
      </table>
    </div>
  );
}