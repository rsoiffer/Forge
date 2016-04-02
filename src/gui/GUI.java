package gui;

import gui.components.GUIComponent;
import java.util.ArrayList;
import java.util.Arrays;

public abstract class GUI {
    
        private boolean visible = false;

        public boolean isOpen() {
            return visible;
        }
        public void setVisible(boolean b){
            visible = b;
        }
        
        public abstract void open();
        public abstract void close();

	protected final ArrayList<GUIComponent> components = new ArrayList();

	public GUI add(GUIComponent... comp){
            Arrays.asList(comp).forEach(c -> {
                components.add(c);
            });
            return this;
	}
        
        public void remove(GUIComponent c){
            if(components.contains(c)) components.remove(c);
        }
        
        public ArrayList<GUIComponent> getComponents(){
            return components;
        }
        
	public abstract void update();
        public abstract void draw();

}