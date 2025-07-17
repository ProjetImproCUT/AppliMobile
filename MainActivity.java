package com.example.vote;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private Button voteButton;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        voteButton = (Button) findViewById(R.id.btn_equipe1);



        voteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int matchId = 1; // valeur à adapter
                int roundId = 1; // valeur à adapter
                String equipe = "TeamX"; // à récupérer depuis EditText ou Spinner

                VoteRequest vote = new VoteRequest(matchId, roundId, equipe);

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://voteimpro-afg6c3atf4a6cwcr.canadacentral-01.azurewebsites.net/") // ← adapte l’URL
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                ApiService api = retrofit.create(ApiService.class);
                Call<ResponseBody> call = api.enregistrerVote(vote);

                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Vote enregistré!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Erreur côté serveur", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "Erreur de connexion", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }


}