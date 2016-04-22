/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.components;

import graphics.Graphics2D;
import static gui.GUIController.FONT;
import gui.types.GUIComponent;
import org.newdawn.slick.Color;
import util.Color4;
import util.Vec2;

/**
 *
 * @author cbarnum18
 */
public class GUILabel extends GUIComponent{

    private Color color;
    private String text;
    
    public GUILabel(String n, Vec2 p, String t, Color c){
        
        super(n, null, p, Vec2.ZERO);
        color = c;
        text = t;
    }

    public Color getColor() {
        
        return color;
    }

    public void setColor(Color c) {
        
        this.color = c;
    }
    
    public void setLabel(String l){
        
        text = l;
    }
    
    public String getLabel(){
        
        return text;
    }
    
    @Override
    public void draw() {

        Graphics2D.drawText(text, "Console", pos.add(new Vec2(0, FONT.getHeight())), color);
    }

    @Override
    public void update() {}
}
