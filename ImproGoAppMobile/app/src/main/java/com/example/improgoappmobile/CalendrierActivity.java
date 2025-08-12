package com.example.improgoappmobile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.improgoappmobile.utils.ApiService;
import com.example.improgoappmobile.utils.Donnee;
import com.example.improgoappmobile.utils.layout_calendrier.Evenement;
import com.example.improgoappmobile.utils.layout_calendrier.EvenementAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CalendrierActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView message;
    private Button bRetour;

    private Donnee donnee;
    private ArrayList<Evenement> dataSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_calendrier);

        donnee = Donnee.getInstance();
        message = findViewById(R.id.t_message);

        dataSet = new ArrayList<>();
        recyclerView = findViewById(R.id.listeCalendrier);

        initialiserListeCalendrier(this);

        bRetour = findViewById(R.id.b_retourC);
        bRetour.setOnClickListener((view) -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initialiserListeCalendrier(Context context) {

        ApiService api = donnee.getApi();
        Call<ResponseBody> call = api.getCalendrier();

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful()) {

                    try {
                        String responseBody = response.body().string();

                        Gson gson = new Gson();
                        Type type = new TypeToken<Map<String, String>[]>() {}.getType();
                        Map<String, String>[] arrayMaps = gson.fromJson(responseBody, type);

                        for (Map<String, String> map : arrayMaps) {
                            String dateVal = map.get("date_match");
                            dateVal = dateVal.replace('-', '/');
                            String heureVal = map.get("heure_match");
                            heureVal = heureVal.substring(0, 5);
                            String lieuVal = map.get("lieu");
                            String equipe1Val = map.get("equipe1");
                            String equipe2Val = map.get("equipe2");

                            String[] equipes = { equipe1Val, equipe2Val };

                            dataSet.add(new Evenement(dateVal, lieuVal, heureVal, equipes));
                        }

                        recyclerView.setLayoutManager(new LinearLayoutManager(context,
                                LinearLayoutManager.VERTICAL, false));
                        recyclerView.setAdapter(new EvenementAdapter(dataSet));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    String mess = "Erreur côté serveur";
                    Toast.makeText(CalendrierActivity.this, mess, Toast.LENGTH_SHORT).show();
                    message.setText(mess);
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                String mess = "Erreur de connexion";
                Toast.makeText(CalendrierActivity.this, mess, Toast.LENGTH_SHORT).show();
                message.setText(mess);
            }

        });

    }

}