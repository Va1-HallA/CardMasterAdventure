package org.deckmaster;

import processing.core.PImage;

public class PlayerAssets {
    static Game g = Game.game;

    static PImage playerImage = g.loadImage("images/player/hero.png");
    static PImage up = playerImage.get(32, 32, 32, 32);
    static PImage down = playerImage.get(32, 64, 32, 32);
    static PImage left = playerImage.get(32, 0, 32, 32);
}
