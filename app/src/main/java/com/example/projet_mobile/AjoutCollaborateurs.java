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