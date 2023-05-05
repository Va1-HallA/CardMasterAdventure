package org.deckmaster.mapgen;

import org.deckmaster.Drawable;
import processing.core.*;

import java.util.ArrayList;
import java.util.Random;


public class MapTile implements Drawable {
    public static Integer TILE_SIZE = 50;
    public TileLocation location;
    float prevPlayerHeight = 0;
    public float height;
    public TileState state = TileState.EMPTY;
    static Random random = new Random();
    static PImage tree = g.loadImage("images/map/Tree3.png");
    static PImage flower_tree = g.loadImage("images/map/Flower_tree2-1.png");
    static PImage fruit_tree = g.loadImage("images/map/Fruit_tree3.png");
    static PImage pine_tree = g.loadImage("images/map/Christmas_tree3.png");

    static PImage moss_rock_1 = g.loadImage("images/map/Rock4_3.png");
    static PImage moss_rock_2 = g.loadImage("images/map/Rock4_4.png");
    static PImage moss_rock_3 = g.loadImage("images/map/Rock4_1.png");

    static PImage rock_1 = g.loadImage("images/map/Rock1_3.png");
    static PImage rock_2 = g.loadImage("images/map/Rock1_4.png");
    static PImage rock_3 = g.loadImage("images/map/Rock1_1.png");
    static PImage white_rock_1 = g.loadImage("images/map/Rock5_3.png");
    static PImage white_rock_2 = g.loadImage("images/map/Rock5_4.png");
    static PImage white_rock_3 = g.loadImage("images/map/Rock5_1.png");

    static PImage snow_rock_1 = g.loadImage("images/map/Rock3_3.png");
    static PImage snow_rock_2 = g.loadImage("images/map/Rock3_4.png");
    static PImage snow_rock_3 = g.loadImage("images/map/Rock3_1.png");
    static PImage building = g.loadImage("images/map/Smithy-TD.png");

