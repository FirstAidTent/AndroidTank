package com.example.firstaidtent.androidtank;

import com.example.firstaidtent.framework.Game;
import com.example.firstaidtent.framework.Graphics;
import com.example.firstaidtent.framework.Screen;
import com.example.firstaidtent.framework.Graphics.ImageFormat;

public class SplashLoadingScreen extends Screen {
    public SplashLoadingScreen(Game game) {
        super(game);
    }

    @Override
    public void update(float deltaTime) {
        Graphics g = game.getGraphics();
        Assets.splash= g.newImage("splash.jpg", ImageFormat.RGB565);


        game.setScreen(new LoadingScreen(game));

    }

    @Override
    public void paint(float deltaTime) {

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