package com.example.posedetection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;


import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.ColorDrawable;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.Surface;
import android.view.TextureView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.posedetection.ml.LiteModelMovenetSingleposeLightningTfliteFloat164;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.ImageProcessor;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.image.ops.ResizeOp;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RealtimeProcessingActivity extends AppCompatActivity {

    private ImageView imageView;
    private Bitmap bitmap;
    private TextureView textureView;
    private CameraManager cameraManager;

    private Handler handler;
    private HandlerThread handlerThread;

    private LiteModelMovenetSingleposeLightningTfliteFloat164 model;

    private ImageProcessor imageProcessor;

    private Paint paint;
    private Paint paint2;

    private ImageView cameraBtn, captureBtn, imagesBtn;
    private int typeCamera = 0;

    private Bitmap mutable;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realtime_processing);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.baseline_keyboard_backspace_30);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white)));



        imageProcessor = new ImageProcessor.Builder().add(new ResizeOp(192, 192, ResizeOp.ResizeMethod.BILINEAR)).build();

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

        captureBtn.setOnClickListener(v->{capture();});
        imagesBtn.setOnClickListener(v->{imageBtnClick();});











        handlerThread = new HandlerThread("videoThread");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());

        paint = new Paint();
        paint2 = new Paint();
        paint.setColor(getResources().getColor(R.color.red));
        paint2.setColor(getResources().getColor(R.color.teal_200));

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
                float[] outputFeature0 = outputs.getOutputFeature0AsTensorBuffer().getFloatArray();

                Log.i("Output", String.valueOf(outputFeature0.length));

                mutable = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                Canvas canvas = new Canvas(mutable);
                float h = bitmap.getHeight();
                float w = bitmap.getWidth();
                int x = 0;

                while(x <= 49){
                    if(outputFeature0[x+2] > 0.45 ){
                        canvas.drawCircle(outputFeature0[x+1] * w, outputFeature0[x] * h, 10f, paint);
                    }
                    x += 3;
                }

