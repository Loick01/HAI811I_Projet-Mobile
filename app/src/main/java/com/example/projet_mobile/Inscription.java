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
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class Inscription extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);
        mAuth = FirebaseAuth.getInstance();

        EditText emailField= findViewById(R.id.emailField);
        EditText passwordField= findViewById(R.id.passwordField);
        EditText lastnameField= findViewById(R.id.lastnameField);
        EditText firstnameField= findViewById(R.id.firstnameField);
        Button inscriptionButton = findViewById(R.id.confirmer_inscription);

        ((Button)findViewById(R.id.retour_menu)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Inscription.this, MainActivity.class);
                startActivity(i);
            }
        });

        inscriptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailField.getText().toString().trim();
                String password = passwordField.getText().toString().trim();
                String lastname = lastnameField.getText().toString().trim();
                String firstname = firstnameField.getText().toString().trim();

                if (email.isEmpty()) {
                    emailField.setError("Veuillez rentrer une adresse mail");
                    emailField.requestFocus();
                    return;
                }

                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailField.setError("Adresse mail non valide");
                    emailField.requestFocus();
                    return;
                }

                if (password.isEmpty()) {
                    passwordField.setError("Veuillez rentrer un mot de passe");
                    passwordField.requestFocus();
                    return;
                }

                if (password.length() < 4) {
                    passwordField.setError("Le mot de passe doit au moins contenir 4 caractères");
                    passwordField.requestFocus();
                    return;
                }

                if (lastname.isEmpty()) {
                    lastnameField.setError("Veuillez rentrer votre nom de famille");
                    lastnameField.requestFocus();
                    return;
                }

                if (firstname.isEmpty()) {
                    firstnameField.setError("Veuillez rentrer votre prénom");
                    firstnameField.requestFocus();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    UserProfileChangeRequest profile_user = new UserProfileChangeRequest.Builder()
                                            .setDisplayName(firstname + " " + lastname)
                                            .build();

                                    FirebaseUser user = mAuth.getCurrentUser();
                                    if (user != null) {
                                        user.updateProfile(profile_user);
                                    }

                                    Toast.makeText(Inscription.this, "Votre inscription est terminée", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(Inscription.this, MainActivity.class); // On renvoie l'utilisateur sur la page d'accueil
                                    startActivity(i);
                                } else {
                                    if (task.getException() instanceof FirebaseAuthWeakPasswordException) {
                                        passwordField.setError("Mot de passe invalide");
                                        passwordField.requestFocus();
                                    } else if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                        emailField.setError("Adresse mail invalide");
                                        emailField.requestFocus();
                                    } else if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                        emailField.setError("Un utilisateur est déjà inscrit avec cette adresse mail");
                                        emailField.requestFocus();
                                    } else {
                                        Toast.makeText(Inscription.this, "L'inscription a échoué : " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                        });
            }
        });
    }
}