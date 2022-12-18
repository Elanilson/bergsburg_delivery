package com.bergburg.bergburgdelivery.ifood.model;

import com.google.gson.annotations.SerializedName;

public class Loja {
    @SerializedName("id")
    private String id;
    @SerializedName("nome")
    private String nome;

    @Override
    public String toString() {
        return "Loja{" +
                "id='" + id + '\'' +
                ", nome='" + nome + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
