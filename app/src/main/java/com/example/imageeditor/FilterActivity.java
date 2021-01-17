package com.example.imageeditor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class FilterActivity extends AppCompatActivity {
    ImageView imageView;
    ImageView imageView2;
    ProgressBar progressBar;
    Button meanbtn;
    Button medianbtn;
    Button savebtn;
    String currentPhotoPath;
    String imagepathuri;
    Uri uri;
    Bitmap myBitmap;
    Bitmap myBitmap1;
    Bitmap b;

    static {
        System.loadLibrary("native-lib");
    }
    public native void Jnimedian(Bitmap bitmap);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        Bundle extras = getIntent().getExtras();
        imageView = findViewById(R.id.imageView2);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        meanbtn = findViewById(R.id.meanbtn);
        medianbtn = findViewById(R.id.medianbtn);
        savebtn = findViewById(R.id.savebtn);
        if (extras != null && extras.containsKey("imagepathuri")) {
            uri = Uri.parse(extras.getString("imagepathuri"));
            imageView.setImageURI(uri);
            try {
                myBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            }catch (Exception e){

            }

        }
        currentPhotoPath = getIntent().getStringExtra("imagepath");

        if(currentPhotoPath != null) {
            File imgFile = new File(currentPhotoPath);
            if (imgFile.exists()) {
                myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                imageView.setImageBitmap(myBitmap);

            }
        }

        medianbtn.setOnClickListener(new View.OnClickListener() {
            @Override
             public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);

                Thread t = new Thread(){
                    public void run(){
                        myBitmap1 = myBitmap.copy(Bitmap.Config.ARGB_8888, true);
                        Jnimedian(myBitmap1);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                imageView2 = findViewById(R.id.imageView3);
                                imageView2.setImageBitmap(myBitmap1);
                                savebtn.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                            }
                        });

                    }
                };
                t.start();


            }
        });

        meanbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                Thread t = new Thread(){
                    public void run(){
                        myBitmap1 = myBitmap.copy(Bitmap.Config.ARGB_8888, true);
                        Jnimedian(myBitmap1);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                imageView2 = findViewById(R.id.imageView3);
                                imageView2.setImageBitmap(myBitmap1);
                                savebtn.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                            }
                        });

                    }
                };
                t.start();

            }
        });

        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String pathofBmp = MediaStore.Images.Media.insertImage(getContentResolver(), myBitmap1,"title", null);
                Uri bmpUri = Uri.parse(pathofBmp);
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
                shareIntent.setType("image/png");
                startActivity(shareIntent);

            }
        });

    }
}