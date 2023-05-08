package org.deckmaster.mapgen;

import org.deckmaster.Drawable;
import processing.core.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;


public class MapTile implements Drawable, Serializable {
    public static Integer TILE_SIZE = 50;
    public TileLocation location;
    float prevPlayerHeight = 0;
    public float height;
    public TileState state = TileState.EMPTY;
    public Building buildingMap = null;
    public Random random = new Random();

    public MapTile(TileLocation loc, float height) {
        this.location = loc;
        this.height = height;

        if (Math.random() < 0.0005f && height >= 0) {
            state = TileState.LOOT;
            return;
        }

        if (Math.random() < 0.0005f && height >= 0) {
            state = TileState.EVENT;
            return;
        }

        double randomVal = Math.random();
        if (height >= 0 && height < 400 && randomVal < 0.2f) {
            int treeSelect = random.nextInt(4);
            switch (treeSelect) {
                case 0 -> state = TileState.TREE;
                case 1 -> state = TileState.FLOWER_TREE;
                case 2 -> state = TileState.FRUIT_TREE;
                case 3 -> state = TileState.PINE_TREE;
            }
        } else if (height >= 0 && height < 400 && randomVal < 0.2025f ) {
            state = TileState.BUILDING_WALL;
            ArrayList<MapTile> tiles = new ArrayList<>();
            for (int i = 0; i > -4; i--) {
                for (int j = 0; j > -4; j--) {
                    if (i == 0 && j == 0) continue;
                    if (g.map.getHeight(new TileLocation(location.x + i, location.y + j)) > 400
                            || g.map.getHeight(new TileLocation(location.x + i, location.y + j)) < 0
                            || Math.abs(height - g.map.getHeight(new TileLocation(location.x + i, location.y + j))) > 50) {
                        this.state = TileState.EMPTY;
                        return;
                    }

                    if (g.map.tileMap.containsKey(new TileLocation(location.x + i, location.y + j))) {
                        MapTile tile = g.map.tileMap.get(new TileLocation(location.x + i, location.y + j));
                        if (tile.state == TileState.BUILDING_WALL || tile.state == TileState.BUILDING_DRAW || tile.state == TileState.BUILDING_ENTRANCE) return;
                    }

                    if ((i == -1 || i == -2) && j == 0) {
                        tiles.add(new MapTile(new TileLocation(location.x + i, location.y + j), height, TileState.BUILDING_ENTRANCE));
                    }
                    else if (i == -1 && j == -1) tiles.add(new MapTile(new TileLocation(location.x + i, location.y + j), height + 1, TileState.BUILDING_DRAW));
                    else if (j == -3) tiles.add(new MapTile(new TileLocation(location.x + i, location.y + j), height, TileState.EMPTY));
                    else tiles.add(new MapTile(new TileLocation(location.x + i, location.y + j), height, TileState.BUILDING_WALL));
                }
            }

            ArrayList<TileLocation> entrances = new ArrayList<>();

            for (MapTile tile: tiles) {
                if (tile.state == TileState.BUILDING_ENTRANCE) entrances.add(tile.location);
            }

            Building building = new Building(height, random.nextInt(4, 9), random.nextInt(4, 9), entrances, this.location);
            for (MapTile tile: tiles) {
                if (tile.state == TileState.BUILDING_ENTRANCE) {
                    tile.buildingMap = building;
                }
                g.map.tileMap.put(tile.location, tile);
            }
        }

        if (height >= 400 && height < 800 && Math.random() < 0.1f) {
            if (height <= 500) {
                int rockSelect = random.nextInt(3);
                switch (rockSelect) {
                    case 0 -> state = TileState.MOSS_ROCK_1;
                    case 1 -> state = TileState.MOSS_ROCK_2;
                    case 2 -> state = TileState.MOSS_ROCK_3;
                }
            } else {
                int rockSelect = random.nextInt(6);
                switch (rockSelect) {
                    case 0 -> state = TileState.ROCK_1;
                    case 1 -> state = TileState.ROCK_2;
                    case 2 -> state = TileState.ROCK_3;
                    case 3 -> state = TileState.WHITE_ROCK_1;
                    case 4 -> state = TileState.WHITE_ROCK_2;
                    case 5 -> state = TileState.WHITE_ROCK_3;
                }
            }
        }

        if (height > 800 && Math.random() < 0.1) {
            int rockSelect = random.nextInt(3);
            switch (rockSelect) {
                case 0 -> state = TileState.SNOW_ROCK_1;
                case 1 -> state = TileState.SNOW_ROCK_2;
                case 2 -> state = TileState.SNOW_ROCK_3;
            }
        }
    }

