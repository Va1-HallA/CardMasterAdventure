package org.deckmaster;

import processing.core.PImage;

public enum Property {
    LUNAR("images/properties/moon-icon-8.png", "");
    PImage icon;
    String description;

    Property(String iconPath, String description) {
        this.icon = Game.game.loadImage(iconPath);
        this.description =description;
    }
}
