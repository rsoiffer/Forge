package gui.components;

import graphics.Graphics2D;
import graphics.loading.FontContainer;
import java.awt.Font;
import java.util.ArrayList;
import org.newdawn.slick.Color;
import util.Vec2;

public class GUIText extends GUIComponent {

    ArrayList<String> lines = new ArrayList();
    String text;
    Vec2 dim;
    int size;
    Color color;
    Vec2 pos;

    public GUIText init(Vec2 d, int s, Color c, Vec2 p, String t) {
        dim = d;
        size = s;
        color = c;
        pos = p;
        lines.add(t);
        FontContainer.add("Console", "Times New Roman", Font.PLAIN, size);
        return this;
    }

    @Override
    public void setImage(String path) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public GUIText appendText(String s) {
        lines.set(Math.max(0, lines.toArray().length-1),lines.get(Math.max(0,lines.toArray().length-1))+s);
        return this;
    }
    
    public GUIText appendLine(String s) {
        lines.add(s);
        return this;
    }

    public GUIText setText(String s) {
        for(String t : lines){
            lines.remove(t);
        }
        return this;
    }

    public ArrayList<String> getLines() {
        return lines;
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
        text="";
        lines.forEach(s -> {
            text+=s;
            if(text.indexOf(s)!=0) text+="\n";
        });
    }

    @Override
    public void draw() {
        Graphics2D.drawText(text, "Console", pos.add(Vec2.ZERO.withY(size)), color, (int) dim.x);
    }

}
