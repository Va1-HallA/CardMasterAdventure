package org.deckmaster;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
                if (nextLine.startsWith("//") || nextLine.equals("")) continue; // allow comment
                String[] attribute = nextLine.split(":");
                String name = attribute[0];
                String fileName = attribute[1];
                nameFileIndexTable.put(name, fileName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Card loadCard(String cardName) {
        String fileName = nameFileIndexTable.get(cardName);
        if (!fileName.startsWith("cards")) return null;
        try {
            Scanner sc = new Scanner(new File(fileName));

            // target attribute variables
            String name = "";
            String imagePath = "";
            HashMap<Property, Integer> propertyTable = new HashMap<>();

            while (sc.hasNextLine()) {
                String nextLine = sc.nextLine();
                if (nextLine.startsWith("//") || nextLine.equals("")) continue;
                String[] attribute = nextLine.split(":");
                switch (attribute[0]) {
                    case "property":
                        String[] data = attribute[1].split("=");
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
        }
        return null;
    }

    public Event loadEvent(String eventName) {
        String fileName = nameFileIndexTable.get(eventName);
        if (!fileName.startsWith("events")) return null;
        try {
            Scanner sc = new Scanner(new File(fileName));

            // target attribute variables
            String title = "";
            String description = "";
            String imagePath = "";
            int tier = 0;
            boolean excluded = false;
            int allowedCardNum = 0;
            String previousEventName = "";
            String nextEventName = "";
//            HashMap<Property, Integer> propertyTable = new HashMap<>();
            HashMap<HashMap<Property, Integer>, HashMap<Result, Integer>> resultTable = new HashMap<>();
            HashMap<HashMap<Property, Integer>, String> resultDes = new HashMap<>();
            HashMap<HashMap<Property, Integer>, ArrayList<String>> rewards = new HashMap<>();
            ArrayList<ArrayList<Property>> slotRequirements = new ArrayList<>();
            while (sc.hasNextLine()) {
                String nextLine = sc.nextLine();
                if (nextLine.startsWith("//") || nextLine.equals("")) continue;
                String[] attribute = nextLine.split(":");
                switch (attribute[0]) {
                    case "result":
                        String[] resultData = attribute[1].split("->");
                        String[] resultKeyStr = resultData[0].split(",");
                        String[] resultValueStr = resultData[1].split(",");

                        HashMap<Property, Integer> newResultKey = new HashMap<>();
                        HashMap<Result, Integer> newResultValue = new HashMap<>();

                        if (!resultKeyStr[0].equals("")) {
                            for (String str : resultKeyStr) {
                                String[] propData = str.split("=");
                                Property property = Property.valueOf(propData[0]);
                                int num = Integer.parseInt(propData[1]);
                                newResultKey.put(property, num);
                            }
                        }

                        for (String str : resultValueStr) {
                            String[] resData = str.split("=");
                            Result result = Result.valueOf(resData[0]);
                            int num = Integer.parseInt(resData[1]);
                            newResultValue.put(result, num);
                        }

                        resultTable.put(newResultKey, newResultValue);
                        break;
                    case "resultDes":
                        String[] resultDesData = attribute[1].split("->");
                        String[] resultDesKeyStr = resultDesData[0].split(",");
                        String des = resultDesData[1];

                        HashMap<Property, Integer> newResultDesKey = new HashMap<>();

                        if (!resultDesKeyStr[0].equals("")) {
                            for (String str : resultDesKeyStr) {
                                String[] propData = str.split("=");
                                Property property = Property.valueOf(propData[0]);
                                int num = Integer.parseInt(propData[1]);
                                newResultDesKey.put(property, num);
                            }
                        }

                        resultDes.put(newResultDesKey, des);
                        break;
                    case "reward":
                        String[] rewardData = attribute[1].split("->");
                        String[] rewardKeyStr = rewardData[0].split(",");
                        String[] rewardValueStr = rewardData[1].split(",");

                        HashMap<Property, Integer> newRewardKey = new HashMap<>();

                        if (!rewardKeyStr[0].equals("")) {
                            for (String str : rewardKeyStr) {
                                String[] propData = str.split("=");
                                Property property = Property.valueOf(propData[0]);
                                int num = Integer.parseInt(propData[1]);
                                newRewardKey.put(property, num);
                            }
                        }

                        ArrayList<String> newRewardValue = new ArrayList<>(Arrays.asList(rewardValueStr));

                        rewards.put(newRewardKey, newRewardValue);
                        break;
                    case "slotRequirement":
                        String[] slotRequirementData = attribute[1].split(",");
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
                    case "excluded":
                        excluded = Boolean.parseBoolean(attribute[1]);
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

            return new Event(title, description, imagePath, tier, excluded, resultTable, resultDes, rewards, slotRequirements, previousEventName, nextEventName, allowedCardNum);

        } catch (IOException e) {
        }
        return null;
    }
}
