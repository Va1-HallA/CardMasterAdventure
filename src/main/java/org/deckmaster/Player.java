package org.deckmaster;

import org.deckmaster.mapgen.Map;
import org.deckmaster.mapgen.MapTile;
import org.deckmaster.mapgen.TileLocation;
import org.deckmaster.mapgen.TileState;
import processing.core.PImage;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.Collections;

public class Player implements Drawable {
    private ArrayList<Card> cards;
    public PVector pos;
    private float size;
    private final float MOVE_SPEED = 350.0f;
    private MovementDir dir = MovementDir.UP;
    private PImage playerImage;
    private PImage up;
    private PImage down;
    private PImage left;

    boolean hasWon;

    public Player (PVector pos, float size) {
        this.pos = pos;
        this.size = size;
        playerImage = g.loadImage("images/player/hero.png");
        up = playerImage.get(32, 32, 32, 32);
        down = playerImage.get(32, 64, 32, 32);
        left = playerImage.get(32, 0, 32, 32);
        cards = new ArrayList<>();
        hasWon = false;
    }

    @Override
    public void setup() {}

    @Override
    public void draw() {
        g.pushMatrix();
//        g.stroke(0, 255, 255);
//        g.fill(0, 255, 255);
        switch (dir) {
            case UP -> {
                g.image(up, pos.x - 16, pos.y - 16, 32, 32);
            }
            case DOWN -> {
                g.image(down, pos.x - 16, pos.y - 16, 32, 32);
            }
            case LEFT -> {
                g.image(left, pos.x - 16, pos.y - 16,32, 32);
            }
            case RIGHT -> {
                g.scale(-1, 1);
                g.image(left, -pos.x - 16, pos.y - 16, 32, 32);
            }
        }
        g.popMatrix();
    }

    @Override
    public void update() {
        handleMovement();
        TileLocation playerLocation = TileLocation.worldToTileCoords(pos);
        if (!g.inBuilding) {
            MapTile tile = g.map.tileMap.get(playerLocation);
            if (tile.state == TileState.LOOT) {
                tile.state = TileState.LOOT_EMPTY;
            }

            if (tile.state == TileState.EVENT) {
                tile.state = TileState.EMPTY;
                //TODO: randomly select an active event in the list
                g.state = GameState.EVENT;
            }

            if (tile.state == TileState.BUILDING_ENTRANCE) {
                g.buildingToDraw = tile.buildingMap;
                g.inBuilding = true;
            }
        } else {
            MapTile tile = g.buildingToDraw.tiles.get(playerLocation);
            if (tile.state == TileState.BUILDING_INSIDE_CHEST) {
                tile.state = TileState.BUILDING_INSIDE_CHEST_OPEN;
            }
            if (tile.state == TileState.BUILDING_EXIT) {
                g.inBuilding = false;
            }
        }
    }

    private void handleMovement() {
        float moveAmount = MOVE_SPEED * (g.UPDATE_TIME / 1000f);
        PVector moveVector = new PVector(0, 0);
        if (Input.aPressed) {
            moveVector = moveVector.add(-1, 0);
            dir = MovementDir.LEFT;
        }
        if (Input.dPressed) {
            moveVector = moveVector.add(1, 0);
            dir = MovementDir.RIGHT;
        }
        if (Input.sPressed) {
            moveVector = moveVector.add(0, 1);
            dir = MovementDir.DOWN;
        }
        if (Input.wPressed) {
            moveVector = moveVector.add(0, -1);
            dir = MovementDir.UP;
        }
        moveVector = moveVector.normalize();
        moveVector = moveVector.mult(moveAmount);
        TileLocation playerLocation = TileLocation.worldToTileCoords(pos);
        PVector nextLocation = PVector.add(pos, moveVector);
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (i == 0 && j == 0) continue;
                MapTile tileToCheck;
                if (!g.inBuilding) {
                    tileToCheck = g.map.tileMap.get(new TileLocation(playerLocation.x + i, playerLocation.y + j));
                } else {
                    tileToCheck = g.buildingToDraw.tiles.get(new TileLocation(playerLocation.x + i, playerLocation.y + j));
                }
                if (tileToCheck.height < 0 || !tileToCheck.state.walkable) {
                    PVector tileLocation = TileLocation.tileToWorldCoords(tileToCheck.location);
                    PVector p = new PVector();
                    if (nextLocation.x <= tileLocation.x) {
                        p.x = tileLocation.x;
                    } else p.x = Math.min(nextLocation.x, tileLocation.x + MapTile.TILE_SIZE);

                    if (nextLocation.y <= tileLocation.y) {
                        p.y = tileLocation.y;
                    } else p.y = Math.min(nextLocation.y, tileLocation.y + MapTile.TILE_SIZE);

                    if (PVector.sub(nextLocation, p).magSq() < 8 * 8) return;
                }
            }
        }

        pos = pos.add(moveVector);
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public void setCards(ArrayList<Card> cards) {this.cards = cards;}

    public void addCard(Card card) {
        cards.add(card);
        Collections.sort(cards);
    }

    public void removeCard(Card card) {
        cards.remove(card);
    }
}

enum MovementDir {
    UP, DOWN, LEFT, RIGHT
}
