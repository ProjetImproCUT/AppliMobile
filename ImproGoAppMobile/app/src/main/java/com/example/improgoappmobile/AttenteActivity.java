package com.example.improgoappmobile;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.improgoappmobile.utils.Donnee;
import com.example.improgoappmobile.utils.InfoServeur;
import com.example.improgoappmobile.utils.MyWebSocketClient;
import com.google.gson.Gson;

public class AttenteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_attente);

        try {

            Donnee instance = Donnee.getInstance();
            MyWebSocketClient client = instance.getConnexionWebSocket();

            //URI uri = new URI("ws://10.192.170.150:8082");
            //client = new MyWebSocketClient(uri);
            client.setMessageListener((message) -> {

                Gson gson = new Gson();
                InfoServeur infoServeur = gson.fromJson(message, InfoServeur.class);

                String commande = infoServeur.getCommande();
                int jouteRendu = infoServeur.getJouteRendu();
                int numeroMatch = infoServeur.getNumeroMatch();

                if (commande.equals("demandeAuVote")) {

                    Intent intent = new Intent(this, ChoixVoteEquipeActivity.class);
                    intent.putExtra("jouteRendu", jouteRendu);
                    intent.putExtra("numeroMatch", numeroMatch);
                    startActivity(intent);

                } else if (commande.equals("")) { // TODO À suivre ...
                    //
                }

            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.attente_page), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

}