package com.bergburg.bergburgdelivery.ifood.model;

import com.google.gson.annotations.SerializedName;

public class Telefone {
    @SerializedName("countryCode")
    private String codigoDoPais;
    @SerializedName("areaCode")
    private String codigoDeArea;
    @SerializedName("number")
    private String numero;

    @Override
    public String toString() {
        return "Telefone{" +
                "codigoDoPais='" + codigoDoPais + '\'' +
                ", codigoDeArea='" + codigoDeArea + '\'' +
                ", numero='" + numero + '\'' +
                '}';
    }

    public String getCodigoDoPais() {
        return codigoDoPais;
    }

    public void setCodigoDoPais(String codigoDoPais) {
        this.codigoDoPais = codigoDoPais;
    }

    public String getCodigoDeArea() {
        return codigoDeArea;
    }

    public void setCodigoDeArea(String codigoDeArea) {
        this.codigoDeArea = codigoDeArea;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }
}
