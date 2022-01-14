package br.edu.ifpe.tads.pdm.projeto.models;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Banheiro {
    private String local;
    private String tipo;
    private CustomLatLng customLatLng;

    public Banheiro() {}

    public Banheiro(String local, String tipo) {
        this.local = local;
        this.tipo = tipo;
    }

    public Banheiro(String local, String tipo, CustomLatLng customLatLng) {
        this.local = local;
        this.tipo = tipo;
        this.customLatLng = customLatLng;
    }

    public String getLocal() {
        return local;
    }

    public String getTipo() {
        return tipo;
    }

    public CustomLatLng getCustomLatLng() {
        return customLatLng;
    }

    public void setCustomLatLng(CustomLatLng customLatLng) {
        this.customLatLng = customLatLng;
    }

    public void setLatLng(LatLng latLng) {
        this.customLatLng = new CustomLatLng(latLng.latitude, latLng.longitude);
    }
}
