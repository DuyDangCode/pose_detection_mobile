package com.example.posedetection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.ColorDrawable;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.MenuItem;
import android.view.Surface;
import android.view.TextureView;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.posedetection.ml.LiteModelMovenetSingleposeLightningTfliteFloat164;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.ImageProcessor;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.image.ops.ResizeOp;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VideoProcessingActivity extends AppCompatActivity {

//    private VideoView vv;
    private int idExercise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_processing);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.baseline_keyboard_backspace_30);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white)));

        Intent intent = getIntent();
        idExercise = intent.getIntExtra("id_exercise", 0);

//        vv = findViewById(R.id.videoView);

        //Creating MediaController
//        MediaController mediaController= new MediaController(this);
//        mediaController.setAnchorView(vv);

        //specify the location of media file
        Uri uri=Uri.parse(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES).toString() +  "/pose-detection-videos/squat.mp4");
        Log.i("uri", uri.toString());

        //Setting MediaController and URI, then starting the videoView
//        vv.setMediaController(mediaController);
//        vv.setVideoURI(uri);
//        vv.requestFocus();
//        vv.start();






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