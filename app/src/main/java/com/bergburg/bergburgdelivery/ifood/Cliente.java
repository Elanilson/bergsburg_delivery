package com.bergburg.bergburgdelivery.ifood;

import com.bergburg.bergburgdelivery.ifood.model.Telefone;
import com.google.gson.annotations.SerializedName;

public class Cliente {
    @SerializedName("name")
    private String nome;
    @SerializedName("phone")
    private Telefone telefone;

    @Override
    public String toString() {
        return "Cliente{" +
                "nome='" + nome + '\'' +
                ", telefone=" + telefone +
                '}';
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Telefone getTelefone() {
        return telefone;
    }

    public void setTelefone(Telefone telefone) {
        this.telefone = telefone;
    }
}
