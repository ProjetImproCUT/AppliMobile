package com.example.improgoappmobile;

import static com.example.improgoappmobile.utils.VoteCommun.createGradientBackground;
import static com.example.improgoappmobile.utils.VoteCommun.setBackgroundOfImageButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.improgoappmobile.utils.Donnee;

public class ChoixVoteEquipeEtoileActivity extends AppCompatActivity {

    private View view;
    private ImageButton btnEquipe1;
    private ImageButton btnEquipe2;

    private Donnee donnee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_choix_vote_equipe_etoile);

        donnee = Donnee.getInstance();

        view = findViewById(R.id.CVEE_page);

        // Fait le dégradé dans le fond du View
        createGradientBackground(view, donnee.getEquipe2().getCouleur(), donnee.getEquipe1().getCouleur());

        Intent intentVersChoixInterprete = new Intent(this, ChoixInterpreteEtoileActivity.class);

        btnEquipe1 = findViewById(R.id.b_equ1_etoile);
        btnEquipe1.setImageResource(R.drawable.logojaune);
        btnEquipe1.setOnClickListener((view) -> {
            intentVersChoixInterprete.putExtra("equipeChoisie", 1);
            startActivity(intentVersChoixInterprete);
        });

        btnEquipe2 = findViewById(R.id.b_equ2_etoile);
        btnEquipe2.setImageResource(R.drawable.logobleu);
        btnEquipe2.setOnClickListener((view) -> {
            intentVersChoixInterprete.putExtra("equipeChoisie", 2);
            startActivity(intentVersChoixInterprete);
        });

        setBackgroundOfImageButton(btnEquipe1, btnEquipe2);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.CVEE_page), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
