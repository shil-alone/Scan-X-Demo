package com.codershil.docxdemo;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;

public class ImageConvertorActivity extends AppCompatActivity {
    private ImageView imgSelectedImage;
    private Button btnSelectImage;
    private Button btnConvertJPEG, btnConvertPng, btnConvertWebp, btnConvertWebpLossy, btnConvertWebpLossless;
    private ActivityResultLauncher<String> galleryLauncher;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_convertor);
        imgSelectedImage = findViewById(R.id.selectedImage2);
        btnSelectImage = findViewById(R.id.btnSelectImage2);
        btnConvertJPEG = findViewById(R.id.btnJpeg);
        btnConvertPng = findViewById(R.id.btnPng);
        btnConvertWebp = findViewById(R.id.btnWebp);
        btnConvertWebpLossless = findViewById(R.id.btnWebpLossless);
        btnConvertWebpLossy = findViewById(R.id.btnWebpLossy);

        btnConvertJPEG.setEnabled(false);
        btnConvertPng.setEnabled(false);
        btnConvertWebp.setEnabled(false);
        btnConvertWebpLossy.setEnabled(false);
        btnConvertWebpLossless.setEnabled(false);

        // initializing gallery launcher to pick image from gallery
        galleryLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                uri -> {
                    imgSelectedImage.setImageURI(uri);
                    imageUri = uri;
                    btnConvertJPEG.setEnabled(true);
                    btnConvertPng.setEnabled(true);
                    btnConvertWebp.setEnabled(true);
                    btnConvertWebpLossy.setEnabled(true);
                    btnConvertWebpLossless.setEnabled(true);
                });

        // launcher to pick image from gallery
        btnSelectImage.setOnClickListener(v -> {
            galleryLauncher.launch("image/*");
        });

        // setting listeners to converting buttons
        btnConvertJPEG.setOnClickListener(v -> {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) imgSelectedImage.getDrawable();
            Bitmap bitmap = bitmapDrawable.getBitmap();
            changeImageType(bitmap, 0);
        });
        btnConvertPng.setOnClickListener(v -> {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) imgSelectedImage.getDrawable();
            Bitmap bitmap = bitmapDrawable.getBitmap();
            changeImageType(bitmap, 1);
        });
        btnConvertWebp.setOnClickListener(v -> {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) imgSelectedImage.getDrawable();
            Bitmap bitmap = bitmapDrawable.getBitmap();
            changeImageType(bitmap, 2);
        });
        btnConvertWebpLossy.setOnClickListener(v -> {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) imgSelectedImage.getDrawable();
            Bitmap bitmap = bitmapDrawable.getBitmap();
            changeImageType(bitmap, 3);
        });
        btnConvertWebpLossless.setOnClickListener(v -> {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) imgSelectedImage.getDrawable();
            Bitmap bitmap = bitmapDrawable.getBitmap();
            changeImageType(bitmap, 4);
        });
    }

    void changeImageType(Bitmap bitmap, int code) {
        FileOutputStream fileOutputStream = null;
        File file = Environment.getExternalStorageDirectory();
        File dir = new File(file.getAbsolutePath() + "/Scan X/" + "My Converted Images");
        dir.mkdirs();

        Bitmap.CompressFormat format = Bitmap.CompressFormat.JPEG;
        String fileName = String.format("%d.jpeg", System.currentTimeMillis());
        ;
        switch (code) {
            case 0:
                format = Bitmap.CompressFormat.JPEG;
                fileName = String.format("%d.jpeg", System.currentTimeMillis());
                break;
            case 1:
                format = Bitmap.CompressFormat.PNG;
                fileName = String.format("%d.png", System.currentTimeMillis());
                break;
            case 2:
                format = Bitmap.CompressFormat.WEBP;
                fileName = String.format("%d.webp", System.currentTimeMillis());
                break;
            case 3:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    format = Bitmap.CompressFormat.WEBP_LOSSY;
                    fileName = String.format("%d.webp", System.currentTimeMillis());
                }
                break;
            case 4:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    format = Bitmap.CompressFormat.WEBP_LOSSLESS;
                    fileName = String.format("%d.webp", System.currentTimeMillis());
                }
                break;
            default:
                format = Bitmap.CompressFormat.JPEG;

        }

        File outFile = new File(dir, fileName);

        try {
            fileOutputStream = new FileOutputStream(outFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        bitmap.compress(format, 100, fileOutputStream);

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
        Toast.makeText(ImageConvertorActivity.this, "image saved to folder Scan X/My Reduced Images", Toast.LENGTH_LONG).show();
    }
}