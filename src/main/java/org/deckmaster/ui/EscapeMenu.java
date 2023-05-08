package org.deckmaster.ui;

import org.deckmaster.Button;
import org.deckmaster.Configurations;
import org.deckmaster.Drawable;
import org.deckmaster.GameState;
import processing.core.PVector;

public class EscapeMenu implements Drawable {
    public GameState previousState = null;

    Button continueGame = new Button((int) (g.width / 2f - 100), (int) (g.height / 2f + 35 - 200), 200, 70, "Continue", () -> {
       g.state = previousState;
    }, new PVector(0,0,0), 150, Configurations.MARCO_FONT, 32);

    Button saveGame = new Button((int) (g.width / 2f - 100), (int) (g.height / 2f + 130 - 200), 200, 70, "Save Game", () -> {
        g.save();
    }, new PVector(0,0,0), 150, Configurations.MARCO_FONT, 32);

    Button mainMenuButton = new Button((int) (g.width / 2f - 100), (int) (g.height / 2f + 230 - 200), 200, 70, "Main Menu", () -> {
        g.toMainMenu();
    }, new PVector(0,0,0), 150, Configurations.MARCO_FONT, 32);

    @Override
    public void setup() {

    }

    @Override
    public void draw() {
        g.pushMatrix();
        g.translate(g.cameraPosition.x - g.width / 2f, g.cameraPosition.y - g.height / 2f);

        continueGame.draw();
        saveGame.draw();
        mainMenuButton.draw();

        g.popMatrix();
    }

    public void checkButtons() {
        continueGame.onClick(g.mouseX, g.mouseY);
        saveGame.onClick(g.mouseX, g.mouseY);
        mainMenuButton.onClick(g.mouseX, g.mouseY);
    }
    @Override
    public void update() {

    }
}
