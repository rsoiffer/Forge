package gui;

import graphics.data.GLFont;
import graphics.loading.FontContainer;
import gui.components.GUIRectangle;
import gui.components.GUIText;
import org.newdawn.slick.Color;
import util.Color4;
import util.Vec2;

public class Console extends GUI {

    public GUIRectangle background;
    public GUIRectangle textground;
    public GUIText outputText;
    public static GLFont font = FontContainer.get("Console");
    
    public Console(String n, Vec2 pos, Vec2 dim) {
        
        super(n);
        background = new GUIRectangle().setPos(pos).setDim(dim).setColor(Color4.BLUE.withA(.5).withG(.5).withB(.5));
        textground = new GUIRectangle().setPos(pos.add(new Vec2(0, dim.y))).setDim(dim.withY(font.getHeight())).setColor(Color4.BLACK.withA(.5));
        outputText = new GUIText(dim, Color.white, pos.add(new Vec2(0, dim.y + font.getHeight())), "", font);
        components.add(background);
        components.add(textground);
        components.add(outputText);
    }

    @Override
    public void update() {
        components.forEach(c -> {
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
