package com.mechdiy.dto;

import java.util.List;

/**
 * DTO completo de carro — retornado ao selecionar um veículo específico.
 * Inclui todos os itens de manutenção com descrição, vídeo e link do produto.
 */
public class CarroDetalheDTO {

    private Long id;
    private String marca;
    private String modelo;
    private Integer ano;
    private String versao;

    /** Lista completa de itens de manutenção disponíveis para este veículo */
    private List<ItemManutencaoDTO> itensManutencao;

    public CarroDetalheDTO(Long id, String marca, String modelo,
                           Integer ano, String versao,
                           List<ItemManutencaoDTO> itensManutencao) {
        this.id = id;
        this.marca = marca;
        this.modelo = modelo;
        this.ano = ano;
        this.versao = versao;
        this.itensManutencao = itensManutencao;
    }

    // Getters

    public Long getId() { return id; }
    public String getMarca() { return marca; }
    public String getModelo() { return modelo; }
    public Integer getAno() { return ano; }
    public String getVersao() { return versao; }
    public List<ItemManutencaoDTO> getItensManutencao() { return itensManutencao; }
}
