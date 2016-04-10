/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.types;

import gui.GUI;
import gui.components.GUIInputComponent;
import gui.components.GUICommandField;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Cruz
 */
public abstract class ComponentInput extends GUI{

    protected List<GUIInputComponent> input;
    
    public ComponentInput(String n, GUIInputComponent... gip) {
        
        super(n);
        input = new ArrayList();
        input.addAll(Arrays.asList(gip));
    }
    
    public abstract void recieve(String name, Object info);
    
    public abstract GUICommandField getTextInput();
}