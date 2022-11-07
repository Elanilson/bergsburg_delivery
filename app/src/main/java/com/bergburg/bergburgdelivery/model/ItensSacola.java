package com.bergburg.bergburgdelivery.model;

public class ItensSacola {
    private Long id;
    private String titulo;
    private String urlImagem;
    private String descricao;
    private String observacao;
    private Float preco;
    private Float total;
    private int quantidade;

    @Override
    public String toString() {
        return "ItensSacola{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", urlImagem='" + urlImagem + '\'' +
                ", descricao='" + descricao + '\'' +
                ", observacao='" + observacao + '\'' +
                ", preco=" + preco +
                ", total=" + total +
                ", quantidade=" + quantidade +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getUrlImagem() {
        return urlImagem;
    }

    public void setUrlImagem(String urlImagem) {
        this.urlImagem = urlImagem;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Float getPreco() {
        return preco;
    }

    public void setPreco(Float preco) {
        this.preco = preco;
    }

    public Float getTotal() {
        return total;
    }

    public void setTotal(Float total) {
        this.total = total;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
}
