package com.mechdiy.dto;

/**
 * DTO resumido de carro — usado na listagem de veículos.
 * Não inclui os itens de manutenção para não sobrecarregar a resposta.
 * O frontend usa este DTO para exibir os cards de seleção de veículo.
 */
public class CarroResumoDTO {

    private Long id;
    private String marca;
    private String modelo;
    private Integer ano;
    private String versao;
    private String logo;
    private int quantidadeItens;

    public CarroResumoDTO(Long id, String marca, String modelo,
                          Integer ano, String versao, String logo, int quantidadeItens) {
        this.id = id;
        this.marca = marca;
        this.modelo = modelo;
        this.ano = ano;
        this.versao = versao;
        this.logo = logo;
        this.quantidadeItens = quantidadeItens;
    }

    // Getters

    public Long getId() { return id; }
    public String getMarca() { return marca; }
    public String getModelo() { return modelo; }
    public Integer getAno() { return ano; }
    public String getVersao() { return versao; }
    public String getLogo() { return logo; }
    public int getQuantidadeItens() { return quantidadeItens; }
}
