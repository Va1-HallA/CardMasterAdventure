package org.deckmaster.mapgen;

import org.deckmaster.Drawable;
import processing.core.PApplet;
import processing.core.PVector;


public class MapTile implements Drawable {
    public static Integer TILE_SIZE = 100;
    TileLocation location;
    float prevPlayerHeight = 0;
    public float height;
    public TileState state = TileState.EMPTY;

    public MapTile(TileLocation loc, float height) {
        this.location = loc;
        this.height = height;
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
        playerHeight = PApplet.lerp(prevPlayerHeight, playerHeight, 0.1f);
        prevPlayerHeight = playerHeight;
        float relativeHeight = height - playerHeight;
        if (height < 0) {
            g.fill(0, 26 + (255 + height) / 2f, 255 + height);
        } else {
            if (height >= 0 && height < 400) {
                float color = Math.min(Math.max(150 + relativeHeight / 2, 50), 200);
                g.fill(0, color, 13);
            } else if (height >= 400 && height < 900) {
                float color = Math.min(Math.max(100 + relativeHeight / 2, 50), 150);
                g.fill(color);
            } else {
                g.fill(255);
            }
        }

        PVector worldLocation = TileLocation.tileToWorldCoords(location);
        g.translate(g.player.pos.x, g.player.pos.y);
        g.scale(PApplet.map(relativeHeight, -1200, 1200, 0.5f, 1.5f));
        g.stroke(0);
        g.rect(worldLocation.x - g.player.pos.x, worldLocation.y - g.player.pos.y, TILE_SIZE, TILE_SIZE);
//            if (g.debug) {
//                g.textSize(12);
//                g.fill(255, 0, 0);
//                g.textAlign(g.CENTER, g.CENTER);
//                g.text(height, worldLocation.x - g.cameraPosition.x + TILE_SIZE / 2f, worldLocation.y - g.cameraPosition.y + TILE_SIZE / 2f);
//            }
        g.popMatrix();
    }

    public void drawUnder() {
        g.pushMatrix();
        float playerHeight = g.map.tileMap.get(TileLocation.worldToTileCoords(g.player.pos)).height;
        float relativeHeight = height - playerHeight;
        if (height < 0) {
            g.stroke(0, 26 + (255 + height) / 2f, 255 + height);
            g.fill(0, 26 + (255 + height) / 2f, 255 + height);
        } else {
            if (height >= 0 && height < 400) {
                float color = Math.min(Math.max(150 + relativeHeight / 2, 50), 200);
                g.stroke(0, color, 13);
                g.fill(0, color, 13);
            } else if (height >= 400 && height < 900) {
                float color = Math.min(Math.max(100 + relativeHeight / 2, 50), 150);
                g.stroke(color);
                g.fill(color);
            } else {
                g.stroke(255);
                g.fill(255);
            }
        }
        PVector worldLocation = TileLocation.tileToWorldCoords(location);
        g.translate(g.player.pos.x, g.player.pos.y);
        g.scale(PApplet.map(relativeHeight, -1200, 1200, 0.5f, 1.5f));
        g.rect(worldLocation.x - g.player.pos.x, worldLocation.y - g.player.pos.y, TILE_SIZE, TILE_SIZE);
        g.popMatrix();
    }

    @Override
    public void update() {

    }
}

