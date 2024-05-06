package com.example.projet_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class AjoutCollaborateurs extends AppCompatActivity {

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajout_collaborateurs);

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
                Toast.makeText(AjoutCollaborateurs.this,"Le nouveau collaborateur a été ajouté", Toast.LENGTH_SHORT).show();
            }
        });
    }
}