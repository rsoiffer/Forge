package gui.components;

import graphics.Graphics2D;
import graphics.loading.FontContainer;
import java.awt.Font;
import org.newdawn.slick.Color;
import util.Vec2;

public class GUIText extends GUIComponent {
    
    String text="";
    int width;
    int size;
    Color color;
    Vec2 pos;
    
    public GUIText init(Color c, int w){
        FontContainer.init();
        color = c;
        width = w;
        return this;
    }
    
    @Override
    public void draw() {
        Graphics2D.drawText(text, "Font", pos, Color.white, width);
    }

    @Override
    public void setImage(String path) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public GUIText appendText(String s){
        text+=s;
        return this;
    }
    public GUIText setText(String s){
        text=s;
        return this;
    }
    public String getText(){
        return text;
    }
    public Vec2 getPos(){
        return pos;
    }
    public GUIText setPos(Vec2 p){
        pos = p;
        return this;
    }
    public int getWidth(){
        return width;
    }
    public GUIText setWidth(int w){
        width = w;
        return this;
    }
    public int getSize(){
        return size;
    }
    public GUIText setSize(int s){
        size = s;
        return this;
    }
    
}
