package com.bergburg.bergburgdelivery.ifood.model;

import com.google.gson.annotations.SerializedName;

public class RespostaDisponibilidadeDeEntrega {
    @SerializedName("quote")
    private Citar citar;
    @SerializedName("deliveryTime")
    private TempoDeEntrega tempoDeEntrega;
    @SerializedName("distance")
    private int distancia;

    @Override
    public String toString() {
        return "RespostaDisponibilidadeDeEntrega{" +
                "citar=" + citar +
                ", tempoDeEntrega=" + tempoDeEntrega +
                ", distancia=" + distancia +
                '}';
    }

    public Citar getCitar() {
        return citar;
    }

    public void setCitar(Citar citar) {
        this.citar = citar;
    }

    public TempoDeEntrega getTempoDeEntrega() {
        return tempoDeEntrega;
    }

    public void setTempoDeEntrega(TempoDeEntrega tempoDeEntrega) {
        this.tempoDeEntrega = tempoDeEntrega;
    }

    public int getDistancia() {
        return distancia;
    }

    public void setDistancia(int distancia) {
        this.distancia = distancia;
    }
}
