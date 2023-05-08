package org.deckmaster.ui;

import org.deckmaster.Button;
import org.deckmaster.Configurations;
import org.deckmaster.Drawable;
import org.deckmaster.GameState;
import processing.core.PFont;
import processing.core.PVector;

public class MainMenu implements Drawable {

    Button startGame = new Button((int) (g.width / 2f - 100), (int) (g.height / 2f + 35 - 100), 200, 70, "Start", () -> {
        g.startGame();
    }, new PVector(0,0,0), 150, Configurations.MARCO_FONT, 32);

    Button loadGame = new Button((int) (g.width / 2f - 100), (int) (g.height / 2f + 130 - 100), 200, 70, "Load Game", () -> {
        g.load();
        g.state = GameState.WORLD;
    }, new PVector(0,0,0), 150, Configurations.MARCO_FONT, 32);

    Button quitButton = new Button((int) (g.width / 2f - 100), (int) (g.height / 2f + 230 - 100), 200, 70, "Quit", () -> {
        g.exit();
    }, new PVector(0,0,0), 150, Configurations.MARCO_FONT, 32);

    @Override
    public void setup() {

    }

    @Override
    public void draw() {
        g.pushMatrix();
        g.translate(g.cameraPosition.x - g.width / 2f, g.cameraPosition.y - g.height / 2f);

        g.textSize(40);
        g.fill(255);
        g.textAlign(g.CENTER, g.CENTER);
        g.textFont(Configurations.MARCO_FONT);
        g.text("Deckmaster's Adventure", g.width / 2f, 300);

        startGame.draw();
        loadGame.draw();
        quitButton.draw();

        g.popMatrix();
    }

    public void checkButtons() {
        startGame.onClick(g.mouseX, g.mouseY);
        loadGame.onClick(g.mouseX, g.mouseY);
        quitButton.onClick(g.mouseX, g.mouseY);
    }
    @Override
    public void update() {

    }
}
