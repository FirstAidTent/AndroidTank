package com.example.firstaidtent.androidtank;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Stage {
    private List<Tile> tiles;
    private List<Enemy> enemies;
    private static HashMap<Integer, Stage> stages = new HashMap<>();

    private Stage(int stageNum) {
        tiles = new ArrayList<>(100);
        enemies = new ArrayList<>(100);

        stages.put(stageNum, this);
    }

    public static Stage createStage(int stageNum) {
        if (stages.containsKey(stageNum)) {
            return stages.get(stageNum);
        }
        return new Stage(stageNum);
    }

    public static Stage getStage(int stageNum) {
        return stages.get(stageNum);
    }

    public boolean addTile(Tile t) {
        return tiles.add(t);
    }

    public boolean addTileArea(int x1, int y1, int x2, int y2, Tile.TILE_TYPE t) {
        if (x1 > x2 || y1 > y2) {
            return false;
        }
        for (int i = y1; i <= y2; i++) {
            for (int j = x1; j <= x2; j++) {
                if (!addTile(new Tile(j, i, t))) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean removeTile(Tile t) {
        return tiles.remove(t);
    }

    public boolean addEnemy(Enemy e) {
        return enemies.add(e);
    }

    public List<Tile> getTiles() {
        return tiles;
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }
}
