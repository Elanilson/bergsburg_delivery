package com.bergburg.bergburgdelivery.model;

public class Conversas{
    private Long id;
    private Long idUsuario;
    private Long idReceptor;
    private String nome;
    private String data_create;




    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Long getIdReceptor() {
        return idReceptor;
    }

    public void setIdReceptor(Long idReceptor) {
        this.idReceptor = idReceptor;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getData_create() {
        return data_create;
    }

    public void setData_create(String data_create) {
        this.data_create = data_create;
    }
}
