package org.deckmaster;
import org.deckmaster.mapgen.Map;
import org.deckmaster.mapgen.MapTile;
import org.deckmaster.mapgen.TileLocation;
import processing.core.PApplet;
import processing.core.PMatrix2D;
import processing.core.PVector;
import processing.opengl.PShader;

import java.io.*;
import java.util.*;


// TODO: display info when hover mouse to slot and property, next event (event train), file loading, save/load, proactive events (hotkey to activate a event that allows player to spend a card, trigger some results e.g. another events)
public class Game extends PApplet {
    public static Game game = null;
    public PVector cameraPosition = new PVector(0, 0);
    public Player player;
    public Map map;
    public boolean debug = true;

    public final float UPDATE_TIME = 16.6f;
    private float lag = 0f;
    public long lastUpdate = System.currentTimeMillis();
    InventoryScreen screen;
    EventScreen evtscreen;
    CardSlot slot;
    GameState state;

    ContentLoader contentLoader;

    ArrayList<String> trackedEvents;

    // Event proactiveEvent = new Event(...);  // use this when press a key to trigger proactive event

    @Override
    public void settings() {
        size(displayWidth, displayHeight, P2D);
        fullScreen();
    }

    @Override
    public void setup() {
        game = this;
        state = GameState.WORLD;
        player = new Player(new PVector(25, 25), 10);

        map = new Map();
        map.setup();

        contentLoader = new ContentLoader();
        trackedEvents = new ArrayList<>();
        initEvents();

        screen = new InventoryScreen(player);

        for (int i = 0; i < 10; i++) {
            Card c = new Card("a", "images/cards/Merlin.png", new HashMap<>());
            c.addProperty(Property.LUNAR, 1);
            player.addCard(c);
        }

        slot = new CardSlot(new ArrayList<>(List.of(Property.LUNAR)));
        slot.setCoord(new PVector((float) g.width * 0.5f, (float) g.height * 0.3f));
        screen.show();

        evtscreen = new EventScreen(new Event("title", "description", "images/cards/background.png", 1, new HashMap<>(), new HashMap<>(), new ArrayList<>(), new ArrayList<>(), "", "", 1), player, screen);
        evtscreen.show();
    }

//    @Override
//    public void mousePressed() {
//        if (state == GameState.WORLD || state == GameState.EVENT) {
//            for (Card card: player.getCards()) {
//                if (card.getCoord() != null) card.onClick(mouseX, mouseY);
//            }
//        }
//    }

//    @Override
//    public void mouseReleased() {
//        if (state == GameState.WORLD) {
//            screen.leftArrow.onClick(mouseX, mouseY);
//            screen.rightArrow.onClick(mouseX, mouseY);
//            for (Card card: player.getCards()) {
//                if (card.getCoord() != null) card.onRelease(new ArrayList<>());
//            }
//        } else if (state == GameState.EVENT) {
//            evtscreen.confirmBtn.onClick(mouseX, mouseY);
//            evtscreen.inventory.leftArrow.onClick(mouseX, mouseY);
//            evtscreen.inventory.rightArrow.onClick(mouseX, mouseY);
//            for (Card card: player.getCards()) {
//                if (card.getCoord() != null) card.onRelease(evtscreen.getSlots());
//            }
//        }
//    }

    @Override
    public void draw() {
        // Calculate deltaTime for Physics
        if (state != GameState.MAIN_MENU) {
            long deltaTime = System.currentTimeMillis() - lastUpdate;
            lastUpdate = System.currentTimeMillis();
            System.out.println(deltaTime);
            lag += deltaTime;
        }

        background(20);

        // Made the camera follow the Player.
        calcCameraPos();
        game.translate(width / 2f - cameraPosition.x, height / 2f - cameraPosition.y);

        switch (state) {
            case MAIN_MENU -> {
            }
            case WORLD -> {
                map.draw();
//                slot.draw();
                player.draw();
//                screen.draw();

                fill(255, 255, 255);
                textAlign(LEFT, CENTER);
                textSize(12);
                text(String.format("X: %f Y: %f Z: %f", player.pos.x, player.pos.y, map.tileMap.get(TileLocation.worldToTileCoords(player.pos)).height), cameraPosition.x - width / 2f + 20, cameraPosition.y - height / 2f + 20);
            }
            case EVENT -> {
                map.draw();
                evtscreen.draw();
            }
        }

        // Show player coordinates.
        update();
    }

