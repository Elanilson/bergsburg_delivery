package com.bergburg.bergburgdelivery.model;

public class Pedido {
    private Long id;
    private Long idUsuario;
    private Float total;
    private Float taxa_entrega;
    private Float subtotal;
    private String status;
    private String data_status;
    private String data_pedido;

    @Override
    public String toString() {
        return "Pedido{" +
                "id=" + id +
                ", idUsuario=" + idUsuario +
                ", total=" + total +
                ", taxa_entrega=" + taxa_entrega +
                ", subtotal=" + subtotal +
                ", status='" + status + '\'' +
                ", data_status='" + data_status + '\'' +
                ", data_pedido='" + data_pedido + '\'' +
                '}';
    }

    public Pedido() {
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

    public Float getTotal() {
        return total;
    }

    public void setTotal(Float total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getData_pedido() {
        return data_pedido;
    }

    public void setData_pedido(String data_pedido) {
        this.data_pedido = data_pedido;
    }

    public String getData_status() {
        return data_status;
    }

    public void setData_status(String data_status) {
        this.data_status = data_status;
    }

    public Float getTaxa_entrega() {
        return taxa_entrega;
    }

    public void setTaxa_entrega(Float taxa_entrega) {
        this.taxa_entrega = taxa_entrega;
    }

    public Float getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Float subtotal) {
        this.subtotal = subtotal;
    }
}
