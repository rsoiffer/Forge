package gui.components;

import graphics.Graphics2D;
import gui.GUI;
import util.Color4;
import util.Vec2;

public class GUIPanel extends GUIComponent<GUI> {

    private Color4 color;

    public GUIPanel(String n, Vec2 p, Vec2 d, Color4 c){
        
        super(n, null, p, d);
        color = c;
    }
    
    @Override
    public void draw() {
        
        Graphics2D.fillRect(pos, dim, color);
    }

    @Override
    public void update() {}

    public Color4 getColor() {
        
        return color;
    }

    public GUIPanel setColor(Color4 c) {
        
        color = c;
        return this;
    }
}
