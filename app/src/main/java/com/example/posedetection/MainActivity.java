package com.example.posedetection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.example.posedetection.ml.LiteModelMovenetSingleposeLightningTfliteFloat164;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.ImageProcessor;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.image.ops.ResizeOp;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.security.Permission;
import java.util.ArrayList;
import java.util.List;



public class MainActivity extends AppCompatActivity {



    private Button videoProcessingBtn;
    private Button imageProcessingBtn;
    private Button realtimeProcessingBtn;






    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //setStatusBarTransparent();
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        getPermission();
        videoProcessingBtn = findViewById(R.id.video_processing_button);
        imageProcessingBtn = findViewById(R.id.image_processing_button);
        realtimeProcessingBtn = findViewById(R.id.realtime_processing_button);

        videoProcessingBtn.setOnClickListener(v->{videoProcessingBtnClick();});
        imageProcessingBtn.setOnClickListener(v->{imageProcessingBtnClick();});
        realtimeProcessingBtn.setOnClickListener(v->{realtimeProcessingBtnClick();});



    }


    private void videoProcessingBtnClick(){
        Intent i = new Intent(MainActivity.this, VideoProcessingActivity.class);
        startActivity(i);
    }
    private void imageProcessingBtnClick(){
        Intent i = new Intent(MainActivity.this, ImageProcessingActivity.class);
        startActivity(i);
    }
    private void realtimeProcessingBtnClick(){
        Intent i = new Intent(MainActivity.this, RealtimeProcessingActivity.class);
        startActivity(i);
    }

    private void getPermission() {
        getCameraPermission();


    }

    private void getCameraPermission(){
        if(checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, 1);
        }
    }

    private void getWriteExternalPermisson(){
        if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 1){
            if(grantResults[0] != PackageManager.PERMISSION_GRANTED){
                getCameraPermission();
            }
        }
        if(requestCode == 2){
            if(grantResults[0] != PackageManager.PERMISSION_GRANTED){
                getWriteExternalPermisson();
            }
        }
    }






//    private void setStatusBarTransparent() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Window window = getWindow();
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//
//            View decorView = window.getDecorView();
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//            } else {
//                decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
//            }
//            window.setStatusBarColor(Color.TRANSPARENT);
//        }
//    }
}

