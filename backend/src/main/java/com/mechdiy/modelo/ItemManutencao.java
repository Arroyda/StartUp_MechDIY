package com.mechdiy.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

/**
 * Representa um item de manutenção disponível para um veículo específico.
 * Corresponde a uma entrada dentro de "detalhes_opcoes" no JSON.
 *
 * Exemplos de tipo: "filtro", "bateria", "ar_condicionado".
 * Cada item possui uma descrição do produto, URL do vídeo tutorial e link de compra.
 */
@Entity
@Table(name = "itens_manutencao")
public class ItemManutencao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Veículo ao qual este item pertence.
     * @ManyToOne: muitos itens pertencem a um carro.
     * @JoinColumn: define o nome da coluna de FK na tabela itens_manutencao.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carro_id", nullable = false)
    private Carro carro;

    /**
     * Tipo/categoria do item de manutenção.
     * Corresponde à chave no JSON (ex.: "filtro", "bateria", "ar_condicionado").
     * Normalizado para minúsculas com underscore.
     */
    @NotBlank
    @Column(nullable = false)
    private String tipo;

    /** Descrição detalhada do produto ou serviço de manutenção */
    @Column(nullable = false, length = 1000)
    private String descricao;

    /**
     * URL do vídeo tutorial em formato MP4.
     * Corresponde ao campo "arquivo_mp4" no JSON.
     */
    @Column(name = "url_video", length = 500)
    private String urlVideo;

    /**
     * URL de compra ou informação do produto.
     * Corresponde ao campo "link" no JSON.
     */
    @Column(name = "url_produto", length = 500)
    private String urlProduto;

    // -------------------------
    // Construtores
    // -------------------------

    public ItemManutencao() {}

    public ItemManutencao(Carro carro, String tipo, String descricao,
                          String urlVideo, String urlProduto) {
        this.carro = carro;
        this.tipo = tipo;
        this.descricao = descricao;
        this.urlVideo = urlVideo;
        this.urlProduto = urlProduto;
    }

    // -------------------------
    // Getters e Setters
    // -------------------------

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Carro getCarro() { return carro; }
    public void setCarro(Carro carro) { this.carro = carro; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getUrlVideo() { return urlVideo; }
    public void setUrlVideo(String urlVideo) { this.urlVideo = urlVideo; }

    public String getUrlProduto() { return urlProduto; }
    public void setUrlProduto(String urlProduto) { this.urlProduto = urlProduto; }
}
