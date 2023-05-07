package org.deckmaster;

import processing.core.PImage;

public enum Property {
    AIR("images/properties/AIR.png", ""),
    DEATH("images/properties/DEATH.png", ""),
    EARTH("images/properties/EARTH.png", ""),
    ETERNAL("images/properties/ETERNAL.png", ""),
    FLAME("images/properties/FLAME.png", ""),
    FUND("images/properties/FUND.png", ""),
    HEART("images/properties/HEART.png", ""),
    LEGEND_DUSK_DRAGON("images/properties/LEGEND_DUSK_DRAGON.png", "Dusk Dragon"),
    LEGEND_PALADIN("images/properties/LEGEND_PALADIN.png", ""),
    LUNAR("images/properties/LUNAR.png", "Lunar"),
    MIRROR("images/properties/MIRROR.png", ""),
    REPUTATION_CITY_OF_TOWERS("images/properties/REPUTATION_CITY_OF_TOWERS.png", ""),
    REPUTATION_DAWN_CHURCH("images/properties/REPUTATION_DAWN_CHURCH.png", ""),
    REPUTATION_SWAMP_GUILD("images/properties/REPUTATION_SWAMP_GUILD.png", ""),
    RITUAL_TOWERS("images/properties/RITUAL_TOWERS.png", "Ritual of Towers"),
    SWORD("images/properties/SWORD.png", ""),
    SERPENT("images/properties/SERPENT.png", ""),
    WATER("images/properties/WATER.png", ""),
    ;
    PImage icon;
    String description;

    Property(String iconPath, String description) {
        this.icon = Game.game.loadImage(iconPath);
        this.description =description;
    }
}
