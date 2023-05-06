package org.deckmaster;

import processing.core.PConstants;
import processing.core.PImage;
import processing.core.PVector;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Card implements Comparable, Drawable, Serializable {

    private static PVector size = new PVector(g.width * Configurations.CARD_WIDTH_PROPORTION, g.height * Configurations.CARD_HEIGHT_PROPORTION);
    private static PImage background = g.loadImage("images/cards/background.png");
    private String name;
    private PImage image;
    private HashMap<Property, Integer> propertyTable;
    private PVector coord;
    private PVector originalCoord;
    private PVector originalMouseCoord;
    private CardSlot pairedSlot;
    private boolean isDragged;

    public Card(String name, String imagePath, HashMap<Property, Integer> propertyTable) {
        this.name = name;
        this.image = g.loadImage(imagePath);
        this.propertyTable = new HashMap<>(propertyTable);
        this.pairedSlot = null;
        this.isDragged = false;
    }

    public String getName() {
        return name;
    }

    public PVector getCoord() {
        return coord;
    }

    public PVector getOriginalCoord() {
        return originalCoord;
    }

    public PVector getOriginalMouseCoord() {
        return originalMouseCoord;
    }

    public boolean pair(CardSlot slot) {
        float slotL = slot.getCoord().x;
        float slotR = slot.getCoord().x + CardSlot.getSize().x;
        float slotT = slot.getCoord().y;
        float slotB = slot.getCoord().y + CardSlot.getSize().y;
        if (coord.x < slotR && coord.x + size.x > slotL && coord.y < slotB && coord.y + size.y > slotT) {
            if (slot.pair(this)) {
                pairedSlot = slot;
                setCoord(slot.getCoord());
                return true;
            }
        }
        return false;
    }

    public void unpair() {
        if (isPaired()) pairedSlot.unpair();
        pairedSlot = null;
    }


    public void drag(int mx, int my) {
        if (!isDragged) {
            originalCoord = new PVector(coord.x, coord.y);
            originalMouseCoord = new PVector(mx, my);
            isDragged = true;
        }
    }

    public void undrag() {
        if (isDragged) {
            originalCoord = new PVector();
            originalMouseCoord = new PVector();
            isDragged = false;
        }
    }

    public boolean isPaired() {
        return pairedSlot != null;
    }

    public boolean isDragged() {
        return isDragged;
    }

    public void setCoord(PVector coord) {
        this.coord = new PVector(coord.x, coord.y);
    }

    public HashMap<Property, Integer> getPropertyTable() {
        return propertyTable;
    }

    public void addProperty(Property property, int value) {
        this.propertyTable.put(property, value);
    }

    public void removeProperty(Property property) {
        this.propertyTable.remove(property);
    }

    public void onClick(int mx, int my) {
        // TODO: onclick = start dragging, onhold = dragging, onrelease = check if inside a slot, if not back to original position, if yes stay in slot
        if (mx > coord.x && mx < coord.x + size.x && my > coord.y && my < coord.y + size.y) {
            drag(mx, my);
        }
    }

    public void onRelease(List<CardSlot> slots) {
        undrag();
        if (isPaired() && !pair(pairedSlot)) {
            unpair();
        } else if (!isPaired()) {
            for (CardSlot s : slots) {
                if (pair(s)) break;
            }
        }
    }

    @Override
    public void setup() {

    }

    @Override
    public void draw() {
        background.resize((int) size.x, (int) size.y);
        image.resize((int) (size.x * Configurations.CARD_IMAGE_X_PROPORTION), (int) (size.y * Configurations.CARD_IMAGE_X_PROPORTION)); // both use X proportion to keep constant size
//        g.fill(g.color(0));
//        g.rect(coord.x, coord.y, size.x, size.y);
        g.image(background, coord.x, coord.y);
        g.fill(g.color(255));
        g.textFont(Configurations.CARD_FONT);
        g.textAlign(PConstants.CENTER);
        g.text(name, coord.x + size.x / 2, coord.y + size.y * Configurations.CARD_TITLE_HEIGHT_PROPORTION);
        g.image(image, coord.x + size.x * ((1 - Configurations.CARD_IMAGE_X_PROPORTION) / 2), coord.y + size.y * ((1 - Configurations.CARD_IMAGE_Y_PROPORTION) / 2));

        // draw property icons
        int iconSizeX = (int) (size.x * Configurations.PROPERTY_ICON_SIZE_PROPORTION);
        int iconSizeY = (int) (size.y * Configurations.PROPERTY_ICON_SIZE_PROPORTION);
        PVector curPropertyCoord = new PVector(
                coord.x + size.x * 0.88f - iconSizeX * (1 + Configurations.PROPERTY_VALUE_INTERVAL_PROPORTION) ,
                coord.y + size.y * 0.92f - iconSizeY
        );
        for (Map.Entry<Property, Integer> entry : propertyTable.entrySet()) {
            PImage icon = entry.getKey().icon;
            icon.resize(iconSizeX, iconSizeY);
            g.image(icon, curPropertyCoord.x, curPropertyCoord.y);
            g.text(entry.getValue(), curPropertyCoord.x + iconSizeX * (1 + Configurations.PROPERTY_VALUE_INTERVAL_PROPORTION), curPropertyCoord.y + iconSizeY);
            curPropertyCoord.add(new PVector(-1 * (iconSizeX * (1 + Configurations.PROPERTY_VALUE_INTERVAL_PROPORTION)), 0));
        }
    }

    @Override
    public void update() {
        if (isDragged()) {
            PVector movement = new PVector(g.mouseX - getOriginalMouseCoord().x, g.mouseY - getOriginalMouseCoord().y);
            PVector finalPosition = new PVector(getOriginalCoord().x, getOriginalCoord().y).add(movement);
            setCoord(finalPosition);
        }
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof Card c) {
            return this.getName().compareTo(c.getName());
        }
        return 0;
    }
}
