package com.example.firstaidtent.androidtank;

import android.util.Log;

import com.example.firstaidtent.framework.Screen;
import com.example.firstaidtent.framework.implementation.AndroidGame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class SampleGame extends AndroidGame {

    public static String map;
    boolean firstTimeCreate = true;

    @Override
    public Screen getInitScreen() {

        if (firstTimeCreate) {
            Assets.load(this);
            firstTimeCreate = false;
        }

        InputStream is = getResources().openRawResource(R.raw.map1);
        map = convertStreamToString(is);

        return new SplashLoadingScreen(this);

    }

    @Override
    public void onBackPressed() {
        getCurrentScreen().backButton();
    }

    private static String convertStreamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String s;

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                s = line + "\n";
                sb.append(s);
            }
        } catch (IOException e) {
            Log.w("LOG", e.getMessage());
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                Log.w("LOG", e.getMessage());
            }
        }
        return sb.toString();
    }

    @Override
    public void onResume() {
        super.onResume();

        Assets.theme.play();

    }

    @Override
    public void onPause() {
        super.onPause();
        Assets.theme.pause();

    }
}