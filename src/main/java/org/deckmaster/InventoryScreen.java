package org.deckmaster;

import processing.core.PConstants;
import processing.core.PVector;

import java.util.*;

public class InventoryScreen implements Drawable {
    private Player player;
    private int curBatchNum;
    List<Card> curCardList;
    Button leftArrow;
    Button rightArrow;

    boolean isDisplayingProperty;
    Property curDisplayingProperty;

    public InventoryScreen(Player player) {
        this.player = player;
        curCardList = new ArrayList<>();
        curBatchNum = 0;

        isDisplayingProperty = false;
        curDisplayingProperty = null;
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
        g.pushMatrix();
        g.translate(g.cameraPosition.x - g.width / 2f, g.cameraPosition.y - g.height / 2f);

        // make sure display is centered
        ArrayList<Card> cardList = player.getCards();
        PVector cardSize = new PVector(g.width * Configurations.CARD_WIDTH_PROPORTION, g.height * Configurations.CARD_HEIGHT_PROPORTION);
        PVector curPosition = new PVector((float) g.width / 2, (float) g.height * Configurations.INVENTORY_HEIGHT_PROPORTION - cardSize.y);
        int startIndex = Configurations.BATCH_MAX_CARDS_NUM * curBatchNum;
        int endIndex = startIndex + Configurations.BATCH_MAX_CARDS_NUM;
        if (endIndex >= cardList.size()) endIndex = cardList.size();
        curPosition.add(new PVector(-1 * cardSize.x * 0.5f * (endIndex - startIndex), 0));

        // draw left button
        float buttonSizeProportion = 0.3f;
        Runnable prev = this::previousBatch;
        leftArrow = new Button((int) (curPosition.x - cardSize.x * buttonSizeProportion), (int) (curPosition.y + cardSize.y * 0.5f), (int) (cardSize.x * buttonSizeProportion), (int) (cardSize.x * buttonSizeProportion), "prev", prev, new PVector(0,0,0), 150, Configurations.CARD_FONT, 16);

        // draw cards
        for (Card card : curCardList) {
            if (!card.isPaired() && !card.isDragged()) card.setCoord(curPosition);
            if (!card.isPaired()) card.draw();
            curPosition.add(new PVector(cardSize.x, 0));
        }

        // draw right button
        Runnable next = this::nextBatch;
        rightArrow = new Button((int) (curPosition.x), (int) (curPosition.y + cardSize.y * 0.5f), (int) (cardSize.x * buttonSizeProportion), (int) (cardSize.x * buttonSizeProportion), "next", next, new PVector(0,0,0), 150, Configurations.CARD_FONT, 16);

        leftArrow.draw();
        rightArrow.draw();

        // display property info if applicable
        if (isDisplayingProperty) {
            g.fill(g.color(55));
            g.rect(g.mouseX, g.mouseY, g.width * Configurations.CARD_PROP_INFO_WIDTH_PROPORTION, g.height * Configurations.CARD_PROP_INFO_HEIGHT_PROPORTION);
            g.fill(g.color(255));
            g.textFont(Configurations.PROPERTY_FONT);
            g.text(curDisplayingProperty.description, g.mouseX, g.mouseY,
                    g.width * Configurations.CARD_PROP_INFO_WIDTH_PROPORTION, g.height * Configurations.CARD_PROP_INFO_HEIGHT_PROPORTION);
            isDisplayingProperty = false;
            curDisplayingProperty = null;
        }

        g.popMatrix();
    }

    @Override
    public void update() {

        // update current card list
        ArrayList<Card> cardList = new ArrayList<>(player.getCards());
        int startIndex = Configurations.BATCH_MAX_CARDS_NUM * curBatchNum;
        int endIndex = startIndex + Configurations.BATCH_MAX_CARDS_NUM;
        if (endIndex >= cardList.size()) endIndex = cardList.size();
        curCardList = cardList.subList(startIndex, endIndex);

        // update each card
        for (Card card : curCardList) if (!card.isPaired()) card.update();

    }
}
