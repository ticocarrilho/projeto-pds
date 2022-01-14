package br.edu.ifpe.tads.pdm.projeto.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Usuario {
    private String name;
    private String email;

    public Usuario() {}

    public Usuario(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
