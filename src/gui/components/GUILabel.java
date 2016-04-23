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
import util.Vec2;

/**
 *
 * @author cbarnum18
 */
public class GUILabel extends GUIComponent{

    private Color color;
    private String text;
    private Vec2 mid;
    private boolean cent;
    
    public GUILabel(String n, Vec2 p, String t, Color c){
        
        super(n, null, p, Vec2.ZERO);
        color = c;
        text = t;
        cent = false;
    }
    
    public GUILabel(String n, Vec2 p, Vec2 d, String t, Color c){
        
        super(n, null, p, d);
        color = c;
        text = t;
        cent = true;
        mid = findMid(t, d);
    }

    public Color getColor() {
        
        return color;
    }

    public void setColor(Color c) {
        
        this.color = c;
    }
    
    public void setLabel(String l){
        
        text = l;
        
        if(cent){
            
            mid = findMid(l, dim);
        }
    }
    
    public String getLabel(){
        
        return text;
    }
    
    @Override
    public void draw() {

        Graphics2D.drawText(text, "Console", pos.add(new Vec2(0, FONT.getHeight())).add(cent ? mid : Vec2.ZERO), color);
    }

    @Override
    public void update() {}
}
