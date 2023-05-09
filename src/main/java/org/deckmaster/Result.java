package org.deckmaster;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public enum Result {
    CURSE,
    COMMODITY,
    CONSUME_FUNDS,
    DRAIN,
    GAME_END,
    FILL,
    REVIVE,
    WITHER,
    ;

    static final Game g = Game.game;

    Result() {

    }

    void execute(int value) {
        switch (this) {
            case CURSE:
                curse(value);
                break;
            case COMMODITY:
                commodity(value);
                break;
            case CONSUME_FUNDS:
                consumeFunds(value);
                break;
            case DRAIN:
                drain(value);
                break;
            case GAME_END:
                gameEnd();
                break;
            case FILL:
                fill(value);
                break;
            case REVIVE:
                revive(value);
                break;
            case WITHER:
                wither(value);
                break;
        }
    }

    private void commodity(int value) {
        // get random non-unique card
        ArrayList<Card> addedCard = new ArrayList<>();

        while (addedCard.size() < value) {
            int randomIdx = ThreadLocalRandom.current().nextInt(0, g.nonUniqueCards.size());
            Card c = g.contentLoader.loadCard(g.nonUniqueCards.get(randomIdx));
            addedCard.add(c);
        }

        for (Card c : addedCard) {
            g.player.addCard(c);
        }
    }

    private void consumeFunds(int value) {
        // remove fund cards that has property value x
        int removed = 0;
        ArrayList<Card> removedCard = new ArrayList<>();
        for (Card c : g.player.getCards()) {
            if (removed >= value) break;
            if (c.hasProperty(Property.FUND)) {
                int val = c.getPropertyTable().get(Property.FUND);
                removed += val;
                removedCard.add(c);
            }
        }

        for (Card c : removedCard) {
            g.player.removeCard(c);
        }
    }

    private void curse(int value) {
        // remove fitness or withering permanently
        int removed = 0;
        ArrayList<Card> removedCard = new ArrayList<>();
        for (Card c : g.player.getCards()) {
            if (removed >= value) break;
            if (c.getName().equals("Withering")) {
                removed += 1;
                removedCard.add(c);
            }
        }

        for (Card c : removedCard) {
            g.player.removeCard(c);
        }

        // if no enough withering, remove fitness instead
        if (value - removed > 0) {
            int newTarget = value - removed;
            int newRemoved = 0;
            removedCard.clear();

            for (Card c : g.player.getCards()) {
                if (newRemoved >= newTarget) break;
                if (c.getName().equals("Fitness")) {
                    newRemoved += 1;
                    removedCard.add(c);
                }
            }

            for (Card c : removedCard) {
                g.player.removeCard(c);
            }
        }
    }

    private void drain(int value) {
        // turn x mana into drainage
        int removed = 0;
        ArrayList<Card> removedCard = new ArrayList<>();
        ArrayList<Card> addedCard = new ArrayList<>();
        for (Card c : g.player.getCards()) {
            if (removed >= value) break;
            if (c.getName().equals("Mana")) {
                removed += 1;
                removedCard.add(c);

                Card drainage = g.contentLoader.loadCard("Drainage");
                addedCard.add(drainage);
            }
        }

        for (Card c : removedCard) {
            g.player.removeCard(c);
        }

        for (Card c : addedCard) {
            g.player.addCard(c);
        }

        // turn rest debt value of fitness into withering if mana is not enough
        if (value - removed > 0) {
            wither(value - removed);
        }
    }

    private void gameEnd() {
        // end game
        g.gameEnd = true;
    }

    private void fill(int value) {
        // turn x drainage into mana
        int recovered = 0;
        ArrayList<Card> removedCard = new ArrayList<>();
        ArrayList<Card> addedCard = new ArrayList<>();
        for (Card c : g.player.getCards()) {
            if (recovered >= value) break;
            if (c.getName().equals("Drainage")) {
                recovered += 1;
                removedCard.add(c);

                Card mana = g.contentLoader.loadCard("Mana");
                addedCard.add(mana);
            }
        }

        for (Card c : removedCard) {
            g.player.removeCard(c);
        }

        for (Card c : addedCard) {
            g.player.addCard(c);
        }
    }

    private void revive(int value) {
        // turn x withering into fitness

        int recovered = 0;
        ArrayList<Card> removedCard = new ArrayList<>();
        ArrayList<Card> addedCard = new ArrayList<>();
        for (Card c : g.player.getCards()) {
            if (recovered >= value) break;
            if (c.getName().equals("Withering")) {
                recovered += 1;
                removedCard.add(c);

                Card fitness = g.contentLoader.loadCard("Fitness");
                addedCard.add(fitness);
            }
        }

        for (Card c : removedCard) {
            g.player.removeCard(c);
        }

        for (Card c : addedCard) {
            g.player.addCard(c);
        }
    }

    private void wither(int value) {
        // turn x fitness into withering

        int removed = 0;
        ArrayList<Card> removedCard = new ArrayList<>();
        ArrayList<Card> addedCard = new ArrayList<>();
        for (Card c : g.player.getCards()) {
            if (removed >= value) break;
            if (c.getName().equals("Fitness")) {
                removed += 1;
                removedCard.add(c);

                Card wither = g.contentLoader.loadCard("Withering");
                addedCard.add(wither);
            }
        }

        for (Card c : removedCard) {
            g.player.removeCard(c);
        }

        for (Card c : addedCard) {
            g.player.addCard(c);
        }
    }
}
