package org.deckmaster;

import processing.core.PImage;

import java.util.ArrayList;
import java.util.HashMap;

public class Event implements Drawable {
    private String title;
    private String description;
    // indicator for developers to decide how hard this event should be, larger tier = harder
    private int tier;
    // link to previous event to form a chain of events
    private String previousEventName;
    private String nextEventName;
    private int allowedCardNum;
    private HashMap<Property, Integer> propertyTable;
    private HashMap<Result, Integer> resultTable;
    private ArrayList<String> rewards; // card names of rewards
    private ArrayList<ArrayList<Property>> slotsRequirements;
    private PImage image;

    public Event(String title, String description, String imagePath, int tier, HashMap<Property, Integer> propertyTable, HashMap<Result, Integer> resultTable, ArrayList<String> rewards, ArrayList<ArrayList<Property>> slotsRequirements, String previousEventName, String nextEventName, int allowedCardNum) {
        this.title = title;
        this.description = description;
        this.tier = tier;
        this.allowedCardNum = allowedCardNum;
        this.previousEventName = previousEventName;
        this.nextEventName = nextEventName;
        this.image = g.loadImage(imagePath);
        this.propertyTable = new HashMap<>(propertyTable);
        this.resultTable = new HashMap<>(resultTable);
        this.rewards = new ArrayList<>(rewards);
        this.slotsRequirements = slotsRequirements;

        image.resize((int) (g.width * Configurations.EVT_IMG_WIDTH_PROPORTION), (int) (g.height * Configurations.EVT_IMG_HEIGHT_PROPORTION));
    }

    public void addProperty(Property property, int value) {
        this.propertyTable.put(property, value);
    }

    public void removeProperty(Property property) {
        this.propertyTable.remove(property);
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public PImage getImage() {
        return image;
    }

    public int getAllowedCardNum() {
        return allowedCardNum;
    }

    public ArrayList<ArrayList<Property>> getSlotsRequirements() {
        return slotsRequirements;
    }

    public boolean handleCardsInput(ArrayList<Card> cardList) {
        HashMap<Property, Integer> curInput = new HashMap<>();
        for (Card c : cardList) {
            HashMap<Property, Integer> cardProperties = c.getPropertyTable();
            for (Property cp : cardProperties.keySet()) {
                int val = cardProperties.get(cp);
                if (!curInput.containsKey(cp)) curInput.put(cp, val);
                else curInput.put(cp, curInput.get(cp) + val);
            }
        }

        // check with required properties table
        for (Property p : propertyTable.keySet()) {
            int targetValue = propertyTable.get(p);
            if (!curInput.containsKey(p) || curInput.get(p) < targetValue) return false;
        }

        return true;
    }

    public void executeResult(boolean successful) {
        for (Result r : resultTable.keySet()) {
            if (r.successfulResult == successful) r.execute(resultTable.get(r));
        }
    }

    @Override
    public void setup() {

    }

    @Override
    public void draw() {

    }

    @Override
    public void update() {

    }
}
