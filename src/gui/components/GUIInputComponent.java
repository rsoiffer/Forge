/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.components;

import gui.types.TextInput;
import util.Vec2;

/**
 *
 * @author Cruz
 */
public abstract class GUIInputComponent extends GUIComponent<TextInput>{
    
    protected String buffer;
    
    public GUIInputComponent(String n, TextInput g, Vec2 p, Vec2 d) {
        
        super(n, g, p, d);
    }
    
    public abstract void send();
}
