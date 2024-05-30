package com.example.projet_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class AjoutCollaborateurs extends AppCompatActivity {

    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajout_collaborateurs);

        // Pour l'instant on affiche dans cette vue tous les utilisateurs qui sont inscrit, plus tard ce sera seulement les collaborateurs au dessin sur lequel on a ouvert la vue AjoutCollaborateurs
        LinearLayout collaboratorsLayout = findViewById(R.id.layoutCollaborators);
        ArrayList<User> listUser = User.getListUser();

        if (listUser.isEmpty()){
            TextView textView = new TextView(this);
            textView.setText("Aucun utilisateur inscrit");
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            layoutParams.gravity = Gravity.CENTER;
            layoutParams.topMargin = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
            textView.setLayoutParams(layoutParams);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
            textView.setTextColor(getResources().getColor(R.color.black));

            collaboratorsLayout.addView(textView);
        }else {
            for (int i = 0; i < listUser.size(); i++) {
                TextView textView = new TextView(this);
                textView.setText((i+1) + " : " + listUser.get(i).getFirstname() + listUser.get(i).getLastname());
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                layoutParams.gravity = Gravity.CENTER;
                layoutParams.topMargin = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
                if (i == listUser.size()-1){ // Pour le dernier utilisateur de la liste, on met un marginBottom (ça fait plus propre)
                    layoutParams.bottomMargin = (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
                }
                textView.setLayoutParams(layoutParams);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
                textView.setTextColor(getResources().getColor(R.color.black));

                collaboratorsLayout.addView(textView);
            }
        }

        ((Button)findViewById(R.id.retour_arriere)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AjoutCollaborateurs.this, EditDessin.class);
                startActivity(i);
            }
        });

        ((Button)findViewById(R.id.valider_ajout)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AjoutCollaborateurs.this,"Pour l'instant pas d'ajout de collaborateurs...", Toast.LENGTH_SHORT).show();
            }
        });
    }
}