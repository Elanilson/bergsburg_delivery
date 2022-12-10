package com.bergburg.bergburgdelivery.ifood.model;

import com.google.gson.annotations.SerializedName;

public class Entrega {
    @SerializedName("merchantFee")
    private Float taxa_do_comerciante = 0f;
    @SerializedName("deliveryAddress")
    private EnderecoDeEntrega enderecoDeEntrega;

    public Float getTaxa_do_comerciante() {
        return taxa_do_comerciante;
    }

    public void setTaxa_do_comerciante(Float taxa_do_comerciante) {
        this.taxa_do_comerciante = taxa_do_comerciante;
    }

    public EnderecoDeEntrega getEnderecoDeEntrega() {
        return enderecoDeEntrega;
    }

    public void setEnderecoDeEntrega(EnderecoDeEntrega enderecoDeEntrega) {
        this.enderecoDeEntrega = enderecoDeEntrega;
    }
}
