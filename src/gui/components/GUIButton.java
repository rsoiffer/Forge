package gui.components;

import graphics.Graphics2D;
import static gui.GUIController.FONT;
import gui.types.ComponentInputGUI;
import gui.types.GUIButtonComponent;
import gui.types.GUIComponent;
import org.newdawn.slick.Color;
import util.Vec2;

/**
 *
 * @author Grant
 */
public class GUIButton extends GUIButtonComponent {

    private Color color;
    private Vec2 mid;
    
    public GUIButton(String n, ComponentInputGUI g, Vec2 p, Vec2 d, String l, Color c) {
        
        super(n, g, p, d, l, false);
        color = c;
        toggle = false;
        mid = findMid(l, d);
    }
    
    public GUIButton(String n, ComponentInputGUI g, Vec2 p, Vec2 d, String l, Color c, boolean t) {
        
        super(n, g, p, d, l, false);
        color = c;
        toggle = t;
    }
    
    @Override
    public void setLabel(String l){
        
        super.setLabel(l);
        mid = findMid(l, dim);
    }

    @Override
    public void draw() {

        Graphics2D.drawText(label, "Console", pos.add(new Vec2(0, FONT.getHeight())).add(mid), color);
    }

    public Color getColor() {
        
        return color;
    }

    public void setColor(Color c) {
        
        color = c;
    }

    @Override
    public void update() {}

    @Override
    public void send() {

        if(toggle){
            
            buffer = !buffer;
            gui.recieve(name, buffer);
        }else{
            
            gui.recieve(name, true);
        }
    }
}
