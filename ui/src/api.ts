import axios from "axios";

const baseUsuarios = import.meta.env.VITE_API_USUARIOS ?? "http://localhost:8081";
const baseInventario = import.meta.env.VITE_API_INVENTARIO ?? "http://localhost:8082";
const baseNotifs = import.meta.env.VITE_API_NOTIFICACIONES ?? "http://localhost:8083";

export const usuariosApi = axios.create({
  baseURL: baseUsuarios,
  headers: { "Content-Type": "application/json" },
});

export const inventarioApi = axios.create({
  baseURL: baseInventario,
  headers: { "Content-Type": "application/json" },
});

export const notificacionesApi = axios.create({
  baseURL: baseNotifs,
  headers: { "Content-Type": "application/json" },
});

// Helpers genéricos por si quieres usarlos luego
export async function list<T = any>(api = axios) {
  const { data } = await api.get<T>("/");
  return data;
}