package com.bergburg.bergburgdelivery.listeners;

public interface OnListenerIfood<T> {
    void criarPedido(T ifood);
    void confirmarPedido(T ifood);
    void cancelarPedido(T ifood);
    void aceitarCancelamento(T ifood);
    void negarCancelamento(T ifood);
    void verificarFrete(T ifood);
}
