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
    private final float MOVE_SPEED = 300.0f;

    public Player (PVector pos, float size) {
        this.pos = pos;
        this.size = size;
        cards = new ArrayList<>();
    }

    @Override
    public void setup() {}

    @Override
    public void draw() {
        g.pushMatrix();
        g.stroke(0, 255, 255);
        g.fill(0, 255, 255);
        g.rect(pos.x - 12, pos.y - 12, 24, 24);
        g.popMatrix();
    }

    @Override
    public void update() {
        handleMovement();
    }

    private void handleMovement() {
        float moveAmount = MOVE_SPEED * (g.UPDATE_TIME / 1000f);
        PVector moveVector = new PVector(0, 0);
        if (Input.wPressed) {
            moveVector = moveVector.add(0, -1);
        }
        if (Input.sPressed) {
            moveVector = moveVector.add(0, 1);
        }
        if (Input.aPressed) {
            moveVector = moveVector.add(-1, 0);
        }
        if (Input.dPressed) {
            moveVector = moveVector.add(1, 0);
        }
        moveVector = moveVector.normalize();
        moveVector = moveVector.mult(moveAmount);
        MapTile tileMovedTo = g.map.tileMap.get(TileLocation.worldToTileCoords(PVector.add(pos, moveVector)));

        if (tileMovedTo.height < 0) return;
        if (tileMovedTo.state == TileState.WHITE_ROCK_3 || tileMovedTo.state == TileState.MOSS_ROCK_3
                || tileMovedTo.state == TileState.ROCK_3 || tileMovedTo.state == TileState.SNOW_ROCK_3) return;
        pos = pos.add(moveVector);
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public void addCard(Card card) {
        cards.add(card);
        Collections.sort(cards);
    }

    public void removeCard(Card card) {
        cards.remove(card);
    }
}
