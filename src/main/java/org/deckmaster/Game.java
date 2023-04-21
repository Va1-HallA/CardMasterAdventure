package org.deckmaster;
import com.jogamp.oculusvr.OVR;
import processing.core.PApplet;
import processing.core.PVector;
import processing.opengl.PShader;

import java.util.*;


// TODO: display info when hover mouse to slot and property, next event (event train), file loading, save/load, proactive events (hotkey to activate a event that allows player to spend a card, trigger some results e.g. another events)
public class Game extends PApplet {
    public static Game game = null;
    PVector cameraPosition = new PVector(0, 0);
    Player player;
    InventoryScreen screen;
    EventScreen evtscreen;
    CardSlot slot;
    GameState state;

    @Override
    public void settings() {
        // TODO: uncomment next line will cause the game only displaying at the bottom half screen, so commented it
//        size(displayWidth, displayHeight, P2D);
        fullScreen();
    }

    @Override
    public void setup() {
        game = this;
        state = GameState.EVENT;
        player = new Player();
        screen = new InventoryScreen(player);
        for (int i = 0; i < 10; i++) {
            Card c = new Card("a", "images/cards/Merlin.png", new HashMap<>());
            c.addProperty(Property.LUNAR, 1);
            player.addCard(c);
        }
        slot = new CardSlot(new ArrayList<>(List.of(Property.LUNAR)));
        slot.setCoord(new PVector((float) g.width * 0.5f, (float) g.height * 0.3f));
//        screen.show();

        evtscreen = new EventScreen(new Event("title", "description", "images/cards/background.png", 1, new HashMap<>(), new HashMap<>(), new ArrayList<>(), new ArrayList<>(), "", "", 1), player, screen);
        evtscreen.show();
    }

    @Override
    public void mousePressed() {
        if (state == GameState.WORLD || state == GameState.EVENT) {
            for (Card card: player.getCards()) {
                if (card.getCoord() != null) card.onClick(mouseX, mouseY);
            }
        }
    }

    @Override
    public void mouseReleased() {
        if (state == GameState.WORLD) {
            screen.leftArrow.onClick(mouseX, mouseY);
            screen.rightArrow.onClick(mouseX, mouseY);
            for (Card card: player.getCards()) {
                if (card.getCoord() != null) card.onRelease(new ArrayList<>());
            }
        } else if (state == GameState.EVENT) {
            evtscreen.confirmBtn.onClick(mouseX, mouseY);
            evtscreen.inventory.leftArrow.onClick(mouseX, mouseY);
            evtscreen.inventory.rightArrow.onClick(mouseX, mouseY);
            for (Card card: player.getCards()) {
                if (card.getCoord() != null) card.onRelease(evtscreen.getSlots());
            }
        }
    }

    @Override
    public void draw() {
        background(150);
        update();
        if (state == GameState.EVENT) evtscreen.draw();
//        slot.draw();
//        screen.draw();

    }

//    private void calcCameraPos() {
//        if (Math.abs(cameraPosition.x - player.location.x) > width / 4f) {
//            if (cameraPosition.x > player.location.x) {
//                cameraPosition.x -= Math.abs(cameraPosition.x - player.location.x) - width / 4f;
//            } else {
//                cameraPosition.x += Math.abs(cameraPosition.x - player.location.x) - width / 4f;
//            }
//        }
//
//        if (Math.abs(cameraPosition.y - player.location.y) > height / 4f) {
//            if (cameraPosition.y > player.location.y) {
//                cameraPosition.y -= Math.abs(cameraPosition.y - player.location.y) - height / 4f;
//            } else {
//                cameraPosition.y += Math.abs(cameraPosition.y - player.location.y) - height / 4f;
//            }
//        }
//    }

    public void update() {
//        slot.update();
//        screen.update();
        if (state == GameState.EVENT) evtscreen.update();
    }

    @Override
    public void keyPressed() {
        Input.checkKeyPressed();
    }

    @Override
    public void keyReleased() {
        Input.checkKeyReleased();
    }

    public boolean overRect(int x, int y, int width, int height) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    public static void main(String[] args) {
        PApplet.main(Game.class);
    }
}
