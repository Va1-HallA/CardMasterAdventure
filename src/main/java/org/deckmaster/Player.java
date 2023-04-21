package org.deckmaster;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Player implements Drawable {
    private ArrayList<Card> cards;

    public Player() {
        cards = new ArrayList<>();
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

    @Override
    public void setup() {

    }

    @Override
    public void draw() {

    }

    @Override
    public void update() {

    }
}
