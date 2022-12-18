package com.bergburg.bergburgdelivery.ifood.model;

public class Coordenadas {
    private Double latitude;
    private Double longitude;

    @Override
    public String toString() {
        return "Coordenadas{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
