package com.example.projet_mobile;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Array;
import java.util.ArrayList;

public class DrawingView extends View { // Comme cette classe hérite de View, on pourra l'utiliser comme vue sur l'activité EditDessin
    private Paint paint;
    private Path path;
    private ArrayList<Path> paths;
    private ArrayList<Integer> sizePath; // Apparemment si on veut des lignes de taille différente on a pas le choix, on est obligé de conserver leurs tailles au moment où elles sont dessinées
    private ArrayList<Integer> colorPath; // Pareil pour avoir des lignes de couleur différente

    private ArrayList<Integer> stylePath; // Pareil pour avoir des styles différents

    private float startX, startY, endX, endY;
    private float centerX, centerY;
    private float radius;
    private int lineWidth;
    private int backgroundColor; // Couleur de fond du dessin
    private int toolColor; // Couleur de l'outil avec lequel on va dessiner

    private int toolType; // 0 pour ligne courbe, 1 pour ligne droite, il faudra en rajouter d'autre
    private int lineStyle; // 0 pour STROKE (juste le contour), 1 pour FILL_AND_STROKE (contour et remplissage)
    public DrawingView(Context context){
        this(context,null);
    }
    public DrawingView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        this.lineWidth = 10;
        this.toolType = 0;
        this.lineStyle = 0;
        this.paint = new Paint();
        this.paint.setStyle(Paint.Style.STROKE); // Style par défaut
        paint.setStrokeCap(Paint.Cap.ROUND); // Définit le style de terminaison (il y en a d'autre possible, voir si on ne ferait pas en sorte de pouvoir les modifier)
        paint.setStrokeJoin(Paint.Join.ROUND); // Définit le style de jonction (il y en a d'autre possible, voir si on ne ferait pas en sorte de pouvoir les modifier)
        this.paint.setStrokeWidth(this.lineWidth);
        this.paint.setAntiAlias(true);

        this.path = new Path();
        this.paths = new ArrayList<>();
        this.sizePath = new ArrayList<>();
        this.colorPath = new ArrayList<>();
        this.stylePath = new ArrayList<>();

