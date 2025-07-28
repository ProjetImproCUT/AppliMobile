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

public class ConnexionSalleActivity extends AppCompatActivity {

    private TextView messageErreur;
    private EditText nom;
    private EditText pin;
    private EditText adresseIpPort;
    private Button bConfirmer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_connexion_salle);

        messageErreur = findViewById(R.id.t_messErr);
        nom = findViewById(R.id.s_nomUtil);
        pin = findViewById(R.id.s_pin);
        adresseIpPort = findViewById(R.id.s_addIp_port);
        bConfirmer = findViewById(R.id.b_confirmer);

        bConfirmer.setOnClickListener((view) -> {
            if (verificationValeur()) {
                // Envoyer au serveur
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

        String pinVal = pin.getText().toString();
        pinVal = pinVal.replaceAll("\\s", "");
        if (pinVal.length() != 8 | strEstNombre(pinVal)) {
            aErreur = true;
        }

        String addIpPortVal = adresseIpPort.getText().toString();
        addIpPortVal = addIpPortVal.replaceAll("\\s", "");
        String[] splitAddIpPort = addIpPortVal.split(":");
        if (splitAddIpPort.length == 2) {
            String addIp = splitAddIpPort[0];

            String[] splitAddIp = addIp.split("\\.");
            if (splitAddIp.length == 4) {
                for (String octetVal : splitAddIp) {
                    if (strEstNombre(octetVal)) {
                        aErreur = true;
                        break;
                    }
                }
            } else {
                aErreur = true;
            }

            String port = splitAddIpPort[1];

            if (strEstNombre(port)) {
                aErreur = true;
            }
        } else {
            aErreur = true;
        }

        if (aErreur) {
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
