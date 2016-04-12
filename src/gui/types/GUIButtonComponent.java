/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.types;

import util.Vec2;

/**
 *
 * @author Grant
 */
public abstract class GUIButtonComponent extends GUIInputComponent<Boolean> {

    protected String label;
    protected boolean toggle;
    
    public GUIButtonComponent(String n, ComponentInputGUI g, Vec2 p, Vec2 d, String l, boolean t) {
        
        super(n, g, p, d);
        label = l;
        toggle = t;
        buffer = false;
    }
    
    public GUIButtonComponent(String n, ComponentInputGUI g, Vec2 p, Vec2 d, String l) {
        
        super(n, g, p, d);
        label = l;
        toggle = false;
        buffer = false;
    }
    
    public void setToggle(boolean t){
        
        toggle = t;
        
        if(t){
            
           buffer = false; 
        }
    }
    
    public void setLabel(String l) {
        
        label = l;
    }
    
    public String getLabel() {
        
        return label;
    }
}
