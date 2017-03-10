package com.example.firstaidtent.androidtank;

import android.graphics.Rect;

import com.example.firstaidtent.framework.Image;

import java.util.ArrayList;

public class Tank {
    // Constants
    private final double MOVE_SPEED = 5.00; // In pixels per frame update
    private final double ROTATE_SPEED = 120.00; // Muzzle rotation speed in degrees per second
    private final double SHOOT_CD = 500.00; // In milliseconds. May not be exact depending on frame update frequency.
    private final double JUMP_SPEED = 20.00; // The speed the tank jump starts with.
    private final double GRAVITY_SPEED = 1.00; // The speed in which gravity drags the tank down
    private final double PHASE_DURATION = 200.00;

    // The x and y of the center of the tank
    private double centerX;
    private double centerY;

    // Used to calculate the next position of the tank when updating
    private double speedX;
    private double speedY;

    private int spriteX;
    private int spriteY;

    // The x and y of the upper-left corner of the muzzle
    private double muzzlePosX;
    private double muzzlePosY;

    // The angle and rotation speed of the muzzle
    private double muzzleAngle;
    private double muzzleSpeed;

    // Used to check whether the tank is moving left or right
    private boolean isMovingLeft;
    private boolean isMovingRight;

    // Used to check whether the muzzle is rotating up or down
    private boolean isRotatingUp;
    private boolean isRotatingDown;

    private boolean isAirborne;
    private boolean isPhasing;
    private boolean isShooting;

    // Used to simulate shooting cooldown
    private double shootTimer;

    // Used for phase duration
    private double phaseTimer;

    private Image tankSprite; // The Image for the Tank
    private Image muzzleSprite; // The Image for the Tank muzzle
    private Image muzzleSpriteOriginal; // The original sprite for the muzzle. Is used to get the
    // length of the muzzle to know where to create the
    // projectiles.

    private Animation anim; // Animation for the tank

    // TODO: Include the muzzle in the hitbox.
    private Rect hitbox; // The Tank hitbox. The muzzle is not included.

    private static ArrayList<Tank> tanks = new ArrayList<Tank>();

    public Tank(double centerX, double centerY) {
        tankSprite = Assets.tank[0];
        muzzleSprite = Assets.tankMuzzle;
        muzzleSpriteOriginal = Assets.tankMuzzleOriginal;

        this.centerX = centerX;
        this.centerY = centerY;

        spriteX = (int) centerX - tankSprite.getWidth() / 2;
        spriteY = (int) centerY - tankSprite.getHeight() / 2;

        muzzlePosX = centerX;
        muzzlePosY = centerY - 23;
        muzzleAngle = 0;

        isMovingLeft = false;
        isMovingRight = false;
        isRotatingUp = false;
        isRotatingDown = false;
        isAirborne = false;
        isPhasing = false;
        isShooting = false;

        shootTimer = 0.00;
        phaseTimer = 0.00;

        anim = new Animation();
        anim.addFrame(Assets.tank[2], 50);
        anim.addFrame(Assets.tank[1], 50);
        anim.addFrame(Assets.tank[0], 50);

        hitbox = new Rect((int) centerX - tankSprite.getWidth() / 2, (int) centerY - tankSprite.getHeight() / 2,
                (int) centerX + tankSprite.getWidth() / 2, (int) centerY + tankSprite.getHeight() / 2);

        tanks.add(this);
    }

