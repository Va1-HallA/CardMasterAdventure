package org.deckmaster;

import processing.core.PFont;
import processing.core.PVector;

public class Button implements Drawable {
    int x, y;
    int w, h;
    boolean hovered = false;
    String label;
    Runnable onClick;
    PVector color;
    float alpha;
    PFont font;
    float fontSize;

    public Button(int x, int y, int w, int h, String label, Runnable onClick, PVector color, float alpha, PFont font, float fontSize) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.label = label;
        this.onClick = onClick;
        this.color = color;
        this.alpha = alpha;
        this.font = font;
        this.fontSize = fontSize;
    }

    @Override
    public void setup() {

    }

    public void draw() {
        hovered = g.overRect(x, y, w, h);
        g.fill(color.x, color.y, color.z, alpha);
        if (hovered) {
            g.fill(color.x + 30, color.y + 30, color.z + 30, alpha);
        }
        g.rect(x, y, w, h, 10);
        g.fill(255);
        g.textAlign(g.CENTER, g.CENTER);
        g.textFont(font);
        g.textSize(fontSize);
        g.text(label, x + w / 2f, y + h / 2f - 8);
    }

    @Override
    public void update() {

    }

    public void onClick(int mx, int my) {
        if (mx > x && mx < x + w && my > y && my < y + h) {
            onClick.run();
        }
    }

    public void switchFunction(Runnable r) {
        onClick = r;
    }
}

