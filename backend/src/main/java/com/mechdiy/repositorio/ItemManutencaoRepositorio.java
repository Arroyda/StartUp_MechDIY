package com.mechdiy.repositorio;

import com.mechdiy.modelo.ItemManutencao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório JPA para a entidade ItemManutencao.
 * Permite buscar itens de manutenção de um carro específico
 * ou filtrar por tipo (filtro, bateria, ar_condicionado, etc.).
 */
@Repository
public interface ItemManutencaoRepositorio extends JpaRepository<ItemManutencao, Long> {

    /**
     * Retorna todos os itens de manutenção de um carro.
     * Spring Data gera: SELECT * FROM itens_manutencao WHERE carro_id = ?
     */
    List<ItemManutencao> findByCarroId(Long carroId);

    /**
     * Retorna todos os itens de um tipo específico entre todos os carros.
     * Ex.: findByTipo("bateria") retorna todas as baterias cadastradas.
     */
    List<ItemManutencao> findByTipo(String tipo);

    /**
     * Retorna o item de manutenção de um carro para um tipo específico.
     * Ex.: findByCarroIdAndTipo(1L, "filtro") retorna o filtro do carro 1.
     */
    Optional<ItemManutencao> findByCarroIdAndTipo(Long carroId, String tipo);
}
