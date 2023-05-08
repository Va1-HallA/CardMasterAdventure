package org.deckmaster.mapgen;

import org.deckmaster.Game;
import processing.core.PImage;

public class MapAssets {
    static Game g = Game.game;
    static PImage event = g.loadImage("images/map/exclamation.png");

    static PImage tree = g.loadImage("images/map/Tree3.png");
    static PImage flower_tree = g.loadImage("images/map/Flower_tree2-1.png");
    static PImage fruit_tree = g.loadImage("images/map/Fruit_tree3.png");
    static PImage pine_tree = g.loadImage("images/map/Christmas_tree3.png");

    static PImage moss_rock_1 = g.loadImage("images/map/Rock4_3.png");
    static PImage moss_rock_2 = g.loadImage("images/map/Rock4_4.png");
    static PImage moss_rock_3 = g.loadImage("images/map/Rock4_1.png");

    static PImage rock_1 = g.loadImage("images/map/Rock1_3.png");
    static PImage rock_2 = g.loadImage("images/map/Rock1_4.png");
    static PImage rock_3 = g.loadImage("images/map/Rock1_1.png");
    static PImage white_rock_1 = g.loadImage("images/map/Rock5_3.png");
    static PImage white_rock_2 = g.loadImage("images/map/Rock5_4.png");
    static PImage white_rock_3 = g.loadImage("images/map/Rock5_1.png");

    static PImage snow_rock_1 = g.loadImage("images/map/Rock3_3.png");
    static PImage snow_rock_2 = g.loadImage("images/map/Rock3_4.png");
    static PImage snow_rock_3 = g.loadImage("images/map/Rock3_1.png");

    static PImage building = g.loadImage("images/map/Smithy-TD.png");

    static PImage chests = g.loadImage("images/map/chests.png");
    static PImage chest = chests.get(0, 0, 32, 32);
    static PImage chestOpen = chests.get(64,0, 32, 32);

    static PImage interiorSet = g.loadImage("images/map/indoorTileset.png");
    static PImage woodenTile = interiorSet.get(interiorSet.width - 64, 4, 64, 64);
}
