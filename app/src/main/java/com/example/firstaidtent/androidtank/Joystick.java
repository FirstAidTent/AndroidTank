package com.example.firstaidtent.androidtank;

import com.example.firstaidtent.framework.Image;

public class Joystick {
    public enum DIRECTION {
        UP, DOWN, LEFT, RIGHT, UP_LEFT, UP_RIGHT, DOWN_LEFT, DOWN_RIGHT, NULL
    }

    private int centerX;
    private int centerY;

    private int middleX;
    private int middleY;

    private double angle;
    private double radius;

    private boolean isTouched;
    private int touchId;
    private DIRECTION direction;

    private Image spriteCircle;
    private Image spriteMiddle;

    private int spriteCircleX;
    private int spriteCircleY;

    private int spriteMiddleX;
    private int spriteMiddleY;

    public Joystick(int x, int y) {
        centerX = x;
        centerY = y;

        middleX = x;
        middleY = y;

        spriteCircle = Assets.joystickCircle;
        spriteMiddle = Assets.joystickMiddle;

        angle = 0.00;
        radius = 50 - spriteMiddle.getWidth() / 2;

        isTouched = false;
        touchId = -1;
        direction = DIRECTION.NULL;

        spriteCircleX = centerX - spriteCircle.getWidth() / 2;
        spriteCircleY = centerY - spriteCircle.getHeight() / 2;

        spriteMiddleX = middleX - spriteMiddle.getWidth() / 2;
        spriteMiddleY = middleY - spriteMiddle.getHeight() / 2;
    }

    public DIRECTION update(int x, int y) {
        angle = calcAngle(x, y);

        if (Math.sqrt(Math.pow((x - centerX), 2) + Math.pow((y - centerY), 2)) >= radius){
            middleX = centerX + (int)(radius * Math.cos(Math.toRadians(angle)));
            middleY = centerY + (int)(radius * Math.sin(Math.toRadians(angle)));
        } else {
            middleX = x;
            middleY = y;
        }

        spriteMiddleX = middleX - spriteMiddle.getWidth() / 2;
        spriteMiddleY = middleY - spriteMiddle.getHeight() / 2;

        if ((angle >= 0.00 && angle < 30.00) || (angle >= 330.00 && angle < 360.00)) {
            direction = DIRECTION.RIGHT;
        }
        if (angle >= 30.00 && angle < 60.00) {
            direction = DIRECTION.DOWN_RIGHT;
        }
        if (angle >= 60.00 && angle < 120.00) {
            direction = DIRECTION.DOWN;
        }
        if (angle >= 120.00 && angle < 150.00) {
            direction = DIRECTION.DOWN_LEFT;
        }
        if (angle >= 150.00 && angle < 210.00) {
            direction = DIRECTION.LEFT;
        }
        if (angle >= 210.00 && angle < 240.00) {
            direction = DIRECTION.UP_LEFT;
        }
        if (angle >= 240.00 && angle < 300.00) {
            direction = DIRECTION.UP;
        }
        if (angle >= 300.00 && angle < 330.00) {
            direction = DIRECTION.UP_RIGHT;
        }

        return direction;
    }

    private double calcAngle(int x, int y) {
        double angle = Math.toDegrees(Math.atan2(y - centerY, x - centerX));

        if (angle < 0) {
            angle += 360.00;
        }

        return angle;
    }

    public void reset() {
        middleX = centerX;
        middleY = centerY;
        spriteMiddleX = middleX - spriteMiddle.getWidth() / 2;
        spriteMiddleY = middleY - spriteMiddle.getHeight() / 2;
        direction = DIRECTION.NULL;
        touchId = -1;
    }

    public int getCenterX() {
        return centerX;
    }

    public void setCenterX(int centerX) {
        this.centerX = centerX;
    }

    public int getCenterY() {
        return centerY;
    }

    public void setCenterY(int centerY) {
        this.centerY = centerY;
    }

    public int getMiddleX() {
        return middleX;
    }

    public void setMiddleX(int middleX) {
        this.middleX = middleX;
    }

    public int getMiddleY() {
        return middleY;
    }

    public void setMiddleY(int middleY) {
        this.middleY = middleY;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public boolean isTouched() {
        return isTouched;
    }

    public void setTouched(boolean touched) {
        isTouched = touched;
    }

    public int getTouchId() {
        return touchId;
    }

    public void setTouchId(int touchId) {
        this.touchId = touchId;
    }

    public DIRECTION getDirection() {
        return direction;
    }

    public void setDirection(DIRECTION direction) {
        this.direction = direction;
    }

    public Image getSpriteCircle() {
        return spriteCircle;
    }

    public void setSpriteCircle(Image spriteCircle) {
        this.spriteCircle = spriteCircle;
    }

    public Image getSpriteMiddle() {
        return spriteMiddle;
    }

    public void setSpriteMiddle(Image spriteMiddle) {
        this.spriteMiddle = spriteMiddle;
    }

    public int getSpriteCircleX() {
        return spriteCircleX;
    }

    public void setSpriteCircleX(int spriteCircleX) {
        this.spriteCircleX = spriteCircleX;
    }

    public int getSpriteCircleY() {
        return spriteCircleY;
    }

    public void setSpriteCircleY(int spriteCircleY) {
        this.spriteCircleY = spriteCircleY;
    }

    public int getSpriteMiddleX() {
        return spriteMiddleX;
    }

    public void setSpriteMiddleX(int spriteMiddleX) {
        this.spriteMiddleX = spriteMiddleX;
    }

    public int getSpriteMiddleY() {
        return spriteMiddleY;
    }

    public void setSpriteMiddleY(int spriteMiddleY) {
        this.spriteMiddleY = spriteMiddleY;
    }
}
