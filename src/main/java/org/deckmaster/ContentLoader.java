package org.deckmaster;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class ContentLoader {
    // record all file names of game contents (cards and events)
    public static final String indexFileName = "contentFilenameMap.txt";
    public final HashMap<String, String> nameFileIndexTable = new HashMap<>(); // map card/event names to file names

    public ContentLoader() {
        fillTables();
    }

    private void fillTables() {
        try {
            Scanner sc = new Scanner(new File(indexFileName));
            while (sc.hasNextLine()) {
                String nextLine = sc.nextLine();
                String[] attribute = nextLine.split(":");
                String name = attribute[0];
                String fileName = attribute[1];
                nameFileIndexTable.put(name, fileName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Card loadCard(String cardName) {
        String fileName = nameFileIndexTable.get(cardName);
        try {
            Scanner sc = new Scanner(new File(fileName));

            // target attribute variables
            String name = "";
            String imagePath = "";
            HashMap<Property, Integer> propertyTable = new HashMap<>();

            while (sc.hasNextLine()) {
                String nextLine = sc.nextLine();
                String[] attribute = nextLine.split(":");
                switch (attribute[0]) {
                    case "property":
                        String[] data = attribute[1].split("-");
                        Property property = Property.valueOf(data[0]);
                        int value = Integer.parseInt(data[1]);
                        propertyTable.put(property, value);
                        break;
                    case "name":
                        name = attribute[1];
                        break;
                    case "imagePath":
                        imagePath = attribute[1];
                        break;
                }
            }

            return new Card(name, imagePath, propertyTable);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Event loadEvent(String eventName) {
        String fileName = nameFileIndexTable.get(eventName);
        try {
            Scanner sc = new Scanner(new File(fileName));

            // target attribute variables
            String title = "";
            String description = "";
            String imagePath = "";
            int tier = 0;
            int allowedCardNum = 0;
            String previousEventName = null;
            String nextEventName = null;
            HashMap<Property, Integer> propertyTable = new HashMap<>();
            HashMap<Result, Integer> resultTable = new HashMap<>();
            ArrayList<String> rewards = new ArrayList<>();
            ArrayList<ArrayList<Property>> slotRequirements = new ArrayList<>();
            while (sc.hasNextLine()) {
                String nextLine = sc.nextLine();
                String[] attribute = nextLine.split(":");
                switch (attribute[0]) {
                    case "property":
                        String[] propertyData = attribute[1].split("-");
                        Property property = Property.valueOf(propertyData[0]);
                        int value = Integer.parseInt(propertyData[1]);
                        propertyTable.put(property, value);
                        break;
                    case "result":
                        String[] resultData = attribute[1].split("-");
                        Result result = Result.valueOf(resultData[0]);
                        int val = Integer.parseInt(resultData[1]);
                        resultTable.put(result, val);
                        break;
                    case "reward":
                        String rewardData = attribute[1];
                        rewards.add(rewardData);
                    case "slotRequirement":
                        String[] slotRequirementData = attribute[1].split("-");
                        ArrayList<Property> reqList = new ArrayList<>();
                        for (String s : slotRequirementData) {
                            Property p = Property.valueOf(s);
                            reqList.add(p);
                        }
                        slotRequirements.add(reqList);
                        break;
                    case "title":
                        title = attribute[1];
                        break;
                    case "description":
                        description = attribute[1];
                        break;
                    case "imagePath":
                        imagePath = attribute[1];
                        break;
                    case "tier":
                        tier = Integer.parseInt(attribute[1]);
                        break;
                    case "allowedCardNum":
                        allowedCardNum = Integer.parseInt(attribute[1]);
                        break;
                    case "previousEvent":
                        previousEventName = attribute[1];
                        break;
                    case "nextEvent":
                        nextEventName = attribute[1];
                        break;
                }
            }

            return new Event(title, description, imagePath, tier, propertyTable, resultTable, rewards, slotRequirements, previousEventName, nextEventName, allowedCardNum);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