    public MapTile(TileLocation loc, float height, TileState state) {
        this.location = loc;
        this.height = height;
        this.state = state;
    }

    public float getHeight() {
        return height;
    }

    @Override
    public void setup() {

    }

    @Override
    public void draw() {
        g.pushMatrix();
        float playerHeight;
        if (!g.inBuilding) {
            playerHeight = g.map.tileMap.get(TileLocation.worldToTileCoords(g.player.pos)).height;
        } else {
            playerHeight = g.buildingToDraw.height;
        }
        playerHeight = PApplet.lerp(prevPlayerHeight, playerHeight, 0.05f);
        prevPlayerHeight = playerHeight;
        float relativeHeight = height < 0 ? -playerHeight : height - playerHeight;

        PVector worldLocation = TileLocation.tileToWorldCoords(location);
        PMatrix3D rightBlockMatrix = null;
        PMatrix3D downBlockMatrix = null;
        PMatrix3D leftBlockMatrix = null;
        PMatrix3D upBlockMatrix = null;
        PMatrix3D blockMatrix;
        TileLocation loc;

        g.push();
        g.translate(g.player.pos.x, g.player.pos.y);
        g.scale(PApplet.map(relativeHeight, -1000, 1000, 0.5f, 1.5f));
        blockMatrix = (PMatrix3D) g.getMatrix();
        g.pop();

        if (g.map.tileMap.containsKey(new TileLocation(location.x + 1, location.y))) {
            MapTile rightTile = g.map.tileMap.get(new TileLocation(location.x + 1, location.y));
            rightBlockMatrix = getMatrix3D(playerHeight, rightTile);
        }

        if (g.map.tileMap.containsKey(new TileLocation(location.x, location.y + 1))) {
            MapTile downTile = g.map.tileMap.get(new TileLocation(location.x, location.y + 1));
            downBlockMatrix = getMatrix3D(playerHeight, downTile);
        }

        if (g.map.tileMap.containsKey(new TileLocation(location.x - 1, location.y))) {
            MapTile leftTile = g.map.tileMap.get(new TileLocation(location.x - 1, location.y));
            leftBlockMatrix = getMatrix3D(playerHeight, leftTile);
        }

        if (g.map.tileMap.containsKey(new TileLocation(location.x, location.y - 1))) {
            MapTile upTile = g.map.tileMap.get(new TileLocation(location.x, location.y - 1));
            upBlockMatrix = getMatrix3D(playerHeight, upTile);
        }

        if (height < 400) {
            g.fill(83, 65, 39);
        } else if (height >= 400 && height < 800) {
            g.fill(120 - (height - 400) / 4f);
        } else {
            g.fill(255);
        }

        loc = new TileLocation(location.x + 1, location.y);
        if (g.map.tileMap.containsKey(loc) && this.height > g.map.tileMap.get(loc).height) {
            g.push();
            PVector p1 = blockMatrix.mult(new PVector(worldLocation.x + TILE_SIZE - g.player.pos.x, worldLocation.y - g.player.pos.y), null);
            PVector p2 = blockMatrix.mult(new PVector(worldLocation.x + TILE_SIZE - g.player.pos.x, worldLocation.y + TILE_SIZE - g.player.pos.y), null);
            PVector p3 = rightBlockMatrix.mult(new PVector(worldLocation.x + TILE_SIZE - g.player.pos.x, worldLocation.y - g.player.pos.y), null);
            PVector p4 = rightBlockMatrix.mult(new PVector(worldLocation.x + TILE_SIZE - g.player.pos.x, worldLocation.y + TILE_SIZE - g.player.pos.y), null);

            g.translate(g.player.pos.x - g.width / 2f, g.player.pos.y - g.height / 2f);
            if (p1.x < p3.x) {
                drawCustomQuad(p1, p2, p3, p4);
            }
            g.pop();
        }

        loc = new TileLocation(location.x, location.y + 1);
        if (g.map.tileMap.containsKey(loc) && this.height > g.map.tileMap.get(loc).height) {
            g.push();
            PVector p1 = blockMatrix.mult(new PVector(worldLocation.x - g.player.pos.x, worldLocation.y - g.player.pos.y + TILE_SIZE), null);
            PVector p2 = blockMatrix.mult(new PVector(worldLocation.x + TILE_SIZE - g.player.pos.x, worldLocation.y + TILE_SIZE - g.player.pos.y), null);
            PVector p3 = downBlockMatrix.mult(new PVector(worldLocation.x - g.player.pos.x, worldLocation.y - g.player.pos.y + TILE_SIZE), null);
            PVector p4 = downBlockMatrix.mult(new PVector(worldLocation.x + TILE_SIZE - g.player.pos.x, worldLocation.y + TILE_SIZE - g.player.pos.y), null);

            g.translate(g.player.pos.x - g.width / 2f, g.player.pos.y - g.height / 2f);
            if (p1.y < p3.y) {
                drawCustomQuad(p1, p2, p3, p4);
            }
            g.pop();
        }

        loc = new TileLocation(location.x - 1, location.y);
        if (g.map.tileMap.containsKey(loc) && this.height > g.map.tileMap.get(loc).height) {
            g.push();
            PVector p1 = blockMatrix.mult(new PVector(worldLocation.x - g.player.pos.x, worldLocation.y - g.player.pos.y), null);
            PVector p2 = blockMatrix.mult(new PVector(worldLocation.x - g.player.pos.x, worldLocation.y + TILE_SIZE - g.player.pos.y), null);
            PVector p3 = leftBlockMatrix.mult(new PVector(worldLocation.x - g.player.pos.x, worldLocation.y - g.player.pos.y), null);
            PVector p4 = leftBlockMatrix.mult(new PVector(worldLocation.x - g.player.pos.x, worldLocation.y + TILE_SIZE - g.player.pos.y), null);

            g.translate(g.player.pos.x - g.width / 2f, g.player.pos.y - g.height / 2f);
            if (p1.x > p3.x) {
                drawCustomQuad(p1, p2, p3, p4);
            }
            g.pop();
        }

        loc = new TileLocation(location.x, location.y - 1);
        if (g.map.tileMap.containsKey(loc) && this.height > g.map.tileMap.get(loc).height) {
            g.push();
            PVector p1 = blockMatrix.mult(new PVector(worldLocation.x - g.player.pos.x, worldLocation.y - g.player.pos.y), null);
            PVector p2 = blockMatrix.mult(new PVector(worldLocation.x + TILE_SIZE - g.player.pos.x, worldLocation.y - g.player.pos.y), null);
            PVector p3 = upBlockMatrix.mult(new PVector(worldLocation.x - g.player.pos.x, worldLocation.y - g.player.pos.y), null);
            PVector p4 = upBlockMatrix.mult(new PVector(worldLocation.x + TILE_SIZE - g.player.pos.x, worldLocation.y - g.player.pos.y), null);

            g.translate(g.player.pos.x - g.width / 2f, g.player.pos.y - g.height / 2f);
            if (p1.y > p3.y) {
                drawCustomQuad(p1, p2, p3, p4);
            }
            g.pop();
        }

        heightColor(height);
        g.stroke(0);

        g.translate(g.player.pos.x, g.player.pos.y);
        g.scale(PApplet.map(relativeHeight, -1000, 1000, 0.5f, 1.5f));
        g.rect(worldLocation.x - g.player.pos.x, worldLocation.y - g.player.pos.y, TILE_SIZE, TILE_SIZE);
        g.popMatrix();
    }

