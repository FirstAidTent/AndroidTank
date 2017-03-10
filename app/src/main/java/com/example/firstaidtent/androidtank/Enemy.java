package com.example.firstaidtent.androidtank;

import android.graphics.Rect;

import com.example.firstaidtent.framework.Image;

import java.util.ArrayList;

/*
 * The common Enemy class. All enemies you want in the game should extend this class.
 * In your new class, you have to set the sprite or create a new Animation and add frames to it.
 * You also have to set the spriteX and s
 * Creating an Enemy object using the constructor will create an enemy without sprite, which will
 * make the game crash when it tries to draw it.
 */

abstract class Enemy {

    // Enemy Stats
    protected int health;
    protected boolean isAffectedByGravity;

    // The center of where the enemy will appear.
    protected double centerX;
    protected double centerY;

    // The speed of the enemy. The enemy will move by this value every frame update.
    protected double speedX;
    protected double speedY;

    // The sprite of the enemy. In case you have an animation, you should set sprite to the
    // first frame of your animation. spriteX and spriteY is where the upper-left corner of your
    // image will be. They are usually (sprite width / 2) from the center.
    // The spriteX and spriteY are also used even if you only have an animation.
    protected Image sprite;
    protected int spriteX;
    protected int spriteY;

    // Animation for the enemy.
    protected Animation anim;

    // Hitbox of the enemy.
    protected Rect hitbox;

    // Arraylist where all enemies are stored.
    protected static ArrayList<Enemy> enemies = new ArrayList<>();

    Enemy(double centerX, double centerY, int health, Image sprite) {
        this.centerX = centerX;
        this.centerY = centerY;

        this.health = health;
        isAffectedByGravity = true;

        speedX = 0;
        speedY = 0;

        this.sprite = sprite;
        anim = null;

        spriteX = (int) centerX - sprite.getWidth();
        spriteY = (int) centerY - sprite.getHeight();

        hitbox = new Rect(0, 0, 0, 0);

        enemies.add(this);
    }

    // The default update method for behavior methods for enemies.
    // Override it if you want to change enemy behavior.
    void update() {
        if (anim != null) {
            sprite = anim.getImage();
        }

        //speedX = (bg.getSpeedX() * 5 + movementSpeed);
        centerX += Math.round(speedX);

        // Simulates the gravity. If you want something not affected by gravity e.g. flying enemies,
        // just set isAffectedByGravity to false.
        if (isAffectedByGravity) {
            speedY += 1.00;
        }
        centerY += Math.round(speedY);

        spriteX = (int) centerX - sprite.getWidth();
        spriteY = (int) centerY - sprite.getHeight();

        updateHitbox();
        checkCollision();

        if (health <= 0) {
            die();
        }
    }

    // Abstract methods

    // What to do when the enemy collides with something.
    // Override this method and create a check for collision inside your method.
    abstract void checkCollision();

    // The enemies hitbox needs to be updated every time the enemy moves.
    abstract void updateHitbox();

    // What happens when the enemy dies.
    void die() {
        getEnemies().remove(this);
    }

    // Getters and Setters
    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public boolean isAffectedByGravity() {
        return isAffectedByGravity;
    }

    public void setAffectedByGravity(boolean affectedByGravity) {
        isAffectedByGravity = affectedByGravity;
    }

    public double getSpeedX() {
        return speedX;
    }

    public void setSpeedX(double speedX) {
        this.speedX = speedX;
    }

    public double getSpeedY() {
        return speedY;
    }

    public void setSpeedY(double speedY) {
        this.speedY = speedY;
    }

    public double getCenterX() {
        return centerX;
    }

    public void setCenterX(double centerX) {
        this.centerX = centerX;
    }

    public double getCenterY() {
        return centerY;
    }

    public void setCenterY(double centerY) {
        this.centerY = centerY;
    }

    public Image getSprite() {
        return sprite;
    }

    public void setSprite(Image sprite) {
        this.sprite = sprite;
    }

    public int getSpriteX() {
        return spriteX;
    }

    public void setSpriteX(int spriteX) {
        this.spriteX = spriteX;
    }

    public int getSpriteY() {
        return spriteY;
    }

    public void setSpriteY(int spriteY) {
        this.spriteY = spriteY;
    }

    public Animation getAnim() {
        return anim;
    }

    public void setAnim(Animation anim) {
        this.anim = anim;
    }

    public Rect getHitbox() {
        return hitbox;
    }

    public void setHitbox(Rect hitbox) {
        this.hitbox = hitbox;
    }

    public static ArrayList<Enemy> getEnemies() {
        return enemies;
    }

    public static void setEnemies(ArrayList<Enemy> enemies) {
        Enemy.enemies = enemies;
    }
}