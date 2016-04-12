/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.components;

import graphics.Graphics2D;
import static gui.GUIController.FONT;
import static gui.TypingManager.clearTyped;
import static gui.TypingManager.getTyped;
import static gui.TypingManager.isTyping;
import static gui.TypingManager.typingLimit;
import gui.types.ComponentInputGUI;
import gui.types.GUITypingComponent;
import java.util.ArrayList;
import java.util.List;
import org.newdawn.slick.Color;
import util.Color4;
import util.Vec2;
import static util.Vec2.ZERO;

/**
 *
 * @author Cruz
 */
public class GUICommandField extends GUITypingComponent {

    private List<String> prevComm;
    private int prevIndex;
    private int maxChar;
    private Color color;

    public GUICommandField(String n, ComponentInputGUI g, Vec2 p, double d, Color c) {

        super(n, g, p, new Vec2(0, d));
        maxChar = (int) (d / FONT.getWidth(" "));
        color = c;
        buffer = "";
        prevComm = new ArrayList();
    }
    
    public GUICommandField(String n, ComponentInputGUI g, Vec2 p, double d, Color c, Color4 cc) {

        super(n, g, p, new Vec2(0, d), cc);
        maxChar = (int) (d / FONT.getWidth(" "));
        color = c;
        buffer = "";
        prevComm = new ArrayList();
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

        if (drawCur && cursor.x > 0 && cursor.x <= maxChar) {

            Vec2 cur = cursor.add(pos).multiply(new Vec2(FONT.getWidth(" "), 0));
            Graphics2D.drawLine(cur.add(pos), cur.subtract(new Vec2(0, FONT.getHeight())).add(pos), curCol, 1);
        }
    }

    @Override
    public void send() {

        String s = buffer;
        buffer = "";
        cursor = ZERO;
        gui.recieve(name, s);
        prevComm.add(0, s);
    }

    @Override
    public void update() {

        if (isTyping()) {

            String b = getTyped();
            clearTyped();
            int l = b.length();
            int bl = buffer.length();

            if (cursor.x > bl) {

                cursor = cursor.withX(bl);
            }

            buffer = buffer.substring(0, (int) cursor.x) + b + buffer.substring((int) cursor.x);
            cursor = cursor.add(new Vec2(l, 0));

            if (buffer.length() > maxChar) {

                buffer = buffer.substring(0, maxChar);
                typingLimit(maxChar);
            }
        }
    }

    @Override
    public void backspace() {

        update();
        buffer = buffer.substring(0, cursor.x > 0 ? ((int) cursor.x) - 1 : 0) + buffer.substring((int) cursor.x);

        if (cursor.x > 0) {

            cursor = cursor.add(new Vec2(-1, 0));
        }
    }

    @Override
    public void tab() {

        buffer = "   " + buffer;
        cursor = cursor.add(new Vec2(3, 0));
        update();
    }

    @Override
    public void up() {

        if (prevComm.size() > 0) {
            
            prevIndex++;

            if (prevIndex >= prevComm.size()) {

                prevIndex--;
            }

            buffer = prevComm.get(prevIndex);
            cursor = cursor.withX(buffer.length());
        }
    }

    @Override
    public void down() {

        if (prevComm.size() > 0) {
            
            prevIndex--;

            if (prevIndex < 0) {

                prevIndex++;
            }

            buffer = prevComm.get(prevIndex);
            cursor = cursor.withX(buffer.length());
        }
    }

    @Override
    public void left() {

        cursor = cursor.subtract(new Vec2(1, 0));

        if (cursor.x < 0) {

            cursor = cursor.withX(0);
        }
    }

    @Override
    public void right() {

        cursor = cursor.add(new Vec2(1, 0));

        if (cursor.x > maxChar) {

            cursor = cursor.withX(maxChar);
        }
    }
}
