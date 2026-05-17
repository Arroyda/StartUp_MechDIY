import { useEffect, useState } from 'react'
import { buscarCarroPorId } from './api'

function MaintenanceItem({ item }) {
  const [videoOpen, setVideoOpen] = useState(false)
  const [descOpen, setDescOpen] = useState(false)
  const tipoLabel = item.tipo.replace(/_/g, ' ')

  return (
    <div className="item">
      <div className="item-header">
        <span className="item-tipo">{tipoLabel}</span>
      </div>
      <div className="item-actions">
        {item.urlVideo && (
          <button
            className="btn btn-primary"
            onClick={() => setVideoOpen(v => !v)}
          >
            {videoOpen ? '⏸ Ocultar vídeo' : '▶ Assistir tutorial'}
          </button>
        )}
        {item.descricao && (
          <button className="btn" onClick={() => setDescOpen(d => !d)}>
            {descOpen ? '📋 Ocultar descrição' : '📋 Ver descrição'}
          </button>
        )}
        {item.urlProduto && (
          <a className="btn" href={item.urlProduto} target="_blank" rel="noopener">
            🛒 Comprar peça
          </a>
        )}
      </div>
      {descOpen && <div className="descricao-wrapper">{item.descricao}</div>}
      {videoOpen && item.urlVideo && (
        <div className="video-wrapper">
          <video controls autoPlay src={item.urlVideo} />
        </div>
      )}
    </div>
  )
}

export default function CarModal({ id, onClose }) {
  const [carro, setCarro] = useState(null)
  const [error, setError] = useState(null)

  useEffect(() => {
    document.body.style.overflow = 'hidden'
    return () => {
      document.body.style.overflow = ''
    }
  }, [])

  useEffect(() => {
    let cancelled = false
    buscarCarroPorId(id)
      .then(data => {
        if (!cancelled) setCarro(data)
      })
      .catch(err => {
        if (!cancelled) setError(err.message)
      })
    return () => {
      cancelled = true
    }
  }, [id])

  useEffect(() => {
    const onKey = e => {
      if (e.key === 'Escape') onClose()
    }
    document.addEventListener('keydown', onKey)
    return () => document.removeEventListener('keydown', onKey)
  }, [onClose])

  return (
    <div
      className="modal-backdrop"
      onClick={e => {
        if (e.target === e.currentTarget) onClose()
      }}
      role="dialog"
      aria-modal="true"
    >
      <div className="modal">
        <button className="modal-close" onClick={onClose} aria-label="Fechar">×</button>
        {!carro && !error && <div className="loading">Carregando tutoriais...</div>}
        {error && <div className="error">Erro ao carregar detalhes: {error}</div>}
        {carro && (
          <>
            <div className="modal-header">
              <div className="modal-brand">
                {carro.logo && (
                  <img
                    className="modal-logo"
                    src={carro.logo}
                    alt={carro.marca}
                    onError={e => (e.currentTarget.style.display = 'none')}
                  />
                )}
                {carro.marca}
              </div>
              <h2 className="modal-title">{carro.modelo}</h2>
              <div className="modal-meta">
                {carro.ano} · {carro.versao || ''}
              </div>
            </div>
            {(carro.itensManutencao || []).map(item => (
              <MaintenanceItem key={item.id} item={item} />
            ))}
          </>
        )}
      </div>
    </div>
  )
}
