package com.example.posedetection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.ColorDrawable;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.MenuItem;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
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

public class ImageProcessingActivity extends AppCompatActivity {
    private ImageView imageView;
    private Bitmap bitmap;
    private TextureView textureView;
    private CameraManager cameraManager;

    private Handler handler;
    private HandlerThread handlerThread;

    private LiteModelMovenetSingleposeLightningTfliteFloat164 model;

    private ImageProcessor imageProcessor;

    private TextView result;

    private ImageView cameraBtn, captureBtn, lockBtn, openBtn;
    private int typeCamera = 0;

    private ImagesUtils imagesUtils;
    private Bitmap mutable;
    private float[] outputFeature0;
    private float[] outputFeatureLock;

    private boolean isLock;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_processing);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.baseline_keyboard_backspace_30);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white)));

        isLock = false;

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
        lockBtn = findViewById(R.id.lock_button);
        openBtn = findViewById(R.id.open_button);
        result = findViewById(R.id.result);



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

        captureBtn.setOnClickListener(v->{capture(mutable, bitmap, outputFeature0);});

        lockBtn.setOnClickListener(v->{lock();});
        openBtn.setOnClickListener(v->{lock();});

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

               if(!isLock){
                   mutable = imagesUtils.drawPose(bitmap, outputFeature0);
                   imageView.setImageBitmap(mutable);
               }else{
                   mutable = imagesUtils.drawPose(bitmap, outputFeatureLock);
                   imageView.setImageBitmap(mutable);
                   if(check()){
                       result.setText("Match");
                       result.setTextColor(Color.GREEN);
                       //capture(mutable, bitmap, outputFeature0);
                   }else{
                       result.setText("Not match");
                       result.setTextColor(Color.RED);
                   }
               }



            }

        });
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
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void capture(Bitmap capture_bitmap, Bitmap bm, float[] results){

//        if(imagesUtils.capture(capture_bitmap))
//            Toast.makeText(this, "Image was saved !!!", Toast.LENGTH_SHORT).show();
//
//        if(imagesUtils.capturePose(capture_bitmap, outputFeature0)){}
//            Toast.makeText(this, "Image was saved !!!", Toast.LENGTH_SHORT).show();

        if(imagesUtils.captureAllTypes(capture_bitmap, bm, results)){}
        Toast.makeText(this, "Image was saved !!!", Toast.LENGTH_SHORT).show();


    }

    private void lock(){
        if(isLock){
            openBtn.setVisibility(View.VISIBLE);
            lockBtn.setVisibility(View.GONE);
            result.setVisibility(View.GONE);
            result.setText("Not match");
            result.setTextColor(Color.RED);
        }else{
            openBtn.setVisibility(View.GONE);
            lockBtn.setVisibility(View.VISIBLE);
            result.setVisibility(View.VISIBLE);
        }

        outputFeatureLock = outputFeature0;
        isLock = !isLock;
    }

    private boolean check(){
        boolean result = true;
        int x = 0;
        while(x <= 49){
            Log.i("checkX", String.valueOf(outputFeature0[x+1]));
            Log.i("checkY", String.valueOf(outputFeatureLock[x+1]));

            if (Math.abs(outputFeature0[x+1] * 100 - outputFeatureLock[x+1] * 100) > 8 || Math.abs(outputFeature0[x] * 100 -outputFeatureLock[x] * 100) > 8 ){

                result = false;
                break;
            }

            x += 3;
        }

        return  result;
    }

}