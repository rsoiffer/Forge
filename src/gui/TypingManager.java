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
    private static final String buffer = "";

    private static boolean shift = false;
    private static boolean ctrl = false;
    private static boolean alt = false;
    private static boolean caps = Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_CAPS_LOCK);

    private static final Map<Integer, Signal<Boolean>> convert = new HashMap();

    //28=enter 29=L Ctrl 42=L Shift 43=\ 56=L Alt 59-68=F1-F10 87-88=F11-F12
    
    private static final Character regKeys[] = {null, null, '1', '2', '3', '4',
        '5', '6', '7', '8', '9', '0', '-', '=', (char) 8, (char) 9, 'q', 'w',
        'e', 'r', 't', 'y', 'u', 'i', 'o', 'p', '[', ']', null, null, 'a', 's',
        'd', 'f', 'g', 'h', 'j', 'k', 'l', ';', '\'', '`', null, null, 'z', 'x',
        'c', 'v', 'b', 'n', 'm', ',', '.', '/', null, '*', null, ' ', null, null,
        null, null, null, null, null, null, null, null, null, null, null, '7',
        '8', '9', '-', '4', '5', '6', '+', '1', '2', '3', '0', '.', null, null,
        null, null, null};

    private static final Character shiftKeys[] = {null, null, '!', '@', '#',
        '%', '^', '&', '*', '(', ')', '_', '+', (char) 8, (char) 9, 'Q', 'W',
        'E', 'R', 'T', 'Y', 'U', 'I', 'O', 'P', '{', '}', null, null, 'A', 'S',
        'D', 'F', 'G', 'H', 'J', 'K', 'L', ':', '"', null, '|', 'Z', 'X', 'C',
        'V', 'B', 'N', 'M', '<', '>', '?', null, '*', null, ' ', null, null,
        null, null, null, null, null, null, null, null, null, null, null, '7',
        '8', '9', '-', '4', '5', '6', '+', '1', '2', '3', '0', '.', null, null,
        null, null, null};

    static {

        for (int i = 0; i <= 88; i++) {

            if (regKeys[i] != null) {

                Signal<Boolean> sb = new Signal(false);

                if (i >= 2 && i <= 11) {

                    
                }
            }
        }
    }
    
    

    public TypingManager() {

        super(false);
        this.filter(x -> x == true).onEvent(() -> {

            prevStates.putAll(Input.getKeyMap());
            Input.getKeyMap().clear();
        });
    }
    
    public static boolean getShift(){
        
        return shift;
    }
    
    public static boolean getCaps(){
        
        return caps;
    }
    
    public static boolean getAlt(){
        
        return alt;
    }
}
