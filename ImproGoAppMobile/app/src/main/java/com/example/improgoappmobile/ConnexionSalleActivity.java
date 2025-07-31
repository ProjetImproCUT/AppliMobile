package com.example.improgoappmobile;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.improgoappmobile.utils.Donnee;
import com.example.improgoappmobile.utils.MyWebSocketClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.util.Map;

public class ConnexionSalleActivity extends AppCompatActivity {

    private TextView messageErreur;
    private EditText nom;
    private EditText pin;
    private EditText adresseIpPort;
    private Button bConfirmer;

    private String nomStr;
    private String pinStr;
    private String addIpPortStr;
    private String addIpStr;
    private String portStr;

    private Donnee donnee;
    private MyWebSocketClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_connexion_salle);

        donnee = Donnee.getInstance();

        messageErreur = findViewById(R.id.t_messErr);
        nom = findViewById(R.id.s_nomUtil);
        pin = findViewById(R.id.s_pin);
        adresseIpPort = findViewById(R.id.s_addIp_port);
        bConfirmer = findViewById(R.id.b_confirmer);

        bConfirmer.setOnClickListener((view) -> {

            nomStr = nom.getText().toString();
            pinStr = pin.getText().toString();
            pinStr = pinStr.replaceAll("\\s", "");

            addIpPortStr = adresseIpPort.getText().toString();
            addIpPortStr = addIpPortStr.replaceAll("\\s", "");

            if (verificationValeur()) {

                try {

                    donnee.initialisation(nomStr, addIpStr, portStr, pinStr);
                    client = donnee.getConnexionWebSocket();

                    if (client.isOpen()) {

                        messageErreur.setText("");
                        messageErreur.setVisibility(View.INVISIBLE);

                        String mess = String.format("{\"commande\":\"demandeDeRejoindre\", " +
                                        "\"utilisateur\":\"%s\", \"pin\":\"%s\", \"addIpPort\":\"%s\"}",
                                nomStr, pinStr, addIpPortStr);
                        client.send(mess);

                        client.setMessageListener((message -> {
                            Gson gson = new Gson();
                            Type type = new TypeToken<Map<String, String>>() {
                            }.getType();
                            Map<String, String> map = gson.fromJson(message, type);

                            String commande = map.get("commande");
                            if (commande != null) {
                                if (commande.equals("demandeAccepter")) {

                                    // TODO Recevoir les données
                                    // TODO Changé d'activité

                                }
                            }
                        }));
                    } else {
                        String messErrConnection = "Il est impossible de se connecter au serveur. " +
                                "Veuillez vérifier l'adresse IP et le numéro de port.";
                        String mess = messageErreur.getText() + messErrConnection;
                        messageErreur.setText(mess);
                        messageErreur.setVisibility(View.VISIBLE);
                    }

                } catch (URISyntaxException | InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private boolean verificationValeur() {

        boolean aErreur = false;
        messageErreur.setText("");

        if (pinStr.length() != 8) {
            String messErrPin = "Le PIN est invalide. ";
            String mess = messageErreur.getText() + messErrPin;
            messageErreur.setText(mess);
            aErreur = true;
        }

        String[] splitAddIpPort = addIpPortStr.split(":");
        if (splitAddIpPort.length == 2) {
            addIpStr = splitAddIpPort[0];

            String messErrAddIP = "L'adresse IP est invalide. ";
            String mess1 = messageErreur.getText() + messErrAddIP;
            String[] splitAddIp = addIpStr.split("\\.");
            if (splitAddIp.length == 4) {
                for (String octetVal : splitAddIp) {
                    if (strEstNombre(octetVal)) {

                        messageErreur.setText(mess1);
                        aErreur = true;
                        break;
                    }
                }
            } else {
                messageErreur.setText(mess1);
                aErreur = true;
            }

            portStr = splitAddIpPort[1];

            if (strEstNombre(portStr)) {
                String messErrPort = "Le numéro de port est invalide. ";
                String mess2 = messageErreur.getText() + messErrPort;
                messageErreur.setText(mess2);
                aErreur = true;
            }
        } else {
            String messErrAddIpPort = "L'adresse IP et le numéro de port sont invalides. ";
            String mess3 = messageErreur.getText() + messErrAddIpPort;
            messageErreur.setText(mess3);
            aErreur = true;
        }


        if (aErreur) {
            String messErrFin = "Veuillez réessayer ...";
            String mess4 = messageErreur.getText() + messErrFin;
            messageErreur.setText(mess4);
            messageErreur.setVisibility(View.VISIBLE);
        } else {
            messageErreur.setVisibility(View.INVISIBLE);
        }

        return !aErreur;

    }

    private boolean strEstNombre(String str) {

        boolean estUnNombre = true;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (!Character.isDigit(c)) {
                estUnNombre = false;
                break;
            }
        }

        return !estUnNombre;
    }
}
