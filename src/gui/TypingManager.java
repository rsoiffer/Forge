/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import engine.Input;
import engine.Signal;
import gui.types.ComponentInputGUI;
import gui.types.GUIButtonComponent;
import gui.types.GUIInputComponent;
import gui.types.GUITypingComponent;
import java.awt.Toolkit;
import static java.awt.event.KeyEvent.VK_CAPS_LOCK;
import java.util.HashMap;
import java.util.Map;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import util.Vec2;

/**
 *
 * @author Cruz
 */
public class TypingManager extends Signal<Boolean> {

    private static final Map<Integer, Signal<Boolean>> prevStates = new HashMap();
    private static TypingManager typeM;
    static String buffer = "";
    private static ComponentInputGUI input = null;
    private static GUIInputComponent comp = null;

    private static Signal<Boolean> shift = new Signal(false);
    private static Signal<Boolean> ctrl = new Signal(false);
    private static Signal<Boolean> alt = new Signal(false);
    private static Signal<Boolean> caps = new Signal(Toolkit.getDefaultToolkit().getLockingKeyState(VK_CAPS_LOCK));

    private static final Map<Integer, Signal<Boolean>> convert;

    //14=backspace 15=tab 28=enter 29=L Ctrl 41-` or ~ 42=L Shift 43=\ 56=L Alt 59-68=F1-F10 87-88=F11-F12
    private static final Character regKeys[];
    private static final Character shiftKeys[];

    static {

        convert = new HashMap();

        regKeys = new Character[]{null, null, '1', '2', '3', '4',
            '5', '6', '7', '8', '9', '0', '-', '=', null, null, 'q', 'w',
            'e', 'r', 't', 'y', 'u', 'i', 'o', 'p', '[', ']', null, null, 'a', 's',
            'd', 'f', 'g', 'h', 'j', 'k', 'l', ';', '\'', null, null, '\\', 'z', 'x',
            'c', 'v', 'b', 'n', 'm', ',', '.', '/', null, '*', null, ' ', null, null,
            null, null, null, null, null, null, null, null, null, null, null, '7',
            '8', '9', '-', '4', '5', '6', '+', '1', '2', '3', '0', '.', null, null,
            null, null, null};

        shiftKeys = new Character[]{null, null, '!', '@', '#', '$',
            '%', '^', '&', '*', '(', ')', '_', '+', null, null, 'Q', 'W',
            'E', 'R', 'T', 'Y', 'U', 'I', 'O', 'P', '{', '}', null, null, 'A', 'S',
            'D', 'F', 'G', 'H', 'J', 'K', 'L', ':', '"', null, null, '|', 'Z', 'X',
            'C', 'V', 'B', 'N', 'M', '<', '>', '?', null, '*', null, ' ', null, null,
            null, null, null, null, null, null, null, null, null, null, null, '7',
            '8', '9', '-', '4', '5', '6', '+', '1', '2', '3', '0', '.', null, null,
            null, null, null};

        for (int i = 0; i <= 88; i++) {

            if (regKeys[i] != null && shiftKeys[i] != null) {

                convert.put(i, new TypeKey(regKeys[i], shiftKeys[i], regKeys[i] >= 0x61 && regKeys[i] <= 0x7B));
            }
        }
    }

