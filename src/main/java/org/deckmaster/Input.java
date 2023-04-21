package org.deckmaster;

public class Input {
    private static Game g = Game.game;
    public static boolean wPressed = false;
    public static boolean sPressed = false;
    public static boolean aPressed = false;
    public static boolean dPressed = false;

    public static void checkKeyPressed() {
        if (g.key == 'w') wPressed = true;
        if (g.key == 'a') aPressed = true;
        if (g.key == 's') sPressed = true;
        if (g.key == 'd') dPressed = true;
    }

    public static void checkKeyReleased() {
        if (g.key == 'w') wPressed = false;
        if (g.key == 'a') aPressed = false;
        if (g.key == 's') sPressed = false;
        if (g.key == 'd') dPressed = false;
    }
}
