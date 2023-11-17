package com.example.posedetection.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

import androidx.core.content.res.ResourcesCompat;

import com.example.posedetection.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class ImagesUtils {

    private double accuracy;

    public double getAccuracy() {
        return accuracy;
    }

    public ImagesUtils() {
        accuracy = 0.45;
    }

    public ImagesUtils(double accuracy) {
        this.accuracy = accuracy;
    }
    public boolean capture(Bitmap bitmap, Bitmap original){
        try {
            Date currentTime = Calendar.getInstance().getTime();
            String root = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES).toString();
            File myDir = new File(root + "/pose-detection-images");
            myDir.mkdirs();
            String fname = "image-" + currentTime.getTime() + ".jpg";
            String fname2 = "image-og-" + currentTime.getTime() + ".jpg";
            File file = new File(myDir, fname);
            File file2 = new File(myDir, fname2);

            FileOutputStream out = new FileOutputStream(file);
            FileOutputStream out2 = new FileOutputStream(file2);

            Bitmap bm = bitmap;
            bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();

            Bitmap bm2 = original;
            bm2.compress(Bitmap.CompressFormat.JPEG, 100, out2);
            out2.flush();
            out2.close();

            return true;

        } catch( Exception e) {
            Log.d("onBtnSavePng", e.toString());
            return false;

        }

    }

    public boolean capturePose(Bitmap bitmap, float[] outputFeature0){
        Paint paint = new Paint();
        Paint paint2 = new Paint();

        paint.setColor(Color.RED);
        paint2.setColor(Color.WHITE);

        float h = bitmap.getHeight();
        float w = bitmap.getWidth();

        Bitmap mutable = Bitmap.createBitmap((int)w, (int)h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mutable);

        int x = 0;

        while(x <= 49){
            if(outputFeature0[x+2] > accuracy ){
                canvas.drawCircle(outputFeature0[x+1] * w, outputFeature0[x] * h, 10f, paint);
            }
            x += 3;
        }

        float[] lines = new float[48];

        if(outputFeature0[17] > accuracy && outputFeature0[20] > accuracy){
            lines[0] = outputFeature0[16] * w;
            lines[1] = outputFeature0[15] * h;
            lines[2] = outputFeature0[19] * w;
            lines[3] = outputFeature0[18] * h;
        }


        if(outputFeature0[20] > accuracy && outputFeature0[26] > accuracy){
            lines[4] = outputFeature0[19] * w;
            lines[5] = outputFeature0[18] * h;
            lines[6] = outputFeature0[25] * w;
            lines[7] = outputFeature0[24] * h;
        }

        if(outputFeature0[17] > accuracy && outputFeature0[23] > accuracy){
            lines[8] = outputFeature0[16] * w;
            lines[9] = outputFeature0[15] * h;
            lines[10] = outputFeature0[22] * w;
            lines[11] = outputFeature0[21] * h;
        }

        if(outputFeature0[23] > accuracy && outputFeature0[29] > accuracy){
            lines[12] = outputFeature0[22] * w;
            lines[13] = outputFeature0[21] * h;
            lines[14] = outputFeature0[28] * w;
            lines[15] = outputFeature0[27] * h;
        }

        if(outputFeature0[26] > accuracy && outputFeature0[32] > accuracy){
            lines[16] = outputFeature0[25] * w;
            lines[17] = outputFeature0[24] * h;
            lines[18] = outputFeature0[31] * w;
            lines[19] = outputFeature0[30] * h;
        }

        if(outputFeature0[35] > accuracy && outputFeature0[38] > accuracy){
            lines[20] = outputFeature0[34] * w;
            lines[21] = outputFeature0[33] * h;
            lines[22] = outputFeature0[37] * w;
            lines[23] = outputFeature0[36] * h;
        }

        if(outputFeature0[17] > accuracy && outputFeature0[35] > accuracy){
            lines[24] = outputFeature0[16] * w;
            lines[25] = outputFeature0[15] * h;
            lines[26] = outputFeature0[34] * w;
            lines[27] = outputFeature0[33] * h;
        }

        if(outputFeature0[20] > accuracy && outputFeature0[38] > accuracy){
            lines[28] = outputFeature0[19] * w;
            lines[29] = outputFeature0[18] * h;
            lines[30] = outputFeature0[37] * w;
            lines[31] = outputFeature0[36] * h;
        }

        if(outputFeature0[35] > accuracy && outputFeature0[41] > accuracy){
            lines[32] = outputFeature0[34] * w;
            lines[33] = outputFeature0[33] * h;
            lines[34] = outputFeature0[40] * w;
            lines[35] = outputFeature0[39] * h;
        }

        if(outputFeature0[41] > accuracy && outputFeature0[47] > accuracy){
            lines[36] = outputFeature0[40] * w;
            lines[37] = outputFeature0[39] * h;
            lines[38] = outputFeature0[46] * w;
            lines[39] = outputFeature0[45] * h;
        }

        if(outputFeature0[38] > accuracy && outputFeature0[44] > accuracy){
            lines[40] = outputFeature0[37] * w;
            lines[41] = outputFeature0[36] * h;
            lines[42] = outputFeature0[43] * w;
            lines[43] = outputFeature0[42] * h;
        }

        if(outputFeature0[44] > accuracy && outputFeature0[50] > accuracy){
            lines[44] = outputFeature0[43] * w;
            lines[45] = outputFeature0[42] * h;
            lines[46] = outputFeature0[49] * w;
            lines[47] = outputFeature0[48] * h;
        }

        canvas.drawLines(lines, paint2);

        try {

            Date currentTime = Calendar.getInstance().getTime();
            String root = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES).toString();
            File myDir = new File(root + "/pose-detection-images");
            myDir.mkdirs();
            String fname = "image-op-" + currentTime.getTime() + ".jpg";
            File file = new File(myDir, fname);

            FileOutputStream out = new FileOutputStream(file);


            mutable.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();

            return true;

        } catch( Exception e) {
            Log.d("onBtnSavePng", e.toString());
            return false;

        }
    }

    public boolean captureAllTypes(Bitmap bitmap, Bitmap original, float[] outputFeature0){
        Paint paint = new Paint();
        Paint paint2 = new Paint();

        paint.setColor(Color.RED);
        paint2.setColor(Color.WHITE);

        float h = bitmap.getHeight();
        float w = bitmap.getWidth();

        Bitmap mutable = Bitmap.createBitmap((int)w, (int)h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mutable);

        int x = 0;

        while(x <= 49){
            if(outputFeature0[x+2] > accuracy ){
                canvas.drawCircle(outputFeature0[x+1] * w, outputFeature0[x] * h, 10f, paint);
            }
            x += 3;
        }

        float[] lines = new float[48];

        if(outputFeature0[17] > accuracy && outputFeature0[20] > accuracy){
            lines[0] = outputFeature0[16] * w;
            lines[1] = outputFeature0[15] * h;
            lines[2] = outputFeature0[19] * w;
            lines[3] = outputFeature0[18] * h;
        }


        if(outputFeature0[20] > accuracy && outputFeature0[26] > accuracy){
            lines[4] = outputFeature0[19] * w;
            lines[5] = outputFeature0[18] * h;
            lines[6] = outputFeature0[25] * w;
            lines[7] = outputFeature0[24] * h;
        }

        if(outputFeature0[17] > accuracy && outputFeature0[23] > accuracy){
            lines[8] = outputFeature0[16] * w;
            lines[9] = outputFeature0[15] * h;
            lines[10] = outputFeature0[22] * w;
            lines[11] = outputFeature0[21] * h;
        }

        if(outputFeature0[23] > accuracy && outputFeature0[29] > accuracy){
            lines[12] = outputFeature0[22] * w;
            lines[13] = outputFeature0[21] * h;
            lines[14] = outputFeature0[28] * w;
            lines[15] = outputFeature0[27] * h;
        }

        if(outputFeature0[26] > accuracy && outputFeature0[32] > accuracy){
            lines[16] = outputFeature0[25] * w;
            lines[17] = outputFeature0[24] * h;
            lines[18] = outputFeature0[31] * w;
            lines[19] = outputFeature0[30] * h;
        }

        if(outputFeature0[35] > accuracy && outputFeature0[38] > accuracy){
            lines[20] = outputFeature0[34] * w;
            lines[21] = outputFeature0[33] * h;
            lines[22] = outputFeature0[37] * w;
            lines[23] = outputFeature0[36] * h;
        }

        if(outputFeature0[17] > accuracy && outputFeature0[35] > accuracy){
            lines[24] = outputFeature0[16] * w;
            lines[25] = outputFeature0[15] * h;
            lines[26] = outputFeature0[34] * w;
            lines[27] = outputFeature0[33] * h;
        }

        if(outputFeature0[20] > accuracy && outputFeature0[38] > accuracy){
            lines[28] = outputFeature0[19] * w;
            lines[29] = outputFeature0[18] * h;
            lines[30] = outputFeature0[37] * w;
            lines[31] = outputFeature0[36] * h;
        }

        if(outputFeature0[35] > accuracy && outputFeature0[41] > accuracy){
            lines[32] = outputFeature0[34] * w;
            lines[33] = outputFeature0[33] * h;
            lines[34] = outputFeature0[40] * w;
            lines[35] = outputFeature0[39] * h;
        }

        if(outputFeature0[41] > accuracy && outputFeature0[47] > accuracy){
            lines[36] = outputFeature0[40] * w;
            lines[37] = outputFeature0[39] * h;
            lines[38] = outputFeature0[46] * w;
            lines[39] = outputFeature0[45] * h;
        }

        if(outputFeature0[38] > accuracy && outputFeature0[44] > accuracy){
            lines[40] = outputFeature0[37] * w;
            lines[41] = outputFeature0[36] * h;
            lines[42] = outputFeature0[43] * w;
            lines[43] = outputFeature0[42] * h;
        }

        if(outputFeature0[44] > accuracy && outputFeature0[50] > accuracy){
            lines[44] = outputFeature0[43] * w;
            lines[45] = outputFeature0[42] * h;
            lines[46] = outputFeature0[49] * w;
            lines[47] = outputFeature0[48] * h;
        }

        canvas.drawLines(lines, paint2);

        try {

            Date currentTime = Calendar.getInstance().getTime();
            String root = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES).toString();
            File myDir = new File(root + "/pose-detection-images");
            myDir.mkdirs();
            String fname1 = "image-" + currentTime.getTime() + ".jpg";
            String fname2 = "image-op-" + currentTime.getTime() + ".jpg";
            String fname3 = "image-og-" + currentTime.getTime() + ".jpg";

            File file1 = new File(myDir, fname1);
            File file2 = new File(myDir, fname2);
            File file3 = new File(myDir, fname3);

            FileOutputStream out1 = new FileOutputStream(file1);
            FileOutputStream out2 = new FileOutputStream(file2);
            FileOutputStream out3 = new FileOutputStream(file3);

            Bitmap bm = bitmap;
            bm.compress(Bitmap.CompressFormat.JPEG, 100, out1);
            out1.flush();
            out1.close();


            mutable.compress(Bitmap.CompressFormat.JPEG, 100, out2);
            out2.flush();
            out2.close();

            Bitmap bm2 = original;
            bm2.compress(Bitmap.CompressFormat.JPEG, 100, out3);
            out1.flush();
            out1.close();

            return true;

        } catch( Exception e) {
            Log.d("onBtnSavePng", e.toString());
            return false;

        }
    }

    public Bitmap drawPose(Bitmap bitmap, float[] outputFeature0){
        Paint paint = new Paint();
        Paint paint2 = new Paint();
        //Paint paint3 = new Paint();

        paint.setColor(Color.RED);
        paint2.setColor(Color.BLUE);
        //paint3.setColor(Color.RED);

        Bitmap mutable = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(mutable);
        float h = bitmap.getHeight();
        float w = bitmap.getWidth();
        int x = 0;

        //9 10 11   12 13 14

//        if(outputFeature0[2] > accuracy){
//            double radius = Math.sqrt((outputFeature0[9] - outputFeature0[12]) * h *(outputFeature0[9] - outputFeature0[12])  * h + (outputFeature0[10] - outputFeature0[13])  * w *(outputFeature0[10] - outputFeature0[13])  * w );
//
//            Log.i("Radius", String.valueOf(radius));
//
////            float left =
//
//
//            RectF rectF = new RectF(outputFeature0[1] * w, outputFeature0[0] * h, (float)radius, (float)radius);
//            canvas.drawOval(rectF, paint3);
//        }

        while(x <= 49){
            if(outputFeature0[x+2] > accuracy ){
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

        if(outputFeature0[17] > accuracy && outputFeature0[20] > accuracy){
            lines[0] = outputFeature0[16] * w;
            lines[1] = outputFeature0[15] * h;
            lines[2] = outputFeature0[19] * w;
            lines[3] = outputFeature0[18] * h;
        }


        if(outputFeature0[20] > accuracy && outputFeature0[26] > accuracy){
            lines[4] = outputFeature0[19] * w;
            lines[5] = outputFeature0[18] * h;
            lines[6] = outputFeature0[25] * w;
            lines[7] = outputFeature0[24] * h;
        }

        if(outputFeature0[17] > accuracy && outputFeature0[23] > accuracy){
            lines[8] = outputFeature0[16] * w;
            lines[9] = outputFeature0[15] * h;
            lines[10] = outputFeature0[22] * w;
            lines[11] = outputFeature0[21] * h;
        }

        if(outputFeature0[23] > accuracy && outputFeature0[29] > accuracy){
            lines[12] = outputFeature0[22] * w;
            lines[13] = outputFeature0[21] * h;
            lines[14] = outputFeature0[28] * w;
            lines[15] = outputFeature0[27] * h;
        }

        if(outputFeature0[26] > accuracy && outputFeature0[32] > accuracy){
            lines[16] = outputFeature0[25] * w;
            lines[17] = outputFeature0[24] * h;
            lines[18] = outputFeature0[31] * w;
            lines[19] = outputFeature0[30] * h;
        }

        if(outputFeature0[35] > accuracy && outputFeature0[38] > accuracy){
            lines[20] = outputFeature0[34] * w;
            lines[21] = outputFeature0[33] * h;
            lines[22] = outputFeature0[37] * w;
            lines[23] = outputFeature0[36] * h;
        }

        if(outputFeature0[17] > accuracy && outputFeature0[35] > accuracy){
            lines[24] = outputFeature0[16] * w;
            lines[25] = outputFeature0[15] * h;
            lines[26] = outputFeature0[34] * w;
            lines[27] = outputFeature0[33] * h;
        }

        if(outputFeature0[20] > accuracy && outputFeature0[38] > accuracy){
            lines[28] = outputFeature0[19] * w;
            lines[29] = outputFeature0[18] * h;
            lines[30] = outputFeature0[37] * w;
            lines[31] = outputFeature0[36] * h;
        }

        if(outputFeature0[35] > accuracy && outputFeature0[41] > accuracy){
            lines[32] = outputFeature0[34] * w;
            lines[33] = outputFeature0[33] * h;
            lines[34] = outputFeature0[40] * w;
            lines[35] = outputFeature0[39] * h;
        }

        if(outputFeature0[41] > accuracy && outputFeature0[47] > accuracy){
            lines[36] = outputFeature0[40] * w;
            lines[37] = outputFeature0[39] * h;
            lines[38] = outputFeature0[46] * w;
            lines[39] = outputFeature0[45] * h;
        }

        if(outputFeature0[38] > accuracy && outputFeature0[44] > accuracy){
            lines[40] = outputFeature0[37] * w;
            lines[41] = outputFeature0[36] * h;
            lines[42] = outputFeature0[43] * w;
            lines[43] = outputFeature0[42] * h;
        }

        if(outputFeature0[44] > accuracy && outputFeature0[50] > accuracy){
            lines[44] = outputFeature0[43] * w;
            lines[45] = outputFeature0[42] * h;
            lines[46] = outputFeature0[49] * w;
            lines[47] = outputFeature0[48] * h;
        }

        canvas.drawLines(lines, paint2);
        return mutable;
    }
    public Bitmap drawPose(Bitmap bitmap, float[] outputFeature0, float[] outputFeature1){
        Bitmap mutable = bitmap.copy(Bitmap.Config.ARGB_8888, true);

        mutable = drawPose(mutable, outputFeature0);
        mutable = drawPose(mutable, outputFeature1);

        return mutable;
    }

    public Bitmap loadImage(String path, int index){


        File f = new File(path);
        Log.i("Path image", path);
        File[] files = f.listFiles();

        if (files.length == 0)
            return null;


        try {

            return BitmapFactory.decodeStream(new FileInputStream(files[index].getPath()));
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public String getNameImage (String path, int index){
        File f = new File(path);
        Log.i("Path image", path);
        File[] files = f.listFiles();

        if (files.length == 0)
            return null;
        return files[index].getName();
    }
//    public List<Bitmap> loadImages(String path){
//        List<Bitmap> bitmaps = new ArrayList<>();
//        File f = new File(path);
//        File[] files = f.listFiles();
//
//        for (int i=0; i < 5; i++)
//        {
//            bitmaps.add(loadImage(files[i].getPath(0)));
//            Log.i("Load image", "success");
//        }
//
//        return bitmaps;
//    }

}
