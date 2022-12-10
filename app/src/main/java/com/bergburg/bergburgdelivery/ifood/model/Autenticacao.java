package com.bergburg.bergburgdelivery.ifood.model;

import com.google.gson.annotations.SerializedName;

public class Autenticacao {
    @SerializedName("type")
    private String tipo;
    @SerializedName("accessToken")
    private String tokenDeAcesso;
    @SerializedName("expiresIn")
    private String tempoDeExpiracao;


    @Override
    public String toString() {
        return "Autenticacao{" +
                "tipo='" + tipo + '\'' +
                ", tokenDeAcesso='" + tokenDeAcesso + '\'' +
                ", tempoDeExpiracao='" + tempoDeExpiracao + '\'' +
                '}';
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getTokenDeAcesso() {
        return tokenDeAcesso;
    }

    public void setTokenDeAcesso(String tokenDeAcesso) {
        this.tokenDeAcesso = tokenDeAcesso;
    }

    public String getTempoDeExpiracao() {
        return tempoDeExpiracao;
    }

    public void setTempoDeExpiracao(String tempoDeExpiracao) {
        this.tempoDeExpiracao = tempoDeExpiracao;
    }
}
