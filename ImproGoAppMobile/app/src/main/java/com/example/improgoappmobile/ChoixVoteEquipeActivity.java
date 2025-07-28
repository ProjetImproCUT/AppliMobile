package com.example.improgoappmobile;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.improgoappmobile.utils.ApiService;
import com.example.improgoappmobile.utils.Donnee;
import com.example.improgoappmobile.utils.MyWebSocketClient;
import com.example.improgoappmobile.utils.VoteRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChoixVoteEquipeActivity extends AppCompatActivity {

    private View view;
    private ImageButton btnEquipe1;
    private ImageButton btnEquipe2;

    private Donnee donnee;
    private MyWebSocketClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_choix_vote_equipe);

        donnee = Donnee.getInstance();
        client = donnee.getConnexionWebSocket();

        Intent intent = getIntent();
        int jouteRendu = intent.getIntExtra("jouteRendu", 0);
        int numeroMatch = intent.getIntExtra("numeroMatch", 0);

        view = findViewById(R.id.CEE_page);

        // Couleur équipe
        // jaune : FFDE59  bleu : 0CC0DF
        // vert : 7FD858   mauve : CB6CE4

        // Fait le dégradé dans le fond du View
        createGradientBackground();

        Intent intentVersAttente = new Intent(this, AttenteActivity.class);

        btnEquipe1 = findViewById(R.id.b_equ1);
        btnEquipe1.setImageResource(R.drawable.logojaune);
        btnEquipe1.setOnClickListener((view) -> {
            envoyerVote(numeroMatch, jouteRendu, donnee.getEquipe1().getNom());
            startActivity(intentVersAttente);
        });

        btnEquipe2 = findViewById(R.id.b_equ2);
        btnEquipe2.setImageResource(R.drawable.logobleu);
        btnEquipe2.setOnClickListener((view) -> {
            envoyerVote(numeroMatch, jouteRendu, donnee.getEquipe2().getNom());
            startActivity(intentVersAttente);
        });

        setBackgroundOfImageButton();

        client.setMessageListener((message) -> {

            Gson gson = new Gson();
            Type type = new TypeToken<Map<String, String>>() {}.getType();
            Map<String, String> map = gson.fromJson(message, type);

            String commande = map.get("commande");
            if (commande != null) {
                if (commande.equals("finVote")) {
                    startActivity(intentVersAttente);
                }
            }

        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.CEE_page), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void createGradientBackground() {

        try {

            int[] colors = new int[2];
            colors[0] = Integer.parseUnsignedInt("FF" + donnee.getEquipe2().getCouleur(), 16);
            colors[1] = Integer.parseUnsignedInt("FF" + donnee.getEquipe1().getCouleur(), 16);

            GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                    colors);
            gradientDrawable.setCornerRadius(0.0f);
            view.setBackground(gradientDrawable);

        } catch (NumberFormatException e) {
            Log.i("Erreur", "Les couleurs pour le dégradé n'ont pas fonctionné.");
        }

    }

    private void setBackgroundOfImageButton() {

        try {

            int color1 = Integer.parseUnsignedInt("3F" + donnee.getEquipe1().getCouleur(), 16);
            int color2 = Integer.parseUnsignedInt("3F" + donnee.getEquipe2().getCouleur(), 16);

            btnEquipe1.setBackgroundColor(color1);
            btnEquipe2.setBackgroundColor(color2);

        } catch (NumberFormatException e) {
            Log.i("Erreur", "Les couleurs pour les équipes n'ont pas fonctionné.");
        }

    }

    private void envoyerVote(int matchId, int jouteId, String equipe) {

        VoteRequest vote = new VoteRequest(matchId, jouteId, equipe);

        ApiService api = donnee.getApi();
        Call<ResponseBody> call = api.enregistrerVote(vote);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ChoixVoteEquipeActivity.this, "Vote enregistré!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ChoixVoteEquipeActivity.this, "Erreur côté serveur", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(ChoixVoteEquipeActivity.this, "Erreur de connexion", Toast.LENGTH_SHORT).show();
            }
        });
    }
}