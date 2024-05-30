package com.example.projet_mobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Connexion extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);
        mAuth = FirebaseAuth.getInstance();

        EditText emailField= findViewById(R.id.emailField);
        EditText passwordField= findViewById(R.id.passwordField);
        Button connexionButton = findViewById(R.id.confirmer_connexion);

        ((Button)findViewById(R.id.retour_menu)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Connexion.this, MainActivity.class);
                startActivity(i);
            }
        });

        connexionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailField.getText().toString().trim();
                String password = passwordField.getText().toString().trim();

                if (email.equals("") || password.equals("")) {
                    Toast.makeText(Connexion.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    String name = user.getDisplayName();
                                    Toast.makeText(Connexion.this, "Vous êtes maintenant connecté", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(Connexion.this, SignedUpUserPage.class);
                                    startActivity(i);
                                } else {
                                    Toast.makeText(Connexion.this, "La connexion a échoué. Vérifiez les informations", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}