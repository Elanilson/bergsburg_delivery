package com.bergburg.bergburgdelivery.ifood.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Item {
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String nome; // 50 caracteres
    @SerializedName("externalCode")
    private Long idItemExtreno; // id do item
    @SerializedName("quantity")
    private int quantidade;
    @SerializedName("unitPrice")
    private float precoUnitario;
    @SerializedName("price")
    private float preco = (quantidade * precoUnitario);
    @SerializedName("price")
    private float precoTotal = preco;
    @SerializedName("totalPrice")
    private float precoTotalDasOpcoes = 0f;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Long getIdItemExtreno() {
        return idItemExtreno;
    }

    public void setIdItemExtreno(Long idItemExtreno) {
        this.idItemExtreno = idItemExtreno;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public float getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(float precoUnitario) {
        this.precoUnitario = precoUnitario;
    }

    public float getPreco() {
        return preco;
    }

    public void setPreco(float preco) {
        this.preco = preco;
    }

    public float getPrecoTotal() {
        return precoTotal;
    }

    public void setPrecoTotal(float precoTotal) {
        this.precoTotal = precoTotal;
    }

    public float getPrecoTotalDasOpcoes() {
        return precoTotalDasOpcoes;
    }

    public void setPrecoTotalDasOpcoes(float precoTotalDasOpcoes) {
        this.precoTotalDasOpcoes = precoTotalDasOpcoes;
    }
}
