/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import engine.Signal;
import static gui.TypingManager.addChar;
import static gui.TypingManager.getCaps;
import static gui.TypingManager.getShift;

/**
 *
 * @author gvandomelen19
 */
public class TypeKey extends Signal<Boolean> {
    
    private final char lower;
    private final char upper;
    private final boolean caps;
    
    public TypeKey(char l, char u, boolean c) {
        
        super(false);
        lower = l;
        upper = u;
        caps = c;
        filter(x -> x == true).onEvent(() -> addChar(((caps && getCaps()) ^ !getShift()) ? lower : upper));
    }
}
