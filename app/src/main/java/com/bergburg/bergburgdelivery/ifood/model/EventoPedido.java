package com.bergburg.bergburgdelivery.ifood.model;

import com.google.gson.annotations.SerializedName;

public class EventoPedido {
    private String id;
    @SerializedName("code")
    private String codigo;
    @SerializedName("fullCode")
    private String codigoCompleto;
    @SerializedName("orderId")
    private String idDoPedido;
    @SerializedName("merchantId")
    private String idDaLoja;
    @SerializedName("createdAt")
    private String dataDeCriacao;
    @SerializedName("metadata")
    private Metadata metadata;
    @SerializedName("error")
    private Error error;


    @Override
    public String toString() {
        return "EventoPedido{" +
                "id='" + id + '\'' +
                ", codigo='" + codigo + '\'' +
                ", codigoCompleto='" + codigoCompleto + '\'' +
                ", idDoPedido='" + idDoPedido + '\'' +
                ", idDaLoja='" + idDaLoja + '\'' +
                ", dataDeCriacao='" + dataDeCriacao + '\'' +
                ", metadata=" + metadata +
                ", error=" + error +
                '}';
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigoCompleto() {
        return codigoCompleto;
    }

    public void setCodigoCompleto(String codigoCompleto) {
        this.codigoCompleto = codigoCompleto;
    }

    public String getIdDoPedido() {
        return idDoPedido;
    }

    public void setIdDoPedido(String idDoPedido) {
        this.idDoPedido = idDoPedido;
    }

    public String getIdDaLoja() {
        return idDaLoja;
    }

    public void setIdDaLoja(String idDaLoja) {
        this.idDaLoja = idDaLoja;
    }

    public String getDataDeCriacao() {
        return dataDeCriacao;
    }

    public void setDataDeCriacao(String dataDeCriacao) {
        this.dataDeCriacao = dataDeCriacao;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }
}
