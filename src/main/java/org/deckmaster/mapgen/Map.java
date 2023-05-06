package org.deckmaster.mapgen;

import org.deckmaster.Drawable;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

public class Map implements Drawable {
    public HashMap<TileLocation, MapTile> tileMap = new HashMap<>();

    public float getHeight(TileLocation loc) {
        return PApplet.map(g.noise(loc.x / 20f + 10000, loc.y / 20f + 10000), 0, 1, -200, 1000);
    }

    public void setup() {
        TileLocation startLocation = TileLocation.worldToTileCoords(new PVector(g.cameraPosition.x - g.width, g.cameraPosition.y - g.height));
        for (int x = 0; x < 2 * g.width / MapTile.TILE_SIZE; x++) {
            for (int y = 0; y < 2 * g.height / MapTile.TILE_SIZE; y++) {
                TileLocation tileLoc = new TileLocation(startLocation.x + x, startLocation.y + y);
                MapTile tile = new MapTile(tileLoc, getHeight(tileLoc));
                tileMap.put(tileLoc, tile);
            }
        }

        MapTile tileToCheck = tileMap.get(TileLocation.worldToTileCoords(g.player.pos));
        while (tileToCheck.height < 0 || !tileToCheck.state.walkable) {
            g.player.pos.x += MapTile.TILE_SIZE;
            tileToCheck = tileMap.get(TileLocation.worldToTileCoords(g.player.pos));
        }
    }

    public void draw() {
        TileLocation startLocation = TileLocation.worldToTileCoords(new PVector(g.cameraPosition.x - 3 * g.width / 5f, g.cameraPosition.y - 3 * g.height / 5f));
        PriorityQueue<MapTile> tiles = new PriorityQueue<>(Comparator.comparing(MapTile::getHeight));
        for (int x = 0; x < 3 * g.width / (2.5f * MapTile.TILE_SIZE); x++) {
            for (int y = 0; y < 3 * g.height / (2.5f * MapTile.TILE_SIZE); y++) {
                TileLocation tileLoc = new TileLocation(startLocation.x + x, startLocation.y + y);
                MapTile tile;
                if (tileMap.containsKey(tileLoc)) {
                    tile = tileMap.get(tileLoc);
                } else {
                    tile = new MapTile(tileLoc, getHeight(tileLoc));
                    tileMap.put(tileLoc, tile);
                }
                tiles.add(tile);
            }
        }

        while (!tiles.isEmpty()) {
            MapTile tile = tiles.poll();
            tile.draw();
            tile.drawObjects();
        }
    }

    public void update() {

    }
}
