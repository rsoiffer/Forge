/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.premadeGuis;

import engine.Input;
import static gui.GUIController.FONT;
import gui.components.GUIInputComponent;
import gui.components.GUICommandField;
import gui.components.GUIListOutputField;
import gui.components.GUIPanel;
import gui.types.TextInput;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.Color;
import util.Color4;
import util.Vec2;
import static gui.TypingManager.typing;

/**
 *
 * @author Cruz
 */
public class Chat extends TextInput {

    private GUIListOutputField output;
    private final Vec2 pos;
    private final Vec2 dim;

    public Chat(String n, int key, Vec2 p, Vec2 d) {

        super(n, null);
        pos = p;
        dim = d;        
        GUIPanel out = new GUIPanel("Output Panel", pos, dim.subtract(new Vec2(0, FONT.getHeight())), Color4.gray(.3).withA(.5));
        GUIPanel in = new GUIPanel("Input Panel", pos.add(new Vec2(0, dim.y - FONT.getHeight())), dim.withY(FONT.getHeight()), Color4.BLACK.withA(.5));
        output = new GUIListOutputField("Output Field", this, pos.add(new Vec2(0, dim.y - FONT.getHeight())), dim.subtract(new Vec2(0, 2 * FONT.getHeight())), Color.white);
        input = new GUICommandField("Input Field", this, pos.add(new Vec2(0, dim.y)), dim.x, Color.white);
        
        components.add(out);
        components.add(in);

        Input.whenKey(key, true).onEvent(() -> {

            this.setVisible(true);
            Mouse.setGrabbed(false);
            typing(this, true);
        });

        Input.whenKey(Keyboard.KEY_BACKSLASH, true).onEvent(() -> {

            this.setVisible(true);
            Mouse.setGrabbed(false);
            typing(this, true, "\\");
        });
    }

    @Override
    public GUICommandField getTextInput() {

        return (GUICommandField) input;
    }

    @Override
    public void recieve(String name, String text) {

        output.appendLine(text);
    }
    
    @Override
    public void update(){
        
        super.update();
        output.update();
        input.update();
    }
    
    @Override
    public void draw(){
        
        super.draw();
        output.draw();
        input.draw();
    }
}
