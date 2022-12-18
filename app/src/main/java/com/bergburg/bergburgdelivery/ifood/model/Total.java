package com.bergburg.bergburgdelivery.ifood.model;

import com.google.gson.annotations.SerializedName;

public class Total {
    @SerializedName("subTotal")
    private  String subTotal;
    @SerializedName("deliveryFee")
    private  String taxaDeEntrega;
    @SerializedName("benefits")
    private  String beneficios;
    @SerializedName("orderAmount")
    private  String valorDoPedido;
    @SerializedName("additionalFees")
    private  String taxasAdicionais;

    @Override
    public String toString() {
        return "Total{" +
                "subTotal='" + subTotal + '\'' +
                ", taxaDeEntrega='" + taxaDeEntrega + '\'' +
                ", beneficios='" + beneficios + '\'' +
                ", valorDoPedido='" + valorDoPedido + '\'' +
                ", taxasAdicionais='" + taxasAdicionais + '\'' +
                '}';
    }

    public String getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(String subTotal) {
        this.subTotal = subTotal;
    }

    public String getTaxaDeEntrega() {
        return taxaDeEntrega;
    }

    public void setTaxaDeEntrega(String taxaDeEntrega) {
        this.taxaDeEntrega = taxaDeEntrega;
    }

    public String getBeneficios() {
        return beneficios;
    }

    public void setBeneficios(String beneficios) {
        this.beneficios = beneficios;
    }

    public String getValorDoPedido() {
        return valorDoPedido;
    }

    public void setValorDoPedido(String valorDoPedido) {
        this.valorDoPedido = valorDoPedido;
    }

    public String getTaxasAdicionais() {
        return taxasAdicionais;
    }

    public void setTaxasAdicionais(String taxasAdicionais) {
        this.taxasAdicionais = taxasAdicionais;
    }
}