    // Update is run all the time with deltaTime being how often it updates
    public void update(float deltaTime) {
        tankSprite = anim.getImage();

        // Moves the Tank.
        centerX += Math.round(speedX);

        // Prevents the tank from going outside of play area
        if (centerX < tankSprite.getWidth() / 2) {
            centerX = tankSprite.getWidth() / 2;
        }

        if (centerX > GameScreen.SCREEN_WIDTH - tankSprite.getWidth() / 2) {
            centerX = GameScreen.SCREEN_WIDTH - tankSprite.getWidth() / 2;
        }

        // Simulates the gravity. speedY is always increased, but is stopped by different things.
        speedY += GRAVITY_SPEED;

        // Sets a limit on how fast the tank can fall
        if (speedY > 20.00) {
            speedY = 20.00;
        }

        // If the tank starts falling, it is airborne
        if (speedY > GRAVITY_SPEED) {
            isAirborne = true;
        }

        centerY += Math.round(speedY);

        spriteX = (int) centerX - tankSprite.getWidth() / 2;
        spriteY = (int) centerY - tankSprite.getHeight() / 2;

        // Counts down the shootTimer, simulating cooldown on shooting.
        if (shootTimer > 0.00) {
            shootTimer -= deltaTime;
        }

        // When shootTimer is 0.00, the tank is able to shoot.
        // isShooting is true when the shoot button is held down.
        if (shootTimer <= 0.00 && isShooting()) {
            shootTimer = SHOOT_CD;
            shoot();
        }

        if (phaseTimer > 0.00) {
            phaseTimer -= deltaTime;
        }

        if (phaseTimer <= 0.00 && isPhasing) {
            isPhasing = false;
        }


        if (muzzleSpeed != 0.00) {
            muzzleAngle += (muzzleSpeed * deltaTime / 1000);

            if (muzzleAngle > 0) {
                muzzleAngle = 0;
            }

            if (muzzleAngle < -180.00) {
                muzzleAngle = -180.00;
            }
        }

        calcMuzzlePos();

        // Sets the Bitmap of the Image to the appropriate muzzle bitmap depending on muzzleAngle
        if (muzzleSpeed != 0.00) {
            muzzleSprite.setBitmap(Assets.tankMuzzleBitMap[(int) Math.round(-muzzleAngle)]);
        }

        // Updates the Tank hitbox
        hitbox.set((int) centerX - tankSprite.getWidth() / 2, (int) centerY - tankSprite.getHeight() / 2,
                (int) centerX + tankSprite.getWidth() / 2, (int) centerY + tankSprite.getHeight() / 2);
    }

    void updateTankPos() {
        calcMuzzlePos();

        spriteX = (int) centerX - tankSprite.getWidth() / 2;
        spriteY = (int) centerY - tankSprite.getHeight() / 2;

        hitbox.set((int) centerX - tankSprite.getWidth() / 2, (int) centerY - tankSprite.getHeight() / 2,
                (int) centerX + tankSprite.getWidth() / 2, (int) centerY + tankSprite.getHeight() / 2);
    }

    void jump() {
        if (!isAirborne) {
            speedY = -JUMP_SPEED;
            isAirborne = true;
        }
    }

    void jumpPhase() {
        if (!isAirborne) {
            isPhasing = true;
            phaseTimer = PHASE_DURATION;
        }
    }

    void stopJump() {
        if (isAirborne && speedY < -5) {
            speedY = -5;
        }
    }

    // Activates shooting when the shoot button is held down
    void startShoot() {
        isShooting = true;
    }

    // Deactivates shooting when the shoot button is released.
    // The shoot cooldown is refreshed whenever the button is released.
    void stopShoot() {
        isShooting = false;
        shootTimer = 0.00;
    }

    private void shoot() {
        double angle = Math.toRadians(muzzleAngle + 360);

        Projectile p = new Projectile((int) ((centerX - 1) + muzzleOriginalWidth() * Math.cos(angle)),
                (int) ((centerY - 22) + muzzleOriginalWidth() * Math.sin(angle)), angle);
        Projectile.getProjectiles().add(p);
    }


    void moveLeft() {
        speedX = -MOVE_SPEED;
        setMovingLeft(true);
    }

    void moveRight() {
        speedX = MOVE_SPEED;
        setMovingRight(true);
    }

    void stopLeft() {
        setMovingLeft(false);
        stop(0);
    }

    void stopRight() {
        setMovingRight(false);
        stop(0);
    }

    void rotateMuzzleUp() {
        muzzleSpeed = -ROTATE_SPEED;
        setRotatingUp(true);
    }

    void rotateMuzzleDown() {
        muzzleSpeed = ROTATE_SPEED;
        setRotatingDown(true);
    }

    void stopRotateMuzzleUp() {
        setRotatingUp(false);
        stop(1);
    }

    void stopRotateMuzzleDown() {
        setRotatingDown(false);
        stop(1);
    }

    private void stop(int type) {
        switch (type) {
            case 0: // Stop Move Actions
                if (!isMovingLeft() && !isMovingRight()) {
                    speedX = 0;
                }

                if (isMovingLeft() && !isMovingRight()) {
                    moveLeft();
                }

                if (!isMovingLeft() && isMovingRight()) {
                    moveRight();
                }
                break;
            case 1: // Stop Rotate Actions
                if (!isRotatingUp() && !isRotatingDown()) {
                    muzzleSpeed = 0.00;
                }

                if (isRotatingUp() && !isRotatingDown()) {
                    rotateMuzzleUp();
                }

                if (!isRotatingUp() && isRotatingDown()) {
                    rotateMuzzleDown();
                }
                break;
            default:
        }
    }

