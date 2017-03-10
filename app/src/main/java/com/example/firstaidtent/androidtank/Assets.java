package com.example.firstaidtent.androidtank;

import android.graphics.Bitmap;

import com.example.firstaidtent.framework.Image;
import com.example.firstaidtent.framework.Music;

class Assets {

    static Image menu, splash, background;
    static Image[] heliboy = new Image[5];
    static Image[] heliboyF = new Image[5];
    static Image[] tank = new Image[4];
    static Image tankMuzzle, tankMuzzleOriginal, projectile;
    static Bitmap[] tankMuzzleBitMap = new Bitmap[181];
    static Image tiledirt, tilegrassTop, tilegrassBot, tilegrassLeft, tilegrassRight;
    static Image button, joystickCircle, joystickMiddle;
    //static Sound click;
    static Music theme;

    static void load(SampleGame sampleGame) {
        theme = sampleGame.getAudio().createMusic("menutheme.mp3");
        theme.setLooping(true);
        theme.setVolume(0.85f);
        theme.play();
    }

}