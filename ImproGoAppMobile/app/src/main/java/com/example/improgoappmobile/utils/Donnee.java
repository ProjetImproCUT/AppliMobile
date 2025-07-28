package com.example.improgoappmobile.utils;

import java.net.URI;
import java.net.URISyntaxException;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class Donnee {

    private static Donnee instance = null;

    private MyWebSocketClient connexionWebSocket;
    private ApiService api;
    private String utilisateur;
    private String pin;
    private InfoEquipe[] equipes;

    private Donnee(String utilisateur, String adresseIP, String port, String pin) throws URISyntaxException {
        this.utilisateur = utilisateur;
        this.pin = pin;
        equipes = new InfoEquipe[2];
        connexionWebSocket = new MyWebSocketClient(new URI("ws://" + adresseIP + ":" + port));
                                                                // ^- adresse IP Serveur WebSocket
        connexionWebSocket.connect();

        initialiserAPIService();
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

    public void ajouterEquipe(String nom, String couleur, String lienLogo) {
        for (int i = 0; i < equipes.length; i++) {
            if (equipes[i] == null) {
                equipes[i] = new InfoEquipe(nom, couleur, lienLogo);
                break;
            }
        }
    }

    public ApiService getApi() {
        return api;
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

    public InfoEquipe getEquipe1() {
        return equipes[0];
    }

    public InfoEquipe getEquipe2() {
        return equipes[1];
    }

    private void initialiserAPIService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://voteimpro-afg6c3atf4a6cwcr.canadacentral-01.azurewebsites.net/") // ← adapte l’URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(ApiService.class);
    }

}
