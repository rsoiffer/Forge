package gui;

import gui.types.GUIComponent;
import java.util.ArrayList;
import java.util.Arrays;

public class GUI {

    private boolean visible = false;
    private final String name;
    protected final ArrayList<GUIComponent> components = new ArrayList();
    
    public GUI(String n){
        
        name = n;
    }
    
    public String getName(){
        
        return name;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean b) {
        visible = b;
    }

    public GUI add(GUIComponent... comp) {
        Arrays.asList(comp).forEach(c -> {
            components.add(c);
        });
        return this;
    }

    public void remove(GUIComponent c) {
        if (components.contains(c)) {
            components.remove(c);
        }
    }

    public ArrayList<GUIComponent> getComponents() {
        return components;
    }

    public void update(){
        
        components.forEach(c -> {
            
            c.update();
        });
    }

    public void draw(){
        
        components.forEach(c -> {
            
            c.draw();
        });
    }
}
