package com.example.improgoappmobile.utils;

import java.net.URI;
import java.net.URISyntaxException;

public final class Donnee {

    private static Donnee instance = null;

    private MyWebSocketClient connexionWebSocket;
    private String utilisateur;
    private String pin;
    private String couleurEqu1;
    private String couleurEqu2;
    // lien image équipe 1 ?
    // lien image équipe 2 ?

    private Donnee(String utilisateur, String adresseIP, String port, String pin) throws URISyntaxException {
        this.utilisateur = utilisateur;
        this.pin = pin;
        couleurEqu1 = "";
        couleurEqu2 = "";
        connexionWebSocket = new MyWebSocketClient(new URI("ws://" + adresseIP + ":" + port));
                                                                // ^- adresse IP Serveur WebSocket
        connexionWebSocket.connect();
    }

    public static void setInstance(String utilisateur, String adresseIP, String port, String pin)
            throws URISyntaxException {
        if (instance == null) {
            instance = new Donnee(utilisateur, adresseIP, port, pin);
        }
    }

    public static Donnee getInstance() {
        if (instance != null) {
            return instance;
        }
        return null;
    }

    public static void supprimerInstance() {
        instance = null;
    }

    public void setCouleurEquipe(String couleurEqu1, String couleurEqu2) {
        if (couleurEqu1.isEmpty() && couleurEqu2.isEmpty()) {
            this.couleurEqu1 = couleurEqu1;
            this.couleurEqu2 = couleurEqu2;
        }
    }

    public MyWebSocketClient getConnexionWebSocket() {
        return connexionWebSocket;
    }

    public String getUtilisateur() {
        return utilisateur;
    }

    public String getPin() {
        return pin;
    }

    public String getCouleurEqu1() {
        return couleurEqu1;
    }

    public String getCouleurEqu2() {
        return couleurEqu2;
    }

}
