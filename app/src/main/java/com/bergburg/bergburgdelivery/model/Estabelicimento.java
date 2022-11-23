package com.bergburg.bergburgdelivery.model;

public class Estabelicimento {
    private Long id ;
   private String nome ;
   private String ramo ;
   private String endereco ;
   private String tempoEntrega ;
   private Float valorMinimo ;
   private String status;
   private String telefone;

    @Override
    public String toString() {
        return "Estabelicimento{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", ramo='" + ramo + '\'' +
                ", endereco='" + endereco + '\'' +
                ", tempoEntrega='" + tempoEntrega + '\'' +
                ", valorMinimo=" + valorMinimo +
                ", status='" + status + '\'' +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getRamo() {
        return ramo;
    }

    public void setRamo(String ramo) {
        this.ramo = ramo;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getTempoEntrega() {
        return tempoEntrega;
    }

    public void setTempoEntrega(String tempoEntrega) {
        this.tempoEntrega = tempoEntrega;
    }

    public Float getValorMinimo() {
        return valorMinimo;
    }

    public void setValorMinimo(Float valorMinimo) {
        this.valorMinimo = valorMinimo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
}


