package com.example.projet_mobile;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import static com.example.projet_mobile.DrawingView.saveBitmapToPng;

public class SaveDrawing extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 100;
    private Bitmap drawing_bitmap;

    private final ActivityResultLauncher<String[]> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
                Boolean writePermission = result.getOrDefault(Manifest.permission.WRITE_EXTERNAL_STORAGE, false);
                Boolean managePermission = result.getOrDefault(Manifest.permission.MANAGE_EXTERNAL_STORAGE, false);

                if (writePermission != null && writePermission) {
                    saveDrawing();
                } else if (managePermission != null && managePermission) {
                    saveDrawing();
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_drawing);

        Intent intent = getIntent();
        byte[] byteArray = intent.getByteArrayExtra("drawing_bitmap");
        if (byteArray != null) {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
            drawing_bitmap = BitmapFactory.decodeStream(inputStream);
            Log.d("SaveDrawing", "Bitmap received, byte array size: " + byteArray.length);
        } else {
            Log.e("SaveDrawing", "Byte array is null");
        }

        ((Button) findViewById(R.id.retour_arriere)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SaveDrawing.this, EditDessin.class);
                startActivity(i);
            }
        });

        ((Button) findViewById(R.id.savePngButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(drawing_bitmap == null);
                if (checkPermission() && drawing_bitmap != null) {
                    saveDrawing();
                } else {
                    requestPermission();
                }
            }
        });

        ((Button) findViewById(R.id.shareMailButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SaveDrawing.this, "Le dessin a été partagé par mail", Toast.LENGTH_SHORT).show();
            }
        });

        ((Button) findViewById(R.id.shareXButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SaveDrawing.this, "Le dessin a été partagé sur X", Toast.LENGTH_SHORT).show();
            }
        });

        ((Button) findViewById(R.id.shareFacebookButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SaveDrawing.this, "Le dessin a été partagé sur Facebook", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager();
        } else {
            int result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            return result == PackageManager.PERMISSION_GRANTED;
        }
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setData(Uri.parse(String.format("package:%s", getApplicationContext().getPackageName())));
                startActivityForResult(intent, PERMISSION_REQUEST_CODE);
            } catch (Exception e) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivityForResult(intent, PERMISSION_REQUEST_CODE);
            }
        } else {
            requestPermissionLauncher.launch(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE});
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    saveDrawing();
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void saveDrawing() {
        if (drawing_bitmap != null) {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
            String fileName = "my_drawing_" + timeStamp;
            saveBitmapToPng(this, drawing_bitmap, fileName);
            Toast.makeText(this, "Drawing saved", Toast.LENGTH_SHORT).show();
        }
    }
}