package com.e.mytravel4;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

import io.realm.RealmObject;

public class Day extends RealmObject {
    private String details1;
    private String details2;
 //   private Bitmap image;
    private byte[] imageBytes;

    public Day() {

    }

    public Day(String details1, String details2, Bitmap image) {
        this.details1 = details1;
        this.details2 = details2;
        this.imageBytes = bitmapToByteArray(image);
    }

    public String getDetails1() {
        return details1;
    }

    public void setDetails1(String details1) {
        this.details1 = details1;
    }

    public String getDetails2() {
        return details2;
    }

    public void setDetails2(String details2) {
        this.details2 = details2;
    }

    public Bitmap getImage() {
        return byteArrayToBitmap(imageBytes);
    }

    public void setImage(Bitmap image) {
        this.imageBytes = bitmapToByteArray(image);
    }

    private byte[] bitmapToByteArray(Bitmap bmp){
        byte[] byteArray = new byte[1];
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        if(bmp != null){
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byteArray = stream.toByteArray();
        }

        return byteArray;
    }

    private Bitmap byteArrayToBitmap(byte[] bitmapdata){
        Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.length);
        return bitmap;
    }

}
