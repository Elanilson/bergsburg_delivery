package com.bergburg.bergburgdelivery.ifood.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Item {
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String nome; // 50 caracteres
    @SerializedName("externalCode")
    private Long idPedidoApp; // id do item
    @SerializedName("quantity")
    private int quantidade;
    @SerializedName("unitPrice")
    private float precoUnitario;
    @SerializedName("price")
    private float precoTotal = (quantidade * precoUnitario);
    @SerializedName("totalPrice")
    private float precoTotalDasOpcoes = 0f;


    public Item(String id, String nome, Long idPedidoApp, int quantidade, float precoUnitario, float precoTotal, float precoTotalDasOpcoes) {
        this.id = id;
        this.nome = nome;
        this.idPedidoApp = idPedidoApp;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
        this.precoTotal = precoTotal;
        this.precoTotalDasOpcoes = precoTotalDasOpcoes;
    }

    public Item() {
    }



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

    public Long getIdPedidoApp() {
        return idPedidoApp;
    }

    public void setIdPedidoApp(Long idPedidoApp) {
        this.idPedidoApp = idPedidoApp;
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
