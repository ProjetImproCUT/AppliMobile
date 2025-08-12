package com.example.improgoappmobile;

import static com.example.improgoappmobile.utils.VoteCommun.createGradientBackground;
import static com.example.improgoappmobile.utils.VoteCommun.setBackgroundOfImageButton;

import android.content.Intent;
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

import com.example.improgoappmobile.utils.Donnee;
import com.example.improgoappmobile.utils.MyWebSocketClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Map;

public class ChoixVoteEquipeActivity extends AppCompatActivity {

    private static final String TAG = "VOTE";

    private View view;
    private ImageButton btnEquipe1;
    private ImageButton btnEquipe2;

    private Donnee donnee;
    private MyWebSocketClient client;

    private int jouteRendu;
    private int numeroMatch;

    private boolean clickedOnce = false; // debounce

    // Listen for "finVote" while this screen is visible (e.g., timer expired)
    private final MyWebSocketClient.MessageListener socketListener = (message) -> {
        try {
            if (message == null || message.isEmpty()) return;
            char first = message.charAt(0);
            if (first != '{' && first != '[') return; // ignore non-JSON frames

            Gson gson = new Gson();
            Type type = new TypeToken<Map<String, Object>>() {}.getType();
            Map<String, Object> map = gson.fromJson(message, type);

            Object cmdObj = map.get("commande");
            if (!(cmdObj instanceof String)) return;
            String commande = (String) cmdObj;

            if ("finVote".equals(commande)) {
                runOnUiThread(() -> {
                    if (!isFinishing()) {
                        Log.d(TAG, "Received finVote -> returning to AttenteActivity");
                        startActivity(new Intent(this, AttenteActivity.class));
                        finish();
                    }
                });
            }
        } catch (Exception e) {
            Log.e(TAG, "Socket parse error: " + message, e);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_choix_vote_equipe);

        donnee = Donnee.getInstance();
        client = donnee.getConnexionWebSocket();

        Intent intent = getIntent();
        jouteRendu = intent.getIntExtra("jouteRendu", -1);
        numeroMatch = intent.getIntExtra("numeroMatch", -1);
        Log.d(TAG, "Entering ChoixVoteEquipeActivity: jouteRendu=" + jouteRendu + ", numeroMatch=" + numeroMatch);

        if (jouteRendu <= 0 || numeroMatch <= 0) {
            Log.e(TAG, "Refusing to send vote with invalid params: joute=" + jouteRendu + ", match=" + numeroMatch);
            Toast.makeText(this, "Paramètres de vote invalides (round/match)", Toast.LENGTH_SHORT).show();
            return;
        }

        view = findViewById(R.id.CVE_page);

        // Dégradé: équipe2 -> équipe1
        createGradientBackground(view, donnee.getEquipe2().getCouleur(), donnee.getEquipe1().getCouleur());

        Intent toWait = new Intent(this, AttenteActivity.class);

        btnEquipe1 = findViewById(R.id.b_equ1);
        btnEquipe1.setImageResource(R.drawable.logojaune);
        btnEquipe1.setOnClickListener(v -> {
            if (clickedOnce) return;
            clickedOnce = true;
            Log.d(TAG, "Sending vote: match=" + numeroMatch + ", joute=" + jouteRendu + ", equipe=" + donnee.getEquipe1().getNom());
            envoyerVoteWS(numeroMatch, jouteRendu, donnee.getEquipe1().getNom());
            startActivity(toWait);
            finish();
        });

        btnEquipe2 = findViewById(R.id.b_equ2);
        btnEquipe2.setImageResource(R.drawable.logobleu);
        btnEquipe2.setOnClickListener(v -> {
            if (clickedOnce) return;
            clickedOnce = true;
            Log.d(TAG, "Sending vote: match=" + numeroMatch + ", joute=" + jouteRendu + ", equipe=" + donnee.getEquipe2().getNom());
            envoyerVoteWS(numeroMatch, jouteRendu, donnee.getEquipe2().getNom());
            startActivity(toWait);
            finish();
        });

        setBackgroundOfImageButton(btnEquipe1, btnEquipe2);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.CVE_page), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (client != null) client.addMessageListener(socketListener);
    }

    @Override
    protected void onStop() {
        if (client != null) client.removeMessageListener(socketListener);
        super.onStop();
    }

    /** Send the vote via WebSocket (Node.js server will handle it). */
    private void envoyerVoteWS(int matchId, int jouteId, String equipe) {
        if (client == null || !client.isOpen()) {
            Toast.makeText(this, "Socket non connecté", Toast.LENGTH_SHORT).show();
            clickedOnce = false; // allow retry
            return;
        }
        try {
            JSONObject obj = new JSONObject();
            obj.put("commande", "enregistrerVote"); // <-- make sure your Node server listens for this
            obj.put("numeroMatch", matchId);
            obj.put("jouteRendu", jouteId);
            obj.put("equipe", equipe);

            String json = obj.toString();
            Log.d(TAG, "WS SEND " + json);
            client.send(json);
            // Optionally toast here; better to toast on ack from server
            // Toast.makeText(this, "Vote envoyé", Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            Log.e(TAG, "JSON build error", e);
            Toast.makeText(this, "Erreur envoi vote", Toast.LENGTH_SHORT).show();
            clickedOnce = false; // allow retry
        }
    }
}
