package gui.components;

import graphics.Graphics2D;
import graphics.loading.FontContainer;
import java.awt.Font;
import org.newdawn.slick.Color;
import util.Vec2;

public class GUIText extends GUIComponent {

    String text = "";
    Vec2 dim;
    int size;
    Color color;
    Vec2 pos;

    public GUIText init(Vec2 d, int s, Color c, Vec2 p, String t) {
        dim = d;
        size = s;
        color = c;
        pos = p;
        text = t;
        FontContainer.add("Console", "Times New Roman", Font.PLAIN, size);
        return this;
    }

    @Override
    public void draw() {
        Graphics2D.drawText(text, "Console", pos.add(Vec2.ZERO.withY(size)), color, (int) dim.x);
    }

    @Override
    public void setImage(String path) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public GUIText appendText(String s) {
        text += s;
        return this;
    }

    public GUIText setText(String s) {
        text = s;
        return this;
    }

    public String getText() {
        return text;
    }

    public Vec2 getPos() {
        return pos;
    }

    public GUIText setPos(Vec2 p) {
        pos = p;
        return this;
    }

    public int getWidth() {
        return (int) dim.x;
    }

    public GUIText setWidth(int w) {
        dim = dim.withX(w);
        return this;
    }

    public int getHeight() {
        return (int) dim.y;
    }

    public GUIText setHeight(int h) {
        dim = dim.withY(h);
        return this;
    }

    public int getSize() {
        return size;
    }

    public GUIText setSize(int s) {
        size = s;
        return this;
    }

    @Override
    public void update() {
        truncate();
    }

    private void truncate() {
        String[] subs = text.split(" ");
        String[] lines = new String[subs.length];
        int n = 1;
        for (int i = 0; i < subs.length; i++) {
            if (FontContainer.get("Console").getWidth(lines[n - 1] + " " + subs[i]) < dim.x) {
                lines[n - 1] += " " + subs[i];
            } else {
                n++;
                lines[n - 1] = subs[i];
            }
        }
        if (n > dim.y / FontContainer.get("Console").getHeight()) { //One to get onscreen, one to leave a space
            int length = 0;
            text = text.substring(lines[0].length() + 1);
            truncate();
        }
    }

}
