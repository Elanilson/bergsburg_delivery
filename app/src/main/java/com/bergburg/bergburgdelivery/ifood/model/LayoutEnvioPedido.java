package com.bergburg.bergburgdelivery.ifood.model;

import com.bergburg.bergburgdelivery.ifood.Cliente;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LayoutEnvioPedido {
    @SerializedName("customer")
    private Cliente cliente;
    @SerializedName("delivery")
    private Entrega entrega;
    @SerializedName("items")
    private List<Item> itens;


    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Entrega getEntrega() {
        return entrega;
    }

    public void setEntrega(Entrega entrega) {
        this.entrega = entrega;
    }

    public List<Item> getItens() {
        return itens;
    }

    public void setItens(List<Item> itens) {
        this.itens = itens;
    }
}
