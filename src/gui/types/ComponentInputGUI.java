/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.types;

import gui.GUI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import util.Vec2;

/**
 *
 * @author Cruz
 */
public abstract class ComponentInputGUI extends GUI{

    protected List<GUIInputComponent> inputs;
    
    public ComponentInputGUI(String n, GUIInputComponent... gip) {
        
        super(n);
        inputs = new ArrayList();
        inputs.addAll(Arrays.asList(gip));
    }
    
    public GUIInputComponent mousePressed(Vec2 p){
        
        for (GUIInputComponent gip : inputs) {
            
            if(gip.containsClick(p)){
                
                return gip;
            }
        }
        
        return null;
    }
    
    public abstract void recieve(String name, Object info);
    
    public abstract GUIInputComponent getDefaultComponent();
}