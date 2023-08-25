package com.example.posedetection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.posedetection.utils.ImagesUtils;

import java.util.List;

public class GalleryActivity extends AppCompatActivity {

    private Gallery gallery;
    private ImageView img;

    private ImagesUtils imagesUtils;
    private CustomizedGalleryAdapter customGalleryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_page);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.baseline_keyboard_backspace_30);
        actionBar.setDisplayShowTitleEnabled(false);

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white)));


        gallery = findViewById(R.id.gallery);
        img = findViewById(R.id.imageView);
        imagesUtils = new ImagesUtils();
        String uri = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES).toString() +  "/pose-detection-images/";
        List<Bitmap> l =  imagesUtils.loadImages(uri);
        customGalleryAdapter = new CustomizedGalleryAdapter(getApplicationContext(), l);


        gallery.setAdapter(customGalleryAdapter);

        gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                img.setImageBitmap(customGalleryAdapter.getImages(i));
            }
        });

        img.setImageBitmap(l.get(0));






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