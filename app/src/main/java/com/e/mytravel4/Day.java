package com.e.mytravel4;

import android.graphics.Bitmap;

public class Day {
    private String details1;
    private String details2;
    private Bitmap image;

    public Day(String details1, String details2, Bitmap image) {
        this.details1 = details1;
        this.details2 = details2;
        this.image = image;
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
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
