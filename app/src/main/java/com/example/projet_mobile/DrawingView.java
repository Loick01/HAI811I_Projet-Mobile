package com.example.projet_mobile;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DrawingView extends View {
    private Paint paint;
    private Path path;
    private ArrayList<Path> paths;
    private ArrayList<Integer> sizePath;
    private ArrayList<Integer> colorPath;
    private ArrayList<Integer> stylePath;
    private ArrayList<String> listeCollaborateurs;

    private FirebaseDatabase firebaseRealtimeDatabase;
    private DatabaseReference drawingsReference;
    private String drawingId;

    private DatabaseReference sizePathListReference;
    private DatabaseReference colorPathListReference;
    private DatabaseReference pathsListReference;
    private DatabaseReference listeCollaborateursReference;

    private FirebaseAuth mAuth;

    private float startX, startY, endX, endY;
    private float centerX, centerY;
    private float radius;
    private int lineWidth;
    private int backgroundColor;
    private int toolColor;
    private int toolType;
    private int lineStyle;

    public DrawingView(Context context) {
        this(context, null);
    }

    public DrawingView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        this.firebaseRealtimeDatabase = FirebaseDatabase.getInstance("https://projet-mobile-33dd0-default-rtdb.europe-west1.firebasedatabase.app/");

        this.lineWidth = 10;
        this.toolType = 0;
        this.lineStyle = 0;
        this.paint = new Paint();
        this.paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);
        this.paint.setStrokeWidth(this.lineWidth);
        this.paint.setAntiAlias(true);

        this.path = new Path();
        this.paths = new ArrayList<>();
        this.sizePath = new ArrayList<>();
        this.colorPath = new ArrayList<>();
        this.stylePath = new ArrayList<>();
        this.listeCollaborateurs = new ArrayList<>();

        this.backgroundColor = ContextCompat.getColor(DrawingView.this.getContext(), R.color.white);
        this.toolColor = Color.BLACK;

        this.drawingsReference = firebaseRealtimeDatabase.getReference("drawings");
        this.drawingId = drawingsReference.push().getKey();

        this.sizePathListReference = this.drawingsReference.child(drawingId).child("sizePathList");
        this.colorPathListReference = this.drawingsReference.child(drawingId).child("colorPathList");
        this.pathsListReference = this.drawingsReference.child(drawingId).child("pathsList");
        this.listeCollaborateursReference = this.drawingsReference.child(drawingId).child("listeCollaborateurs");

        mAuth = FirebaseAuth.getInstance();

        boolean isCreated = true;

        if (isCreated) {
            FirebaseUser user = mAuth.getCurrentUser();
            this.listeCollaborateurs.add(user.getEmail());

            this.listeCollaborateursReference.setValue(this.listeCollaborateurs).addOnSuccessListener(aVoid -> Log.d("TAG", "ArrayList successfully written!"))
                    .addOnFailureListener(e -> Log.w("TAG", "Error writing ArrayList", e));
        }
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(this.backgroundColor);
        for (int i = 0; i < this.paths.size(); i++) {
            this.paint.setStrokeWidth(this.sizePath.get(i));
            this.paint.setColor(this.colorPath.get(i));
            if (this.stylePath.get(i) == 0) {
                this.paint.setStyle(Paint.Style.STROKE);
            } else {
                this.paint.setStyle(Paint.Style.FILL_AND_STROKE);
            }
            canvas.drawPath(this.paths.get(i), this.paint);
        }
        this.paint.setStrokeWidth(this.lineWidth);
        this.paint.setColor(this.toolColor);
        if (this.lineStyle == 0) {
            this.paint.setStyle(Paint.Style.STROKE);
        } else {
            this.paint.setStyle(Paint.Style.FILL_AND_STROKE);
        }
        canvas.drawPath(this.path, this.paint);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        boolean needUpdate = false;

        float x_pos = motionEvent.getX();
        float y_pos = motionEvent.getY();
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                this.path.moveTo(x_pos, y_pos);
                break;
            case MotionEvent.ACTION_UP:
                this.paths.add(new Path(this.path));
                this.sizePath.add(this.lineWidth);
                this.colorPath.add(this.toolColor);
                this.stylePath.add(this.lineStyle);

                // Convertir le Path en liste de points et sauvegarder dans Firebase
                List<Point> points = convertPathToPoints(this.path);
                pathsListReference.push().setValue(points);

                this.path.reset();
                needUpdate = true;
                break;
            case MotionEvent.ACTION_MOVE:
                this.path.lineTo(x_pos, y_pos);
                break;
        }

        if (needUpdate) {
            this.sizePathListReference.setValue(this.sizePath).addOnSuccessListener(aVoid -> Log.d("TAG", "ArrayList successfully written!"))
                    .addOnFailureListener(e -> Log.w("TAG", "Error writing ArrayList", e));
            this.colorPathListReference.setValue(this.colorPath).addOnSuccessListener(aVoid -> Log.d("TAG", "ArrayList successfully written!"))
                    .addOnFailureListener(e -> Log.w("TAG", "Error writing ArrayList", e));
        }

        invalidate();
        return true;
    }

    private List<Point> convertPathToPoints(Path path) {
        List<Point> points = new ArrayList<>();
        PathMeasure pathMeasure = new PathMeasure(path, false);
        float[] coordinates = new float[2];
        float distance = 0f;
        while (distance < pathMeasure.getLength()) {
            pathMeasure.getPosTan(distance, coordinates, null);
            points.add(new Point(coordinates[0], coordinates[1]));
            distance += 1; // Adjust this step size as needed
        }
        return points;
    }

    private Path convertPointsToPath(List<Point> points) {
        Path path = new Path();
        if (points != null && points.size() > 0) {
            path.moveTo(points.get(0).x, points.get(0).y);
            for (int i = 1; i < points.size(); i++) {
                path.lineTo(points.get(i).x, points.get(i).y);
            }
        }
        return path;
    }

    public Bitmap getBitmapFromView() {
        Bitmap bitmap = Bitmap.createBitmap(this.getWidth(), this.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        this.draw(canvas);
        return bitmap;
    }

    public static void saveBitmapToPng(Context context, Bitmap bitmap, String fileName) {
        FileOutputStream outputStream = null;
        try {
            File saveDirectory = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            if (saveDirectory != null && !saveDirectory.exists()) {
                saveDirectory.mkdirs();
            }
            File file = new File(saveDirectory, fileName + ".png");
            Log.d("SaveBitmap", "Saving file to: " + file.getAbsolutePath());

            outputStream = new FileOutputStream(file);
            boolean success = bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.flush();
            if (success) {
                Log.d("SaveBitmap", "Bitmap successfully saved.");
            } else {
                Log.e("SaveBitmap", "Bitmap compression failed.");
            }
        } catch (FileNotFoundException e) {
            Log.e("SaveBitmap", "FileNotFoundException: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("SaveBitmap", "IOException: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    Log.e("SaveBitmap", "IOException while closing outputStream: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    public void setLineWidth(int progress) {
        this.lineWidth = progress;
    }

    public int getLineWidth() {
        return this.lineWidth;
    }

    public int getBackgroundColor() {
        return this.backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void setToolType(int toolType) {
        this.toolType = toolType;
    }

    public int getToolColor() {
        return this.toolColor;
    }

    public void setToolColor(int toolColor) {
        this.toolColor = toolColor;
    }

    public int getLineStyle() {
        return this.lineStyle;
    }

    public void setLineStyle(int lineStyle) {
        this.lineStyle = lineStyle;
        if (lineStyle == 0) {
            this.paint.setStyle(Paint.Style.STROKE);
        } else if (lineStyle == 1) {
            this.paint.setStyle(Paint.Style.FILL_AND_STROKE);
        }
    }

    public void undo() {
        if (paths.size() > 0) {
            int last_index = paths.size() - 1;
            paths.remove(last_index);
            sizePath.remove(last_index);
            colorPath.remove(last_index);
            stylePath.remove(last_index);
            invalidate();

            this.sizePathListReference.setValue(this.sizePath).addOnSuccessListener(aVoid -> Log.d("TAG", "ArrayList successfully written!"))
                    .addOnFailureListener(e -> Log.w("TAG", "Error writing ArrayList", e));
            this.colorPathListReference.setValue(this.colorPath).addOnSuccessListener(aVoid -> Log.d("TAG", "ArrayList successfully written!"))
                    .addOnFailureListener(e -> Log.w("TAG", "Error writing ArrayList", e));
            this.pathsListReference.setValue(this.paths).addOnSuccessListener(aVoid -> Log.d("TAG", "ArrayList successfully written!"))
                    .addOnFailureListener(e -> Log.w("TAG", "Error writing ArrayList", e));
        }
    }

    public String getDrawingId() {
        return this.drawingId;
    }
}