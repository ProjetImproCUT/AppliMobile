package com.example.improgoappmobile.utils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class Donnee {

    private static Donnee instance = null;

    private MyWebSocketClient connexionWebSocket;
    private ApiService api;
    private String utilisateur;
    private String pin;
    private InfoEquipe[] equipes;

    private Donnee() {

        utilisateur = "";
        pin = "";
        equipes = new InfoEquipe[2];

        initialiserAPIService();

    }

    public static Donnee getInstance() {
        if (instance == null) {
            instance = new Donnee();
        }
        return instance;
    }

    public static void supprimerInstance() {
        instance = null;
    }

    public void initialisation(String utilisateur, String adresseIP, String port, String pin)
            throws URISyntaxException, InterruptedException {
        this.utilisateur = utilisateur;
        this.pin = pin;
        connexionWebSocket = null;
        connexionWebSocket = new MyWebSocketClient(new URI("ws://" + adresseIP + ":" + port));
        // ^- adresse IP Serveur WebSocket

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<Void> future = executorService.submit(() -> {
            try {
                connexionWebSocket.connectBlocking();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        });

        try {
            future.get(10, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            System.out.println("La tâche a pris trop de temps et n'a pas pu se terminer.");
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown();
        }

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
                .baseUrl("https://improgo-chfshsc9ahc2c3gw.canadacentral-01.azurewebsites.net/") // ← adapte l’URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(ApiService.class);
    }

}
