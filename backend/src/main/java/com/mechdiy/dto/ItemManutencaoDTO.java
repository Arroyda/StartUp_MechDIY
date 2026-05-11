package com.mechdiy.dto;

/**
 * DTO de um item de manutenção.
 * Contém o tipo, a descrição do produto, o link do vídeo tutorial
 * e o link de compra do produto.
 */
public class ItemManutencaoDTO {

    private Long id;

    /** Tipo do item (ex.: "filtro", "bateria", "ar_condicionado") */
    private String tipo;

    /** Descrição detalhada do produto ou serviço */
    private String descricao;

    /** URL do vídeo tutorial em MP4 */
    private String urlVideo;

    /** URL da página de compra ou informação do produto */
    private String urlProduto;

    public ItemManutencaoDTO(Long id, String tipo, String descricao,
                             String urlVideo, String urlProduto) {
        this.id = id;
        this.tipo = tipo;
        this.descricao = descricao;
        this.urlVideo = urlVideo;
        this.urlProduto = urlProduto;
    }

    // Getters

    public Long getId() { return id; }
    public String getTipo() { return tipo; }
    public String getDescricao() { return descricao; }
    public String getUrlVideo() { return urlVideo; }
    public String getUrlProduto() { return urlProduto; }
}
