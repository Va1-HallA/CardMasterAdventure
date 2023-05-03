package org.deckmaster.mapgen;

import com.google.common.collect.MinMaxPriorityQueue;
import org.checkerframework.checker.units.qual.A;
import org.deckmaster.Drawable;
import org.deckmaster.Game;
import org.deckmaster.GameState;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

public class Map implements Drawable {
    public HashMap<TileLocation, MapTile> tileMap = new HashMap<>();

    private float getHeight(TileLocation loc) {
        return PApplet.map(g.noise(loc.x / 20f + 10000, loc.y / 20f + 10000), 0, 1, -200, 1000);
    }

    public void setup() {
        TileLocation startLocation = TileLocation.worldToTileCoords(new PVector(g.cameraPosition.x - g.width, g.cameraPosition.y - g.height));
        for (int x = 0; x < g.width / MapTile.TILE_SIZE; x++) {
            for (int y = 0; y < g.height / MapTile.TILE_SIZE; y++) {
                TileLocation tileLoc = new TileLocation(startLocation.x + x, startLocation.y + y);
                MapTile tile = new MapTile(tileLoc, getHeight(tileLoc));
                tileMap.put(tileLoc, tile);
            }
        }
    }

    public void draw() {
        TileLocation startLocation = TileLocation.worldToTileCoords(new PVector(g.cameraPosition.x - 3 * g.width / 4f, g.cameraPosition.y - 3 * g.height / 4f));
        PriorityQueue<MapTile> tiles = new PriorityQueue<>(Comparator.comparing(MapTile::getHeight));
        ArrayList<MapTile> orderedTile = new ArrayList<>();
        for (int x = 0; x < 3 * g.width / (2 * MapTile.TILE_SIZE); x++) {
            for (int y = 0; y < 3 * g.height / (2 * MapTile.TILE_SIZE); y++) {
                TileLocation tileLoc = new TileLocation(startLocation.x + x, startLocation.y + y);
                MapTile tile;
                if (tileMap.containsKey(tileLoc)) {
                    tile = tileMap.get(tileLoc);
                } else {
                    tile = new MapTile(tileLoc, getHeight(tileLoc));
                    tileMap.put(tileLoc, tile);
                }
                tiles.add(tile);
                orderedTile.add(tile);
            }
        }

        for (MapTile tile: orderedTile) {
            tile.drawUnder();
        }

        while (!tiles.isEmpty()) {
            tiles.poll().draw();
        }

    }

    public void update() {

    }
}
