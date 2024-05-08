package com.example.projet_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class SaveDrawing extends AppCompatActivity {

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_drawing);

        ((Button)findViewById(R.id.retour_arriere)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SaveDrawing.this, EditDessin.class);
                startActivity(i);
            }
        });

        // Pour l'instant, les boutons de sauvegarde et de partage ne font qu'afficher des Toast

        ((Button)findViewById(R.id.savePngButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SaveDrawing.this,"Le dessin a été sauvegardé au format png", Toast.LENGTH_SHORT).show();
            }
        });

        ((Button)findViewById(R.id.shareMailButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SaveDrawing.this,"Le dessin a été partagé par mail", Toast.LENGTH_SHORT).show();
            }
        });

        ((Button)findViewById(R.id.shareXButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SaveDrawing.this,"Le dessin a été partagé sur X", Toast.LENGTH_SHORT).show();
            }
        });

        ((Button)findViewById(R.id.shareFacebookButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SaveDrawing.this,"Le dessin a été partagé sur Facebook", Toast.LENGTH_SHORT).show();
            }
        });
    }
}