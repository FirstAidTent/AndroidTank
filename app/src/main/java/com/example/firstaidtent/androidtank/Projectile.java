package com.example.firstaidtent.androidtank;

import android.graphics.Rect;

import com.example.firstaidtent.framework.Image;

import java.util.ArrayList;

class Projectile {

    private int x, y;
    private double startX, startY;
    private double speed;
    private double angle;
    private boolean visible;
    private Image sprite;
    private Rect AoE;

    private double moveCounter = 0.00;

    private static ArrayList<Projectile> projectiles = new ArrayList<>();

    Projectile(int startX, int startY) {
        this.startX = startX;
        this.startY = startY;
        x = startX;
        y = startY;
        angle = 0.00;
        speed = 5;
        visible = true;
        sprite = Assets.projectile;

        AoE = new Rect(0, 0, 0, 0);
    }

    Projectile(int startX, int startY, double angle) {
        this.startX = startX;
        this.startY = startY;
        x = startX;
        y = startY;
        this.angle = angle;
        speed = 5;
        visible = true;
        sprite = Assets.projectile;

        AoE = new Rect(0, 0, 0, 0);
    }

    Projectile(int startX, int startY, int speed, Image sprite) {
        this.startX = startX;
        this.startY = startY;
        x = startX;
        y = startY;
        angle = 0.00;
        this.speed = speed;
        visible = true;
        this.sprite = sprite;

        AoE = new Rect(0, 0, 0, 0);
    }

    public void update() {
        moveCounter += 1.00;
        x = (int)(startX + speed * Math.cos(angle) * moveCounter);
        y = (int)(startY + speed * Math.sin(angle) * moveCounter);
        AoE.set(x - sprite.getWidth() / 2, y - sprite.getHeight() / 2,
                x + sprite.getWidth() / 2, y + sprite.getHeight() / 2);
        if ((x > -10 && x <= 810) && (y > -10 && y <= 490)) {
            checkCollision();
        } else {
            visible = false;
            AoE = null;
        }

        if (!visible) {
            projectiles.remove(this);
        }
    }

    private void checkCollision() {
        for (int i = 0; i < Enemy.getEnemies().size(); i++) {
            Enemy e = Enemy.getEnemies().get(i);
            if (Rect.intersects(AoE, e.getHitbox())) {
                visible = false;

                if (e.getHealth() > 0) {
                    e.setHealth(e.getHealth() - 1);
                }

                if (e.getHealth() == 0) {
                    e.die();
                }
            }
        }
    }


    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public double getSpeed() {
        return speed;
    }


    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public Image getSprite() {
        return sprite;
    }

    public void setSprite(Image sprite) {
        this.sprite = sprite;
    }

    public static ArrayList<Projectile> getProjectiles() {
        return projectiles;
    }

    public static void setProjectiles(ArrayList<Projectile> projectiles) {
        Projectile.projectiles = projectiles;
    }
}