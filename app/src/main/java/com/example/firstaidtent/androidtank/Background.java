package com.example.firstaidtent.androidtank;

import com.example.firstaidtent.framework.Image;

class Background {

    private int bgX;
    private int bgY;
    private int speedX;
    private Image image;

    public Background(int x, int y, Image image) {
        bgX = x;
        bgY = y;
        speedX = 0;
        this.image = image;
    }

    public void update() {
        bgX += speedX;

        if (bgX <= -image.getWidth()) {
            bgX += image.getWidth() * 2;
        }
    }

    public int getBgX() {
        return bgX;
    }

    public void setBgX(int bgX) {
        this.bgX = bgX;
    }

    public int getBgY() {
        return bgY;
    }

    public void setBgY(int bgY) {
        this.bgY = bgY;
    }

    public int getSpeedX() {
        return speedX;
    }

    public void setSpeedX(int speedX) {
        this.speedX = speedX;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }
}