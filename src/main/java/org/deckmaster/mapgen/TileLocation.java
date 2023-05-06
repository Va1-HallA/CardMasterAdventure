package org.deckmaster.mapgen;

import processing.core.PVector;

import java.io.Serializable;

public class TileLocation implements Serializable {
    public int x;
    public int y;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TileLocation that = (TileLocation) o;

        if (x != that.x) return false;
        return y == that.y;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }

    public TileLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static TileLocation worldToTileCoords(PVector worldCoords) {
        return new TileLocation((int) Math.floor(worldCoords.x / MapTile.TILE_SIZE), (int) Math.floor(worldCoords.y/ MapTile.TILE_SIZE));
    }

    public static PVector tileToWorldCoords(TileLocation tileLocation) {
        return new PVector(tileLocation.x * MapTile.TILE_SIZE, tileLocation.y * MapTile.TILE_SIZE);
    }
}