        this.backgroundColor = ContextCompat.getColor(DrawingView.this.getContext(), R.color.white); // Par défaut, la couleur de fond est blanche
        this.toolColor = Color.BLACK; // Par défaut, l'outil dessine en noir
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(this.backgroundColor);
        // On dessine toutes les lignes précédentes
        for (int i = 0 ; i < this.paths.size() ; i++){
            this.paint.setStrokeWidth(this.sizePath.get(i));
            this.paint.setColor(this.colorPath.get(i));
            if (this.stylePath.get(i) == 0){
                this.paint.setStyle(Paint.Style.STROKE);
            }else {
                this.paint.setStyle(Paint.Style.FILL_AND_STROKE);
            }
            canvas.drawPath(this.paths.get(i), this.paint);
        }
        // Puis on dessine la nouvelle ligne (avec la taille actuelle)
        this.paint.setStrokeWidth(this.lineWidth);
        this.paint.setColor(this.toolColor);
        if (this.lineStyle == 0){
            this.paint.setStyle(Paint.Style.STROKE);
        }else{
            this.paint.setStyle(Paint.Style.FILL_AND_STROKE);
        }
        canvas.drawPath(this.path,this.paint);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent){
        if (this.toolType == 0) { // Dessine une courbe
            float x_pos = motionEvent.getX();
            float y_pos = motionEvent.getY();
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    this.path.moveTo(x_pos, y_pos);
                    break;
                case MotionEvent.ACTION_UP: // L'utilisateur a fini de tracer, donc on ajoute la ligne à la liste de Path
                    this.paths.add(new Path(this.path));
                    this.sizePath.add(this.lineWidth); // On conserve la taille de la ligne au moment où elle est dessinée
                    this.colorPath.add(this.toolColor); // Pareil pour la couleur
                    this.stylePath.add(this.lineStyle); // Pareil pour le style
                    this.path.reset(); // Efface la ligne de l'objet this.path que l'on vient d'ajouter
                    break;
                case MotionEvent.ACTION_MOVE:
                    this.path.lineTo(x_pos, y_pos);
                    break;
            }
        }else if (this.toolType == 1){ // Dessine une ligne droite
            float x_pos = motionEvent.getX();
            float y_pos = motionEvent.getY();
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    path.moveTo(x_pos, y_pos);
                    startX = x_pos;
                    startY = y_pos;
                    break;
                case MotionEvent.ACTION_MOVE:
                    path.reset();
                    path.moveTo(startX, startY);
                    path.lineTo(x_pos, y_pos);
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    this.paths.add(new Path(this.path));
                    this.sizePath.add(this.lineWidth); // On conserve la taille de la ligne au moment où elle est dessinée
                    this.colorPath.add(this.toolColor); // Pareil pour la couleur
                    this.stylePath.add(this.lineStyle); // Pareil pour le style
                    this.path.reset(); // Efface la ligne de l'objet this.path que l'on vient d'ajouter
                    break;
            }
        }else if (this.toolType == 2){ // Dessine un cercle
            float x_pos = motionEvent.getX();
            float y_pos = motionEvent.getY();
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    centerX = motionEvent.getX();
                    centerY = motionEvent.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    path.reset();
                    float dx = motionEvent.getX() - centerX;
                    float dy = motionEvent.getY() - centerY;
                    radius = (float) Math.sqrt(dx * dx + dy * dy);
                    path.addCircle(centerX, centerY, radius, Path.Direction.CW);
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    this.paths.add(new Path(this.path));
                    this.sizePath.add(this.lineWidth); // On conserve la taille de la ligne au moment où elle est dessinée
                    this.colorPath.add(this.toolColor); // Pareil pour la couleur
                    this.stylePath.add(this.lineStyle); // Pareil pour le style
                    this.path.reset(); // Efface le cercle de l'objet this.path que l'on vient d'ajouter
                    break;
            }
        }else if (this.toolType == 3){ // Dessine un rectangle
            float x_pos = motionEvent.getX();
            float y_pos = motionEvent.getY();
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    startX = motionEvent.getX();
                    startY = motionEvent.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    path.reset();
                    endX = motionEvent.getX();
                    endY = motionEvent.getY();

                    float left = Math.min(startX, endX);
                    float right = Math.max(startX, endX);
                    float top = Math.min(startY, endY);
                    float bottom = Math.max(startY, endY);
                    path.addRect(left, top, right, bottom, Path.Direction.CW);

                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    this.paths.add(new Path(this.path));
                    this.sizePath.add(this.lineWidth); // On conserve la taille de la ligne au moment où elle est dessinée
                    this.colorPath.add(this.toolColor); // Pareil pour la couleur
                    this.stylePath.add(this.lineStyle); // Pareil pour le style
                    this.path.reset(); // Efface le cercle de l'objet this.path que l'on vient d'ajouter
                    break;
            }
        }

        invalidate(); // Redessine la vue
        return true;
    }

    public Bitmap getBitmapFromView(){ // Cette fonction sert à la sauvegarde en png du dessin
        Bitmap bitmap = Bitmap.createBitmap(this.getWidth(),this.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        this.draw(canvas);
        return bitmap;
    }

    public void saveBitmapToPng(Context context, Bitmap bitmap, String fileName){
        FileOutputStream outputStream = null;
        try{
            File saveDirectory = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES); // L'image sera enregistrée dans le répertoire des images du téléphone
            File file = new File(saveDirectory, fileName+".png"); // Le dessin est sauvegardé au format png
            outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream); // Compression au format PNG (le deuxième paramèter c'est la qualité, voir si l'utilisateur ne pourrait pas la faire varier)
            outputStream.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void setLineWidth(int progress){
        this.lineWidth = progress;
    }

    public int getLineWidth(){
        return this.lineWidth;
    }

    public int getBackgroundColor(){
        return this.backgroundColor;
    }
    public void setBackgroundColor(int backgroundColor){
        this.backgroundColor = backgroundColor;
    }

    public void setToolType(int toolType) {
        this.toolType = toolType;
    }

    public int getToolColor(){
        return this.toolColor;
    }
    public void setToolColor(int toolColor){
        this.toolColor = toolColor;
    }

    public int getLineStyle() {
        return this.lineStyle;
    }

    public void setLineStyle(int lineStyle) {
        this.lineStyle = lineStyle;
        if (lineStyle == 0){
            this.paint.setStyle(Paint.Style.STROKE);
        }else if (lineStyle == 1){
            this.paint.setStyle(Paint.Style.FILL_AND_STROKE);
        }
    }

    public void undo() { // Annule la dernière modification faite sur le dessin (si elle existe)
        if (paths.size() > 0) {
            int last_index = paths.size() - 1;
            // Les 3 listes font normalement la même taille donc l'indice du dernier élément est le même
            paths.remove(last_index);
            sizePath.remove(last_index);
            colorPath.remove(last_index);
            stylePath.remove(last_index);
            invalidate(); // Redessine pour prendre en compte l'annulation
        }
    }
}