    public MapTile(TileLocation loc, float height) {
        this.location = loc;
        this.height = height;
        double randomVal = Math.random();
        if (height >= 0 && height < 400 && randomVal < 0.2f) {
            int treeSelect = random.nextInt(4);
            switch (treeSelect) {
                case 0 -> state = TileState.TREE;
                case 1 -> state = TileState.FLOWER_TREE;
                case 2 -> state = TileState.FRUIT_TREE;
                case 3 -> state = TileState.PINE_TREE;
            }
        } else if (height >= 0 && height < 400 && randomVal < 0.205f ) {
            state = TileState.BUILDING_WALL;
            ArrayList<MapTile> tiles = new ArrayList<>();
            for (int i = 0; i > -4; i--) {
                for (int j = 0; j > -4; j--) {
                    if (i == 0 && j == 0) continue;
                    if (g.map.getHeight(new TileLocation(location.x + i, location.y + j)) > 400 || Math.abs(height - g.map.getHeight(new TileLocation(location.x + i, location.y + j))) > 50) {
                        this.state = TileState.EMPTY;
                        return;
                    }

                    if (g.map.tileMap.containsKey(new TileLocation(location.x + i, location.y + j))) {
                        MapTile tile = g.map.tileMap.get(new TileLocation(location.x + i, location.y + j));
                        if (tile.state == TileState.BUILDING_WALL || tile.state == TileState.BUILDING_DRAW || tile.state == TileState.BUILDING_ENTRANCE) return;
                    }

                    if ((i == -1 || i == -2) && j == 0) tiles.add(new MapTile(new TileLocation(location.x + i, location.y + j), height, TileState.BUILDING_ENTRANCE));
                    else if (i == -1 && j == -1) tiles.add(new MapTile(new TileLocation(location.x + i, location.y + j), height + 1, TileState.BUILDING_DRAW));
                    else if (j == -3) tiles.add(new MapTile(new TileLocation(location.x + i, location.y + j), height, TileState.EMPTY));
                    else tiles.add(new MapTile(new TileLocation(location.x + i, location.y + j), height, TileState.BUILDING_WALL));
                }
            }

            for (MapTile tile: tiles) {
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
        float playerHeight = g.map.tileMap.get(TileLocation.worldToTileCoords(g.player.pos)).height;
        playerHeight = PApplet.lerp(prevPlayerHeight, playerHeight, 0.05f);
        prevPlayerHeight = playerHeight;
        float relativeHeight = height < 0 ? -playerHeight : height - playerHeight;

        PVector worldLocation = TileLocation.tileToWorldCoords(location);
        PMatrix3D rightBlockMatrix = null;
        PMatrix3D downBlockMatrix = null;
        PMatrix3D leftBlockMatrix = null;
        PMatrix3D upBlockMatrix = null;
        PMatrix3D blockMatrix;

        g.push();
        g.translate(g.player.pos.x, g.player.pos.y);
        g.scale(PApplet.map(relativeHeight, -1000, 1000, 0.5f, 1.5f));
        blockMatrix = (PMatrix3D) g.getMatrix();
        g.pop();

        if (g.map.tileMap.containsKey(new TileLocation(location.x + 1, location.y))) {
            MapTile rightTile = g.map.tileMap.get(new TileLocation(location.x + 1, location.y));
            float rightBlockRelHeight = rightTile.height < 0 ? -playerHeight : rightTile.height - playerHeight;
            g.push();
            g.translate(g.player.pos.x, g.player.pos.y);
            g.scale(PApplet.map(rightBlockRelHeight, -1000, 1000, 0.5f, 1.5f));
            rightBlockMatrix = (PMatrix3D) g.getMatrix();
            g.pop();
        }

        if (g.map.tileMap.containsKey(new TileLocation(location.x, location.y + 1))) {
            MapTile downTile = g.map.tileMap.get(new TileLocation(location.x, location.y + 1));
            float downBlockRelHeight = downTile.height < 0 ? -playerHeight : downTile.height - playerHeight;
            g.push();
            g.translate(g.player.pos.x, g.player.pos.y);
            g.scale(PApplet.map(downBlockRelHeight, -1000, 1000, 0.5f, 1.5f));
            downBlockMatrix = (PMatrix3D) g.getMatrix();
            g.pop();
        }

        if (g.map.tileMap.containsKey(new TileLocation(location.x - 1, location.y))) {
            MapTile leftTile = g.map.tileMap.get(new TileLocation(location.x - 1, location.y));
            float leftBlockRelHeight = leftTile.height < 0 ? -playerHeight : leftTile.height - playerHeight;
            g.push();
            g.translate(g.player.pos.x, g.player.pos.y);
            g.scale(PApplet.map(leftBlockRelHeight, -1000, 1000, 0.5f, 1.5f));
            leftBlockMatrix = (PMatrix3D) g.getMatrix();
            g.pop();
        }

        if (g.map.tileMap.containsKey(new TileLocation(location.x, location.y - 1))) {
            MapTile upTile = g.map.tileMap.get(new TileLocation(location.x, location.y - 1));
            float upBlockRelHeight = upTile.height < 0 ? -playerHeight : upTile.height - playerHeight;
            g.push();
            g.translate(g.player.pos.x, g.player.pos.y);
            g.scale(PApplet.map(upBlockRelHeight, -1000, 1000, 0.5f, 1.5f));
            upBlockMatrix = (PMatrix3D) g.getMatrix();
            g.pop();
        }

        if (height < 400) {
            g.fill(83,65,39);
        } else if (height >= 400 && height < 800) {
            g.fill(120 - (height - 400) / 4f);
        } else {
            g.fill(255);
        }

        if (g.map.tileMap.containsKey(new TileLocation(location.x + 1, location.y))) {
            g.push();
            PVector p1 = blockMatrix.mult(new PVector(worldLocation.x + TILE_SIZE - g.player.pos.x, worldLocation.y - g.player.pos.y), null);
            PVector p2 = blockMatrix.mult(new PVector(worldLocation.x + TILE_SIZE - g.player.pos.x, worldLocation.y + TILE_SIZE - g.player.pos.y), null);
            PVector p3 = rightBlockMatrix.mult(new PVector(worldLocation.x + TILE_SIZE - g.player.pos.x, worldLocation.y - g.player.pos.y), null);
            PVector p4 = rightBlockMatrix.mult(new PVector(worldLocation.x + TILE_SIZE - g.player.pos.x, worldLocation.y + TILE_SIZE - g.player.pos.y), null);

            g.translate(g.player.pos.x - g.width / 2f, g.player.pos.y - g.height / 2f);

            if (p1.x < p3.x) {
                g.beginShape();
                g.vertex(p1.x, p1.y);
                g.vertex(p2.x, p2.y);
                g.vertex(p4.x, p4.y);
                g.vertex(p3.x, p3.y);
                g.endShape();
            }
            g.pop();
        }

        if (g.map.tileMap.containsKey(new TileLocation(location.x, location.y + 1))) {
            g.push();
            PVector p1 = blockMatrix.mult(new PVector(worldLocation.x - g.player.pos.x, worldLocation.y - g.player.pos.y + TILE_SIZE), null);
            PVector p2 = blockMatrix.mult(new PVector(worldLocation.x + TILE_SIZE - g.player.pos.x, worldLocation.y + TILE_SIZE - g.player.pos.y), null);
            PVector p3 = downBlockMatrix.mult(new PVector(worldLocation.x - g.player.pos.x, worldLocation.y - g.player.pos.y + TILE_SIZE), null);
            PVector p4 = downBlockMatrix.mult(new PVector(worldLocation.x + TILE_SIZE - g.player.pos.x, worldLocation.y + TILE_SIZE - g.player.pos.y), null);


            if (p1.y < p3.y) {
                g.translate(g.player.pos.x - g.width / 2f, g.player.pos.y - g.height / 2f);

                g.beginShape();
                g.vertex(p1.x, p1.y);
                g.vertex(p2.x, p2.y);
                g.vertex(p4.x, p4.y);
                g.vertex(p3.x, p3.y);
                g.endShape();
            }
            g.pop();
        }

        if (g.map.tileMap.containsKey(new TileLocation(location.x - 1, location.y))) {
            g.push();
            PVector p1 = blockMatrix.mult(new PVector(worldLocation.x - g.player.pos.x, worldLocation.y - g.player.pos.y), null);
            PVector p2 = blockMatrix.mult(new PVector(worldLocation.x - g.player.pos.x, worldLocation.y + TILE_SIZE - g.player.pos.y), null);
            PVector p3 = leftBlockMatrix.mult(new PVector(worldLocation.x - g.player.pos.x, worldLocation.y - g.player.pos.y), null);
            PVector p4 = leftBlockMatrix.mult(new PVector(worldLocation.x - g.player.pos.x, worldLocation.y + TILE_SIZE - g.player.pos.y), null);


            if (p1.x > p3.x) {
                g.translate(g.player.pos.x - g.width / 2f, g.player.pos.y - g.height / 2f);

                g.beginShape();
                g.vertex(p1.x, p1.y);
                g.vertex(p2.x, p2.y);
                g.vertex(p4.x, p4.y);
                g.vertex(p3.x, p3.y);
                g.endShape();
            }
            g.pop();
        }

        if (g.map.tileMap.containsKey(new TileLocation(location.x, location.y - 1))) {
            g.push();
            PVector p1 = blockMatrix.mult(new PVector(worldLocation.x - g.player.pos.x, worldLocation.y - g.player.pos.y), null);
            PVector p2 = blockMatrix.mult(new PVector(worldLocation.x + TILE_SIZE - g.player.pos.x, worldLocation.y - g.player.pos.y), null);
            PVector p3 = upBlockMatrix.mult(new PVector(worldLocation.x - g.player.pos.x, worldLocation.y - g.player.pos.y), null);
            PVector p4 = upBlockMatrix.mult(new PVector(worldLocation.x + TILE_SIZE - g.player.pos.x, worldLocation.y - g.player.pos.y), null);


            if (p1.y > p3.y) {
                g.translate(g.player.pos.x - g.width / 2f, g.player.pos.y - g.height / 2f);

                g.beginShape();
                g.vertex(p1.x, p1.y);
                g.vertex(p2.x, p2.y);
                g.vertex(p4.x, p4.y);
                g.vertex(p3.x, p3.y);
                g.endShape();
            }
            g.pop();
        }

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

        g.stroke(0);
        g.translate(g.player.pos.x, g.player.pos.y);
        g.scale(PApplet.map(relativeHeight, -1000, 1000, 0.5f, 1.5f));
        g.rect(worldLocation.x - g.player.pos.x, worldLocation.y - g.player.pos.y, TILE_SIZE, TILE_SIZE);
        g.popMatrix();
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

            }
            case EVENT -> {

            }

            case TREE -> {
                g.image(tree, worldLocation.x - g.player.pos.x + (TILE_SIZE - tree.width) / 2f, worldLocation.y - g.player.pos.y + (TILE_SIZE - tree.height) / 2f);
            }
            case PINE_TREE -> {
                g.image(pine_tree, worldLocation.x - g.player.pos.x + (TILE_SIZE - pine_tree.width) / 2f, worldLocation.y - g.player.pos.y + (TILE_SIZE - pine_tree.height) / 2f);
            }
            case FLOWER_TREE -> {
                g.image(flower_tree, worldLocation.x - g.player.pos.x + (TILE_SIZE - flower_tree.width) / 2f, worldLocation.y - g.player.pos.y + (TILE_SIZE - flower_tree.height) / 2f);
            }
            case FRUIT_TREE -> {
                g.image(fruit_tree, worldLocation.x - g.player.pos.x + (TILE_SIZE - fruit_tree.width) / 2f, worldLocation.y - g.player.pos.y + (TILE_SIZE - fruit_tree.height) / 2f);
            }

            case MOSS_ROCK_1 -> {
                g.image(moss_rock_1, worldLocation.x - g.player.pos.x + (TILE_SIZE - moss_rock_1.width) / 2f, worldLocation.y - g.player.pos.y + (TILE_SIZE - moss_rock_1.height) / 2f);
            }
            case MOSS_ROCK_2 -> {
                g.image(moss_rock_2, worldLocation.x - g.player.pos.x + (TILE_SIZE - moss_rock_2.width) / 2f, worldLocation.y - g.player.pos.y + (TILE_SIZE - moss_rock_2.height) / 2f);
            }
            case MOSS_ROCK_3 -> {
                g.image(moss_rock_3, worldLocation.x - g.player.pos.x, worldLocation.y - g.player.pos.y, 50, 50);
            }


            case ROCK_1 -> {
                g.image(rock_1, worldLocation.x - g.player.pos.x + (TILE_SIZE - rock_1.width) / 2f, worldLocation.y - g.player.pos.y + (TILE_SIZE - rock_1.height) / 2f);
            }
            case ROCK_2 -> {
                g.image(rock_2, worldLocation.x - g.player.pos.x + (TILE_SIZE - rock_2.width) / 2f, worldLocation.y - g.player.pos.y + (TILE_SIZE - rock_2.height) / 2f);
            }
            case ROCK_3 -> {
                g.image(rock_3, worldLocation.x - g.player.pos.x, worldLocation.y - g.player.pos.y, 50, 50);
            }
            case WHITE_ROCK_1 -> {
                g.image(white_rock_1, worldLocation.x - g.player.pos.x + (TILE_SIZE - white_rock_1.width) / 2f, worldLocation.y - g.player.pos.y + (TILE_SIZE - white_rock_1.height) / 2f);
            }
            case WHITE_ROCK_2 -> {
                g.image(white_rock_2, worldLocation.x - g.player.pos.x + (TILE_SIZE - white_rock_2.width) / 2f, worldLocation.y - g.player.pos.y + (TILE_SIZE - white_rock_2.height) / 2f);
            }
            case WHITE_ROCK_3 -> {
                g.image(white_rock_3, worldLocation.x - g.player.pos.x, worldLocation.y - g.player.pos.y, 50, 50);
            }

            case SNOW_ROCK_1 -> {
                g.image(snow_rock_1, worldLocation.x - g.player.pos.x + (TILE_SIZE - snow_rock_1.width) / 2f, worldLocation.y - g.player.pos.y + (TILE_SIZE - snow_rock_1.height) / 2f);
            }
            case SNOW_ROCK_2 -> {
                g.image(snow_rock_2, worldLocation.x - g.player.pos.x + (TILE_SIZE - snow_rock_2.width) / 2f, worldLocation.y - g.player.pos.y + (TILE_SIZE - snow_rock_2.height) / 2f);
            }
            case SNOW_ROCK_3 -> {
                g.image(snow_rock_3, worldLocation.x - g.player.pos.x, worldLocation.y - g.player.pos.y, 50, 50);
            }

            case BUILDING_DRAW -> {
                g.image(building, worldLocation.x - g.player.pos.x - 2 * TILE_SIZE, worldLocation.y - g.player.pos.y - 2 * TILE_SIZE, 4 * TILE_SIZE, 4 * TILE_SIZE);
            }
        }
        g.popMatrix();
    }

    @Override
    public void update() {

    }
}

