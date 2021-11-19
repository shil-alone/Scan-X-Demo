package com.codershil.docxdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private CardView imageToPdfCard, imageReducerCard, imageTypeCard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageToPdfCard = findViewById(R.id.imageToPdfCard);
        imageReducerCard = findViewById(R.id.imageReducerCard);
        imageTypeCard = findViewById(R.id.imageTypeCard);

        imageToPdfCard.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, ImageToPdfActivity.class));
        });

        imageReducerCard.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, ImageReducerActivity.class));
        });

        imageTypeCard.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this,ImageConvertorActivity.class));
        });

        // requesting for external storage permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE
                }, PackageManager.PERMISSION_GRANTED);
            }

        }
    }
}