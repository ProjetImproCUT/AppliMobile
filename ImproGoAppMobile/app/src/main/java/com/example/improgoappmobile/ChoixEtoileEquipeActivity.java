package com.example.improgoappmobile;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ChoixEtoileEquipeActivity extends AppCompatActivity {

    private View view;
    private ImageButton btnEquipe1;
    private ImageButton btnEquipe2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_choix_etoile_equipe);

        view = findViewById(R.id.CEE_page);

        // Couleur équipe
        // jaune : FFDE59  bleu : 0CC0DF
        // vert : 7FD858   mauve : CB6CE4

        // Fait le dégradé dans le fond du View
        createGradientBackground("FFDE59", "0CC0DF");

        btnEquipe1 = findViewById(R.id.b_equ1);
        btnEquipe1.setImageResource(R.drawable.logojaune);

        btnEquipe2 = findViewById(R.id.b_equ2);
        btnEquipe2.setImageResource(R.drawable.logobleu);

        setBackgroundOfImageButton("FFDE59", "0CC0DF");

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.CEE_page), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void createGradientBackground(String colorTeam1, String colorTeam2) {

        try {

            int[] colors = new int[2];
            colors[0] = Integer.parseUnsignedInt("FF" + colorTeam2, 16);
            colors[1] = Integer.parseUnsignedInt("FF" + colorTeam1, 16);

            GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                    colors);
            gradientDrawable.setCornerRadius(0.0f);
            view.setBackground(gradientDrawable);

        } catch (NumberFormatException e) {
            Log.i("Erreur", "Les couleurs pour le dégradé n'ont pas fonctionné.");
        }

    }

    private void setBackgroundOfImageButton(String colorTeam1, String colorTeam2) {

        try {

            int color1 = Integer.parseUnsignedInt("3F" + colorTeam1, 16);
            int color2 = Integer.parseUnsignedInt("3F" + colorTeam2, 16);

            btnEquipe1.setBackgroundColor(color1);
            btnEquipe2.setBackgroundColor(color2);

        } catch (NumberFormatException e) {
            Log.i("Erreur", "Les couleurs pour les équipes n'ont pas fonctionné.");
        }

    }
}