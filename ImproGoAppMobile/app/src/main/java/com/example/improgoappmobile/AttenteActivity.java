package com.example.improgoappmobile;

import android.content.Intent;
import android.os.Bundle;

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
import java.util.Map;

public class AttenteActivity extends AppCompatActivity {

    private Donnee donnee;
    private MyWebSocketClient client;

    // Avoid duplicate navigations if the server re-sends a command
    private volatile boolean isNavigating = false;

    // One stable listener instance so we can remove it in onStop()
    private final MyWebSocketClient.MessageListener socketListener = (message) -> {

        try {
            if (message == null || message.isEmpty()) return;
            char c = message.charAt(0);
            if (c != '{' && c != '[') return;  // ignore plain text frames


            // Ignore non-JSON frames (e.g., "reload (Ask Gemini)")
            char first = message.charAt(0);
            if (first != '{' && first != '[') return;

            Gson gson = new Gson();
            Type type = new TypeToken<Map<String, Object>>() {}.getType();
            Map<String, Object> map = gson.fromJson(message, type);

            Object cmdObj = map.get("commande");
            if (!(cmdObj instanceof String)) return;  // ignore frames without a proper command

            String commande = (String) cmdObj;

            if ("demandeAuVote".equals(commande)) {
                int joute = safeToInt(map.get("jouteRendu"), -1);
                int match = safeToInt(map.get("numeroMatch"), -1);
                if (joute <= 0 || match <= 0) return;
                runOnUiThread(() -> navigateToVoteEquipe(joute, match));
            }

            if ("finVote".equals(commande)) {
                runOnUiThread(() -> {
                    if (!isFinishing()) startActivity(new Intent(this, AttenteActivity.class));
                });
            }

            // ...other commands
        } catch (Exception e) {
            android.util.Log.e("AttenteActivity", "WS parse error: " + message, e);
        }
    };

    private static int safeToInt(Object o, int fallback) {
        try {
            if (o instanceof Number) return ((Number) o).intValue();
            if (o instanceof String) return Integer.parseInt(((String) o).trim());
        } catch (Exception ignored) {}
        return fallback;
    }


    private static String firstNonNull(String... vals) {
        for (String v : vals) if (v != null && !v.isEmpty()) return v;
        return null;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_attente);

        donnee = Donnee.getInstance();
        client = donnee.getConnexionWebSocket();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.attente_page), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Re-register listener every time this screen becomes visible
        if (client != null) client.addMessageListener(socketListener);
        // Reset navigation guard when coming back to this screen
        isNavigating = false;
    }

    @Override
    protected void onStop() {
        // Remove listener to avoid leaks / duplicate callbacks
        if (client != null) client.removeMessageListener(socketListener);
        super.onStop();
    }

    private void navigateToVoteEquipe(int jouteRendu, int numeroMatch) {
        if (isNavigating) return; // prevent double starts
        isNavigating = true;

        Intent intent = new Intent(this, ChoixVoteEquipeActivity.class);
        intent.putExtra("jouteRendu", jouteRendu);
        intent.putExtra("numeroMatch", numeroMatch);
        startActivity(intent);
    }

    // Example for another vote screen if you need it later:
    // private void navigateToVoteEtoile(int jouteRendu, int numeroMatch) {
    //     if (isNavigating) return;
    //     isNavigating = true;
    //     Intent intent = new Intent(this, ChoixVoteEquipeEtoileActivity.class);
    //     intent.putExtra("jouteRendu", jouteRendu);
    //     intent.putExtra("numeroMatch", numeroMatch);
    //     startActivity(intent);
    // }

    private static int safeParseInt(String s, int fallback) {
        try { return Integer.parseInt(s); } catch (Exception e) { return fallback; }
    }
}
