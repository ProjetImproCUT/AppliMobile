package com.example.improgoappmobile.utils.layout_interprete;

import com.example.improgoappmobile.R;

public class Interprete {

    private String nom;
    private String prenom;
    private int image;

    Interprete() {
        this.nom = " ";
        this.prenom = " ";
        this.image = R.drawable.none;
    }

    public Interprete(String nom, String prenom, int image) {
        this.nom = nom;
        this.prenom = prenom;
        this.image = image;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public int getImage() {
        return image;
    }

}
