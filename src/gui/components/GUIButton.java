package gui.components;

import graphics.Graphics2D;
import static gui.GUIController.FONT;
import gui.types.ComponentInputGUI;
import gui.types.GUIButtonComponent;
import org.newdawn.slick.Color;
import util.Vec2;

/**
 *
 * @author Grant
 */
public class GUIButton extends GUIButtonComponent {

    private Color color;
    
    public GUIButton(String n, ComponentInputGUI g, Vec2 p, Vec2 d, String l, Color c) {
        
        super(n, g, p, d, l, false);
        color = c;
        toggle = false;
    }
    
    public GUIButton(String n, ComponentInputGUI g, Vec2 p, Vec2 d, String l, Color c, boolean t) {
        
        super(n, g, p, d, l, false);
        color = c;
        toggle = t;
    }

    @Override
    public void draw() {

        Graphics2D.drawText(label, "Console", pos.add(new Vec2(0, FONT.getHeight())), color);
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
