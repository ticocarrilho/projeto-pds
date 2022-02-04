package br.edu.ifpe.tads.pdm.projeto.models;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Banheiro {
    private String local;
    private String tipo;
    private String preco;
    private float avaliacao;
    private int qntAvalicoes;
    private boolean fraldario;
    private CustomLatLng customLatLng;

    public Banheiro() {}

    public Banheiro(String local, String tipo, String preco, float avaliacao, int qntAvalicoes, boolean fraldario) {
        this.local = local;
        this.tipo = tipo;
        this.preco = preco;
        this.avaliacao = avaliacao;
        this.qntAvalicoes = qntAvalicoes;
        this.fraldario = fraldario;
    }

    public Banheiro(String local, String tipo, String preco, float avaliacao, int qntAvalicoes, boolean fraldario, CustomLatLng customLatLng) {
        this.local = local;
        this.tipo = tipo;
        this.preco = preco;
        this.avaliacao = avaliacao;
        this.qntAvalicoes = qntAvalicoes;
        this.fraldario = fraldario;
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

    public String getPreco() {
        return preco;
    }

    public boolean isFraldario() {
        return fraldario;
    }

    public float getAvaliacao() {
        return avaliacao;
    }

    public int getQntAvalicoes() {
        return qntAvalicoes;
    }
}
