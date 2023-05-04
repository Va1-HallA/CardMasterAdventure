package org.deckmaster.mapgen;

import org.deckmaster.Drawable;
import processing.core.*;

import java.util.Random;


public class MapTile implements Drawable {
    public static Integer TILE_SIZE = 50;
    TileLocation location;
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

    public MapTile(TileLocation loc, float height) {
        this.location = loc;
        this.height = height;
        if (height >= 0 && height < 400 && Math.random() < 0.2f) {
            int treeSelect = random.nextInt(4);
            switch (treeSelect) {
                case 0 -> state = TileState.TREE;
                case 1 -> state = TileState.FLOWER_TREE;
                case 2 -> state = TileState.FRUIT_TREE;
                case 3 -> state = TileState.PINE_TREE;
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
        float relativeHeight = height - playerHeight;
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

        PVector worldLocation = TileLocation.tileToWorldCoords(location);
        g.stroke(0);
        g.translate(g.player.pos.x, g.player.pos.y);
        g.scale(PApplet.map(relativeHeight, -1000, 1000, 0.5f, 1.5f));
        g.rect(worldLocation.x - g.player.pos.x, worldLocation.y - g.player.pos.y, TILE_SIZE, TILE_SIZE);
//        if (g.debug) {
//            g.textSize(12);
//            g.fill(255, 0, 0);
//            g.textAlign(g.CENTER, g.CENTER);
//            g.text(height, worldLocation.x - g.player.pos.x + TILE_SIZE / 2f, worldLocation.y - g.player.pos.y + TILE_SIZE / 2f);
//        }

//        PVector playerLoc = new PVector(g.player.pos.x, g.player.pos.y, playerHeight);
//        PVector blockLoc = new PVector(worldLocation.x, worldLocation.y, height);
//
//        g.fill(200, PVector.dist(playerLoc, blockLoc) / 5f);
//        g.rect(worldLocation.x - g.player.pos.x, worldLocation.y - g.player.pos.y, TILE_SIZE, TILE_SIZE);

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
        }
        g.popMatrix();
    }

    @Override
    public void update() {

    }
}

