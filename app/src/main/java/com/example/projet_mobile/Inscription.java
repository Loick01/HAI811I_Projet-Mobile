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
                String password = String.valueOf(((EditText) findViewById(R.id.passwordField)).getText());

                if (!(lastname.equals("")) && !(firstname.equals("")) && !(email.equals("")) && !(password.equals(""))){ // Seul le champ du numéro de téléphone est optionnel
                    try {
                        int phoneNumber = -1;
                        if (!(phone.equals(""))){
                            phoneNumber = Integer.parseInt(phone);
                        }
                        User u = new User(lastname, firstname, email, phoneNumber, password); // Inscrit l'utilisateur dans la liste statique de la classe User (voir constructeur)
                        Toast.makeText(Inscription.this, firstname + lastname + ", votre inscription a bien été prise en compte", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(Inscription.this, SignedUpUserPage.class);
                        startActivity(i);
                    } catch (NumberFormatException e) {
                        Toast.makeText(Inscription.this, "Le numéro de téléphone saisi n'est pas valide, veuillez réessayer", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(Inscription.this, "Impossible de vous inscrire, vérifiez les informations saisies", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}