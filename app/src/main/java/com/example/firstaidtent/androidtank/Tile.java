package com.example.firstaidtent.androidtank;

import android.graphics.Rect;

import com.example.firstaidtent.framework.Graphics;
import com.example.firstaidtent.framework.Image;

import java.util.ArrayList;
import java.util.List;

public class Tile {
    public enum TILE_TYPE {
        DIRT, GRASS_LEFT, GRASS_TOP, GRASS_TOP_PHASE, GRASS_RIGHT, GRASS_BOT, NULL
    }

    private int tileX;
    private int tileY;
    private int speedX;

    // If the tank can go through the tile by pressing DOWN+JUMP or not.
    private boolean isPhaseable;

    private TILE_TYPE type;
    private Image tileImage;

    private Background bg = GameScreen.getBg1();

    private Rect r;

    private static List<Tile> tiles = new ArrayList<>(100);

    public Tile(int x, int y, TILE_TYPE type) {
        tileX = x * 40;
        tileY = y * 40;

        isPhaseable = false;

        this.type = type;

        r = new Rect(tileX, tileY, tileX + 40, tileY + 40);

        switch (type) {
            case DIRT:
                tileImage = Assets.tiledirt;
                break;
            case GRASS_LEFT:
                tileImage = Assets.tilegrassLeft;
                break;
            case GRASS_TOP:
                tileImage = Assets.tilegrassTop;
                break;
            case GRASS_TOP_PHASE:
                tileImage = Assets.tilegrassTop;
                isPhaseable = true;
                break;
            case GRASS_RIGHT:
                tileImage = Assets.tilegrassRight;
                break;
            case GRASS_BOT:
                tileImage = Assets.tilegrassBot;
                break;
        }
    }

    public void update() {
        speedX = -10;
        tileX += speedX;

        if (tileX < -40) {
            tiles.remove(this);
        }

        r.set(tileX, tileY, tileX + 40, tileY + 40);

        ArrayList<Tank> tanks = Tank.getTanks();
        for (int i = 0; i < tanks.size(); i++) {
            Tank t = tanks.get(i);
            if (Rect.intersects(r, t.getHitbox()) && type != TILE_TYPE.NULL) {
                checkVerticalCollision(t);
            }
        }


    }

    private void checkVerticalCollision(Tank t) {
        switch (type) {
            case GRASS_TOP:
            case GRASS_TOP_PHASE:
                if (t.getSpeedY() > 0.00 && t.getCenterY() < tileY) {
                    if (!(isPhaseable && t.isPhasing())) {
                        t.setSpeedY(0.00);
                        t.setCenterY(tileY - (t.getTankSprite().getHeight() / 2));
                        t.setAirborne(false);
                        t.updateTankPos();
                    }
                }
                break;
        }
    }

    /*public void checkSideCollision(Rect rleft, Rect rright, Rect leftfoot, Rect rightfoot) {
        if (type != 5 && type != 2 && type != 0) {
            if (Rect.intersects(rleft, hitbox)) {
                robot.setCenterX(tileX + 102);

                robot.setSpeed(0);

            } else if (Rect.intersects(leftfoot, hitbox)) {

                robot.setCenterX(tileX + 85);
                robot.setSpeed(0);
            }

            if (Rect.intersects(rright, hitbox)) {
                robot.setCenterX(tileX - 62);

                robot.setSpeed(0);
            } else if (Rect.intersects(rightfoot, hitbox)) {
                robot.setCenterX(tileX - 45);
                robot.setSpeed(0);
            }
        }
    }*/

    public void drawTile(Graphics g) {
        if ((tileX > -100 && tileX < GameScreen.SCREEN_WIDTH + 100) &&
                (tileY > -100 && tileY < GameScreen.SCREEN_HEIGHT + 100)) {

            g.drawImage(tileImage, tileX, tileY);

        }
    }

    public static void copyTilesToClass(List<Tile> list) {
        tiles.clear();
        for (int i = 0; i < list.size(); i++) {
            Tile t = list.get(i);
            tiles.add(new Tile(t.getTileX() / 40, t.getTileY() / 40, t.getType()));
        }
    }

    // Getters and Setters
    public int getTileX() {
        return tileX;
    }

    public void setTileX(int tileX) {
        this.tileX = tileX;
    }

    public int getTileY() {
        return tileY;
    }

    public void setTileY(int tileY) {
        this.tileY = tileY;
    }

    public int getSpeedX() {
        return speedX;
    }

    public void setSpeedX(int speedX) {
        this.speedX = speedX;
    }

    public boolean isPhaseable() {
        return isPhaseable;
    }

    public void setPhaseable(boolean phaseable) {
        isPhaseable = phaseable;
    }

    public TILE_TYPE getType() {
        return type;
    }

    public Image getTileImage() {
        return tileImage;
    }

    public void setTileImage(Image tileImage) {
        this.tileImage = tileImage;
    }

    public static List<Tile> getTiles() {
        return tiles;
    }
}