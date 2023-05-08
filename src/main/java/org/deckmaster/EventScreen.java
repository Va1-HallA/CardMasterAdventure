package org.deckmaster;

import processing.core.PImage;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.List;

public class EventScreen implements Drawable{
    private static PImage background = g.loadImage("images/events/EventScreenBackground.jpg");
    private List<CardSlot> slots;
    private Event event;
    private Player player;
    InventoryScreen inventory;
    Button confirmBtn;
    private State state;

    enum State{EVENT, RESULT}

    public EventScreen(Event event, Player player, InventoryScreen inventoryScreen) {
//        slot.update();
//        screen.update();
        this.event = event;
        this.player = player;
        this.inventory = inventoryScreen;
        this.slots = new ArrayList<>();
        this.state = State.EVENT;
        Runnable confirm = this::handleCardsInput;
        PVector curPosition = new PVector(
                g.width * (1 - Configurations.EVT_MAIN_WIDTH_PROPORTION - Configurations.EVT_IMG_WIDTH_PROPORTION) / 2,
                g.height * Configurations.EVT_Y_PROPORTION
        );
        curPosition.add(new PVector(event.getImage().width, 0));
        PVector btnSize = new PVector(g.width * Configurations.EVT_BTN_SIZE_PROPORTION, g.height * Configurations.EVT_BTN_SIZE_PROPORTION);
        PVector btnPosition = new PVector(
                curPosition.x + g.width * Configurations.EVT_MAIN_WIDTH_PROPORTION * 0.5f - btnSize.x * 0.5f,
                curPosition.y + g.height * Configurations.EVT_MAIN_HEIGHT_PROPORTION * Configurations.EVT_BTN_HEIGHT_PROPORTION
        );
        this.confirmBtn = new Button((int) btnPosition.x, (int) btnPosition.y, (int) btnSize.x, (int) btnSize.y, "confirm", confirm, new PVector(0,0,0), 150, Configurations.CARD_FONT, 20);
    }

    public List<CardSlot> getSlots() {
        return slots;
    }

    public void show() {
        for (int i = 0; i < event.getAllowedCardNum(); i++) {
            CardSlot newSlot;
            if (i < event.getSlotsRequirements().size()) {
                ArrayList<Property> slotRequirement = event.getSlotsRequirements().get(i);
                newSlot = new CardSlot(slotRequirement);
            } else newSlot = new CardSlot(new ArrayList<>());
            slots.add(newSlot);
        }

        inventory.show();
    }

    public void vanish() {
        inventory.vanish();
        slots.clear();
    }

    @Override
    public void setup() {

    }

    @Override
    public void draw() {
        g.pushMatrix();
        g.translate(g.cameraPosition.x - g.width / 2f, g.cameraPosition.y - g.height / 2f);
        PVector curPosition = new PVector(
                g.width * (1 - Configurations.EVT_MAIN_WIDTH_PROPORTION - Configurations.EVT_IMG_WIDTH_PROPORTION) / 2,
                g.height * Configurations.EVT_Y_PROPORTION
        );

        // drawing event image
        g.image(event.getImage(), curPosition.x, curPosition.y);

        curPosition.add(new PVector(g.width * Configurations.EVT_IMG_WIDTH_PROPORTION, 0));
        background.resize((int) (g.width * Configurations.EVT_MAIN_WIDTH_PROPORTION), (int) (g.height * Configurations.EVT_MAIN_HEIGHT_PROPORTION));
        g.image(background, curPosition.x, curPosition.y);
//        g.fill(50);
//        g.rect(curPosition.x, curPosition.y, g.width * Configurations.EVT_MAIN_WIDTH_PROPORTION, g.height * Configurations.EVT_MAIN_HEIGHT_PROPORTION);

        g.fill(0);
        g.textFont(Configurations.EVT_TITLE_FONT);
        PVector textPosition = new PVector(curPosition.x, curPosition.y);
        textPosition.add(new PVector(g.width * Configurations.EVT_MAIN_WIDTH_PROPORTION * 0.5f, g.height * Configurations.EVT_TITLE_Y_PROPORTION));
        g.text(event.getTitle(), textPosition.x, textPosition.y);

        if (state == State.EVENT) drawEventInfo(curPosition, textPosition);
        else if(state == State.RESULT) drawResultInfo(curPosition, textPosition);
        // drawing button
        confirmBtn.draw();

        // drawing inventory
        g.popMatrix();
        inventory.draw();
    }

