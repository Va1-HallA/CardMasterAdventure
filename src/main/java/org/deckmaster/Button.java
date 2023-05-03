package org.deckmaster;

public class Button implements Drawable {
    private final Game g = Game.game;
    int x, y;
    int w, h;
    boolean hovered = false;
    String label;
    Runnable onClick;

    public Button(int x, int y, int w, int h, String label, Runnable onClick) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.label = label;
        this.onClick = onClick;
    }

    @Override
    public void setup() {

    }

    public void draw() {
        hovered = g.overRect(x, y, w, h);
        g.fill(0);
        if (hovered) {
            g.fill(30);
        }
        g.rect(x, y, w, h, 10);
        g.textAlign(g.CENTER, g.CENTER);
        g.fill(255);
//        g.textSize(40);
        g.textFont(Configurations.CARD_FONT);
        g.text(label, x + w / 2f, y + h / 2f);
    }

    @Override
    public void update() {

    }

    public void onClick(int mx, int my) {
        if (mx > x && mx < x + w && my > y && my < y + h) {
            onClick.run();
        }
    }
}

