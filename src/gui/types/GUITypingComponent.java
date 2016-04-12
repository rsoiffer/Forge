/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.types;

import util.Vec2;
import static util.Vec2.ZERO;

/**
 *
 * @author Grant
 */
public abstract class GUITypingComponent extends GUIInputComponent<String> {

    protected Vec2 cursor;
    protected boolean drawCur;

    public GUITypingComponent(String n, ComponentInputGUI g, Vec2 p, Vec2 d) {

        super(n, g, p, d);
        cursor = ZERO;
        drawCur = false;
    }

    public void DrawCursor(boolean dc) {

        drawCur = dc;
    }

    public boolean isDrawingCursor() {

        return drawCur;
    }

    public void setCursorPos(Vec2 cp) {

        cursor = cp;
    }

    public Vec2 getCursorPos() {

        return cursor;
    }

    public void moveCursor(Vec2 s) {

        cursor = cursor.add(s);

        if (cursor.x < 0) {

            cursor = cursor.withX(0);
        }

        if (cursor.y < 0) {

            cursor = cursor.withY(0);
        }
    }
}
