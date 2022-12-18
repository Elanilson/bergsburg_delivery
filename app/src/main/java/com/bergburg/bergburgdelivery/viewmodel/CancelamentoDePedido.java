package com.bergburg.bergburgdelivery.viewmodel;

import com.google.gson.annotations.SerializedName;

public class CancelamentoDePedido {
    @SerializedName("reason")
    private String motivo;
    @SerializedName("cancellationCode")
    private int codigoDeCancelamento;
//https://developer.ifood.com.br/pt-BR/docs/guides/order/workflow/#cancelado-pela-loja

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public int getCodigoDeCancelamento() {
        return codigoDeCancelamento;
    }

    public void setCodigoDeCancelamento(int codigoDeCancelamento) {
        this.codigoDeCancelamento = codigoDeCancelamento;
    }
}
