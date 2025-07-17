package com.example.vote;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiService {

    @Headers("Content-Type: application/json")
    @POST("vote.php")
    Call<ResponseBody> enregistrerVote(@Body VoteRequest voteRequest);
}
