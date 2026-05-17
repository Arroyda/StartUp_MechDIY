const API_BASE = import.meta.env.VITE_API_BASE || 'http://localhost:8080/api'

async function fetchAPI(path) {
  const resp = await fetch(API_BASE + path)
  if (!resp.ok) throw new Error(`HTTP ${resp.status}`)
  return resp.json()
}

export const listarCarros = (termo = '') => {
  const path = termo
    ? `/carros/buscar?termo=${encodeURIComponent(termo)}`
    : '/carros'
  return fetchAPI(path)
}

export const buscarCarroPorId = (id) => fetchAPI(`/carros/${id}`)
