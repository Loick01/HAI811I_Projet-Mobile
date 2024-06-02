package com.example.projet_mobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AjoutCollaborateurs extends AppCompatActivity {

    private EditText newCollaborateur;

    private FirebaseDatabase firebaseRealtimeDatabase;
    private DatabaseReference drawingsReference;
    private DatabaseReference listeCollaborateurReference;
    private String drawingId;

    private LinearLayout layout_collaborators;
    LinearLayout.LayoutParams layoutParams;

    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajout_collaborateurs);

        this.drawingId = getIntent().getStringExtra("drawingId");
        this.firebaseRealtimeDatabase = FirebaseDatabase.getInstance("https://projet-mobile-33dd0-default-rtdb.europe-west1.firebasedatabase.app/");
        this.drawingsReference = this.firebaseRealtimeDatabase.getReference("drawings");
        this.listeCollaborateurReference = this.drawingsReference.child(drawingId).child("listeCollaborateurs");

        this.layout_collaborators = findViewById(R.id.layoutCollaborators);

        this.layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        this.layoutParams.gravity = Gravity.CENTER;

        listeCollaborateurReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> listeCollab = new ArrayList<>();
                String collab = null;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    collab = snapshot.getValue(String.class);
                    if (collab != null){
                        TextView tv_collab = new TextView(AjoutCollaborateurs.this);
                        tv_collab.setText(collab);
                        tv_collab.setTextSize(20);
                        tv_collab.setTextColor(R.color.black);
                        tv_collab.setLayoutParams(layoutParams);
                        layout_collaborators.addView(tv_collab);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("MainActivity", "Fail to read data", databaseError.toException());
            }
        });

        this.newCollaborateur = ((EditText)findViewById(R.id.new_collaborateur));
        ((Button)findViewById(R.id.retour_arriere)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AjoutCollaborateurs.this, EditDessin.class);
                EditDessin.drawingId = drawingId;
                startActivity(i);
            }
        });

        ((Button)findViewById(R.id.valider_ajout)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newCollab = String.valueOf(newCollaborateur.getText());

                if (newCollab.equals("")){
                    Toast.makeText(AjoutCollaborateurs.this,"Veuillez saisir le nom d'un collaborateur", Toast.LENGTH_SHORT).show();
                }else{

                    listeCollaborateurReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @SuppressLint("ResourceAsColor")
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            ArrayList<String> newListeCollab = new ArrayList<>();
                            String collab = null;
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                                collab = snapshot.getValue(String.class);
                                if (collab != null){
                                    newListeCollab.add(collab);
                                }
                            }

                            if (!(newListeCollab.contains(newCollab))) {
                                newListeCollab.add(newCollab);
                                listeCollaborateurReference.setValue(newListeCollab).addOnSuccessListener(aVoid -> Log.d("TAG", "ArrayList successfully written!"))
                                        .addOnFailureListener(e -> Log.w("TAG", "Error writing ArrayList", e));
                                TextView tv_collab = new TextView(AjoutCollaborateurs.this);
                                tv_collab.setText(newCollab);
                                tv_collab.setTextSize(20);
                                tv_collab.setTextColor(R.color.black);
                                tv_collab.setLayoutParams(layoutParams);
                                layout_collaborators.addView(tv_collab);
                            }else{
                                Toast.makeText(AjoutCollaborateurs.this,"Cet utilisateur est déjà un collaborateur", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.e("MainActivity", "Fail to read data", databaseError.toException());
                        }
                    });
                }
            }
        });
    }
}