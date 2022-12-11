package com.bergburg.bergburgdelivery.ifood.model;

import com.google.gson.annotations.SerializedName;

public class Citar {
    @SerializedName("grossValue")
    private float valorBruto;
    @SerializedName("discount")
    private float desconto;
    @SerializedName("netValue")
    private float valorLiquido;

    @Override
    public String toString() {
        return "Citar{" +
                "valorBruto=" + valorBruto +
                ", desconto=" + desconto +
                ", valorLiquido=" + valorLiquido +
                '}';
    }

    public float getValorBruto() {
        return valorBruto;
    }

    public void setValorBruto(float valorBruto) {
        this.valorBruto = valorBruto;
    }

    public float getDesconto() {
        return desconto;
    }

    public void setDesconto(float desconto) {
        this.desconto = desconto;
    }

    public float getValorLiquido() {
        return valorLiquido;
    }

    public void setValorLiquido(float valorLiquido) {
        this.valorLiquido = valorLiquido;
    }
}