    public void update() {
        while (lag > UPDATE_TIME) {
            switch(state) {
                case MAIN_MENU -> {
                }
                case WORLD -> {
                    player.update();
                    slot.update();
                    screen.update();
                }
                case EVENT -> {
                    evtscreen.update();
                }
            }

            lag -= UPDATE_TIME;
        }
    }

    private void calcCameraPos() {
//        if (Math.abs(cameraPosition.x - player.pos.x) > width / 4f) {
//            if (cameraPosition.x > player.pos.x) {
//                cameraPosition.x -= Math.abs(cameraPosition.x - player.pos.x) - width / 4f;
//            } else {
//                cameraPosition.x += Math.abs(cameraPosition.x - player.pos.x) - width / 4f;
//            }
//        }
//
//        if (Math.abs(cameraPosition.y - player.pos.y) > height / 4f) {
//            if (cameraPosition.y > player.pos.y) {
//                cameraPosition.y -= Math.abs(cameraPosition.y - player.pos.y) - height / 4f;
//            } else {
//                cameraPosition.y += Math.abs(cameraPosition.y - player.pos.y) - height / 4f;
//            }
//        }
        if (cameraPosition.x > player.pos.x) {
            cameraPosition.x -= Math.abs(cameraPosition.x - player.pos.x);
        } else {
            cameraPosition.x += Math.abs(cameraPosition.x - player.pos.x);
        }

        if (cameraPosition.y > player.pos.y) {
            cameraPosition.y -= Math.abs(cameraPosition.y - player.pos.y);
        } else {
            cameraPosition.y += Math.abs(cameraPosition.y - player.pos.y);
        }
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

    public void save() {
        try {
            // saving map
            FileOutputStream mapFoe = new FileOutputStream(Configurations.MAP_SAVING_LOCATION);
            ObjectOutputStream mapOos = new ObjectOutputStream(mapFoe);
            mapOos.writeObject(map.tileMap);

            // saving player stats
            FileOutputStream playerFoe = new FileOutputStream(Configurations.PLAYER_SAVING_LOCATION);
            ObjectOutputStream playerOos = new ObjectOutputStream(playerFoe);
            playerOos.writeObject(player.getCards());

            // saving active events list
            FileOutputStream eventFoe = new FileOutputStream(Configurations.EVENT_SAVING_LOCATION);
            ObjectOutputStream eventOos = new ObjectOutputStream(eventFoe);
            eventOos.writeObject(trackedEvents);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load() {
        try {
            // loading map
            FileInputStream mapFie = new FileInputStream(Configurations.MAP_SAVING_LOCATION);
            ObjectInputStream mapOis = new ObjectInputStream(mapFie);
            map.tileMap = (HashMap<TileLocation, MapTile>) mapOis.readObject();

            // loading player
            FileInputStream playerFie = new FileInputStream(Configurations.PLAYER_SAVING_LOCATION);
            ObjectInputStream playerOis = new ObjectInputStream(playerFie);
            player.setCards((ArrayList<Card>) playerOis.readObject());

            // loading events
            FileInputStream eventsFie = new FileInputStream(Configurations.EVENT_SAVING_LOCATION);
            ObjectInputStream eventOis = new ObjectInputStream(eventsFie);
            trackedEvents = (ArrayList<String>) eventOis.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void initEvents() {
        // TODO: when event has been triggered, update this list
        for (String name : contentLoader.nameFileIndexTable.keySet()) {
            Event e = contentLoader.loadEvent(name);
            if (e != null && e.getPreviousEventName().equals("")) {
                trackedEvents.add(e.getTitle());
            }
        }
    }
}
