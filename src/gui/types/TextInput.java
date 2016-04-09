/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.types;

import gui.GUI;
import gui.components.GUIInputComponent;
import gui.components.GUICommandField;

/**
 *
 * @author Cruz
 */
public abstract class TextInput extends GUI{

    protected GUIInputComponent input;
    
    public TextInput(String n, GUIInputComponent gip) {
        
        super(n);
        input = gip;
    }
    
    public abstract void recieve(String name, String text);
    
    public abstract GUICommandField getTextInput();
}
