package gui.components;

import graphics.Graphics2D;
import graphics.data.GLFont;
import static gui.TypingManager.getTypedSave;
import java.util.ArrayList;
import java.util.List;
import org.newdawn.slick.Color;
import util.Vec2;

public class GUIText implements GUIComponent {

    private static final char[] nextL = {' ', '-', ':', ';', ',', '.', '?', '!', ')', '}', ']', '%'};
    List<String> lines = new ArrayList();
    Vec2 dim;
    Color color;
    Vec2 pos;
    GLFont font;

    public GUIText(Vec2 d, Color c, Vec2 p, String t, GLFont f) {
        dim = d;
        color = c;
        pos = p;
        lines.add(t);
        font = f;
        fixLines();
    }

    @Override
    public void setImage(String path) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public GUIText appendText(String s) {
        lines.set(0, lines.get(0) + s);
        fixLines();
        return this;
    }

    public GUIText appendLine(String s) {
        lines.add(0, s);
        fixLines();
        return this;
    }

    public GUIText setText(String s) {
        lines = new ArrayList();
        lines.add(0, s);
        fixLines();
        return this;
    }

    public List<String> getLines() {
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

    @Override
    public void update() {

    }

    private int indexOfLine(int i) {

        boolean lineChar = false;
        int n = i;
        
        do{
            
            if(isLineChar(lines.get(0).charAt(n))){
                
                return n;
            }
            
            n--;
        }while(!lineChar && n > 0);
        
        return i;
    }
    
    private boolean isLineChar(char c){
        
        for (char ch : nextL) {
            
            if(ch == c){
                
                return true;
            }
        }
        
        return false;
    }

    public void fixLines() {

        if (font.getWidth(lines.get(0)) > dim.x) {
            int n = (int) Math.floor(dim.x / font.getWidth("_"));

            n = indexOfLine(n);
            lines.set(0, lines.get(0).substring(0, n) + "\n" + lines.get(0).substring(n));
        }

        if (!lines.get(0).contains("\n")) {
            return;
        }

        for (int i = 0; i < lines.get(0).length(); i++) {

            if (lines.get(0).charAt(i) == '\n') {

                String s = lines.get(0);
                lines.set(0, s.substring(0, i));
                lines.add(0, s.substring(i + 1));
                fixLines();
                return;
            }
        }
    }

    @Override
    public void draw() {
        for (int i = 0; i < Math.floor(dim.y / font.getHeight()) && i < lines.size(); i++) {
            Graphics2D.drawText(lines.get(i), "Console", pos.subtract(new Vec2(0, dim.y - font.getHeight() * i)), color);
        }
        
        Graphics2D.drawText(getTypedSave(), "Console", pos, color);
    }

}
