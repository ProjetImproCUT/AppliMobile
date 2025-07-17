package com.example.vote;

public class VoteRequest {
    private int match_id;
    private int round_id;
    private String equipe_choisie;

    public VoteRequest(int match_id, int round_id, String equipe_choisie) {
        this.match_id = match_id;
        this.round_id = round_id;
        this.equipe_choisie = equipe_choisie;
    }
}
