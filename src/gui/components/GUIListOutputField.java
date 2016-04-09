/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.components;

import graphics.Graphics2D;
import gui.GUI;
import java.util.ArrayList;
import java.util.List;
import org.newdawn.slick.Color;
import util.Vec2;
import static gui.GUIController.FONT;

/**
 *
 * @author Cruz
 */
public class GUIListOutputField extends GUIComponent<GUI> {

    private static final char[] nextL = {' ', '-', ':', ';', ',', '.', '?', '!', 
        ')', '}', ']', '%'};
    
    private final List<String> lines = new ArrayList();
    private final Vec2 printDim;
    private Color color;
    
    
    public GUIListOutputField(String n, GUI g, Vec2 p, Vec2 d, Color c) {
        
        super(n, g, p, d);
        color = c;
        printDim = new Vec2(Math.floor(d.x / FONT.getWidth(" ")), Math.floor(d.y / FONT.getHeight()));
        
    }
    
    public Color getColor(){
        
        return color;
    }
    
    public void setColor(Color c){
        
        color = c;
    }
    
    public GUIListOutputField appendText(String s){
        
        lines.set(0, lines.get(0) + s);
        regulate();
        return this;
    }
    
    public GUIListOutputField appendLine(String s){
        
        lines.add(0, s);
        regulate();
        return this;
    }
    
    private void regulate(){
        
        if(lines.get(0).length() > (int) printDim.x){
            
            int index = indexOfLine((int) printDim.x);
            lines.set(0, lines.get(0).substring(0, index) + "\n" + lines.get(0).substring(index));
        }
        
        if (!lines.get(0).contains("\n")) {
            
            return;
        }
        
        for (int i = 0; i < lines.get(0).length(); i++) {

            if (lines.get(0).charAt(i) == '\n') {

                String s = lines.get(0);
                lines.set(0, s.substring(0, i));
                lines.add(0, s.substring(i + 1));
                regulate();
                return;
            }
        }
    }
    
    private int indexOfLine(int i) {

        boolean lineChar = false;
        int n = i;
        
        do{
            
            if(isLineChar(lines.get(0).charAt(n))){
                
                return n;
            }
            
            n--;
        }while(!lineChar && n > 0);
        
        return i;
    }
    
    private boolean isLineChar(char c){
        
        for (char ch : nextL) {
            
            if(ch == c){
                
                return true;
            }
        }
        
        return false;
    }

    @Override
    public void draw() {

        for (int i = 0; i < printDim.y && i < lines.size(); i++) {
            
            Graphics2D.drawText(lines.get(i), "Console", pos.subtract(new Vec2(0, dim.y - FONT.getHeight() * i)), color);
        }
    }

    @Override
    public void update() {}
}
