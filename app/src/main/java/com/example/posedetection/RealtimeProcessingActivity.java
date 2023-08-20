package com.example.posedetection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;


import android.annotation.SuppressLint;
import android.app.Dialog;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Surface;
import android.view.TextureView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.posedetection.ml.LiteModelMovenetSingleposeLightningTfliteFloat164;
import com.example.posedetection.utils.ImagesUtils;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.ImageProcessor;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.image.ops.ResizeOp;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RealtimeProcessingActivity extends AppCompatActivity {

    private ImageView imageView;
    private Bitmap bitmap;
    private TextureView textureView;
    private CameraManager cameraManager;

    private Handler handler;
    private HandlerThread handlerThread;

    private LiteModelMovenetSingleposeLightningTfliteFloat164 model;

    private ImageProcessor imageProcessor;



    private ImageView cameraBtn, captureBtn, imagesBtn;
    private int typeCamera = 0;

    private ImagesUtils imagesUtils;
    private Bitmap mutable;
    private float[] outputFeature0;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realtime_processing);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.baseline_keyboard_backspace_30);
        actionBar.setDisplayShowTitleEnabled(false);

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white)));

        Dialog dialog = new Dialog(getApplicationContext());




        imageProcessor = new ImageProcessor.Builder().add(new ResizeOp(192, 192, ResizeOp.ResizeMethod.BILINEAR)).build();
        imagesUtils = new ImagesUtils();
        try {
            model = LiteModelMovenetSingleposeLightningTfliteFloat164.newInstance(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        textureView = findViewById(R.id.texttureView);
        cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        imageView = findViewById(R.id.image);
        cameraBtn = findViewById(R.id.change_camera_button);
        captureBtn = findViewById(R.id.capture_button);
        imagesBtn = findViewById(R.id.images_button);

        cameraBtn.setOnClickListener(v->{
            if(typeCamera == 1){
                typeCamera = 0;
            }
            else typeCamera = 1;

            try {
                openCamera();
            } catch (CameraAccessException e) {
                throw new RuntimeException(e);
            }
        });

        captureBtn.setOnClickListener(v->{capture(mutable);});
        imagesBtn.setOnClickListener(v->{imageBtnClick();});


        handlerThread = new HandlerThread("videoThread");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());




        textureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surfaceTexture, int i, int i1) {
                try{
                    openCamera();
                }catch (Exception e){
                    Log.e("Err camera", "onSurfaceTextureAvailable: ", e );
                }
            }

            @Override
            public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture surfaceTexture, int i, int i1) {

            }

            @Override
            public boolean onSurfaceTextureDestroyed(@NonNull SurfaceTexture surfaceTexture) {
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(@NonNull SurfaceTexture surfaceTexture) {
                bitmap = textureView.getBitmap();

                TensorImage image = new TensorImage(DataType.UINT8);
                image.load(bitmap);
                image = imageProcessor.process(image);


                TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 192, 192, 3}, DataType.UINT8);

                inputFeature0.loadBuffer(image.getBuffer());


                LiteModelMovenetSingleposeLightningTfliteFloat164.Outputs outputs = model.process(inputFeature0);
                outputFeature0 = outputs.getOutputFeature0AsTensorBuffer().getFloatArray();

                Log.i("Output", String.valueOf(outputFeature0.length));

                mutable = imagesUtils.drawPose(bitmap, outputFeature0);
                imageView.setImageBitmap(mutable);


            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.realtime_menu, menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        model.close();
    }

    @SuppressLint("MissingPermission")
    private void openCamera() throws CameraAccessException {
        cameraManager.openCamera(cameraManager.getCameraIdList()[typeCamera], new CameraDevice.StateCallback() {
            @Override
            public void onOpened(@NonNull CameraDevice cameraDevice) {
                try {
                    CaptureRequest.Builder captureRequest = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
                    Surface surface = new Surface(textureView.getSurfaceTexture());
                    captureRequest.addTarget(surface);
                    List<Surface> list = new ArrayList<Surface>();
                    list.add(surface);
                    cameraDevice.createCaptureSession(list, new CameraCaptureSession.StateCallback() {
                        @Override
                        public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                            try {
                                cameraCaptureSession.setRepeatingRequest(captureRequest.build(), null, null);
                            } catch (CameraAccessException e) {
                                throw new RuntimeException(e);
                            }
                        }

                        @Override
                        public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {

                        }
                    }, handler);

                } catch (CameraAccessException e) {
                    throw new RuntimeException(e);
                }

            }

            @Override
            public void onDisconnected(@NonNull CameraDevice cameraDevice) {

            }

            @Override
            public void onError(@NonNull CameraDevice cameraDevice, int i) {

            }
        }, handler);
    }


    private void capture(Bitmap capture_bitmap){

//        if(imagesUtils.capture(capture_bitmap))
//            Toast.makeText(this, "Image was saved !!!", Toast.LENGTH_SHORT).show();
//
//        if(imagesUtils.capturePose(capture_bitmap, outputFeature0)){}
//            Toast.makeText(this, "Image was saved !!!", Toast.LENGTH_SHORT).show();

        if(imagesUtils.captureAllTypes(capture_bitmap, bitmap, outputFeature0)){}
            Toast.makeText(this, "Image was saved !!!", Toast.LENGTH_SHORT).show();


    }




    //error
    private void imageBtnClick() {
        Uri selectedUri = Uri.parse(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES).toString());
        Log.i("Check uri: ", String.valueOf(selectedUri));
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(selectedUri, "resource/folder");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.realtime_settings:
                Toast.makeText(this, "Oke", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}