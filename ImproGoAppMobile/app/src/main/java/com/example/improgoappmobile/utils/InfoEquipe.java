package com.example.improgoappmobile.utils;

public class InfoEquipe {

    private String nom;
    private String couleur;
    private String lienLogo;

    public InfoEquipe(String nom, String couleur, String lienLogo) {
        this.nom = nom;
        this.couleur = couleur;
        this.lienLogo = lienLogo;
    }

    public String getNom() {
        return nom;
    }

    public String getCouleur() {
        return couleur;
    }

    public String getLienLogo() {
        return lienLogo;
    }

}
