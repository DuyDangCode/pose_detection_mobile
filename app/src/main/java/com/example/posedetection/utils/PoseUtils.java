package com.example.posedetection.utils;

import android.util.Log;

public class PoseUtils {

    private int pos;

    public PoseUtils() {
        this.pos = 0;
    }

    public boolean comparePoseAngle(float[] p1, float[] p2, float w, float h, double accuracy, int matchScore){

//        if(compareAngle(p1[19] * w, p1[18] * h, p1[25] * w, p1[24] * h, p1[31] * w, p1[30] * h, p2[19] * w, p2[18] * h, p2[25] * w, p2[24] * h, p2[31] * w, p2[30] * h, 10) &&
//                compareAngle(p1[16] * w, p1[15] * h, p1[22] * w, p1[21] * h, p1[28] * w, p1[27] * h, p2[16] * w, p2[15] * h, p2[22] * w, p2[21] * h, p2[28] * w, p2[27] * h,10) &&
//                compareAngle(p1[25] * w, p1[24] * h, p1[19] * w, p1[18] * h, p1[37] * w, p1[36] * h, p2[25] * w, p2[24] * h, p2[19] * w, p2[18] * h, p2[37] * w, p2[36] * h, 10) &&
//                compareAngle(p1[22] * w, p1[21] * h, p1[16] * w, p1[15] * h, p1[34] * w, p1[33] * h, p2[22] * w, p2[21] * h, p2[16] * w, p2[15] * h, p2[34] * w, p2[33] * h, 10) &&
//                compareAngle(p1[19] * w, p1[18] * h, p1[37] * w, p1[36] * h, p1[43] * w, p1[42] * h, p2[19] * w, p2[18] * h, p2[37] * w, p2[36] * h, p2[43] * w, p2[42] * h, 10) &&
//                compareAngle(p1[16] * w, p1[15] * h, p1[34] * w, p1[33] * h, p1[40] * w, p1[39] * h, p2[16] * w, p2[15] * h, p2[34] * w, p2[33] * h, p2[40] * w, p2[39] * h, 10) &&
//                compareAngle(p1[37] * w, p1[36] * h, p1[43] * w, p1[42] * h, p1[49] * w, p1[48] * h, p2[37] * w, p2[36] * h, p2[43] * w, p2[42] * h, p2[49] * w, p2[48] * h, 10) &&
//                compareAngle(p1[34] * w, p1[33] * h, p1[40] * w, p1[39] * h, p1[46] * w, p1[45] * h, p2[34] * w, p2[33] * h, p2[40] * w, p2[39] * h, p2[46] * w, p2[45] * h, 10))
//            return true;
//        return false;

        int flag = 0;
        boolean result = true;
        if(p1[20] >= accuracy && p1[26] >= accuracy){
            result = result && compareAngle(p1[19] * w, p1[18] * h, p1[25] * w, p1[24] * h, p1[31] * w, p1[30] * h, p2[19] * w, p2[18] * h, p2[25] * w, p2[24] * h, p2[31] * w, p2[30] * h, matchScore);
            flag++;
        }
        if(p1[17] >= accuracy && p1[23] >= accuracy){
            result = result && compareAngle(p1[16] * w, p1[15] * h, p1[22] * w, p1[21] * h, p1[28] * w, p1[27] * h, p2[16] * w, p2[15] * h, p2[22] * w, p2[21] * h, p2[28] * w, p2[27] * h,matchScore);
            flag++;
        }
        if(p1[26] >= accuracy && p1[20] >= accuracy){
            result = result && compareAngle(p1[25] * w, p1[24] * h, p1[19] * w, p1[18] * h, p1[37] * w, p1[36] * h, p2[25] * w, p2[24] * h, p2[19] * w, p2[18] * h, p2[37] * w, p2[36] * h, matchScore);
            flag++;
        }
        if(p1[23] >= accuracy && p1[17] >= accuracy){
            result = result && compareAngle(p1[22] * w, p1[21] * h, p1[16] * w, p1[15] * h, p1[34] * w, p1[33] * h, p2[22] * w, p2[21] * h, p2[16] * w, p2[15] * h, p2[34] * w, p2[33] * h, matchScore);
            flag++;
        }
        if(p1[20] >= accuracy && p1[38] >= accuracy){
            result = result && compareAngle(p1[19] * w, p1[18] * h, p1[37] * w, p1[36] * h, p1[43] * w, p1[42] * h, p2[19] * w, p2[18] * h, p2[37] * w, p2[36] * h, p2[43] * w, p2[42] * h, matchScore);
            flag++;
        }
        if(p1[17] >= accuracy && p1[35] >= accuracy){
            result = result && compareAngle(p1[16] * w, p1[15] * h, p1[34] * w, p1[33] * h, p1[40] * w, p1[39] * h, p2[16] * w, p2[15] * h, p2[34] * w, p2[33] * h, p2[40] * w, p2[39] * h, matchScore);
            flag++;
        }
        if(p1[38] >= accuracy && p1[44] >= accuracy){
            result = result && compareAngle(p1[37] * w, p1[36] * h, p1[43] * w, p1[42] * h, p1[49] * w, p1[48] * h, p2[37] * w, p2[36] * h, p2[43] * w, p2[42] * h, p2[49] * w, p2[48] * h, matchScore);
            flag++;
        }
        if(p1[35] >= accuracy && p1[41] >= accuracy){
            result = result && compareAngle(p1[34] * w, p1[33] * h, p1[40] * w, p1[39] * h, p1[46] * w, p1[45] * h, p2[34] * w, p2[33] * h, p2[40] * w, p2[39] * h, p2[46] * w, p2[45] * h, matchScore);
            flag++;
        }

//        if(compareAngle(p1[19] * w, p1[18] * h, p1[25] * w, p1[24] * h, p1[31] * w, p1[30] * h, p2[19] * w, p2[18] * h, p2[25] * w, p2[24] * h, p2[31] * w, p2[30] * h, 15) &&
//                compareAngle(p1[16] * w, p1[15] * h, p1[22] * w, p1[21] * h, p1[28] * w, p1[27] * h, p2[16] * w, p2[15] * h, p2[22] * w, p2[21] * h, p2[28] * w, p2[27] * h,15) &&
//
//                compareAngle(p1[25] * w, p1[24] * h, p1[19] * w, p1[18] * h, p1[37] * w, p1[36] * h, p2[25] * w, p2[24] * h, p2[19] * w, p2[18] * h, p2[37] * w, p2[36] * h, 15) &&
//
//                compareAngle(p1[22] * w, p1[21] * h, p1[16] * w, p1[15] * h, p1[34] * w, p1[33] * h, p2[22] * w, p2[21] * h, p2[16] * w, p2[15] * h, p2[34] * w, p2[33] * h, 15) &&
//
//                compareAngle(p1[19] * w, p1[18] * h, p1[37] * w, p1[36] * h, p1[43] * w, p1[42] * h, p2[19] * w, p2[18] * h, p2[37] * w, p2[36] * h, p2[43] * w, p2[42] * h, 15) &&
//
//                compareAngle(p1[16] * w, p1[15] * h, p1[34] * w, p1[33] * h, p1[40] * w, p1[39] * h, p2[16] * w, p2[15] * h, p2[34] * w, p2[33] * h, p2[40] * w, p2[39] * h, 15) &&
//
//                compareAngle(p1[37] * w, p1[36] * h, p1[43] * w, p1[42] * h, p1[49] * w, p1[48] * h, p2[37] * w, p2[36] * h, p2[43] * w, p2[42] * h, p2[49] * w, p2[48] * h, 15) &&
//
//                compareAngle(p1[34] * w, p1[33] * h, p1[40] * w, p1[39] * h, p1[46] * w, p1[45] * h, p2[34] * w, p2[33] * h, p2[40] * w, p2[39] * h, p2[46] * w, p2[45] * h, 15))
//            return true;
//        return false;


        return flag != 0 ? result : false;
    }

