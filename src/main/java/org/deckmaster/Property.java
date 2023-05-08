package org.deckmaster;

import processing.core.PImage;

public enum Property {
    AIR("images/properties/AIR.png", "Air element. Power rising from below, steps light as air. The element represents the human soul, ethereal and elusive palace of thoughts."),
    CONNECTION_CITY_OF_TOWERS("images/properties/CONNECTION_CITY_OF_TOWERS.png", "Connection of the City of Towers. Connection refers to the collective influence of reputation, social networks, and power, representing the level of influence one has over a target organization."),
    CONNECTION_DAWN_CHURCH("images/properties/CONNECTION_DAWN_CHURCH.png", "Connection of the Dawn Church. Connection refers to the collective influence of reputation, social networks, and power, representing the level of influence one has over a target organization."),
    CONNECTION_DRUID_SOCIETY("images/properties/CONNECTION_DRUID_SOCIETY.png", "Connection of the Druid Society. Connection refers to the collective influence of reputation, social networks, and power, representing the level of influence one has over a target organization."),
    CONNECTION_SWAMP_GUILD("images/properties/CONNECTION_SWAMP_GUILD.png", "Connection of the Swamp Guild. Connection refers to the collective influence of reputation, social networks, and power, representing the level of influence one has over a target organization."),
    DEATH("images/properties/DEATH.png", "Death. A crow's corpse, a dusk-colored lake. Death is an inevitable stage, a symbol of the end of all things."),
    EARTH("images/properties/EARTH.png", "Earth element. The gate of stillness, where all things come to a halt. This element represents the human body and the fundamental elements that make up all things."),
    ETERNAL("images/properties/ETERNAL.png", "Eternity. Something that is not bound by the end, the opposite of death. Everything will come to an end, but at some point, something will be momentarily eternal. (This card will not be consumed after an event.)"),
    FLAME("images/properties/FLAME.png", "Flame Element. The wand, the prayer, and the destruction. This element represents human wisdom, beautiful aspirations, and infinite creativity."),
    FUND("images/properties/FUND.png", "Fund. The foundation of action, the bond and the balance: it diminishes with time."),
    HEART("images/properties/HEART.png", "Heart. A symbol of continuity and permanence, the connection between life and the present world."),
    LEGEND_DUSK_DRAGON("images/properties/LEGEND_DUSK_DRAGON.png", "Legend of the Dusk Dragon. At the end of time, a dragon lurks, whose breath forms the present world. The world dies while it sleeps and is reborn when it awakens. Its end will cause time to collapse, and everything will return to nothingness."),
    LEGEND_PALADIN("images/properties/LEGEND_PALADIN.png", "The Legend of the Paladin. It is rumored that there is only one paladin left in the world, spreading miracles in the name of dawn. He will heal all misfortunes and cut down any evil."),
    LEGEND_UNIQUE("images/properties/LEGEND_UNIQUE.png", "In storybooks and folk tales, there are often vague stories mentioned that are yet to be examined for their authenticity. Nevertheless, regardless of the truth, legends remain as legends."),
    LUNAR("images/properties/LUNAR.png", "Moon. The source of magic, a mysterious symbol. Every night, mana flows through her body like threads, and then drifts down to the ground."),
    MIRROR("images/properties/MIRROR.png", "Mirror. The eye behind the door, the whisper at the feast. Mirror is the synonym of exploration, the mysterious opposite."),
    RITUAL_TOWERS("images/properties/RITUAL_TOWERS.png", "The Ritual of the Towers. A ritual is a large-scale magic that combines spells, runes, and planning. This is the most secretive ritual from a certain high tower."),
    SWORD("images/properties/SWORD.png", "Sword. The sword is a symbol of conquest and harm, all lives come into direct contact with it meets their ends without exception."),
    SERPENT("images/properties/SERPENT.png", "Serpent. The serpent is a symbol of secrecy, lurking in the shadows of the night."),
    SETTLEMENT("", ""),
    WATER("images/properties/WATER.png", "Water Element. The flowing moon, the place of origin for all things. This element represents human life, an intangible and delicate crown."),
    ;
    PImage icon;
    String description;

    Property(String iconPath, String description) {
        this.icon = Game.game.loadImage(iconPath);
        this.description =description;
    }
}
