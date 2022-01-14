package br.edu.ifpe.tads.pdm.projeto.models;

import com.google.android.gms.maps.model.LatLng;

public class CustomLatLng {
    private Double latitude;
    private Double longitude;

    public CustomLatLng() {}

    public CustomLatLng(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
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
