package gui;

import gui.components.GUIRectangle;
import gui.components.GUIText;
import gui.components.GUIComponent;
import java.util.ArrayList;
import org.newdawn.slick.Color;
import util.Color4;
import util.Vec2;

public class Console extends GUI {

    private ArrayList<GUIComponent> components = super.components;
    
    public GUIRectangle background;
    public GUIText outputText;

    public Console init(Vec2 pos, Vec2 dim, int size) {
        background = new GUIRectangle().setPos(pos).setDim(dim).setColor(Color4.BLACK);
        outputText = new GUIText().init(dim.subtract(new Vec2(0,size)), size, Color.white, pos.add(new Vec2(0,dim.y-size)),"Hello World");
        components.add(background);
        components.add(outputText);
        return this;
    }

    @Override
    public void update() {
        components.forEach(c->{
            c.update();
        });
    }
    @Override
    public void open() {
            super.setVisible(true);
            //Initialize stuff
    }
    @Override
    public void close() {
            super.setVisible(false);
            //Close stuff
    }
    @Override
    public void draw() {
        components.forEach(c -> {
            c.draw();
        });
    }
}