    @Override
    public void update() {
        // track cards that are paired with slots (which are not tracked by inventory screen)

        if (state == State.EVENT) {
            for (CardSlot slot : slots) {
                if (slot.isFilled()) slot.getFilledCard().update();
            }
        }

        inventory.update();
    }

    private void drawEventInfo(PVector curPosition, PVector textPosition) {
        // drawing event info
        float textHeight = g.textAscent() + g.textDescent();
        textPosition.add(new PVector(-0.5f * g.width * Configurations.EVT_MAIN_WIDTH_PROPORTION * Configurations.EVT_TEXT_WIDTH_PROPORTION, textHeight));
        g.textFont(Configurations.EVT_DES_FONT);
        g.text(event.getDescription(), textPosition.x, textPosition.y,
                g.width * Configurations.EVT_MAIN_WIDTH_PROPORTION * Configurations.EVT_TEXT_WIDTH_PROPORTION, g.height * Configurations.EVT_MAIN_HEIGHT_PROPORTION * Configurations.EVT_TEXT_HEIGHT_PROPORTION);

        // drawing card slots
        float slotsHeight = curPosition.y + g.height * Configurations.EVT_MAIN_HEIGHT_PROPORTION * Configurations.SLOT_HEIGHT_PROPORTION;
        float slotWidth = g.width * Configurations.CARD_WIDTH_PROPORTION;
        PVector slotPosition = new PVector(
                curPosition.x + g.width * Configurations.EVT_MAIN_WIDTH_PROPORTION * 0.5f + (-1 * slotWidth * event.getAllowedCardNum() * 0.5f),
                slotsHeight
        );
        for (CardSlot slot : slots) {
            slot.setCoord(slotPosition);
            slotPosition.add(new PVector(slotWidth, 0));
            slot.draw();
        }
    }

    private void drawResultInfo(PVector curPosition, PVector textPosition) {
        // drawing result info
        float textHeight = g.textAscent() + g.textDescent();
        textPosition.add(new PVector(-0.5f * g.width * Configurations.EVT_MAIN_WIDTH_PROPORTION * Configurations.EVT_TEXT_WIDTH_PROPORTION, textHeight));
        g.textFont(Configurations.EVT_DES_FONT);
        g.text(event.getCurDes(), textPosition.x, textPosition.y,
                g.width * Configurations.EVT_MAIN_WIDTH_PROPORTION * Configurations.EVT_TEXT_WIDTH_PROPORTION, g.height * Configurations.EVT_MAIN_HEIGHT_PROPORTION * Configurations.EVT_TEXT_HEIGHT_PROPORTION);

        // drawing rewards (if any)
        float slotsHeight = curPosition.y + g.height * Configurations.EVT_MAIN_HEIGHT_PROPORTION * Configurations.SLOT_HEIGHT_PROPORTION;
        float slotWidth = g.width * Configurations.CARD_WIDTH_PROPORTION;
        PVector slotPosition = new PVector(
                curPosition.x + g.width * Configurations.EVT_MAIN_WIDTH_PROPORTION * 0.5f + (-1 * slotWidth * event.getAllowedCardNum() * 0.5f),
                slotsHeight
        );
        for (Card card : event.getCurRewardCards()) {
            card.setCoord(slotPosition);
            slotPosition.add(new PVector(slotWidth, 0));
            card.draw();
        }

    }

    void handleCardsInput() {
        ArrayList<Card> input = new ArrayList<>();
        for (CardSlot cardSlot: slots) {
            if (cardSlot.isFilled()) {
                Card card = cardSlot.getFilledCard();
                input.add(card);
                card.setCoord(new PVector(0, 0));
                card.unpair();
                // delete appropriate cards after push the button
                if (!card.hasProperty(Property.ETERNAL)) player.removeCard(card);
            }
        }

        event.handleCardsInput(input);
        this.state = State.RESULT;
        confirmBtn.switchFunction(this::handleEventScreen);
    }

    void handleEventScreen() {
        this.vanish();
        this.state = State.EVENT;
        confirmBtn.switchFunction(this::handleCardsInput);

        // if the result contains function that ends the game, end the game
        if (g.gameEnd) {
            g.gameEnd = false;
            g.state = GameState.MAIN_MENU;
        } else g.state = GameState.WORLD;
    }
}