    private void calcMuzzlePos() {
        double angle = muzzleAngle + 360.00;

        if (angle >= 180.00 && angle < 270.00) {
            muzzlePosX = (int) (centerX + 8 * Math.sin(Math.toRadians(muzzleAngle)) + muzzleOriginalWidth() * Math.cos(Math.toRadians(muzzleAngle)));
            muzzlePosY = (int) ((centerY - 15) + 8 * Math.cos(Math.toRadians(muzzleAngle)) + muzzleOriginalWidth() * Math.sin(Math.toRadians(muzzleAngle)));
        }

        if (angle >= 270.00 && angle <= 360.00) {
            muzzlePosX = (int) (centerX + 8 * Math.sin(Math.toRadians(muzzleAngle)));
            muzzlePosY = (int) ((centerY - 15) + 8 * Math.cos(Math.toRadians(muzzleAngle))) - muzzleSprite.getHeight();
        }
    }

    // Resets the muzzle to its original 0 degree position.
    static void resetMuzzle() {
        Assets.tankMuzzle.setBitmap(Assets.tankMuzzleBitMap[0]);
    }

    int muzzleOriginalWidth() {
        return muzzleSpriteOriginal.getWidth();
    }

    int muzzleOriginalHeight() {
        return muzzleSpriteOriginal.getHeight();
    }

    // Getters and Setters
    double getCenterX() {
        return centerX;
    }

    void setCenterX(double centerX) {
        this.centerX = centerX;
    }

    double getCenterY() {
        return centerY;
    }

    void setCenterY(double centerY) {
        this.centerY = centerY;
    }

    double getSpeedX() {
        return speedX;
    }

    void setSpeedX(double speedX) {
        this.speedX = speedX;
    }

    double getSpeedY() {
        return speedY;
    }

    void setSpeedY(double speedY) {
        this.speedY = speedY;
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

    double getMuzzlePosX() {
        return muzzlePosX;
    }

    void setMuzzlePosX(double muzzlePosX) {
        this.muzzlePosX = muzzlePosX;
    }

    double getMuzzlePosY() {
        return muzzlePosY;
    }

    void setMuzzlePosY(double muzzlePosY) {
        this.muzzlePosY = muzzlePosY;
    }

    boolean isMovingLeft() {
        return isMovingLeft;
    }

    void setMovingLeft(boolean movingLeft) {
        isMovingLeft = movingLeft;
    }

    boolean isMovingRight() {
        return isMovingRight;
    }

    void setMovingRight(boolean movingRight) {
        isMovingRight = movingRight;
    }

    boolean isRotatingUp() {
        return isRotatingUp;
    }

    void setRotatingUp(boolean rotatingUp) {
        isRotatingUp = rotatingUp;
    }

    boolean isRotatingDown() {
        return isRotatingDown;
    }

    void setRotatingDown(boolean rotatingDown) {
        isRotatingDown = rotatingDown;
    }

    public boolean isAirborne() {
        return isAirborne;
    }

    public void setAirborne(boolean airborne) {
        isAirborne = airborne;
    }

    public boolean isPhasing() {
        return isPhasing;
    }

    public void setPhasing(boolean phasing) {
        isPhasing = phasing;
    }

    boolean isShooting() {
        return isShooting;
    }

    void setShooting(boolean shooting) {
        isShooting = shooting;
    }

    Image getTankSprite() {
        return tankSprite;
    }

    void setTankSprite(Image tankSprite) {
        this.tankSprite = tankSprite;
    }

    Image getMuzzleSprite() {
        return muzzleSprite;
    }

    void setMuzzleSprite(Image muzzleSprite) {
        this.muzzleSprite = muzzleSprite;
    }

    Rect getHitbox() {
        return hitbox;
    }

    void setHitbox(Rect hitbox) {
        this.hitbox = hitbox;
    }

    Animation getAnim() {
        return anim;
    }

    public static ArrayList<Tank> getTanks() {
        return tanks;
    }

    public static void setTanks(ArrayList<Tank> tanks) {
        Tank.tanks = tanks;
    }
}
