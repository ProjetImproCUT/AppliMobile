package com.example.improgoappmobile.utils.layout_interprete;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.improgoappmobile.R;

import java.util.ArrayList;

public class InterpreteAdapter extends RecyclerView.Adapter<InterpreteAdapter.MonViewHolder> {

    private final ArrayList<Interprete> dataSet;
    private final RecyclerView recyclerView;

    public static class MonViewHolder extends RecyclerView.ViewHolder {

        private final ImageView photo;
        private final TextView prenom;
        private final TextView nom;

        public MonViewHolder(@NonNull View itemView) {
            super(itemView);

            photo = itemView.findViewById(R.id.interp_photo);
            prenom = itemView.findViewById(R.id.interp_prenom);
            nom = itemView.findViewById(R.id.interp_nom);
        }

        public ImageView getPhoto() {
            return photo;
        }

        public TextView getPrenom() {
            return prenom;
        }

        public TextView getNom() {
            return nom;
        }

    }

    public InterpreteAdapter(ArrayList<Interprete> dataSet, RecyclerView recyclerView) {
        this.dataSet = dataSet;
        this.recyclerView = recyclerView;

        this.dataSet.add(0, new Interprete());
        this.dataSet.add(new Interprete());
    }

    @NonNull
    @Override
    public MonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.interprete_layout, parent, false);

        return new MonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MonViewHolder holder, int position) {
        holder.getPhoto().setImageResource(dataSet.get(position).getImage());
        holder.getPrenom().setText(dataSet.get(position).getPrenom());
        holder.getNom().setText(dataSet.get(position).getNom());

        holder.itemView.setOnClickListener(v -> {
            int position1 = holder.getBindingAdapterPosition();
            if (position1 != RecyclerView.NO_POSITION) {
                recyclerView.smoothScrollToPosition(position1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

}
