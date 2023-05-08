package org.deckmaster.mapgen;

import org.deckmaster.Drawable;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PMatrix3D;
import processing.core.PVector;

import java.io.Serializable;

public class BuildingTile implements Drawable, Serializable {
    public static Integer TILE_SIZE = 50;
    public TileLocation location;
    float prevPlayerHeight = 0;
    public float height;
    public TileState state;
    public BuildingTile(TileLocation loc, float height, TileState state) {
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

        g.push();
        g.translate(g.player.pos.x, g.player.pos.y);
        g.scale(PApplet.map(relativeHeight, -1000, 1000, 0.5f, 1.5f));
        g.pop();

        g.noFill();
        g.noStroke();
        if (state == TileState.BUILDING_EXIT) {
            float outsideHeight = g.map.tileMap.get(this.location).height;
            g.stroke(0);
            heightColor(outsideHeight);
        }

        g.translate(g.player.pos.x, g.player.pos.y);
        g.scale(PApplet.map(relativeHeight, -1000, 1000, 0.5f, 1.5f));
        if (state == TileState.BUILDING_INSIDE || state == TileState.BUILDING_INSIDE_CHEST || state == TileState.BUILDING_INSIDE_CHEST_OPEN) {
            g.image(MapAssets.woodenTile, worldLocation.x - g.player.pos.x, worldLocation.y - g.player.pos.y, TILE_SIZE, TILE_SIZE);
        } else {
            g.rect(worldLocation.x - g.player.pos.x, worldLocation.y - g.player.pos.y, TILE_SIZE, TILE_SIZE);
        }
        g.popMatrix();
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
            case BUILDING_INSIDE_CHEST -> {
                g.image(MapAssets.chest, worldLocation.x - g.player.pos.x + (TILE_SIZE - MapAssets.chest.width) / 2f, worldLocation.y - g.player.pos.y + (TILE_SIZE - MapAssets.chest.height) / 2f);
            }
            case BUILDING_INSIDE_CHEST_OPEN -> {
                g.image(MapAssets.chestOpen, worldLocation.x - g.player.pos.x + (TILE_SIZE - MapAssets.chestOpen.width) / 2f, worldLocation.y - g.player.pos.y + (TILE_SIZE - MapAssets.chestOpen.height) / 2f);
            }
            case BUILDING_INSIDE_EVENT -> {
                g.image(MapAssets.event, worldLocation.x - g.player.pos.x + (TILE_SIZE - 12) / 2f, worldLocation.y - g.player.pos.y + (TILE_SIZE - 34) / 2f, 12, 34);
            }
        }
        g.popMatrix();
    }
    @Override
    public void update() {

    }
}
