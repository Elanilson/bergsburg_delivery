package com.bergburg.bergburgdelivery.ifood.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Error {
    @SerializedName("code")
    private String codigo;
    @SerializedName("field")
    private String campo;
    @SerializedName("message")
    private String mensagem;
    @SerializedName("details")
    private List<String> details;


    @Override
    public String toString() {
        return "Error{" +
                "codigo='" + codigo + '\'' +
                ", campo='" + campo + '\'' +
                ", mensagem='" + mensagem + '\'' +
                '}';
    }

    public List<String> getDetails() {
        return details;
    }

    public void setDetails(List<String> details) {
        this.details = details;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCampo() {
        return campo;
    }

    public void setCampo(String campo) {
        this.campo = campo;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}
