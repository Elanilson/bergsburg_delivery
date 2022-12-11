package com.bergburg.bergburgdelivery.ifood.model;

import com.google.gson.annotations.SerializedName;

public class Error {
    @SerializedName("code")
    private String codigo;
    @SerializedName("field")
    private String campo;
    @SerializedName("message")
    private String mensagem;


    @Override
    public String toString() {
        return "Error{" +
                "codigo='" + codigo + '\'' +
                ", campo='" + campo + '\'' +
                ", mensagem='" + mensagem + '\'' +
                '}';
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
