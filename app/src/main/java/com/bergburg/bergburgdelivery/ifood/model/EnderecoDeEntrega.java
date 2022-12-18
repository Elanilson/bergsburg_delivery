package com.bergburg.bergburgdelivery.ifood.model;

import com.google.gson.annotations.SerializedName;

public class EnderecoDeEntrega {
    @SerializedName("postalCode")
    private String codigo_postal;
    @SerializedName("streetNumber")
    private String numero_da_casa;
    @SerializedName("streetName")
    private String nome_da_rua; // 50 caracteres
    @SerializedName("complement")
    private String complemento; // 50 caracteres
    @SerializedName("reference")
    private String referencia; // 70 caracteres
    @SerializedName("neighborhood")
    private String bairro; // 50 cacacteres
    @SerializedName("city")
    private String cidade; // 2 a 50 caracteres
    @SerializedName("state")
    private String estado;
    @SerializedName("country")
    private String pais; //Duas letras
    @SerializedName("coordinates")
    private Coordenadas coordenadas;


    @Override
    public String toString() {
        return "EnderecoDeEntrega{" +
                "codigo_postal='" + codigo_postal + '\'' +
                ", numero_da_casa='" + numero_da_casa + '\'' +
                ", nome_da_rua='" + nome_da_rua + '\'' +
                ", complemento='" + complemento + '\'' +
                ", referencia='" + referencia + '\'' +
                ", bairro='" + bairro + '\'' +
                ", cidade='" + cidade + '\'' +
                ", estado='" + estado + '\'' +
                ", pais='" + pais + '\'' +
                ", coordenadas=" + coordenadas +
                '}';
    }

    public String getCodigo_postal() {
        return codigo_postal;
    }

    public void setCodigo_postal(String codigo_postal) {
        this.codigo_postal = codigo_postal;
    }

    public String getNumero_da_casa() {
        return numero_da_casa;
    }

    public void setNumero_da_casa(String numero_da_casa) {
        this.numero_da_casa = numero_da_casa;
    }

    public String getNome_da_rua() {
        return nome_da_rua;
    }

    public void setNome_da_rua(String nome_da_rua) {
        this.nome_da_rua = nome_da_rua;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public Coordenadas getCoordenadas() {
        return coordenadas;
    }

    public void setCoordenadas(Coordenadas coordenadas) {
        this.coordenadas = coordenadas;
    }
}
