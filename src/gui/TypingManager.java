/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import engine.Input;
import engine.Signal;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Cruz
 */
public class TypingManager extends Signal<Boolean> {

    private static final Map<Integer, Signal<Boolean>> prevStates = new HashMap();
    private static final List<Integer> pressed = new ArrayList();
    
    private static boolean shift = false;
    private static boolean ctrl = false;
    private static boolean alt = false;
    private static boolean caps = Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_CAPS_LOCK);
    
    private static final Map<Integer, Signal<Boolean>> convert = new HashMap();
    private static final Character toChar[] = {null, '1', '2', '3', '4', '5',
        '6', '7', '8', '9', '0', '-', '=', (char) 8, (char) 9, 'q', 'w', 'e',
        'r', 't', 'y', 'u', 'i', 'o', 'p', '[', ']', null, null, 'a', 's', 'd', 
        'f', 'g', 'h', 'j', 'k', 'l', ';', '\'', '`', null, null, 'z', 'x', 'c',
        'v', 'b', 'n', 'm', ',', '.', '/', null, '*', null, ' ', null, null, null,
        null, null, null, null, null, null, null, null, null, null, '7', '8', '9',
        '-', '4', '5', '6', '+', '1', '2', '3', '0', '.', null, null, null, null, null};
    
    
    static{
        
        
    }

    public TypingManager() {

        super(false);
        this.onEvent(() -> {

            prevStates.putAll(Input.getKeyMap());
            Input.getKeyMap().clear();
        });
    }
}