    public TypingManager(ComponentInputGUI ti) {

        super(false);
        input = ti;

        this.filter(x -> x == true).onEvent(() -> {

            Map<Integer, Signal<Boolean>> in = Input.getKeyMap();
            prevStates.putAll(in);
            in.clear();
            in.putAll(convert);

            Input.whenMouse(0, true).onEvent(() -> {

                compClose(comp);

                if (input != null) {

                    GUIInputComponent gip = input.mousePressed(Input.getMouse());
                    comp = gip;
                    compOpen(comp);
                }
            });

            Input.whenKey(1, true).onEvent(() -> { //Esc code 1

                input.setVisible(false);
                Mouse.setGrabbed(true);
                typeM.set(false);
                buffer = "";
            });

            Input.whenKey(Keyboard.KEY_RETURN, true).onEvent(() -> {

                if (comp != null) {

                    comp.send();
                }
            });

            shift = Input.keySignal(Keyboard.KEY_LSHIFT);

            alt = Input.keySignal(56); //Alt code 56

            caps = Input.whenKey(Keyboard.KEY_CAPITAL, true).reduce(
                    Toolkit.getDefaultToolkit().getLockingKeyState(VK_CAPS_LOCK), b -> !b);

            Input.whenKey(14, true).onEvent(() -> { //Backspace code 14

                if (comp instanceof GUITypingComponent) {

                    ((GUITypingComponent) comp).backspace();
                }
            });

            Input.whenKey(Keyboard.KEY_TAB, true).onEvent(() -> {

                if (comp instanceof GUITypingComponent) {

                    ((GUITypingComponent) comp).tab();
                }
            });

            Input.whenKey(Keyboard.KEY_UP, true).onEvent(() -> {

                if (comp instanceof GUITypingComponent) {

                    ((GUITypingComponent) comp).up();
                }
            });
            
            Input.whenKey(Keyboard.KEY_DOWN, true).onEvent(() -> {

                if (comp instanceof GUITypingComponent) {

                    ((GUITypingComponent) comp).down();
                }
            });
            
            Input.whenKey(Keyboard.KEY_LEFT, true).onEvent(() -> {

                if (comp instanceof GUITypingComponent) {

                    ((GUITypingComponent) comp).left();
                }
            });
            
            Input.whenKey(Keyboard.KEY_RIGHT, true).onEvent(() -> {

                if (comp instanceof GUITypingComponent) {

                    ((GUITypingComponent) comp).right();
                }
            });
        });

        this.filter(x -> x == false).onEvent(() -> {

            Map<Integer, Signal<Boolean>> in = Input.getKeyMap();
            in.clear();
            in.putAll(prevStates);
            prevStates.clear();
        });

        comp = input.getDefaultComponent();
        typeM = this;
    }

    private static void compOpen(GUIInputComponent gip) {

        if (gip != null) {

            if (gip instanceof GUIButtonComponent) {

                gip.send();
            } else if (gip instanceof GUITypingComponent) {

                ((GUITypingComponent) gip).DrawCursor(true);
            }
        }
    }

    private static void compClose(GUIInputComponent gip) {

        if (gip != null) {

            if (gip instanceof GUITypingComponent) {

                ((GUITypingComponent) gip).DrawCursor(false);
            }
        }
    }

    public static void typing(ComponentInputGUI ti, boolean b) {

        compClose(comp);

        if (ti != null) {

            input = ti;
            typeM.set(b);
            comp = input.getDefaultComponent();

            if (b) {

                compOpen(comp);
            }
        }
    }

    public static void typing(boolean b) {

        typeM.set(b);

        if (b) {

            compOpen(comp);
        } else {

            compClose(comp);
        }
    }

    public static void typing(ComponentInputGUI ti, boolean b, String s) {

        compClose(comp);

        if (ti != null) {

            input = ti;
            typeM.set(b);
            comp = input.getDefaultComponent();
            buffer = s;

            if (b) {

                compOpen(comp);
            }
        }
    }

    public static boolean isTyping() {

        return typeM.get();
    }

    public static void clearTyped() {

        buffer = "";
    }

    public static String getTyped() {

        return buffer;
    }

    public static void typingLimit(int lim) {

        if (buffer.length() > lim) {

            buffer = buffer.substring(0, lim);
        }
    }

    public static void addChar(char c) {

        buffer += c;
    }

    public static boolean getShift() {

        return shift.get();
    }

    public static boolean getCaps() {

        return caps.get();
    }

    public static boolean getAlt() {

        return alt.get();
    }
}
