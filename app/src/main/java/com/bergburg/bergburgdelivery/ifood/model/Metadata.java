package com.bergburg.bergburgdelivery.ifood.model;

import com.google.gson.annotations.SerializedName;

public class Metadata {
    @SerializedName("CANCEL_STAGE")
    private String cancelarEstagio;
    @SerializedName("ORIGIN")
    private String origem;
    @SerializedName("CANCEL_CODE")
    private String codigoDeCancelamento;
    @SerializedName("TIMEOUT_EVENT")
    private String tempoEsgotadoDoEvento;
    @SerializedName("CANCEL_ORIGIN")
    private String origemDoCancelamento;
    @SerializedName("CANCEL_USER")
    private String cancelarUsuario;
    @SerializedName("CANCEL_REASON")
    private String motivoDoCancelamento;
    @SerializedName("CANCELLATION_REQUESTED_EVENT_ID")
    private String idDoEnventoSolicitadoDeCancelamento;


    @Override
    public String toString() {
        return "Metadata{" +
                "cancelarEstagio='" + cancelarEstagio + '\'' +
                ", origem='" + origem + '\'' +
                ", codigoDeCancelamento='" + codigoDeCancelamento + '\'' +
                ", tempoEsgotadoDoEvento='" + tempoEsgotadoDoEvento + '\'' +
                ", origemDoCancelamento='" + origemDoCancelamento + '\'' +
                ", cancelarUsuario='" + cancelarUsuario + '\'' +
                ", motivoDoCancelamento='" + motivoDoCancelamento + '\'' +
                ", idDoEnventoSolicitadoDeCancelamento='" + idDoEnventoSolicitadoDeCancelamento + '\'' +
                '}';
    }

    public String getCancelarEstagio() {
        return cancelarEstagio;
    }

    public void setCancelarEstagio(String cancelarEstagio) {
        this.cancelarEstagio = cancelarEstagio;
    }

    public String getOrigem() {
        return origem;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }

    public String getCodigoDeCancelamento() {
        return codigoDeCancelamento;
    }

    public void setCodigoDeCancelamento(String codigoDeCancelamento) {
        this.codigoDeCancelamento = codigoDeCancelamento;
    }

    public String getTempoEsgotadoDoEvento() {
        return tempoEsgotadoDoEvento;
    }

    public void setTempoEsgotadoDoEvento(String tempoEsgotadoDoEvento) {
        this.tempoEsgotadoDoEvento = tempoEsgotadoDoEvento;
    }

    public String getOrigemDoCancelamento() {
        return origemDoCancelamento;
    }

    public void setOrigemDoCancelamento(String origemDoCancelamento) {
        this.origemDoCancelamento = origemDoCancelamento;
    }

    public String getCancelarUsuario() {
        return cancelarUsuario;
    }

    public void setCancelarUsuario(String cancelarUsuario) {
        this.cancelarUsuario = cancelarUsuario;
    }

    public String getMotivoDoCancelamento() {
        return motivoDoCancelamento;
    }

    public void setMotivoDoCancelamento(String motivoDoCancelamento) {
        this.motivoDoCancelamento = motivoDoCancelamento;
    }

    public String getIdDoEnventoSolicitadoDeCancelamento() {
        return idDoEnventoSolicitadoDeCancelamento;
    }

    public void setIdDoEnventoSolicitadoDeCancelamento(String idDoEnventoSolicitadoDeCancelamento) {
        this.idDoEnventoSolicitadoDeCancelamento = idDoEnventoSolicitadoDeCancelamento;
    }
}
