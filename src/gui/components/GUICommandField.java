/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.components;

import graphics.Graphics2D;
import static gui.TypingManager.clearTyped;
import static gui.TypingManager.getTypedSave;
import static gui.TypingManager.isTyping;
import static gui.TypingManager.typingLimit;
import gui.types.ComponentInput;
import org.newdawn.slick.Color;
import util.Vec2;
import static gui.GUIController.FONT;

/**
 *
 * @author Cruz
 */
public class GUICommandField extends GUIInputComponent {

    private int maxChar;
    private Color color;

    public GUICommandField(String n, ComponentInput g, Vec2 p, double d, Color c) {

        super(n, g, p, new Vec2(0, d));
        maxChar = (int) (d / FONT.getWidth(" "));
        color = c;
        buffer = "";
    }
    
    public Color getColor(){
        
        return color;
    }
    
    public void setColor(Color c){
        
        color = c;
    }

    @Override
    public void draw() {

        Graphics2D.drawText(buffer, "Console", pos, color);
    }
    
    @Override
    public void send(){
        
        String s = buffer;
        buffer = "";
        clearTyped();
        gui.recieve(name, s);
    }

    @Override
    public void update() {

        if (isTyping()) {
            
            buffer = getTypedSave();

            if (buffer.length() > maxChar) {

                buffer = buffer.substring(0, maxChar);
                typingLimit(maxChar);
            }
        }
    }
}
