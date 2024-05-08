package com.example.projet_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class EditDessin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_dessin);

        ((Button)findViewById(R.id.retour_arriere)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(EditDessin.this, SignedUpUserPage.class);
                startActivity(i);
            }
        });

        ((Button)findViewById(R.id.ajout_collaborateurs)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(EditDessin.this, AjoutCollaborateurs.class);
                startActivity(i);
            }
        });

        ((Button)findViewById(R.id.sauvegarder_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* La sauvegarde du dessin en png ne fonctionne pas encore
                DrawingView dv = findViewById(R.id.drawingView);
                Bitmap bitmap = dv.getBitmapFromView();
                dv.saveBitmapToPng(dv.getContext(),bitmap,"my_draw");
                */
                Intent i = new Intent(EditDessin.this, SaveDrawing.class);
                startActivity(i);
            }
        });
    }
}