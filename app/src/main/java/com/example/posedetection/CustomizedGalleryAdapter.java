package com.example.posedetection;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import java.util.List;

public class CustomizedGalleryAdapter extends BaseAdapter {


    private final Context context;
    private final List<Bitmap> images;


    public CustomizedGalleryAdapter(Context c, List<Bitmap> images) {
        context = c;
        this.images = images;
    }


    public int getCount() {
        return images.size();
    }


    public Object getItem(int position) {
        return position;
    }


    public long getItemId(int position) {
        return position;
    }


    public View getView(int position, View convertView, ViewGroup parent) {

        ImageView imageView = new ImageView(context);


        imageView.setImageBitmap(images.get(position));


        imageView.setLayoutParams(new Gallery.LayoutParams(200, 200));
        return imageView;
    }

    public Bitmap getImages(int position) {
        return images.get(position);
    }

}
