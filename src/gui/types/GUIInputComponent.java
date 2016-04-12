/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.types;

import util.Vec2;

/**
 *
 * @author Cruz
 */
public abstract class GUIInputComponent<B> extends GUIComponent<ComponentInputGUI> {

    protected B buffer;

    public GUIInputComponent(String n, ComponentInputGUI g, Vec2 p, Vec2 d) {

        super(n, g, p, d);
    }

    public abstract void send();
}
