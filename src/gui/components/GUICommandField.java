/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.components;

import graphics.Graphics2D;
import static gui.TypingManager.clearTyped;
import static gui.TypingManager.isTyping;
import static gui.TypingManager.typingLimit;
import gui.types.ComponentInputGUI;
import org.newdawn.slick.Color;
import util.Vec2;
import static gui.GUIController.FONT;
import static gui.TypingManager.getTyped;
import gui.types.GUITypingComponent;
import static util.Vec2.ZERO;

/**
 *
 * @author Cruz
 */
public class GUICommandField extends GUITypingComponent {

    private int maxChar;
    private Color color;

    public GUICommandField(String n, ComponentInputGUI g, Vec2 p, double d, Color c) {

        super(n, g, p, new Vec2(0, d));
        maxChar = (int) (d / FONT.getWidth(" "));
        color = c;
        buffer = "";
    }

    public Color getColor() {

        return color;
    }

    public void setColor(Color c) {

        color = c;
    }

    @Override
    public void draw() {

        Graphics2D.drawText(buffer, "Console", pos, color);
        
        if (drawCur) {
            
            Vec2 cur = cursor.add(pos).multiply(new Vec2(FONT.getWidth(" "), 0));
            Graphics2D.drawLine(cur, cur.add(new Vec2(0, FONT.getHeight())));
        }
    }

    @Override
    public void send() {

        String s = buffer;
        buffer = "";
        cursor = ZERO;
        gui.recieve(name, s);
    }

    @Override
    public void update() {

        if (isTyping()) {

            String b = getTyped();
            clearTyped();
            int l = b.length();
            buffer = buffer.substring(0, (int) cursor.x) + b + buffer.substring((int) cursor.x);

            if (buffer.length() > maxChar) {

                buffer = buffer.substring(0, maxChar);
                typingLimit(maxChar);
            }
        }
    }
}