    private boolean compareAngle(float x1, float y1, float x2 ,float y2,float x3 ,float y3,float x12, float y12, float x22 ,float y22,float x32 ,float y32,double a){

        Log.i("Angle", String.valueOf(pos));
        double a1 = calAngle(x1, y1, x2, y2, x3, y3);
        Log.i("Angle 1: ", String.valueOf(a1));
        double a2 = calAngle(x12, y12, x22, y22, x32, y32);
        Log.i("Angle 2: ", String.valueOf(a2));

        pos++;

        if(Math.abs(a1-a2) <= a){
            return true;
        }
        return false;
    }



    private double calAngle(float x1, float y1, float x2, float y2, float x3, float y3){
        double angle = 0;
        double[] s0 = {x1 ,y1,0};
        double[] s1 = {x2 ,y2,0};
        double[] s2 = {x3 ,y3,0};
        angle = computeAngle(s1, s0, s2);

        //Log.i("Angle", String.valueOf(angle));
        return angle;
    }


    ///
    public double computeAngle(double[] p0, double[] p1, double[] p2) {
        double[] v0 = createVector(p0, p1);
        double[] v1 = createVector(p0, p2);

        double dotProduct = computeDotProduct(v0, v1);

        double length1 = length(v0);
        double length2 = length(v1);




        double denominator = length1 * length2;

         double product = denominator != 0.0 ? dotProduct / denominator : 0.0;


        //Log.i("product: ", String.valueOf(product));
        double angle = Math.acos(product) /  3.141592653589793 * 180;


        return angle;
    }
    /**
     * Construct the vector specified by two points.
     *
     * @param p0, p1 Points the construct vector between [x,y,z].
     * @return v Vector from p0 to p1 [x,y,z].
     */
    public double[] createVector(double[] p0, double[] p1) {
        double v[] = { p1[0] - p0[0], p1[1] - p0[1], p1[2] - p0[2] };
        return v;
    }
    /**
     * Compute the dot product (a scalar) between two vectors.
     *
     * @param v0, v1 Vectors to compute dot product between [x,y,z].
     * @return Dot product of given vectors.
     */
    public double computeDotProduct(double[] v0, double[] v1) {
        return v0[0] * v1[0] + v0[1] * v1[1] + v0[2] * v1[2];
    }
    /**
     * Return the length of a vector.
     *
     * @param v Vector to compute length of [x,y,z].
     * @return Length of vector.
     */
    public double length(double[] v) {
        return Math.sqrt(v[0] * v[0] + v[1] * v[1] + v[2] * v[2]);
    }
    /**
     * Compute distance between two points.
     *
     * @param p0, p1 Points to compute distance between [x,y,z].
     * @return Distance between points.
     */
    public double length(double[] p0, double[] p1) {
        double[] v = createVector(p0, p1);
        return length(v);
    }
    /**
     * Compute the length of the line from (x0,y0) to (x1,y1)
     *
     * @param x0, y0 First line end point.
     * @param x1, y1 Second line end point.
     * @return Length of line from (x0,y0) to (x1,y1).
     */
    public double length(int x0, int y0, int x1, int y1) {
        return length((double) x0, (double) y0, (double) x1,
                (double) y1);
    }
    /**
     * Compute the length of the line from (x0,y0) to (x1,y1)
     *
     * @param x0, y0 First line end point.
     * @param x1, y1 Second line end point.
     * @return Length of line from (x0,y0) to (x1,y1).
     */
    public double length(double x0, double y0, double x1, double y1) {
        double dx = x1 - x0;
        double dy = y1 - y0;

        return Math.sqrt(dx * dx + dy * dy);
    }
//    /**
//     * Compute the length of a polyline.
//     *
//     * @param x, y Arrays of x,y coordinates
//     * @param nPoints Number of elements in the above.
//     * @param isClosed True if this is a closed polygon, false otherwise
//     * @return Length of polyline defined by x, y and nPoints.
//     */
    public double length(int[] x, int[] y, boolean isClosed) {
        double length = 0.0;

        int nPoints = x.length;
        for (int i = 0; i < nPoints - 1; i++) {
            length += length(x[i], y[i], x[i + 1], y[i + 1]);
        }

        // Add last leg if this is a polygon
        if (isClosed && nPoints > 1) {
            length += length(x[nPoints - 1], y[nPoints - 1],
                    x[0], y[0]);
        }

        return length;
    }
}