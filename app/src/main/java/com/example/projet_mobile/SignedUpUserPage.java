package com.example.projet_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignedUpUserPage extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextView welcomeMessage;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signed_up_user_page);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        welcomeMessage = findViewById(R.id.welcome_message);

        if (user != null){ // Si un utilisateur est connecté, son nom est affiché dans le message d'accueil. ATTENTION : Il faut bien vérifier si ce n'est pas un utilisateur anonyme qui est sur l'application
            welcomeMessage.setText("Bonjour " + user.getDisplayName() + " !");
        }

        ((Button)findViewById(R.id.retour_menu)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignedUpUserPage.this, MainActivity.class);
                startActivity(i);
            }
        });

        ((Button)findViewById(R.id.creer_dessin)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignedUpUserPage.this, EditDessin.class);
                startActivity(i);
            }
        });
    }
}