    private PMatrix3D getMatrix3D(float playerHeight, MapTile tile) {
        PMatrix3D downBlockMatrix;
        float downBlockRelHeight = tile.height < 0 ? -playerHeight : tile.height - playerHeight;
        g.push();
        g.translate(g.player.pos.x, g.player.pos.y);
        g.scale(PApplet.map(downBlockRelHeight, -1000, 1000, 0.5f, 1.5f));
        downBlockMatrix = (PMatrix3D) g.getMatrix();
        g.pop();
        return downBlockMatrix;
    }

    private void drawCustomQuad(PVector p1, PVector p2, PVector p3, PVector p4) {
        g.beginShape();
        g.vertex(p1.x, p1.y);
        g.vertex(p2.x, p2.y);
        g.vertex(p4.x, p4.y);
        g.vertex(p3.x, p3.y);
        g.endShape();
    }

    private void heightColor(float height) {
        if (height < 0) {
            g.fill(0, 26 + (255 + height) / 2f, 255 + height);
        } else {
            if (height >= 0 && height < 400) {
                g.fill(72 - height / 7f, 150 - height / 8f, 15);
            } else if (height >= 400 && height < 800) {
                g.fill(150 - (height - 400) / 4f);
            } else {
                g.fill(255);
            }
        }
    }

