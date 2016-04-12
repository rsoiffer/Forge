package gui.types;

import gui.GUI;
import util.Vec2;

public abstract class GUIComponent<G extends GUI> {

    protected final String name;
    protected final G gui;
    protected final Vec2 pos;
    protected final Vec2 dim;
    
    public GUIComponent(String n, G g, Vec2 p, Vec2 d){
        
        name = n;
        gui = g;
        pos = p;
        dim = d;
    }
    
    public abstract void draw();

    public abstract void update();

    public String getName() {
        return name;
    }

    public Vec2 getPos() {
        return pos;
    }

    public Vec2 getDim() {
        return dim;
    }
    
    public boolean containsClick(Vec2 click){
        
        return click.containedBy(pos, pos.add(dim));
    }
}