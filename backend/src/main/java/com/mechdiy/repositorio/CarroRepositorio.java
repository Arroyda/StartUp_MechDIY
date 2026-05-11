package com.mechdiy.repositorio;

import com.mechdiy.modelo.Carro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório JPA para a entidade Carro.
 * Fornece operações CRUD automáticas e consultas personalizadas
 * para filtrar veículos por marca, modelo e ano.
 */
@Repository
public interface CarroRepositorio extends JpaRepository<Carro, Long> {

    /**
     * Busca um carro pela chave do JSON (ex.: "carro_001").
     * Usado pelo InicializadorDados para evitar duplicatas.
     */
    Optional<Carro> findByChave(String chave);

    /**
     * Retorna todos os carros de uma marca (case-insensitive).
     * Ex.: buscarPorMarca("toyota") retorna todos os Toyotas.
     */
    @Query("SELECT c FROM Carro c WHERE LOWER(c.marca) = LOWER(:marca)")
    List<Carro> buscarPorMarca(@Param("marca") String marca);

    /**
     * Retorna todos os carros de um modelo específico (case-insensitive).
     */
    @Query("SELECT c FROM Carro c WHERE LOWER(c.modelo) = LOWER(:modelo)")
    List<Carro> buscarPorModelo(@Param("modelo") String modelo);

    /**
     * Retorna todos os carros de uma marca e modelo combinados (case-insensitive).
     * Ex.: buscarPorMarcaEModelo("Toyota", "Corolla")
     */
    @Query("SELECT c FROM Carro c WHERE LOWER(c.marca) = LOWER(:marca) AND LOWER(c.modelo) = LOWER(:modelo)")
    List<Carro> buscarPorMarcaEModelo(@Param("marca") String marca, @Param("modelo") String modelo);

    /**
     * Retorna todos os carros de um determinado ano.
     */
    List<Carro> findByAno(Integer ano);

    /**
     * Busca textual em marca ou modelo (case-insensitive).
     * Permite ao usuário digitar parte do nome para encontrar o carro.
     */
    @Query("SELECT c FROM Carro c WHERE " +
           "LOWER(c.marca) LIKE LOWER(CONCAT('%', :termo, '%')) OR " +
           "LOWER(c.modelo) LIKE LOWER(CONCAT('%', :termo, '%'))")
    List<Carro> buscarPorTermo(@Param("termo") String termo);
}