    public void drawObjects() {
        g.pushMatrix();
        float playerHeight = g.map.tileMap.get(TileLocation.worldToTileCoords(g.player.pos)).height;
        playerHeight = PApplet.lerp(prevPlayerHeight, playerHeight, 0.05f);
        prevPlayerHeight = playerHeight;
        float relativeHeight = height - playerHeight;

        PVector worldLocation = TileLocation.tileToWorldCoords(location);
        g.translate(g.player.pos.x, g.player.pos.y);
        g.scale(PApplet.map(relativeHeight, -1000, 1000, 0.5f, 1.5f));

        switch (state) {
            case LOOT -> {
                g.image(MapAssets.chest, worldLocation.x - g.player.pos.x + (TILE_SIZE - MapAssets.chest.width) / 2f, worldLocation.y - g.player.pos.y + (TILE_SIZE - MapAssets.chest.height) / 2f);
            }
            case LOOT_EMPTY -> {
                g.image(MapAssets.chestOpen, worldLocation.x - g.player.pos.x + (TILE_SIZE - MapAssets.chestOpen.width) / 2f, worldLocation.y - g.player.pos.y + (TILE_SIZE - MapAssets.chestOpen.height) / 2f);
            }

            case EVENT -> {
                g.image(MapAssets.event, worldLocation.x - g.player.pos.x + (TILE_SIZE - 12) / 2f, worldLocation.y - g.player.pos.y + (TILE_SIZE - 34) / 2f, 12, 34);
            }

            case TREE -> {
                g.image(MapAssets.tree, worldLocation.x - g.player.pos.x + (TILE_SIZE - MapAssets.tree.width) / 2f, worldLocation.y - g.player.pos.y + (TILE_SIZE - MapAssets.tree.height) / 2f);
            }
            case PINE_TREE -> {
                g.image(MapAssets.pine_tree, worldLocation.x - g.player.pos.x + (TILE_SIZE - MapAssets.pine_tree.width) / 2f, worldLocation.y - g.player.pos.y + (TILE_SIZE - MapAssets.pine_tree.height) / 2f);
            }
            case FLOWER_TREE -> {
                g.image(MapAssets.flower_tree, worldLocation.x - g.player.pos.x + (TILE_SIZE - MapAssets.flower_tree.width) / 2f, worldLocation.y - g.player.pos.y + (TILE_SIZE - MapAssets.flower_tree.height) / 2f);
            }
            case FRUIT_TREE -> {
                g.image(MapAssets.fruit_tree, worldLocation.x - g.player.pos.x + (TILE_SIZE - MapAssets.fruit_tree.width) / 2f, worldLocation.y - g.player.pos.y + (TILE_SIZE - MapAssets.fruit_tree.height) / 2f);
            }

            case MOSS_ROCK_1 -> {
                g.image(MapAssets.moss_rock_1, worldLocation.x - g.player.pos.x + (TILE_SIZE - MapAssets.moss_rock_1.width) / 2f, worldLocation.y - g.player.pos.y + (TILE_SIZE - MapAssets.moss_rock_1.height) / 2f);
            }
            case MOSS_ROCK_2 -> {
                g.image(MapAssets.moss_rock_2, worldLocation.x - g.player.pos.x + (TILE_SIZE - MapAssets.moss_rock_2.width) / 2f, worldLocation.y - g.player.pos.y + (TILE_SIZE - MapAssets.moss_rock_2.height) / 2f);
            }
            case MOSS_ROCK_3 -> {
                g.image(MapAssets.moss_rock_3, worldLocation.x - g.player.pos.x, worldLocation.y - g.player.pos.y, 50, 50);
            }


            case ROCK_1 -> {
                g.image(MapAssets.rock_1, worldLocation.x - g.player.pos.x + (TILE_SIZE - MapAssets.rock_1.width) / 2f, worldLocation.y - g.player.pos.y + (TILE_SIZE - MapAssets.rock_1.height) / 2f);
            }
            case ROCK_2 -> {
                g.image(MapAssets.rock_2, worldLocation.x - g.player.pos.x + (TILE_SIZE - MapAssets.rock_2.width) / 2f, worldLocation.y - g.player.pos.y + (TILE_SIZE - MapAssets.rock_2.height) / 2f);
            }
            case ROCK_3 -> {
                g.image(MapAssets.rock_3, worldLocation.x - g.player.pos.x, worldLocation.y - g.player.pos.y, 50, 50);
            }
            case WHITE_ROCK_1 -> {
                g.image(MapAssets.white_rock_1, worldLocation.x - g.player.pos.x + (TILE_SIZE - MapAssets.white_rock_1.width) / 2f, worldLocation.y - g.player.pos.y + (TILE_SIZE - MapAssets.white_rock_1.height) / 2f);
            }
            case WHITE_ROCK_2 -> {
                g.image(MapAssets.white_rock_2, worldLocation.x - g.player.pos.x + (TILE_SIZE - MapAssets.white_rock_2.width) / 2f, worldLocation.y - g.player.pos.y + (TILE_SIZE - MapAssets.white_rock_2.height) / 2f);
            }
            case WHITE_ROCK_3 -> {
                g.image(MapAssets.white_rock_3, worldLocation.x - g.player.pos.x, worldLocation.y - g.player.pos.y, 50, 50);
            }

            case SNOW_ROCK_1 -> {
                g.image(MapAssets.snow_rock_1, worldLocation.x - g.player.pos.x + (TILE_SIZE - MapAssets.snow_rock_1.width) / 2f, worldLocation.y - g.player.pos.y + (TILE_SIZE - MapAssets.snow_rock_1.height) / 2f);
            }
            case SNOW_ROCK_2 -> {
                g.image(MapAssets.snow_rock_2, worldLocation.x - g.player.pos.x + (TILE_SIZE - MapAssets.snow_rock_2.width) / 2f, worldLocation.y - g.player.pos.y + (TILE_SIZE - MapAssets.snow_rock_2.height) / 2f);
            }
            case SNOW_ROCK_3 -> {
                g.image(MapAssets.snow_rock_3, worldLocation.x - g.player.pos.x, worldLocation.y - g.player.pos.y, 50, 50);
            }

            case BUILDING_DRAW -> {
                g.image(MapAssets.building, worldLocation.x - g.player.pos.x - 2 * TILE_SIZE, worldLocation.y - g.player.pos.y - 2 * TILE_SIZE, 4 * TILE_SIZE, 4 * TILE_SIZE);
            }
        }
        g.popMatrix();
    }

    @Override
    public void update() {

    }
}

