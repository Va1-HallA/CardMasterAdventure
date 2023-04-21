package org.deckmaster;

import processing.core.PImage;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class CardSlot implements Drawable {

    private static PVector size = new PVector(g.width * Configurations.CARD_WIDTH_PROPORTION, g.height * Configurations.CARD_HEIGHT_PROPORTION);

    private PVector coord;

    private ArrayList<Property> requirements;

    private Card filledCard;

    public CardSlot(ArrayList<Property> requirements) {
        this.requirements = new ArrayList<>(requirements);
        this.coord = new PVector();
        this.filledCard = null;
    }

    public PVector getCoord() {
        return coord;
    }

    public static PVector getSize() {
        return size;
    }

    public Card getFilledCard() {
        return filledCard;
    }

    public boolean isFilled() {
        return filledCard != null;
    }

    public void setCoord(PVector coord) {
        this.coord = new PVector(coord.x, coord.y);
    }

    public boolean pair(Card card) {
        if (filledCard != null && filledCard != card) return false;
        ArrayList<Property> pList = new ArrayList<>(card.getPropertyTable().keySet());

        if (requirements.size() == 0) {
            filledCard = card;
            return true;
        }

        // if one property is in requirements, return true

        for (Property r : requirements) {
            for (Property p : pList) {
                if (p.equals(r)) {
                    filledCard = card;
                    return true;
                }
            }
        }

        return false;
    }

    public void unpair() {
        filledCard = null;
    }

    @Override
    public void setup() {

    }

    @Override
    public void draw() {
//        g.fill(0);
//        g.rect(coord.x, coord.y, size.x, size.y);
        PImage bckground = g.loadImage("images/cards/background.png");
        bckground.resize((int) size.x, (int) size.y);
        g.image(bckground, coord.x, coord.y);
        if (filledCard != null) filledCard.draw();
    }

    @Override
    public void update() {

    }
}
