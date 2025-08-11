package com.example.improgoappmobile;

import static com.example.improgoappmobile.utils.VoteCommun.createGradientBackground;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.improgoappmobile.utils.Donnee;
import com.example.improgoappmobile.utils.layout_interprete.Interprete;
import com.example.improgoappmobile.utils.layout_interprete.InterpreteAdapter;

import java.util.ArrayList;
import java.util.Arrays;

public class ChoixInterpreteEtoileActivity extends AppCompatActivity {

    private int currentIndex = 1;
    private ArrayList<Interprete> dataSet;
    private Donnee donnee;

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private View view;
    private ImageView logoEquipe;
    private ImageButton bPrec;
    private ImageButton bSuiv;
    private Button bEtoileSelect;
    private Button bRetour;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_choix_interprete_etoile);

        donnee = Donnee.getInstance();

        Intent intent = getIntent();
        int equipeCode = intent.getIntExtra("equipeChoisie", 0);

        view = findViewById(R.id.CIE_page);

        logoEquipe = findViewById(R.id.logo_equ_etoile);

        switch (equipeCode) {
            case 1:
                createGradientBackground(view, donnee.getEquipe2().getCouleur(), donnee.getEquipe1().getCouleur());
                logoEquipe.setImageResource(R.drawable.logojaune); // À modifier
                break;
            case 2:
                createGradientBackground(view, donnee.getEquipe1().getCouleur(), donnee.getEquipe2().getCouleur());
                logoEquipe.setImageResource(R.drawable.logobleu); // À modifier
                break;
            default:
                Log.e("Erreur", "Le code de l'équipe est invalide.");
                break;
        }

        dataSet = new ArrayList<>(Arrays.asList(
                new Interprete("nom1", "prenom1", R.drawable.logobleu),
                new Interprete("nom2", "prenom2", R.drawable.logojaune),
                new Interprete("nom3", "prenom3", R.drawable.logomauve),
                new Interprete("nom4", "prenom4", R.drawable.logovert)
        ));

        recyclerView = findViewById(R.id.listeInterpretes);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new InterpreteAdapter(dataSet, recyclerView));
        recyclerView.setOnTouchListener((v, event) -> true);

        bPrec = findViewById(R.id.b_defil_gauche);
        bSuiv = findViewById(R.id.b_defil_droite);

        centerItem();
        showArrowButton();

        bPrec.setOnClickListener((view) -> {
            if (currentIndex > 1) {
                currentIndex--;
                centerItem();
                showArrowButton();
            }
        });

        bSuiv.setOnClickListener((view) -> {
            if (currentIndex < dataSet.size() - 2) {
                currentIndex++;
                centerItem();
                showArrowButton();
            }
        });

        bEtoileSelect = findViewById(R.id.b_envoyer_etoile);
        bEtoileSelect.setOnClickListener((view) -> {
            //
        });

        bRetour = findViewById(R.id.b_retour);
        bRetour.setOnClickListener((view) -> {
            Intent intentVersChoixEquipeEtoile = new Intent(this, ChoixVoteEquipeEtoileActivity.class);
            startActivity(intentVersChoixEquipeEtoile);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.CIE_page), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void centerItem() {
        recyclerView.post(() -> {
            View itemView = layoutManager.findViewByPosition(currentIndex);
            if (itemView != null) {
                int offset = (recyclerView.getWidth() / 2) - (itemView.getWidth() / 2);
                layoutManager.scrollToPositionWithOffset(currentIndex, offset);
            } else {
                recyclerView.scrollToPosition(currentIndex);

                // Essayer de recentrer après un court délai
                recyclerView.postDelayed(() -> {
                    View item = layoutManager.findViewByPosition(currentIndex);
                    if (item != null) {
                        int offset = (recyclerView.getWidth() / 2) - (item.getWidth() / 2);
                        layoutManager.scrollToPositionWithOffset(currentIndex, offset);
                    }
                }, 100);
            }
        });
    }

    private void showArrowButton() {
        bPrec.setEnabled(currentIndex > 1);
        bPrec.setAlpha(bPrec.isEnabled() ? 1.0f : 0.5f);

        bSuiv.setEnabled(currentIndex < dataSet.size() - 2);
        bSuiv.setAlpha(bSuiv.isEnabled() ? 1.0f : 0.5f);
    }

}