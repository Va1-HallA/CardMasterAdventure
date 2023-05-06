package org.deckmaster.mapgen;

import org.deckmaster.Drawable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Building implements Drawable {
    float height;
    public HashMap<TileLocation, MapTile> tiles = new HashMap<>();
    static Random random = new Random();

    public Building(float height, int x, int y, ArrayList<TileLocation> entrances, TileLocation startTile) {
        int numberOfChestsToAdd = random.nextInt(4);
        this.height = height;
        for (int i = 0; i <= x; i++) {
            for (int j = 0; j <= y; j++) {
                TileLocation loc = new TileLocation(startTile.x - i, startTile.y - j);
                if (Math.random() < 0.2f && numberOfChestsToAdd > 0 && !entrances.contains(loc)) {
                    tiles.put(loc, new MapTile(loc, height, TileState.BUILDING_INSIDE_CHEST));
                    numberOfChestsToAdd--;
                } else {
                    tiles.put(loc, new MapTile(loc, height, TileState.BUILDING_INSIDE));
                }
            }
        }

        for (int i = -1; i <= y + 1; i++) {
            TileLocation loc = new TileLocation(startTile.x + 1, startTile.y - i);
            tiles.put(loc, new MapTile(loc, height, TileState.BUILDING_INSIDE_WALL));
        }

        for (int i = -1; i <= y + 1; i++) {
            TileLocation loc = new TileLocation(startTile.x - x - 1, startTile.y - i);
            tiles.put(loc, new MapTile(loc, height, TileState.BUILDING_INSIDE_WALL));
        }

        for (int i = 0; i <= x; i++) {
            TileLocation loc = new TileLocation(startTile.x - i, startTile.y + 1);
            tiles.put(loc, new MapTile(loc, height, TileState.BUILDING_INSIDE_WALL));
        }

        for (int i = 0; i <= x; i++) {
            TileLocation loc = new TileLocation(startTile.x - i, startTile.y - y - 1);
            tiles.put(loc, new MapTile(loc, height, TileState.BUILDING_INSIDE_WALL));
        }

        for (TileLocation loc: entrances) {
            tiles.put(new TileLocation(loc.x, loc.y + 1), new MapTile(new TileLocation(loc.x, loc.y + 1), height, TileState.BUILDING_EXIT));
        }
    }

    @Override
    public void setup() {

    }

    @Override
    public void draw() {
        for (MapTile tile: tiles.values()) {
            tile.draw();
            tile.drawObjects();
        }
    }

    @Override
    public void update() {

    }
}
