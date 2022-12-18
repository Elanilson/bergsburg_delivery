package com.bergburg.bergburgdelivery.model;

import com.bergburg.bergburgdelivery.ifood.model.EventoPedido;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Dados {
    @SerializedName("error")
    private String error = "";
    @SerializedName("idEvento")
    private String idEvento = "";
    @SerializedName("status")
    private Boolean status = false;
    @SerializedName("enviado")
    private Boolean enviado = false;
    @SerializedName("mensagem")
    private Mensagem mensagem;
    @SerializedName("usuario")
    private Usuario usuario;
    @SerializedName("token")
    private Token token;
    @SerializedName("estabelicimento")
    private Estabelicimento estabelicimento;
    @SerializedName("categorias")
    private List<Categoria> categorias;
    @SerializedName("entradas")
    private List<Produto> entradas;
    @SerializedName("produtos")
    private List<Produto> produtos;
    @SerializedName("produto")
    private Produto produto;
    @SerializedName("pedido")
    private Pedido pedido;
    @SerializedName("endereco")
    private Endereco endereco;
    @SerializedName("pedidos")
    private List<Pedido> pedidos;
    @SerializedName("ItensPedido")
    private List<ItensPedido> itensPedido;
    @SerializedName("itensSacola")
    private List<ItensSacola> itensSacola;
    @SerializedName("status_pedido")
    private List<Status_pedido> status_pedido;
    @SerializedName("mensagens")
    private List<Mensagem> Mensagens;
    @SerializedName("conversas")
    private List<Conversas> conversas;
    @SerializedName("eventos")
    private List<EventoPedido> eventos;


    public String getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(String idEvento) {
        this.idEvento = idEvento;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<Categoria> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<Categoria> categorias) {
        this.categorias = categorias;
    }

    public List<Produto> getEntradas() {
        return entradas;
    }

    public void setEntradas(List<Produto> entradas) {
        this.entradas = entradas;
    }

    public List<Produto> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<Produto> produtos) {
        this.produtos = produtos;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public List<ItensSacola> getItensSacola() {
        return itensSacola;
    }

    public void setItensSacola(List<ItensSacola> itensSacola) {
        this.itensSacola = itensSacola;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }


    public List<Pedido> getPedidos() {
        return pedidos;
    }

    public void setPedidos(List<Pedido> pedidos) {
        this.pedidos = pedidos;
    }

    public List<ItensPedido> getItensPedido() {
        return itensPedido;
    }

    public void setItensPedido(List<ItensPedido> itensPedido) {
        this.itensPedido = itensPedido;
    }

    public List<Status_pedido> getStatus_pedido() {
        return status_pedido;
    }

    public void setStatus_pedido(List<Status_pedido> status_pedido) {
        this.status_pedido = status_pedido;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public Estabelicimento getEstabelicimento() {
        return estabelicimento;
    }

    public void setEstabelicimento(Estabelicimento estabelicimento) {
        this.estabelicimento = estabelicimento;
    }

    public Boolean getEnviado() {
        return enviado;
    }

    public void setEnviado(Boolean enviado) {
        this.enviado = enviado;
    }

    public List<Mensagem> getMensagens() {
        return Mensagens;
    }

    public void setMensagens(List<Mensagem> mensagens) {
        Mensagens = mensagens;
    }

    public List<Conversas> getConversas() {
        return conversas;
    }

    public void setConversas(List<Conversas> conversas) {
        this.conversas = conversas;
    }


    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public Mensagem getMensagem() {
        return mensagem;
    }

    public void setMensagem(Mensagem mensagem) {
        this.mensagem = mensagem;
    }

    public List<EventoPedido> getEventos() {
        return eventos;
    }

    public void setEventos(List<EventoPedido> eventos) {
        this.eventos = eventos;
    }
}
