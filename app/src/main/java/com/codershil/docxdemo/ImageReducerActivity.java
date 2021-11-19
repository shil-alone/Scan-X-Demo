package com.codershil.docxdemo;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ImageReducerActivity extends AppCompatActivity {
    private ImageView imgSelectedImage;
    private Button btnSelectImage, btnReduceImage;
    private TextView txtPercentage;
    private SeekBar seekBar;
    private ActivityResultLauncher<String> galleryLauncher;
    private int imageQuality = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_reducer);
        // initializing views
        imgSelectedImage = findViewById(R.id.selectedImage1);
        btnSelectImage = findViewById(R.id.btnSelectImage1);
        btnReduceImage = findViewById(R.id.btnReduceImage);
        txtPercentage = findViewById(R.id.txtPercentage);
        seekBar = findViewById(R.id.seekBar);
        btnReduceImage.setEnabled(false);
        seekBar.setProgress(100);

        // initializing gallery launcher to pick image from gallery
        galleryLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                uri -> {
                    imgSelectedImage.setImageURI(uri);
                    btnReduceImage.setEnabled(true);
                });

        // launcher to pick image from gallery
        btnSelectImage.setOnClickListener(v -> {
            galleryLauncher.launch("image/*");
        });

        // changing value of imageQuality on seekBar progress
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                imageQuality = progress;
                txtPercentage.setText(progress + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // converting imageView into BitMap
        btnReduceImage.setOnClickListener(v -> {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) imgSelectedImage.getDrawable();
            Bitmap bitmap = bitmapDrawable.getBitmap();
            saveImageToExternalStorage1(bitmap);
        });

    }

    // method to reduce image size and save it to external storage
    void saveImageToExternalStorage1(Bitmap bitmap) {
        FileOutputStream fileOutputStream = null;
        File file = Environment.getExternalStorageDirectory();
        File dir = new File(file.getAbsolutePath() + "/Scan X/" + "My Reduced Images");
        dir.mkdirs();

        String fileName = String.format("%d.jpeg", System.currentTimeMillis());
        File outFile = new File(dir, fileName);

        try {
            fileOutputStream = new FileOutputStream(outFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        bitmap.compress(Bitmap.CompressFormat.JPEG, imageQuality, fileOutputStream);

        try {
            fileOutputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Toast.makeText(ImageReducerActivity.this, "image saved to folder Scan X/My Reduced Images", Toast.LENGTH_LONG).show();
    }

}