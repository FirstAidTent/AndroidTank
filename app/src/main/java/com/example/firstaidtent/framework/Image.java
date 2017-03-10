package com.example.firstaidtent.framework;

import android.graphics.Bitmap;

import com.example.firstaidtent.framework.Graphics.ImageFormat;

public interface Image {
    public int getWidth();

    public int getHeight();

    public ImageFormat getFormat();

    public void setFormat(ImageFormat format);

    public Bitmap getBitmap();

    public void setBitmap(Bitmap bitmap);

    public void dispose();
}