package com.example.firstaidtent.androidtank;

import android.graphics.Rect;

import java.util.ArrayList;

class Heliboy extends Enemy {
    private double distanceMoved;
    private int facing;

    Heliboy(double centerX, double centerY, int facing) {
        super(centerX, centerY, 1, Assets.heliboy[0]);

        isAffectedByGravity = false;

        Animation a = new Animation();
        if (facing == 0) {
            a.addFrame(Assets.heliboy[0], 100);
            a.addFrame(Assets.heliboy[1], 100);
            a.addFrame(Assets.heliboy[2], 100);
            a.addFrame(Assets.heliboy[3], 100);
            a.addFrame(Assets.heliboy[4], 100);
            a.addFrame(Assets.heliboy[3], 100);
            a.addFrame(Assets.heliboy[2], 100);
            a.addFrame(Assets.heliboy[1], 100);
        } else {
            sprite = Assets.heliboyF[0];
            a.addFrame(Assets.heliboyF[0], 100);
            a.addFrame(Assets.heliboyF[1], 100);
            a.addFrame(Assets.heliboyF[2], 100);
            a.addFrame(Assets.heliboyF[3], 100);
            a.addFrame(Assets.heliboyF[4], 100);
            a.addFrame(Assets.heliboyF[3], 100);
            a.addFrame(Assets.heliboyF[2], 100);
            a.addFrame(Assets.heliboyF[1], 100);
        }
        anim = a;

        spriteX = (int) centerX - 48;
        spriteY = (int) centerY - 48;

        distanceMoved = 0;
        this.facing = facing;
    }

    void update() {
        if (anim != null) {
            sprite = anim.getImage();
        }

        if (facing == 0) {
            speedX = -1.00;
        } else {
            speedX = 1.00;
        }
        distanceMoved += Math.abs(speedX);

        //speedX = (bg.getSpeedX() * 5 + movementSpeed);
        centerX += Math.round(speedX);

        speedY = 60 * Math.sin(Math.toRadians(distanceMoved)) - 60 * Math.sin(Math.toRadians(distanceMoved - speedX));

        // Simulates the gravity. If you want something not affected by gravity e.g. flying enemies,
        // just set isAffectedByGravity to false.
        if (isAffectedByGravity) {
            speedY += 1.00;
        }
        centerY += speedY;

        spriteX = (int) centerX - 48;
        spriteY = (int) centerY - 48;

        updateHitbox();
        checkCollision();

        if (health <= 0) {
            die();
        }

        if ((centerX < -100 || centerX > 900) || (centerY < -100 || centerY > 580)) {
            die();
        }
    }

    @Override
    void checkCollision() {
        ArrayList<Tank> tanks = Tank.getTanks();
        for (int i = 0; i < tanks.size(); i++) {
            Tank t = tanks.get(i);
            if (Rect.intersects(hitbox, t.getHitbox())) {
                health = 0;
            }
        }
    }

    @Override
    void updateHitbox() {
        hitbox.set((int) getCenterX() - 25, (int) getCenterY() - 25, (int) getCenterX() + 25, (int) getCenterY() + 35);
    }
}