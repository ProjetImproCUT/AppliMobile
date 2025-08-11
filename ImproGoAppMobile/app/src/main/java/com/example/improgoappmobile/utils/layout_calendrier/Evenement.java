package com.example.improgoappmobile.utils.layout_calendrier;

public class Evenement {

    private String date;
    private String lieu;
    private String heure;
    private String[] equipes;

    public Evenement(String date, String lieu, String heure, String[] equipes) {
        this.date = date;
        this.lieu = lieu;
        this.heure = heure;
        this.equipes = equipes;
    }

    public String getDate() {
        return date;
    }

    public String getLieu() {
        return lieu;
    }

    public String getHeure() {
        return heure;
    }

    public String[] getEquipes() {
        return equipes;
    }
}
