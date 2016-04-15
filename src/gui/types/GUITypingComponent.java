/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.types;

import static gui.GUIController.FONT;
import util.Color4;
import static util.Color4.BLACK;
import util.Vec2;
import static util.Vec2.ZERO;

/**
 *
 * @author Grant
 */
public abstract class GUITypingComponent extends GUIInputComponent<String> {

    protected Vec2 cursor;
    protected boolean drawCur;
    protected Color4 curCol;

    public GUITypingComponent(String n, ComponentInputGUI g, Vec2 p, Vec2 d) {

        super(n, g, p, d);
        cursor = ZERO;
        drawCur = false;
        curCol = BLACK;
    }
    
    public GUITypingComponent(String n, ComponentInputGUI g, Vec2 p, Vec2 d, Color4 cc) {

        super(n, g, p, d);
        cursor = ZERO;
        drawCur = false;
        curCol = cc;
    }
    
    public void setCursorPosRaw(Vec2 mp){
        
        if(containsClick(mp)){
            
            cursor = mp.subtract(pos).divide(new Vec2(FONT.getWidth(" "), FONT.getHeight()));
            cursor = new Vec2(Math.round(cursor.x), Math.round(cursor.y));
            
            if(cursor.x < 0){
                
                cursor = cursor.withX(0);
            }
            
            if(cursor.y < 0){
                
                cursor = cursor.withY(0);
            }
        }
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

    public void setCursorColor(Color4 cc) {
        this.curCol = cc;
    }

    public Color4 getCursorColor() {
        
        return curCol;
    }
    
    public abstract void backspace();
    
    public abstract void tab();
    
    public abstract void up();
    
    public abstract void down();
    
    public abstract void left();
    
    public abstract void right();
}
