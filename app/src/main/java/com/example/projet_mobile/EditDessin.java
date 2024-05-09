package com.example.projet_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.SeekBar;

import yuku.ambilwarna.AmbilWarnaDialog;

public class EditDessin extends AppCompatActivity {

    private DrawingView drawingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_dessin);

        drawingView = findViewById(R.id.drawingView);
        
        ((ImageView)findViewById(R.id.retour_arriere)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            	Intent i = new Intent(EditDessin.this, SignedUpUserPage.class);
                startActivity(i);
            }
        });
        
        ((ImageView)findViewById(R.id.ajout_collaborateurs)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            	Intent i = new Intent(EditDessin.this, AjoutCollaborateurs.class);
                startActivity(i);
            }
        });
        
        ((ImageView)findViewById(R.id.partager_bouton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            	Intent i = new Intent(EditDessin.this, SaveDrawing.class);
                startActivity(i);
            }
        });
        
        ((ImageView)findViewById(R.id.sauvegarder_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            	/* La sauvegarde du dessin en png ne fonctionne pas encore
                DrawingView dv = findViewById(R.id.drawingView);
                Bitmap bitmap = dv.getBitmapFromView();
                dv.saveBitmapToPng(dv.getContext(),bitmap,"my_draw");
                */
            }
        });

        ((Button)findViewById(R.id.buttonLineWidth)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                showViewPopup(v);
            }
        });

        ((Button)findViewById(R.id.buttonBackgroudColor)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                changeBackgroundColor();
            }
        });

        ((Button)findViewById(R.id.buttonColorLine)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                changeLineColor();
            }
        });
    }

    private void changeBackgroundColor(){
        AmbilWarnaDialog awd = new AmbilWarnaDialog(this, drawingView.getBackgroundColor(), new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
                // Rien à faire à priori
            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                drawingView.setBackgroundColor(color);
                drawingView.invalidate();
            }
        });
        awd.show();
    }

    private void changeLineColor(){
        AmbilWarnaDialog awd = new AmbilWarnaDialog(this, drawingView.getBackgroundColor(), new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
                // Rien à faire à priori
            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                drawingView.setToolColor(color);
            }
        });
        awd.show();
    }

    private void showViewPopup(View v) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.line_width_slider_layout,null);
        PopupWindow pw = new PopupWindow( // Le dernier paramètre fait que la popup se ferme si on clique ailleurs
                popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true
        );

        pw.showAsDropDown(v, 0, -400, Gravity.BOTTOM); // Positionne la popup

        SeekBar sb = popupView.findViewById(R.id.slider_width_line);
        sb.setProgress(drawingView.getLineWidth());
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // On met à jour l'épaisseur de l'outil ici
                drawingView.setLineWidth(progress);
                sb.setProgress(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Rien ici
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Rien non plus
            }
        });
    }
}
