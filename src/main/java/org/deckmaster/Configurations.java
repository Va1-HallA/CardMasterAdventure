package org.deckmaster;

import processing.core.PFont;

public class Configurations {
    /**
     * Font settings
     */
    public static float CARD_FONT_SIZE_PROPORTION = 0.0055f;
    public static PFont CARD_FONT = Game.game.createFont("Bell MT", Game.game.width * Configurations.CARD_FONT_SIZE_PROPORTION);
    public static float EVT_TITLE_FONT_SIZE_PROPORTION = 0.028f;
    public static float EVT_DES_FONT_SIZE_PROPORTION = 0.012f;
    public static PFont EVT_TITLE_FONT = Game.game.createFont("Edwardian Script ITC", Game.game.width * Configurations.EVT_TITLE_FONT_SIZE_PROPORTION);
    public static PFont EVT_DES_FONT = Game.game.createFont("Bell MT", Game.game.width * Configurations.EVT_DES_FONT_SIZE_PROPORTION);

    /**
     * Card settings
     */

    public static float CARD_HEIGHT_PROPORTION = 0.18f;
    public static float CARD_WIDTH_PROPORTION = 0.055f;
    public static float CARD_IMAGE_X_PROPORTION = 0.78f;
    public static float CARD_IMAGE_Y_PROPORTION = 0.65f;

    public static float CARD_TITLE_HEIGHT_PROPORTION = 0.11f;
    public static float CARD_PROP_INFO_WIDTH_PROPORTION = 0.12f;
    public static float CARD_PROP_INFO_HEIGHT_PROPORTION = 0.05f;

    /**
     * Property settings
     */
    public static float PROPERTY_ICON_WIDTH_PROPORTION = 0.064f;
    public static float PROPERTY_ICON_HEIGHT_PROPORTION = 0.044f;
    public static float PROPERTY_VALUE_INTERVAL_PROPORTION = 0.4f;
    public static PFont PROPERTY_FONT = Game.game.createFont("Bell MT", Game.game.width * Configurations.CARD_FONT_SIZE_PROPORTION * 0.75f);

    /**
     * Inventory settings
     */
    public static int BATCH_MAX_CARDS_NUM = 8;
    public static float INVENTORY_HEIGHT_PROPORTION = 0.975f;

    /**
     * Event screen settings
     */
    public static float EVT_MAIN_WIDTH_PROPORTION = 0.54f;
    public static float EVT_MAIN_HEIGHT_PROPORTION = 0.7f;
    public static float EVT_TITLE_Y_PROPORTION = 0.07f;
    public static float EVT_DES_Y_PROPORTION = 0.07f;
    public static float EVT_TEXT_WIDTH_PROPORTION = 0.9f;
    public static float EVT_TEXT_HEIGHT_PROPORTION = 0.4f;
    public static float EVT_IMG_WIDTH_PROPORTION = 0.125f;
    public static float EVT_IMG_HEIGHT_PROPORTION = 0.35f;
    public static float EVT_Y_PROPORTION = 0.07f;
    public static float SLOT_HEIGHT_PROPORTION = 0.64f;
    public static float EVT_BTN_HEIGHT_PROPORTION = 0.935f;
    public static float EVT_BTN_SIZE_PROPORTION = 0.04f;

    /**
     * Saving settings
     */

    public static String MAP_SAVING_LOCATION = "saving/map.txt";
    public static String PLAYER_SAVING_LOCATION = "saving/player.txt";
    public static String EVENT_SAVING_LOCATION = "saving/event.txt";
    public static String POS_SAVING_LOCATION = "saving/pos.txt";
}
