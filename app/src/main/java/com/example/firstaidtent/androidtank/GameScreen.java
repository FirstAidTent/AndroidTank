package com.example.firstaidtent.androidtank;

import android.graphics.Color;
import android.graphics.Paint;

import com.example.firstaidtent.framework.Game;
import com.example.firstaidtent.framework.Graphics;
import com.example.firstaidtent.framework.Input.TouchEvent;
import com.example.firstaidtent.framework.Screen;
import com.example.firstaidtent.framework.implementation.AndroidGame;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class GameScreen extends Screen {
    private enum GameState {
        Ready, Running, Paused, GameOver
    }

    private GameState state = GameState.Ready;

    // Constants
    private final int BG_SPEED = 2; // Background scroll speed. In pixels per update.
    private final double ENEMY_SPAWN_RATE = 0.02; // Enemies will spawn at this interval.

    // Variable Setup

    // There are 2 backgrounds which are both the same. One will be displayed and scrolled.
    // When one background's end is reached, the other will take over.
    private static Background bg1, bg2;
    private Tank tank;
    private Joystick joystick;

    private int livesLeft;
    private Paint paint;
    private Paint paint2;
    private Paint paintDebug;

    private int shootTouchId;
    private int jumpTouchId;

    private Stage currentStage;

    private double timer;
    private int timer_milli;
    private int timer_sec;
    private int timer_min;
    private String timerString;

    private static int[] JUMP_BTN_POS = {700, 350, 65, 65};
    private static int[] SHOOT_BTN_POS = {600, 350, 65, 65};
    private static int[] PAUSE_BTN_POS = {0, 0, 35, 35};

    public static final int SCREEN_WIDTH = AndroidGame.getFrameBufferWidth();
    public static final int SCREEN_HEIGHT = AndroidGame.getFrameBufferHeight();

    public static String debugString = "";
    public static String debugString2 = "";
    public static String debugString3 = "";
    private static String debugStringJoystick = "";
    private static float debugFloat = 0;

    GameScreen(Game game) {
        super(game);

        // Initialize game objects here

        bg1 = new Background(0, 0, Assets.background);
        bg2 = new Background(Assets.background.getWidth(), 0, Assets.background);
        bg1.setSpeedX(-BG_SPEED);
        bg2.setSpeedX(-BG_SPEED);

        tank = new Tank(100, 420);
        joystick = new Joystick(100, 405);

        shootTouchId = -1;
        jumpTouchId = -1;

        livesLeft = 1;

        timer = 0.00;
        timerString = "00:00.00";

        //loadMap();
        //loadMapSimple();

        currentStage = Stage.getStage(0);
        Tile.copyTilesToClass(currentStage.getTiles());

        // Defining a paint object
        paint = new Paint();
        paint.setTextSize(30);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);

        paint2 = new Paint();
        paint2.setTextSize(100);
        paint2.setTextAlign(Paint.Align.CENTER);
        paint2.setAntiAlias(true);
        paint2.setColor(Color.WHITE);

        paintDebug = new Paint();
        paintDebug.setTextSize(30);
        paintDebug.setTextAlign(Paint.Align.CENTER);
        paintDebug.setAntiAlias(true);
        paintDebug.setColor(Color.RED);
    }

    private void loadMap() {
        ArrayList<String> lines = new ArrayList<>();
        int width = 0;
        int height;

        Scanner scanner = new Scanner(SampleGame.map);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            // no more lines to read
            if (line == null) {
                break;
            }

            if (!line.startsWith("!")) {
                lines.add(line);
                width = Math.max(width, line.length());
            }
        }
        height = lines.size();

        for (int j = 0; j < height; j++) {
            String line = lines.get(j);
            for (int i = 0; i < width; i++) {

                if (i < line.length()) {
                    char ch = line.charAt(i);
                    Tile.TILE_TYPE type;
                    switch (Character.getNumericValue(ch)) {
                        case 5:
                            type = Tile.TILE_TYPE.DIRT;
                            break;
                        case 4:
                            type = Tile.TILE_TYPE.GRASS_LEFT;
                            break;
                        case 8:
                            type = Tile.TILE_TYPE.GRASS_TOP;
                            break;
                        case 6:
                            type = Tile.TILE_TYPE.GRASS_RIGHT;
                            break;
                        case 2:
                            type = Tile.TILE_TYPE.GRASS_BOT;
                            break;
                        default:
                            type = Tile.TILE_TYPE.NULL;
                    }
                    Tile t = new Tile(i, j, type);
                    Tile.getTiles().add(t);
                }

            }
        }

    }

    private void loadMapSimple() {
        for (int i = 0; i < 22; i++) {
            currentStage.addTile(new Tile(i, 11, Tile.TILE_TYPE.GRASS_TOP));
        }
    }

    @Override
    public void update(float deltaTime) {
        List touchEvents = game.getInput().getTouchEvents();

        // We have four separate update methods in this example.
        // Depending on the state of the game, we call different update methods.
        // Refer to Unit 3's code. We did a similar thing without separating the
        // update methods.

        if (state == GameState.Ready) {
            updateReady(touchEvents);
        }
        if (state == GameState.Running) {
            updateRunning(touchEvents, deltaTime);
        }
        if (state == GameState.Paused) {
            updatePaused(touchEvents);
        }
        if (state == GameState.GameOver) {
            updateGameOver(touchEvents);
        }
    }

    private void updateReady(List touchEvents) {

        // This example starts with a "Ready" screen.
        // When the user touches the screen, the game begins.
        // state now becomes GameState.Running.
        // Now the updateRunning() method will be called!

        if (touchEvents.size() > 0) {
            state = GameState.Running;
        }
    }

    private void updateRunning(List touchEvents, float deltaTime) {
        timer += deltaTime;
        timer_milli = (int) timer % 1000;
        timer_sec = (int) (timer / 1000) % 60;
        timer_min = (int) (timer / 60000) % 60;
        timerString = String.format(Locale.ENGLISH, "%02d:%02d.%03d", timer_min, timer_sec, timer_milli);

        // All touch inputs are handled here:
        int len = touchEvents.size();
        debugFloat = deltaTime;
        for (int i = 0; i < len; i++) {
            TouchEvent event = (TouchEvent) touchEvents.get(i);

            // Checks if a button is pressed down and applies the appropriate action
            if (event.type == TouchEvent.TOUCH_DOWN) {
                if (inBoundsRect(event, JUMP_BTN_POS[0], JUMP_BTN_POS[1], JUMP_BTN_POS[2], JUMP_BTN_POS[3]) && jumpTouchId == -1) { // JUMP
                    jumpTouchId = event.pointer;
                    if (joystick.getDirection() == Joystick.DIRECTION.DOWN) {
                        tank.jumpPhase();
                    } else {
                        tank.jump();
                    }
                }

                // If the player presses the shoot button, the tank will shoot
                if (inBoundsRect(event, SHOOT_BTN_POS[0], SHOOT_BTN_POS[1], SHOOT_BTN_POS[2], SHOOT_BTN_POS[3]) && shootTouchId == -1) { // SHOOT
                    shootTouchId = event.pointer;
                    tank.startShoot();
                }

                if (inBoundsCircle(event, joystick.getCenterX(), joystick.getCenterY(), joystick.getRadius() + 30) && !joystick.isTouched()) { // Joystick
                    joystick.setTouched(true);
                    joystick.setTouchId(event.pointer);
                    debugStringJoystick = joystick.update(event.x, event.y).toString();
                }
            }

            // If the player releases a button, the appropriate action will stop
            if (event.type == TouchEvent.TOUCH_UP) {
                if (jumpTouchId == event.pointer) { // JUMP
                    jumpTouchId = -1;
                    tank.stopJump();
                }

                if (shootTouchId == event.pointer) { // SHOOT
                    shootTouchId = -1;
                    tank.stopShoot();
                }

                if (inBoundsRect(event, PAUSE_BTN_POS[0], PAUSE_BTN_POS[1], PAUSE_BTN_POS[2], PAUSE_BTN_POS[3])) { // PAUSE
                    pause();
                }

                if (joystick.isTouched() && event.pointer == joystick.getTouchId()) {
                    joystick.setTouched(false);
                    joystick.reset();
                }
            }

            if (event.type == TouchEvent.TOUCH_DRAGGED) {
                if (inBoundsRect(event, JUMP_BTN_POS[0], JUMP_BTN_POS[1], JUMP_BTN_POS[2], JUMP_BTN_POS[3]) && jumpTouchId == -1) { // JUMP
                    jumpTouchId = event.pointer;
                    if (joystick.getDirection() == Joystick.DIRECTION.DOWN) {
                        tank.jumpPhase();
                    } else {
                        tank.jump();
                    }
                } else if (!inBoundsRect(event, JUMP_BTN_POS[0], JUMP_BTN_POS[1], JUMP_BTN_POS[2], JUMP_BTN_POS[3]) && jumpTouchId == event.pointer) {
                    jumpTouchId = -1;
                    tank.stopJump();
                }

                if (inBoundsRect(event, SHOOT_BTN_POS[0], SHOOT_BTN_POS[1], SHOOT_BTN_POS[2], SHOOT_BTN_POS[3]) && shootTouchId == -1) { // SHOOT
                    shootTouchId = event.pointer;
                    tank.startShoot();
                } else if (!inBoundsRect(event, SHOOT_BTN_POS[0], SHOOT_BTN_POS[1], SHOOT_BTN_POS[2], SHOOT_BTN_POS[3]) && shootTouchId == event.pointer) {
                    shootTouchId = -1;
                    tank.stopShoot();
                }

                if (inBoundsCircle(event, joystick.getCenterX(), joystick.getCenterY(), joystick.getRadius() + 30) && !joystick.isTouched()) {
                    joystick.setTouched(true);
                    joystick.setTouchId(event.pointer);
                    debugStringJoystick = joystick.update(event.x, event.y).toString();
                } else if (joystick.isTouched() && event.pointer == joystick.getTouchId()) {
                    debugStringJoystick = joystick.update(event.x, event.y).toString();
                }
            }
        }

        tank.stopRotateMuzzleUp();
        tank.stopRotateMuzzleDown();
        tank.stopLeft();
        tank.stopRight();

        switch (joystick.getDirection()) {
            case UP:
                tank.rotateMuzzleUp();
                break;
            case UP_RIGHT:
                tank.rotateMuzzleUp();
                tank.moveRight();
                break;
            case RIGHT:
                tank.moveRight();
                break;
            case DOWN_RIGHT:
                tank.rotateMuzzleDown();
                tank.moveRight();
                break;
            case DOWN:
                tank.rotateMuzzleDown();
                break;
            case DOWN_LEFT:
                tank.rotateMuzzleDown();
                tank.moveLeft();
                break;
            case LEFT:
                tank.moveLeft();
                break;
            case UP_LEFT:
                tank.rotateMuzzleUp();
                tank.moveLeft();
                break;
            default:
                tank.stopRotateMuzzleUp();
                tank.stopRotateMuzzleDown();
                tank.stopLeft();
                tank.stopRight();
        }

        // Checks miscellaneous events like death:


        // Falling down below the visible area kills you
        if (tank.getCenterY() > 550) {
            state = GameState.GameOver;
        }

        if (livesLeft == 0) {
            state = GameState.GameOver;
        }

        if (Math.random() < ENEMY_SPAWN_RATE) {
            int side = (int) (Math.random() * 2);

            if (side == 0) {
                Heliboy h = new Heliboy(-30, (int) (Math.random() * 100) + 60, 1);
            } else {
                Heliboy h = new Heliboy(810, (int) (Math.random() * 100) + 60, 0);
            }
        }

        // Call individual update() methods here.
        // From here on is where all the game updates happen.
        updateTank(deltaTime);
        updateProjectile();
        updateEnemies();
        updateTiles();

        // Background Update
        bg1.update();
        bg2.update();

        // Animates everything that has an animation
        animate();
    }

    private boolean inBoundsRect(TouchEvent event, int x, int y, int width,
                                 int height) {
        return (event.x > x && event.x < x + width - 1 && event.y > y
                && event.y < y + height - 1);
    }

    private boolean inBoundsCircle(TouchEvent event, int x, int y, double radius) {
        return Math.sqrt(Math.pow((event.x - x), 2) + Math.pow((event.y - y), 2)) <= radius;
    }

    private void updatePaused(List touchEvents) {
        int len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            TouchEvent event = (TouchEvent) touchEvents.get(i);
            if (event.type == TouchEvent.TOUCH_UP) {
                if (inBoundsRect(event, 0, 0, 800, 240)) {

                    if (!inBoundsRect(event, 0, 0, 35, 35)) {
                        resume();
                    }
                }

                if (inBoundsRect(event, 0, 240, 800, 240)) {
                    nullify();
                    goToMenu();
                }
            }
        }
    }

    private void updateGameOver(List touchEvents) {
        int len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            TouchEvent event = (TouchEvent) touchEvents.get(i);
            if (event.type == TouchEvent.TOUCH_DOWN) {
                if (inBoundsRect(event, 0, 0, 800, 480)) {
                    nullify();
                    goToMenu();
                    return;
                }
            }
        }

    }

    private void updateTank(float deltaTime) {

        ArrayList<Tank> tanks = Tank.getTanks();
        for (int i = 0; i < tanks.size(); i++) {
            Tank t = tanks.get(i);
            t.update(deltaTime);
        }

    }

    private void updateProjectile() {

        ArrayList projectiles = Projectile.getProjectiles();
        for (int i = 0; i < projectiles.size(); i++) {
            Projectile p = (Projectile) projectiles.get(i);

            // A projectile going out of the visible area is removed from the game
            if (p.isVisible()) {
                p.update();
            }
        }

    }

    private void updateEnemies() {

        ArrayList<Enemy> enemies = Enemy.getEnemies();
        for (int i = 0; i < enemies.size(); i++) {
            Enemy e = enemies.get(i);
            e.update();
        }

    }

    private void updateTiles() {
        List<Tile> tiles = Tile.getTiles();
        for (int i = 0; i < tiles.size(); i++) {
            Tile t = tiles.get(i);
            t.update();
        }
    }

    // Draws everything on the screen
    @Override
    public void paint(float deltaTime) {
        Graphics g = game.getGraphics();

        // The Background and Tiles
        g.drawImage(Assets.background, bg1.getBgX(), bg1.getBgY());
        g.drawImage(Assets.background, bg2.getBgX(), bg2.getBgY());
        paintTiles(g);

        // The Projectiles
        ArrayList projectiles = Projectile.getProjectiles();
        for (int i = 0; i < projectiles.size(); i++) {
            Projectile p = (Projectile) projectiles.get(i);
            g.drawImage(p.getSprite(), p.getX(), p.getY());
        }

        // The Enemies
        ArrayList<Enemy> enemies = Enemy.getEnemies();
        for (int i = 0; i < enemies.size(); i++) {
            Enemy e = enemies.get(i);
            if ((e.getSpriteX() > -100 && e.getSpriteX() < SCREEN_WIDTH + 100) &&
                    (e.getSpriteY() > -100 && e.getSpriteY() < SCREEN_HEIGHT + 100)) {

                g.drawImage(e.getAnim().getImage(), e.getSpriteX(), e.getSpriteY());

            }
        }

        // The Tank
        ArrayList<Tank> tanks = Tank.getTanks();
        for (int i = 0; i < tanks.size(); i++) {
            Tank t = tanks.get(i);
            g.drawImage(t.getMuzzleSprite(), (int) t.getMuzzlePosX(), (int) t.getMuzzlePosY());
            g.drawImage(t.getTankSprite(), t.getSpriteX(), t.getSpriteY());
        }

        // Debug Stuff
        if (!debugString.equals("")) {
//            g.drawString(debugString, 150, 60, paintDebug);
//            g.drawString(debugString2, 150, 110, paintDebug);
//            g.drawString(debugString3, 150, 160, paintDebug);
            g.drawString("FPS: " + Math.round(1000.00f / debugFloat), 700, 60, paintDebug);
            g.drawString(timerString, 400, 60, paintDebug);
//            g.drawString(debugStringJoystick, 600, 110, paintDebug);
        }

        // Draws the UI above the other things
        if (state == GameState.Ready)
            drawReadyUI();
        if (state == GameState.Running)
            drawRunningUI();
        if (state == GameState.Paused)
            drawPausedUI();
        if (state == GameState.GameOver)
            drawGameOverUI();

    }

    private void paintTiles(Graphics g) {
        List<Tile> tiles = Tile.getTiles();
        for (int i = 0; i < tiles.size(); i++) {
            Tile t = tiles.get(i);
            //g.drawImage(t.getTileImage(), t.getTileX(), t.getTileY());
            t.drawTile(g);
        }
    }

    private void animate() {
        tank.getAnim().update(25);

        ArrayList<Enemy> enemies = Enemy.getEnemies();
        for (int i = 0; i < enemies.size(); i++) {
            Enemy e = enemies.get(i);
            e.getAnim().update(50);
        }
    }

    private void nullify() {

        // Set all variables to null. You will be recreating them in the
        // constructor.

        paint = null;
        bg1 = null;
        bg2 = null;
        tank = null;

        Tank.resetMuzzle();
        Tank.getTanks().clear();
        Enemy.getEnemies().clear();
        Projectile.getProjectiles().clear();
        Tile.getTiles().clear();

        // Call garbage collector to clean up memory.
        System.gc();

    }

    private void drawReadyUI() {
        Graphics g = game.getGraphics();

        g.drawARGB(155, 0, 0, 0);
        g.drawString("Tap to Start", 400, 240, paint);

    }

    private void drawRunningUI() {
        Graphics g = game.getGraphics();

        // Draws the Buttons for the UI.
        // g.drawImage(srcImg, x, y, srcX, srcY, width, height)

        g.drawImage(Assets.button, JUMP_BTN_POS[0], JUMP_BTN_POS[1], 0, 0, JUMP_BTN_POS[2], JUMP_BTN_POS[3]);
        g.drawImage(Assets.button, SHOOT_BTN_POS[0], SHOOT_BTN_POS[1], 0, 65, SHOOT_BTN_POS[2], SHOOT_BTN_POS[3]);
        g.drawImage(Assets.button, PAUSE_BTN_POS[0], PAUSE_BTN_POS[1], 0, 195, PAUSE_BTN_POS[2], PAUSE_BTN_POS[3]);
        g.drawImage(joystick.getSpriteCircle(), joystick.getSpriteCircleX(), joystick.getSpriteCircleY());
        g.drawImage(joystick.getSpriteMiddle(), joystick.getSpriteMiddleX(), joystick.getSpriteMiddleY());
    }

    private void drawPausedUI() {
        Graphics g = game.getGraphics();
        // Darken the entire screen so you can display the Paused screen.
        g.drawARGB(155, 0, 0, 0);
        g.drawString("Resume", 400, 165, paint2);
        g.drawString("Menu", 400, 360, paint2);

    }

    private void drawGameOverUI() {
        Graphics g = game.getGraphics();
        g.drawRect(0, 0, 1281, 801, Color.BLACK);
        g.drawString("GAME OVER", 400, 240, paint2);
        g.drawString("Tap to return", 400, 290, paint);

    }

    @Override
    public void pause() {
        if (state == GameState.Running)
            state = GameState.Paused;

    }

    @Override
    public void resume() {
        if (state == GameState.Paused)
            state = GameState.Running;
    }

    @Override
    public void dispose() {

    }

    @Override
    public void backButton() {
        pause();
    }

    private void goToMenu() {
        game.setScreen(new MainMenuScreen(game));

    }

    static Background getBg1() {
        return bg1;
    }

    static Background getBg2() {
        return bg2;
    }

}