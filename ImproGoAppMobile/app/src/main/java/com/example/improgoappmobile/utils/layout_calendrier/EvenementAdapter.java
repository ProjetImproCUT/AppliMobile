package com.example.improgoappmobile.utils.layout_calendrier;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.improgoappmobile.R;

import java.util.ArrayList;

public class EvenementAdapter extends RecyclerView.Adapter<EvenementAdapter.MonViewHolder> {

    private final ArrayList<Evenement> dataSet;

    public static class MonViewHolder extends RecyclerView.ViewHolder {

        private final TextView date;
        private final TextView lieu;
        private final TextView heure;
        private final TextView equipe;

        public MonViewHolder(@NonNull View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.date_event);
            lieu = itemView.findViewById(R.id.lieu_event);
            heure = itemView.findViewById(R.id.heure_event);
            equipe = itemView.findViewById(R.id.equipe_event);
        }

        public TextView getDate() {
            return date;
        }

        public TextView getLieu() {
            return lieu;
        }

        public TextView getHeure() {
            return heure;
        }

        public TextView getEquipe() {
            return equipe;
        }

    }

    public EvenementAdapter(ArrayList<Evenement> dataSet) {
        this.dataSet = dataSet;
    }

    @NonNull
    @Override
    public MonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.calendrier_layout, parent, false);

        return new MonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MonViewHolder holder, int position) {
        String textDate = "Date : " + dataSet.get(position).getDate();
        holder.getDate().setText(textDate);

        String textLieu = "Lieu : " + dataSet.get(position).getLieu();
        holder.getLieu().setText(textLieu);

        String textHeure = "Heure : " + dataSet.get(position).getHeure();
        holder.getHeure().setText(textHeure);

        String[] tab = dataSet.get(position).getEquipes();
        String textEquipe = "Equipes : " + tab[0] + " vs " + tab[1];
        holder.getEquipe().setText(textEquipe);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

}
