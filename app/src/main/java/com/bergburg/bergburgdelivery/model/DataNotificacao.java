package com.bergburg.bergburgdelivery.model;

public class DataNotificacao {
    private Long idUsuario;
    private Long idConversa;


    public DataNotificacao(Long idUsuario, Long idConversa) {
        this.idUsuario = idUsuario;
        this.idConversa = idConversa;
    }

    @Override
    public String toString() {
        return "DataNotificacao{" +
                "idUsuario='" + idUsuario + '\'' +
                ", idConversa='" + idConversa + '\'' +
                '}';
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Long getIdConversa() {
        return idConversa;
    }

    public void setIdConversa(Long idConversa) {
        this.idConversa = idConversa;
    }
}
