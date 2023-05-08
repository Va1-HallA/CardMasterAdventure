package org.deckmaster;

import org.checkerframework.checker.units.qual.A;
import org.deckmaster.mapgen.*;
import processing.core.PImage;
import processing.core.PVector;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import static org.deckmaster.PlayerAssets.*;

public class Player implements Drawable {
    private ArrayList<Card> cards;
    public PVector pos;
    private final float MOVE_SPEED = 350.0f;
    private MovementDir dir = MovementDir.UP;
    private Random random = new Random();

    public Player (PVector pos) {
        this.pos = pos;
        cards = new ArrayList<>();
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
        checkDeath();
        handleMovement();
        TileLocation playerLocation = TileLocation.worldToTileCoords(pos);
        if (!g.inBuilding) {
            MapTile tile = g.map.tileMap.get(playerLocation);
            if (tile.state == TileState.LOOT) {
                tile.state = TileState.LOOT_EMPTY;
                g.givePlayerLoot();
            }

            if (tile.state == TileState.EVENT) {
                tile.state = TileState.EMPTY;
                //TODO: randomly select an active event in the list
                g.openNewEvent();
                g.state = GameState.EVENT;
            }

            if (tile.state == TileState.BUILDING_ENTRANCE) {
                g.buildingToDraw = tile.buildingMap;
                g.inBuilding = true;
            }
        } else {
            BuildingTile tile = g.buildingToDraw.tiles.get(playerLocation);
            if (tile.state == TileState.BUILDING_INSIDE_CHEST) {
                tile.state = TileState.BUILDING_INSIDE_CHEST_OPEN;
                g.givePlayerLoot();
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
                if (!g.inBuilding) {
                    MapTile tileToCheck = g.map.tileMap.get(new TileLocation(playerLocation.x + i, playerLocation.y + j));
                    if (checkCollision(nextLocation, tileToCheck.height, tileToCheck.state, tileToCheck.location))
                        return;
                } else {
                    BuildingTile tileToCheck = g.buildingToDraw.tiles.get(new TileLocation(playerLocation.x + i, playerLocation.y + j));
                    if (checkCollision(nextLocation, tileToCheck.height, tileToCheck.state, tileToCheck.location))
                        return;
                }
            }
        }

        pos = pos.add(moveVector);
    }

    private boolean checkCollision(PVector nextLocation, float height, TileState state, TileLocation location) {
        if (height < 0 || !state.walkable) {
            PVector tileLocation = TileLocation.tileToWorldCoords(location);
            PVector p = new PVector();
            if (nextLocation.x <= tileLocation.x) {
                p.x = tileLocation.x;
            } else p.x = Math.min(nextLocation.x, tileLocation.x + MapTile.TILE_SIZE);

            if (nextLocation.y <= tileLocation.y) {
                p.y = tileLocation.y;
            } else p.y = Math.min(nextLocation.y, tileLocation.y + MapTile.TILE_SIZE);

            if (PVector.sub(nextLocation, p).magSq() < 8 * 8) return true;
        }
        return false;
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public void setCards(ArrayList<Card> cards) {this.cards = cards;}

    public void addCard(Card card) {
        // at most hold 1 same LEGEND card
        boolean isLegend = false;
        for (Property p : Property.values()) {
            if (p.name().startsWith("LEGEND") && card.hasProperty(p)) {
                isLegend = true;
                break;
            }
        }

        if (isLegend) {
            boolean haveSameCard = false;
            for (Card c : cards) {
                if (c.getName().equals(card.getName())) {
                    haveSameCard = true;
                    break;
                }
            }

            if (!haveSameCard) cards.add(card);
        } else cards.add(card);
        Collections.sort(cards);
    }

    public void removeCard(Card card) {
        cards.remove(card);
    }

    private void checkDeath() {
        // check current fitness when in world state, if no fitness, trigger special event and end game
        if (g.state == GameState.WORLD) {
            boolean death = true;
            for (Card c : cards) {
                if (c.getName().equals("Fitness")) {
                    death = false;
                    break;
                }
            }
            if (death) g.triggerSpecialEvents("Sudden Death");
        }
    }
}

enum MovementDir {
    UP, DOWN, LEFT, RIGHT
}
