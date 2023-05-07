package org.deckmaster;

import processing.core.PImage;

public enum Property {
    AIR("", ""),
    EARTH("", ""),
    ETERNAL("", ""),
    FLAME("", ""),
    GOLD("", ""),
    HEART("", ""),
    LEGEND_DUSK_DRAGON("images/properties/LEGEND_DUSK_DRAGON.png", "Dusk Dragon"),
    LEGEND_PALADIN("", ""),
    LUNAR("images/properties/moon-icon-8.png", "Lunar"),
    MIRROR("", ""),
    REPUTATION_CITY_OF_TOWERS("", ""),
    REPUTATION_DAWN_CHURCH("", ""),
    REPUTATION_SWAMP_GUILD("", ""),
    RITUAL_TOWERS("images/properties/RITUAL_TOWERS.png", "Ritual of Towers"),
    SWORD("", ""),
    SERPENT("", ""),
    WATER("", ""),
    ;
    PImage icon;
    String description;

    Property(String iconPath, String description) {
        this.icon = Game.game.loadImage(iconPath);
        this.description =description;
    }
}
