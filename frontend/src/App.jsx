import { useEffect, useRef, useState } from 'react'
import { listarCarros } from './api'
import { useTheme } from './useTheme'
import CarModal from './CarModal'

function ThemeToggle({ theme, onToggle }) {
  return (
    <button
      className="theme-toggle"
      onClick={onToggle}
      aria-label={theme === 'dark' ? 'Modo claro' : 'Modo escuro'}
      title={theme === 'dark' ? 'Mudar para modo claro' : 'Mudar para modo escuro'}
    >
      {theme === 'dark' ? '☀️' : '🌙'}
    </button>
  )
}

function CarCard({ carro, onClick }) {
  return (
    <article
      className="card"
      tabIndex={0}
      role="button"
      onClick={onClick}
      onKeyDown={e => {
        if (e.key === 'Enter' || e.key === ' ') {
          e.preventDefault()
          onClick()
        }
      }}
    >
      <div className="card-brand">
        {carro.logo && (
          <img
            className="card-logo"
            src={carro.logo}
            alt={carro.marca}
            onError={e => (e.currentTarget.style.display = 'none')}
          />
        )}
        {carro.marca}
      </div>
      <div className="card-model">{carro.modelo}</div>
      <div className="card-meta">{carro.ano} · {carro.versao || ''}</div>
      <div className="card-footer">
        <span className="card-items">
          {carro.quantidadeItens} {carro.quantidadeItens === 1 ? 'tutorial' : 'tutoriais'}
        </span>
        <span className="card-arrow">Ver detalhes →</span>
      </div>
    </article>
  )
}

export default function App() {
  const { theme, toggle } = useTheme()
  const [carros, setCarros] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)
  const [termo, setTermo] = useState('')
  const [selectedId, setSelectedId] = useState(null)
  const debounceRef = useRef(null)

  useEffect(() => {
    clearTimeout(debounceRef.current)
    debounceRef.current = setTimeout(() => {
      setLoading(true)
      setError(null)
      listarCarros(termo.trim())
        .then(data => setCarros(data))
        .catch(err => setError(err.message))
        .finally(() => setLoading(false))
    }, 300)
    return () => clearTimeout(debounceRef.current)
  }, [termo])

  return (
    <>
      <header>
        <div className="header-inner">
          <div className="logo">
            <div className="logo-mark">
              <img src="https://i.imgur.com/OsotiFT.png" alt="MechDIY" />
            </div>
            <div>
              <span className="logo-mech">Mech</span>
              <span className="logo-diy">DIY</span>
            </div>
          </div>
          <div className="header-right">
            <div className="header-meta">Tutoriais de manutenção automotiva</div>
            <ThemeToggle theme={theme} onToggle={toggle} />
          </div>
        </div>
      </header>

      <main>
        <section className="hero">
          <h1>
            Faça você a manutenção do seu carro, <span>passo a passo</span>.
          </h1>
          <p>
            Selecione um veículo abaixo para ver os tutoriais em vídeo de filtros,
            baterias e outros itens. Em dúvida sobre por onde começar? Use o assistente
            no canto inferior.
          </p>
        </section>

        <div className="toolbar">
          <div className="search">
            <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
              <circle cx="11" cy="11" r="8" />
              <path d="m21 21-4.3-4.3" />
            </svg>
            <input
              type="text"
              placeholder="Buscar por marca ou modelo..."
              autoComplete="off"
              value={termo}
              onChange={e => setTermo(e.target.value)}
            />
          </div>
        </div>

        <div className="grid">
          {loading && <div className="loading">Carregando veículos...</div>}
          {error && (
            <div className="error">
              <p style={{ fontWeight: 600, marginBottom: '.5rem' }}>
                Não foi possível carregar os veículos.
              </p>
              <p>
                Verifique se o backend está rodando em <code>http://localhost:8080</code>.
              </p>
              <p style={{ marginTop: '.5rem', fontSize: '.85rem', opacity: 0.7 }}>
                {error}
              </p>
            </div>
          )}
          {!loading && !error && carros.length === 0 && (
            <div className="empty">Nenhum veículo encontrado.</div>
          )}
          {!loading && !error && carros.map(c => (
            <CarCard key={c.id} carro={c} onClick={() => setSelectedId(c.id)} />
          ))}
        </div>
      </main>

      {selectedId && (
        <CarModal id={selectedId} onClose={() => setSelectedId(null)} />
      )}
    </>
  )
}
