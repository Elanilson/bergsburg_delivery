package com.bergburg.bergburgdelivery.model;

import com.google.gson.annotations.SerializedName;

public class Resposta {
    @SerializedName("mensagem")
    private String mensagem;
    @SerializedName("status")
    private Boolean status = false;

    public Resposta() {
    }

    public Resposta(String mensagem, Boolean status) {
        this.mensagem = mensagem;
        this.status = status;
    }

    public Resposta(String mensagem) {
        this.mensagem = mensagem;
    }

    public Resposta(Boolean status) {
        this.status = status;
    }

    public String getMensagem() {
        return mensagem;
    }


    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
