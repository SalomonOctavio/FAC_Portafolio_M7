import axios from "axios";

export const usuariosApi = axios.create({
  baseURL: import.meta.env.VITE_API_USUARIOS || "http://localhost:8081",
});

export const inventarioApi = axios.create({
  baseURL: import.meta.env.VITE_API_INVENTARIO || "http://localhost:8082",
});