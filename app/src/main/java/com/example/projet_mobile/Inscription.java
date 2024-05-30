package com.example.projet_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Inscription extends AppCompatActivity {

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        ((Button)findViewById(R.id.retour_menu)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Inscription.this, MainActivity.class);
                startActivity(i);
            }
        });

        ((Button)findViewById(R.id.confirmer_inscription)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String lastname = String.valueOf(((EditText)findViewById(R.id.lastnameField)).getText());
                String firstname = String.valueOf(((EditText)findViewById(R.id.firstnameField)).getText());
                String phone = String.valueOf(((EditText)findViewById(R.id.phoneField)).getText());
                String email = String.valueOf(((EditText)findViewById(R.id.emailField)).getText());
                String passord = String.valueOf(((EditText) findViewById(R.id.passwordField)).getText());

                Toast.makeText(Inscription.this, "Votre prénom : " + firstname , Toast.LENGTH_SHORT).show();
                Intent i = new Intent(Inscription.this, SignedUpUserPage.class);
                startActivity(i);
            }
        });
    }
}