package com.example.firstaidtent.androidtank;

import android.graphics.Bitmap;
import android.graphics.Matrix;

import com.example.firstaidtent.framework.Game;
import com.example.firstaidtent.framework.Graphics;
import com.example.firstaidtent.framework.Graphics.ImageFormat;
import com.example.firstaidtent.framework.Screen;
import com.example.firstaidtent.framework.implementation.AndroidImage;

import java.util.Locale;

class LoadingScreen extends Screen {
    LoadingScreen(Game game) {
        super(game);
    }

    @Override
    public void update(float deltaTime) {
        Graphics g = game.getGraphics();
        Assets.menu = g.newImage("menu.png", ImageFormat.RGB565);
        Assets.background = g.newImage("background.png", ImageFormat.RGB565);

        // Heliboy
        for (int i = 0; i < 5; i++) {
            if (i == 0) {
                Assets.heliboy[i] = g.newImage("heliboy.png", ImageFormat.ARGB4444);
            } else {
                Assets.heliboy[i] = g.newImage(String.format(Locale.ENGLISH, "heliboy%d.png", i + 1), ImageFormat.ARGB4444);
            }
        }

        // Heliboy Flipped
        Matrix m1 = new Matrix();
        m1.preScale(-1.00f, 1.00f);
        Bitmap[] heliF = new Bitmap[5];
        for (int i = 0; i < 5; i++) {
            heliF[i] = Bitmap.createBitmap(Assets.heliboy[i].getBitmap(), 0, 0, Assets.heliboy[i].getWidth(),
                    Assets.heliboy[i].getHeight(), m1, false);
            Assets.heliboyF[i] = new AndroidImage(heliF[i], ImageFormat.ARGB4444);
        }

        // Tank
        for (int i = 0; i < 4; i++) {
            if (i == 0) {
                Assets.tank[i] = g.newImage("tank.png", ImageFormat.ARGB4444);
            } else {
                Assets.tank[i] = g.newImage(String.format(Locale.ENGLISH, "tank%d.png", i + 1), ImageFormat.ARGB4444);
            }
        }

        // Other Tank related things
        Assets.tankMuzzle = g.newImage("tankmuzzle.png", ImageFormat.ARGB4444);
        Assets.tankMuzzleOriginal = g.newImage("tankmuzzle.png", ImageFormat.ARGB4444);
        Assets.projectile = g.newImage("projectile.png", ImageFormat.ARGB4444);

        // Tank Muzzle different angles preloading
        Matrix m2 = new Matrix();
        for (int i = 0; i <= 180; i++) {
            m2.setRotate(i + 180);
            Assets.tankMuzzleBitMap[180 - i] = Bitmap.createBitmap(Assets.tankMuzzleOriginal.getBitmap(), 0, 0,
                    Assets.tankMuzzleOriginal.getWidth(), Assets.tankMuzzleOriginal.getHeight(), m2, false);
        }

        // Tiles
        Assets.tiledirt = g.newImage("tiledirt.png", ImageFormat.RGB565);
        Assets.tilegrassTop = g.newImage("tilegrasstop.png", ImageFormat.RGB565);
        Assets.tilegrassBot = g.newImage("tilegrassbot.png", ImageFormat.RGB565);
        Assets.tilegrassLeft = g.newImage("tilegrassleft.png", ImageFormat.RGB565);
        Assets.tilegrassRight = g.newImage("tilegrassright.png", ImageFormat.RGB565);

        // Buttons
        Assets.button = g.newImage("button.jpg", ImageFormat.RGB565);

        // Joystick
        Assets.joystickCircle = g.newImage("joystickcircle.png", ImageFormat.ARGB4444);
        Assets.joystickMiddle = g.newImage("joystickmiddle.png", ImageFormat.ARGB4444);

        // Stages
        Stage[] stage = new Stage[3];
        stage[0] = Stage.createStage(0);
        stage[0].addTileArea(0, 11, 300, 11, Tile.TILE_TYPE.GRASS_TOP);
        stage[0].addTileArea(105, 6, 300, 6, Tile.TILE_TYPE.GRASS_TOP_PHASE);
        stage[0].addTileArea(205, 1, 300, 1, Tile.TILE_TYPE.GRASS_TOP_PHASE);
        stage[0].addTileArea(310, 11, 600, 11, Tile.TILE_TYPE.GRASS_TOP);
        stage[0].addTileArea(310, 4, 600, 10, Tile.TILE_TYPE.GRASS_TOP_PHASE);

        //This is how you would load a sound if you had one.
        //Assets.click = game.getAudio().createSound("explode.ogg");

        game.setScreen(new MainMenuScreen(game));

    }

    @Override
    public void paint(float deltaTime) {
        Graphics g = game.getGraphics();
        g.drawImage(Assets.splash, 0, 0);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }

    @Override
    public void backButton() {

    }
}