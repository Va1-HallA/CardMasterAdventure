package org.deckmaster;

import processing.core.PVector;

import java.util.*;

public class InventoryScreen implements Drawable {
    private Player player;
    private int curBatchNum;
    List<Card> curCardList;
    Button leftArrow;
    Button rightArrow;

    public InventoryScreen(Player player) {
        this.player = player;
        curCardList = new ArrayList<>();
        curBatchNum = 0;
        // build a map for card highlighting

//        // convert to list, sort and convert back to hashmap
//        List<Map.Entry<Card, Boolean>> temp = new ArrayList<>(highlightMap.entrySet());
//        temp.sort(new Comparator<Map.Entry<Card, Boolean>>() {
//            public int compare(Map.Entry<Card, Boolean> o1, Map.Entry<Card, Boolean> o2) {
//                return o1.getKey().compareTo(o2.getKey());
//            }
//        });
//
//        highlightMap = new HashMap<>();
//        for (Map.Entry<Card, Boolean> e : temp) {
//            highlightMap.put(e.getKey(), e.getValue());
//        }
    }

    public void show() {
        // curBatchNum = 0;
    }

    public void vanish() {
//        curBatchNum = 0;
        for (Card c : player.getCards()) {
            // TODO: clean cards
        }
    }

    public void nextBatch() {
        if ((curBatchNum + 1) * Configurations.BATCH_MAX_CARDS_NUM < player.getCards().size()) {
            curBatchNum += 1;
        }
    }

    public void previousBatch() {
        if ((curBatchNum - 1) * Configurations.BATCH_MAX_CARDS_NUM >= 0) {
            curBatchNum -= 1;
        }
    }

    @Override
    public void setup() {
    }

    @Override
    public void draw() {

        // make sure display is centered
        ArrayList<Card> cardList = player.getCards();
        PVector cardSize = new PVector(g.width * Configurations.CARD_WIDTH_PROPORTION, g.height * Configurations.CARD_HEIGHT_PROPORTION);
        PVector curPosition = new PVector((float) g.width / 2, (float) g.height * Configurations.INVENTORY_HEIGHT_PROPORTION - cardSize.y);
        int startIndex = Configurations.BATCH_MAX_CARDS_NUM * curBatchNum;
        int endIndex = startIndex + Configurations.BATCH_MAX_CARDS_NUM;
        if (endIndex >= cardList.size()) endIndex = cardList.size();
        curPosition.add(new PVector(-1 * cardSize.x * 0.5f * (endIndex - startIndex), 0));

        // draw left button
        float buttonSizePorportion = 0.3f;

        Runnable prev = new Runnable() {
            @Override
            public void run() {
                previousBatch();
            }
        };

        leftArrow = new Button((int) (curPosition.x - cardSize.x * buttonSizePorportion), (int) (curPosition.y + cardSize.y * 0.5f), (int) (cardSize.x * buttonSizePorportion), (int) (cardSize.x * buttonSizePorportion), "prev", prev);

        // draw cards

        for (Card card : curCardList) {
            if (!card.isPaired() && !card.isDragged()) card.setCoord(curPosition);
            if (!card.isPaired()) card.draw();
            curPosition.add(new PVector(cardSize.x, 0));
        }

        // draw right button
        Runnable next = new Runnable() {
            @Override
            public void run() {
                nextBatch();
            }
        };
        rightArrow = new Button((int) (curPosition.x), (int) (curPosition.y + cardSize.y * 0.5f), (int) (cardSize.x * buttonSizePorportion), (int) (cardSize.x * buttonSizePorportion), "next", next);

        leftArrow.draw();
        rightArrow.draw();

    }

    @Override
    public void update() {

        // update current card list
        ArrayList<Card> cardList = player.getCards();
        int startIndex = Configurations.BATCH_MAX_CARDS_NUM * curBatchNum;
        int endIndex = startIndex + Configurations.BATCH_MAX_CARDS_NUM;
        if (endIndex >= cardList.size()) endIndex = cardList.size();
        curCardList = cardList.subList(startIndex, endIndex);

        // update each card
        for (Card card : curCardList) if (!card.isPaired()) card.update();
    }
}
