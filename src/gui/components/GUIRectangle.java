package gui.components;

import graphics.Graphics2D;
import util.Color4;
import util.Vec2;

public class GUIRectangle extends GUIComponent{
    
    private Vec2 pos;
    private Vec2 dim;
    private Color4 color;
    
    @Override
    public void draw() {
        Graphics2D.fillRect(pos, dim, color);
    }

    @Override
    public void setImage(String path) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public Vec2 getPos(){
        return pos;
    }
    public GUIRectangle setPos(Vec2 p){
        pos = p;
        return this;
    }
    public Vec2 getDim(){
        return dim;
    }
    public GUIRectangle setDim(Vec2 d){
        dim = d;
        return this;
    }
    public Color4 getColor(){
        return color;
    }
    public GUIRectangle setColor(Color4 c){
        color = c;
        return this;
    }
    
}
