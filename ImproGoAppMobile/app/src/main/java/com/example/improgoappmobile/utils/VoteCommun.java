package com.example.improgoappmobile.utils;

import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

public class VoteCommun {

    private static Donnee donnee = Donnee.getInstance();

    public static void createGradientBackground(View view, String couleurHaut, String couleurBas) {

        try {

            int[] colors = new int[2];
            colors[0] = Integer.parseUnsignedInt("FF" + couleurHaut, 16);
            colors[1] = Integer.parseUnsignedInt("FF" + couleurBas, 16);

            GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                    colors);
            gradientDrawable.setCornerRadius(0.0f);
            view.setBackground(gradientDrawable);

        } catch (NumberFormatException e) {
            Log.i("Erreur", "Les couleurs pour le dégradé n'ont pas fonctionné.");
        }

    }

    public static void setBackgroundOfImageButton(ImageButton btnEquipe1, ImageButton btnEquipe2) {

        try {

            int color1 = Integer.parseUnsignedInt("3F" + donnee.getEquipe1().getCouleur(), 16);
            int color2 = Integer.parseUnsignedInt("3F" + donnee.getEquipe2().getCouleur(), 16);

            btnEquipe1.setBackgroundColor(color1);
            btnEquipe2.setBackgroundColor(color2);

        } catch (NumberFormatException e) {
            Log.i("Erreur", "Les couleurs pour les équipes n'ont pas fonctionné.");
        }

    }

}
