package com.codershil.docxdemo;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageToPdfActivity extends AppCompatActivity {
    private ImageView selectedImage;
    private Button btnSelectImage, btnConvertToPdf;
    private ActivityResultLauncher<String> galleryLauncher;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_to_pdf);
        // initializing views
        selectedImage = findViewById(R.id.selectedImage);
        btnSelectImage = findViewById(R.id.btnSelectImage);
        btnConvertToPdf = findViewById(R.id.btnConvertToPdf);
        btnConvertToPdf.setEnabled(false);


        // initializing gallery launcher to pick image from gallery
        galleryLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                uri -> {
                    selectedImage.setImageURI(uri);
                    imageUri = uri;
                    btnConvertToPdf.setEnabled(true);
                });

        // listener for getting image from gallery
        btnSelectImage.setOnClickListener(v -> galleryLauncher.launch("image/*"));

        // listener to btnConvertToPdf button
        btnConvertToPdf.setOnClickListener(v -> {
            try {
                // converting uri to bitmap
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                convertImageToPdf(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }

    // a method that converts image into pdf format and saves it into external storage
    public void convertImageToPdf(Bitmap bitmap) {

        // creating pdfDocument object which is going to be final product
        PdfDocument pdfDocument = new PdfDocument();

        // filling the info for first page of pdf file
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(bitmap.getWidth(), bitmap.getHeight(), 1).create();
        // creating a page with given page info
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);

        // drawing bitmap onto the pdf's first page
        page.getCanvas().drawBitmap(bitmap, 0, 0, null);
        pdfDocument.finishPage(page);

        // creating a directory where we want to save the pdf document
        File file = new File(Environment.getExternalStorageDirectory() + "/Scan X/My PDF Documents");
        if (!file.mkdirs()) {
            file.mkdirs();
        }
        String filePath = file.getAbsolutePath() + File.separator + "MyPdfFile.pdf";

        // writing pdf to the path specified
        try {
            pdfDocument.writeTo(new FileOutputStream(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        // closing pdf document it is necessary for memory management
        pdfDocument.close();
        Toast.makeText(ImageToPdfActivity.this, "pdf saved to \"Scan X /My PDF Documents\" folder", Toast.LENGTH_LONG).show();

    }


}