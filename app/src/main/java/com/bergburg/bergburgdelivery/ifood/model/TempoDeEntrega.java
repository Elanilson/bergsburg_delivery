package com.bergburg.bergburgdelivery.ifood.model;

public class TempoDeEntrega {
    private int minimo;
    private int maximo;


    @Override
    public String toString() {
        return "TempoDeEntrega{" +
                "minimo=" + minimo +
                ", maximo=" + maximo +
                '}';
    }

    public int getMinimo() {
        return minimo;
    }

    public void setMinimo(int minimo) {
        this.minimo = minimo;
    }

    public int getMaximo() {
        return maximo;
    }

    public void setMaximo(int maximo) {
        this.maximo = maximo;
    }
}
