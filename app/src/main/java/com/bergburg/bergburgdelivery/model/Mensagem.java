package com.bergburg.bergburgdelivery.model;

public class Mensagem {
    private Long id;
    private Long idUsuario;
    private String texto;
    private String visualizado;
    private String data_create;

    @Override
    public String toString() {
        return "Mensagem{" +
                "id=" + id +
                ", idUsuario=" + idUsuario +
                ", texto='" + texto + '\'' +
                ", visualizado='" + visualizado + '\'' +
                ", data_create='" + data_create + '\'' +
                '}';
    }

    public Mensagem() {
    }



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

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getData_create() {
        return data_create;
    }

    public void setData_create(String data_create) {
        this.data_create = data_create;
    }

    public String getVisualizado() {
        return visualizado;
    }

    public void setVisualizado(String visualizado) {
        this.visualizado = visualizado;
    }
}
