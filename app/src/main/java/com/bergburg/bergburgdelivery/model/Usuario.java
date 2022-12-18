package com.bergburg.bergburgdelivery.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import retrofit2.http.Field;


public class Usuario implements Serializable {
    private Long id;
    private Long idSacola;
    private Long idConversa;
    private String login;
    private String nome;
    private String senha;
    private String email;
    private int ddd;
    private String telefone;
    private String status;
    private String contaAtiva;

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", idSacola=" + idSacola +
                ", idConversa=" + idConversa +
                ", login='" + login + '\'' +
                ", nome='" + nome + '\'' +
                ", senha='" + senha + '\'' +
                ", email='" + email + '\'' +
                ", ddd=" + ddd +
                ", telefone='" + telefone + '\'' +
                ", status='" + status + '\'' +
                ", contaAtiva='" + contaAtiva + '\'' +
                '}';
    }

    public Long getIdConversa() {
        return idConversa;
    }

    public void setIdConversa(Long idConversa) {
        this.idConversa = idConversa;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdSacola() {
        return idSacola;
    }

    public void setIdSacola(Long idSacola) {
        this.idSacola = idSacola;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public int getDdd() {
        return ddd;
    }

    public void setDdd(int ddd) {
        this.ddd = ddd;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContaAtiva() {
        return contaAtiva;
    }

    public void setContaAtiva(String contaAtiva) {
        this.contaAtiva = contaAtiva;
    }
}
