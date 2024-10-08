package org.deckmaster;

import org.checkerframework.checker.units.qual.A;
import processing.core.PImage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Event implements Drawable, Serializable {
    private String title;
    private String description;
    // indicator for developers to decide how hard this event should be, larger tier = harder
    private int tier;
    // link to previous event to form a chain of events
    private String previousEventName;
    private String nextEventName;
    private int allowedCardNum;
//    private HashMap<Property, Integer> propertyTable;
//    private HashMap<Result, Integer> resultTable;
    private HashMap<HashMap<Property, Integer>, HashMap<Result, Integer>> resultTable;
//    private ArrayList<String> rewards; // card names of rewards
    private HashMap<HashMap<Property, Integer>, ArrayList<String>> rewards;
    private HashMap<HashMap<Property, Integer>, String> resultDes; // empty key for fail condition
    private ArrayList<ArrayList<Property>> slotsRequirements;
    private PImage image;
    private String imagePath;

    // result vars
    private String curDes;
    private ArrayList<Card> curRewardCards;

    boolean excluded;

    public Event(String title, String description, String imagePath, int tier, boolean excluded, HashMap<HashMap<Property, Integer>, HashMap<Result, Integer>> resultTable, HashMap<HashMap<Property, Integer>, String> resultDes, HashMap<HashMap<Property, Integer>, ArrayList<String>> rewards, ArrayList<ArrayList<Property>> slotsRequirements, String previousEventName, String nextEventName, int allowedCardNum) {
        this.title = title;
        this.description = description;
        this.tier = tier;
        this.allowedCardNum = allowedCardNum;
        this.previousEventName = previousEventName;
        this.nextEventName = nextEventName;
        this.image = g.loadImage(imagePath);
        this.imagePath = imagePath;
        this.resultTable = new HashMap<>(resultTable);
        this.rewards = new HashMap<>(rewards);
        this.slotsRequirements = new ArrayList<>(slotsRequirements);
        this.resultDes = new HashMap<>(resultDes);

        image.resize((int) (g.width * Configurations.EVT_IMG_WIDTH_PROPORTION), (int) (g.height * Configurations.EVT_IMG_HEIGHT_PROPORTION));

        curDes = "";
        curRewardCards = new ArrayList<>();

        this.excluded = excluded;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getPreviousEventName() {
        return previousEventName;
    }

    public String getNextEventName() {
        return nextEventName;
    }

    public PImage getImage() {
        if (image == null) image = g.loadImage(imagePath);
        return image;
    }

    public int getAllowedCardNum() {
        return allowedCardNum;
    }

    public ArrayList<ArrayList<Property>> getSlotsRequirements() {
        return slotsRequirements;
    }

    public String getCurDes() {
        return curDes;
    }

    public ArrayList<Card> getCurRewardCards() {
        return curRewardCards;
    }

    //    public boolean handleCardsInput(ArrayList<Card> cardList) {
//        HashMap<Property, Integer> curInput = new HashMap<>();
//        for (Card c : cardList) {
//            HashMap<Property, Integer> cardProperties = c.getPropertyTable();
//            for (Property cp : cardProperties.keySet()) {
//                int val = cardProperties.get(cp);
//                if (!curInput.containsKey(cp)) curInput.put(cp, val);
//                else curInput.put(cp, curInput.get(cp) + val);
//            }
//        }
//
//        // check with required properties table
//        for (Property p : propertyTable.keySet()) {
//            int targetValue = propertyTable.get(p);
//            if (!curInput.containsKey(p) || curInput.get(p) < targetValue) return false;
//        }
//
//        return true;
//    }

    public void handleCardsInput(ArrayList<Card> cardList) {
        HashMap<Property, Integer> curInput = new HashMap<>();

        for (Card c : cardList) {
            HashMap<Property, Integer> cardProperties = c.getPropertyTable();
            for (Property cp : cardProperties.keySet()) {
                int val = cardProperties.get(cp);
                if (!curInput.containsKey(cp)) curInput.put(cp, val);
                else curInput.put(cp, curInput.get(cp) + val);
            }
        }

        boolean fail = true;

        // sort the condition list, so complicated conditions are checked first
        ArrayList<HashMap<Property, Integer>> conditionList = new ArrayList<>(resultDes.keySet());
        ArrayList<HashMap<Property, Integer>> sorted = new ArrayList<>();
        int maxSize = 0;
        for (HashMap<Property, Integer> conditions : conditionList) {
            if (conditions.size() > maxSize) maxSize = conditions.size();
        }

        while (maxSize >= 0) {
            for (HashMap<Property, Integer> conditions : conditionList) if (conditions.size() == maxSize) sorted.add(conditions);
            maxSize -= 1;
        }

        for (HashMap<Property, Integer> conditions : sorted) {
            if (conditions.keySet().size() == 0) continue; // ignore failure condition
            boolean fulfill = true;
            for (Property p : conditions.keySet()) {
                int targetValue = conditions.get(p);
                if (!curInput.containsKey(p) || curInput.get(p) < targetValue) {
                    fulfill = false;
                    break;
                }
            }

            if (fulfill) {
                fail = false;
                curRewardCards = new ArrayList<>();
                HashMap<Result, Integer> resExecution = new HashMap<>();
                ArrayList<String> rewStr = new ArrayList<>();
                String resDes = "";
                if (resultTable.containsKey(conditions)) resExecution = resultTable.get(conditions);
                if (rewards.containsKey(conditions)) rewStr = rewards.get(conditions);
                if (resultDes.containsKey(conditions)) resDes = resultDes.get(conditions);

                curDes = resDes;
                for (String str : rewStr) {
                    Card card = g.contentLoader.loadCard(str);
                    curRewardCards.add(card);
                }

                for (Result r : resExecution.keySet()) {
                    r.execute(resExecution.get(r));
                }

                for (Card c : curRewardCards) {
                    g.player.addCard(c);
                }

                // update game active events
                if (!this.previousEventName.equals("") || !this.nextEventName.equals("")) g.trackedEvents.remove(this.getTitle());

                if (!this.nextEventName.equals("")) g.trackedEvents.add(nextEventName);
                System.out.println(g.trackedEvents.toString());

                break;
            }
        }

        if (fail) {
            HashMap<Property, Integer> failCondition = new HashMap<>();
            HashMap<Result, Integer> resExecution = resultTable.get(failCondition);
            curDes = resultDes.get(failCondition);

            if (resExecution != null) {
                for (Result r : resExecution.keySet()) {
                    r.execute(resExecution.get(r));
                }
            }
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
