package com.example.improgoappmobile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private Button bCalendrier;
    private Button bParticiper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        bCalendrier = findViewById(R.id.b_calendrier);
        bCalendrier.setOnClickListener((event) -> {
            Intent intent = new Intent(this, CalendrierActivity.class);
            startActivity(intent);
        });

        bParticiper = findViewById(R.id.b_participer);
        bParticiper.setOnClickListener((event) -> {
            Intent intent = new Intent(this, ConnexionSalleActivity.class);
            startActivity(intent);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_page), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}