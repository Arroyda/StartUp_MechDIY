package com.mechdiy.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa um veículo cadastrado no sistema.
 * Cada carro possui uma chave única (ex.: "carro_001") que corresponde
 * à chave no arquivo JSON, além de marca, modelo, ano, versão e
 * uma lista de itens de manutenção disponíveis.
 */
@Entity
@Table(name = "carros")
public class Carro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Chave única do carro no JSON (ex.: "carro_001").
     * Usada para identificar o veículo na origem dos dados.
     */
    @NotBlank
    @Column(nullable = false, unique = true)
    private String chave;

    @NotBlank
    @Column(nullable = false)
    private String marca;

    @NotBlank
    @Column(nullable = false)
    private String modelo;

    @Column(nullable = false)
    private Integer ano;

    /** Versão/trim do veículo (ex.: "XEi", "Touring", "LTZ") */
    @Column
    private String versao;

    /**
     * Itens de manutenção disponíveis para este veículo.
     * Relação 1:N — um carro possui vários itens (filtro, bateria, ar_condicionado, etc.).
     * cascade = ALL: ao salvar o carro, os itens também são salvos.
     * orphanRemoval = true: itens removidos da lista são deletados do banco.
     */
    @OneToMany(mappedBy = "carro", cascade = CascadeType.ALL,
               fetch = FetchType.LAZY, orphanRemoval = true)
    private List<ItemManutencao> itensManutencao = new ArrayList<>();

    // -------------------------
    // Construtores
    // -------------------------

    public Carro() {}

    public Carro(String chave, String marca, String modelo, Integer ano, String versao) {
        this.chave = chave;
        this.marca = marca;
        this.modelo = modelo;
        this.ano = ano;
        this.versao = versao;
    }

    // -------------------------
    // Getters e Setters
    // -------------------------

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getChave() { return chave; }
    public void setChave(String chave) { this.chave = chave; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    public Integer getAno() { return ano; }
    public void setAno(Integer ano) { this.ano = ano; }

    public String getVersao() { return versao; }
    public void setVersao(String versao) { this.versao = versao; }

    public List<ItemManutencao> getItensManutencao() { return itensManutencao; }
    public void setItensManutencao(List<ItemManutencao> itensManutencao) { this.itensManutencao = itensManutencao; }
}