//                canvas.drawLine(outputFeature0[4], outputFeature0[3], outputFeature0[7], outputFeature0[6], paint);
//                canvas.drawLine(outputFeature0[10], outputFeature0[9], outputFeature0[13], outputFeature0[12], paint);


                //canvas.drawLine(outputFeature0[x+1], outputFeature0[x], outputFeature0[x+4], outputFeature0[x+3], paint);


                //canvas.drawLine(outputFeature0[16] * w, outputFeature0[15] * h, outputFeature0[19] * w, outputFeature0[18] * h, paint);
                //canvas.drawLine(outputFeature0[19]  * w, outputFeature0[18] * h, outputFeature0[25] * w, outputFeature0[24] * h, paint);
                //canvas.drawLine(outputFeature0[16]  * w, outputFeature0[15] * h, outputFeature0[22] * w, outputFeature0[21] * h, paint);
                //canvas.drawLine(outputFeature0[22] * w, outputFeature0[21] * h, outputFeature0[28] * w, outputFeature0[27] * h, paint);
                //canvas.drawLine(outputFeature0[25] * w, outputFeature0[24] * h, outputFeature0[31] * w, outputFeature0[30] * h, paint);
                //canvas.drawLine(outputFeature0[34] * w, outputFeature0[33] * h, outputFeature0[37] * w, outputFeature0[36] * h, paint);
                //canvas.drawLine(outputFeature0[16] * w, outputFeature0[15] * h, outputFeature0[34] * w, outputFeature0[33] * h, paint);
                //canvas.drawLine(outputFeature0[19] * w, outputFeature0[18] * h, outputFeature0[37] * w, outputFeature0[36] * h, paint);

                //canvas.drawLine(outputFeature0[34] * w, outputFeature0[33] * h, outputFeature0[40] * w, outputFeature0[39] * h, paint);
                //canvas.drawLine(outputFeature0[40] * w, outputFeature0[39] * h, outputFeature0[46] * w, outputFeature0[45] * h, paint);
                //canvas.drawLine(outputFeature0[37] * w, outputFeature0[36] * h, outputFeature0[43] * w, outputFeature0[42] * h, paint);
                //canvas.drawLine(outputFeature0[43] * w, outputFeature0[42] * h, outputFeature0[49] * w, outputFeature0[48] * h, paint);

                float[] lines = new float[48];

                if(outputFeature0[17] > 0.45 && outputFeature0[20] > 0.45){
                    lines[0] = outputFeature0[16] * w;
                    lines[1] = outputFeature0[15] * h;
                    lines[2] = outputFeature0[19] * w;
                    lines[3] = outputFeature0[18] * h;
                }


                if(outputFeature0[20] > 0.45 && outputFeature0[26] > 0.45){
                    lines[4] = outputFeature0[19] * w;
                    lines[5] = outputFeature0[18] * h;
                    lines[6] = outputFeature0[25] * w;
                    lines[7] = outputFeature0[24] * h;
                }

                if(outputFeature0[17] > 0.45 && outputFeature0[23] > 0.45){
                    lines[8] = outputFeature0[16] * w;
                    lines[9] = outputFeature0[15] * h;
                    lines[10] = outputFeature0[22] * w;
                    lines[11] = outputFeature0[21] * h;
                }

                if(outputFeature0[23] > 0.45 && outputFeature0[29] > 0.45){
                    lines[12] = outputFeature0[22] * w;
                    lines[13] = outputFeature0[21] * h;
                    lines[14] = outputFeature0[28] * w;
                    lines[15] = outputFeature0[27] * h;
                }

                if(outputFeature0[26] > 0.45 && outputFeature0[32] > 0.45){
                    lines[16] = outputFeature0[25] * w;
                    lines[17] = outputFeature0[24] * h;
                    lines[18] = outputFeature0[31] * w;
                    lines[19] = outputFeature0[30] * h;
                }

                if(outputFeature0[35] > 0.45 && outputFeature0[38] > 0.45){
                    lines[20] = outputFeature0[34] * w;
                    lines[21] = outputFeature0[33] * h;
                    lines[22] = outputFeature0[37] * w;
                    lines[23] = outputFeature0[36] * h;
                }

                if(outputFeature0[17] > 0.45 && outputFeature0[35] > 0.45){
                    lines[24] = outputFeature0[16] * w;
                    lines[25] = outputFeature0[15] * h;
                    lines[26] = outputFeature0[34] * w;
                    lines[27] = outputFeature0[33] * h;
                }

                if(outputFeature0[20] > 0.45 && outputFeature0[38] > 0.45){
                    lines[28] = outputFeature0[19] * w;
                    lines[29] = outputFeature0[18] * h;
                    lines[30] = outputFeature0[37] * w;
                    lines[31] = outputFeature0[36] * h;
                }

                if(outputFeature0[35] > 0.45 && outputFeature0[41] > 0.45){
                    lines[32] = outputFeature0[34] * w;
                    lines[33] = outputFeature0[33] * h;
                    lines[34] = outputFeature0[40] * w;
                    lines[35] = outputFeature0[39] * h;
                }

                if(outputFeature0[41] > 0.45 && outputFeature0[47] > 0.45){
                    lines[36] = outputFeature0[40] * w;
                    lines[37] = outputFeature0[39] * h;
                    lines[38] = outputFeature0[46] * w;
                    lines[39] = outputFeature0[45] * h;
                }

                if(outputFeature0[38] > 0.45 && outputFeature0[44] > 0.45){
                    lines[40] = outputFeature0[37] * w;
                    lines[41] = outputFeature0[36] * h;
                    lines[42] = outputFeature0[43] * w;
                    lines[43] = outputFeature0[42] * h;
                }

                if(outputFeature0[44] > 0.45 && outputFeature0[50] > 0.45){
                    lines[44] = outputFeature0[43] * w;
                    lines[45] = outputFeature0[42] * h;
                    lines[46] = outputFeature0[49] * w;
                    lines[47] = outputFeature0[48] * h;
                }

                canvas.drawLines(lines, paint2);


                imageView.setImageBitmap(mutable);


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


    private void capture(){

        try {
            Random generator = new Random();
            int n = 10000;
            n = generator.nextInt(n);
            String root = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES).toString();
            File myDir = new File(root + "/pose-detection-images");
            myDir.mkdirs();
            String fname = "image-" + n + ".jpg";
            File file = new File(myDir, fname);

            FileOutputStream out = new FileOutputStream(file);
            Bitmap bm = mutable;
            bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();

            Toast.makeText(this, "Image was saved !!!", Toast.LENGTH_SHORT).show();
            Log.i("CAPTURE: ", "ok");

        } catch( Exception e) {
            Log.d("onBtnSavePng", e.toString());
        }

    }

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
        }
        return super.onOptionsItemSelected(item);
    }
}