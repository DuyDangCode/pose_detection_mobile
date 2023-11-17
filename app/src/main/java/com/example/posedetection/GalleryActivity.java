package com.example.posedetection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.posedetection.utils.ImagesUtils;

import java.io.File;
import java.util.List;

public class GalleryActivity extends AppCompatActivity {


    private ImageView img;

    private ImagesUtils imagesUtils;
    private int index, max;

    private ImageView prevBtn, nextBtn;
    private String uri;
    private TextView nameImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_page);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.baseline_keyboard_backspace_30);
        actionBar.setDisplayShowTitleEnabled(false);

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white)));


        index = 0;
        makeDir();

        prevBtn = findViewById(R.id.prevBtn);
        nextBtn = findViewById(R.id.nextBtn);
        img = findViewById(R.id.imageView);
        nameImage = findViewById(R.id.nameImage);
        imagesUtils = new ImagesUtils();
        uri = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES).toString() +  "/pose-detection-images/";
        File f = new File(uri);
        File[] files = f.listFiles();
        max = files.length;

        ///storage/emulated/0/Pictures/pose-detection-images/

        Log.i("Uri", uri);

        Bitmap temp = imagesUtils.loadImage(uri, index);
        if(temp == null){
            img.setImageDrawable(getResources().getDrawable(R.drawable.no_image_available));
        }
        else {
            img.setImageBitmap(temp);
            nameImage.setText(imagesUtils.getNameImage(uri, index));
        }


        prevBtn.setOnClickListener(v-> changeImage(true));
        nextBtn.setOnClickListener(v->changeImage(false));
    }

    private void makeDir(){
        File directory = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES).toString() +  "/pose-detection-images/");
        if(!directory.isDirectory()){
            directory.mkdir();
        }
    }

    private void changeImage(boolean type){
        if(type){
            index--;
            if(index == -1) {
                index = max-1;
            }

        }else {
            index++;
            if(index == max){
                index = 0;
            }
        }
        img.setImageBitmap(imagesUtils.loadImage(uri, index));
        nameImage.setText(imagesUtils.getNameImage(uri, index));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}