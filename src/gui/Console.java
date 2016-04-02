package gui;

import gui.components.GUIRectangle;
import gui.components.GUIComponent;
import java.util.ArrayList;
import util.Color4;
import util.Vec2;

public class Console extends GUI {

    private ArrayList<GUIComponent> components = super.components;

    public void init(Vec2 pos, Vec2 dim) {
        components.add(new GUIRectangle().setPos(pos).setDim(dim).setColor(Color4.BLACK));
    }

    @Override
    public void update() {

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
