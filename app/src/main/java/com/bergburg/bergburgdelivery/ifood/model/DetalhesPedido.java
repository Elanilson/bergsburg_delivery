package com.bergburg.bergburgdelivery.ifood.model;

import com.bergburg.bergburgdelivery.ifood.Cliente;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DetalhesPedido {
    private String id;
    @SerializedName("delivery")
    private Entrega entrega;
    @SerializedName("orderType")
    private String tipoDePedido;
    @SerializedName("orderTiming")
    private String horarioDoPedido;
    @SerializedName("displayId")
    private String iDdeExibição;
    @SerializedName("createdAt")
    private String dataDeCricao;
    @SerializedName("preparationStartDateTime")
    private String dataEhoraDeInicioDaPreparação;
    @SerializedName("isTest")
    private String eTeste;
    @SerializedName("merchant")
    private Loja loja;
    @SerializedName("customer")
    private Cliente cliente;
    @SerializedName("items")
    private List<Item> itens;
    @SerializedName("salesChannel")
    private String canalDeVenda;
    @SerializedName("total")
    private Total total;


    @Override
    public String toString() {
        return "DetalhesPedido{" +
                "id='" + id + '\'' +
                ", entrega=" + entrega +
                ", tipoDePedido='" + tipoDePedido + '\'' +
                ", horarioDoPedido='" + horarioDoPedido + '\'' +
                ", iDdeExibição='" + iDdeExibição + '\'' +
                ", dataDeCricao='" + dataDeCricao + '\'' +
                ", dataEhoraDeInicioDaPreparação='" + dataEhoraDeInicioDaPreparação + '\'' +
                ", eTeste='" + eTeste + '\'' +
                ", loja=" + loja +
                ", cliente=" + cliente +
                ", itens=" + itens +
                ", canalDeVenda='" + canalDeVenda + '\'' +
                ", total=" + total +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Entrega getEntrega() {
        return entrega;
    }

    public void setEntrega(Entrega entrega) {
        this.entrega = entrega;
    }

    public String getTipoDePedido() {
        return tipoDePedido;
    }

    public void setTipoDePedido(String tipoDePedido) {
        this.tipoDePedido = tipoDePedido;
    }

    public String getHorarioDoPedido() {
        return horarioDoPedido;
    }

    public void setHorarioDoPedido(String horarioDoPedido) {
        this.horarioDoPedido = horarioDoPedido;
    }

    public String getiDdeExibição() {
        return iDdeExibição;
    }

    public void setiDdeExibição(String iDdeExibição) {
        this.iDdeExibição = iDdeExibição;
    }

    public String getDataDeCricao() {
        return dataDeCricao;
    }

    public void setDataDeCricao(String dataDeCricao) {
        this.dataDeCricao = dataDeCricao;
    }

    public String getDataEhoraDeInicioDaPreparação() {
        return dataEhoraDeInicioDaPreparação;
    }

    public void setDataEhoraDeInicioDaPreparação(String dataEhoraDeInicioDaPreparação) {
        this.dataEhoraDeInicioDaPreparação = dataEhoraDeInicioDaPreparação;
    }

    public String geteTeste() {
        return eTeste;
    }

    public void seteTeste(String eTeste) {
        this.eTeste = eTeste;
    }

    public Loja getLoja() {
        return loja;
    }

    public void setLoja(Loja loja) {
        this.loja = loja;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<Item> getItens() {
        return itens;
    }

    public void setItens(List<Item> itens) {
        this.itens = itens;
    }

    public String getCanalDeVenda() {
        return canalDeVenda;
    }

    public void setCanalDeVenda(String canalDeVenda) {
        this.canalDeVenda = canalDeVenda;
    }

    public Total getTotal() {
        return total;
    }

    public void setTotal(Total total) {
        this.total = total;
    }
}
