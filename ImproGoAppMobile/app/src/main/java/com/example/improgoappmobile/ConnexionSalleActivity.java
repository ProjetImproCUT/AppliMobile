package com.example.improgoappmobile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

    // --- Fixed dev server for Android emulator ---
    private static final String SERVER_HOST = "10.0.2.2"; // host machine from emulator
    private static final int SERVER_PORT = 3000;

    private TextView messageErreur;
    private EditText nom;
    private EditText pin;
    private EditText adresseIpPort; // kept to show the fixed value (read-only)
    private Button bConfirmer;

    private String nomStr;
    private String pinStr;

    private String addIpStr = SERVER_HOST;
    private String portStr = String.valueOf(SERVER_PORT);
    private String addIpPortStr = SERVER_HOST + ":" + SERVER_PORT;

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

        // Show fixed address and lock the field
        adresseIpPort.setText(addIpPortStr);
        adresseIpPort.setEnabled(false);

        bConfirmer.setOnClickListener(view -> {
            nomStr = nom.getText().toString().trim();
            pinStr = pin.getText().toString().replaceAll("\\s", "");

            if (verificationValeur()) {
                Toast.makeText(this, "Veuillez patienter un petit moment ...", Toast.LENGTH_SHORT).show();

                try {
                    // Initialise with fixed host/port
                    donnee.initialisation(nomStr, addIpStr, portStr, pinStr);

                    // Pré-charger 2 équipes (comme avant)
                    donnee.ajouterEquipe("Team A", "FFDE59", "");
                    donnee.ajouterEquipe("Team B", "0CC0DF", "");

                    client = donnee.getConnexionWebSocket();

                    if (client.isOpen()) {
                        messageErreur.setText("");
                        messageErreur.setVisibility(TextView.INVISIBLE);

                        String mess = String.format(
                                "{\"commande\":\"demandeDeRejoindre\",\"utilisateur\":\"%s\",\"pin\":\"%s\",\"addIpPort\":\"%s\"}",
                                nomStr, pinStr, addIpPortStr
                        );
                        client.send(mess);

                        startActivity(new Intent(this, AttenteActivity.class));

                        // Listen to server messages
                        client.setMessageListener(message -> {
                            Gson gson = new Gson();
                            Type type = new TypeToken<Map<String, String>>(){}.getType();
                            Map<String, String> map = gson.fromJson(message, type);

                            String commande = map.get("commande");
                            if ("demandeAccepter".equals(commande)) {
                                // TODO: handle success (charger données / changer d'activité)
                            }
                        });

                    } else {
                        appendErr("Il est impossible de se connecter au serveur. " +
                                "Le serveur doit être accessible sur 10.0.2.2:3000.");
                    }

                } catch (URISyntaxException | InterruptedException e) {
                    e.printStackTrace();
                    appendErr("Erreur de connexion.");
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
        messageErreur.setText("");
        boolean aErreur = false;

        // PIN: 8 digits (adjust if your rule is different)
        if (pinStr.length() != 8 || !isNumeric(pinStr)) {
            appendErr("Le PIN est invalide. ");
            aErreur = true;
        }

        if (aErreur) {
            appendErr("Veuillez réessayer ...");
            messageErreur.setVisibility(TextView.VISIBLE);
        } else {
            messageErreur.setVisibility(TextView.INVISIBLE);
        }
        return !aErreur;
    }

    private void appendErr(String s) {
        messageErreur.setText(messageErreur.getText().toString() + s);
    }

    private static boolean isNumeric(String s) {
        if (s == null || s.isEmpty()) return false;
        for (int i = 0; i < s.length(); i++) {
            if (!Character.isDigit(s.charAt(i))) return false;
        }
        return true;
    }
}
