package engine;

import graphics.Window2D;
import graphics.Window3D;
import java.util.HashMap;
import java.util.Map;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import util.Vec2;
import static util.Vec2.ZERO;

public class Input {

    private static final Map<Integer, Signal<Boolean>> mouseMap = new HashMap();
    private static final Map<Integer, Signal<Boolean>> keyMap = new HashMap();

    public static final Signal<Integer> mouseWheel = new Signal<>(0);

    private static Vec2 mouse = ZERO;
    private static Vec2 mouseDelta = ZERO;
    private static Vec2 mouseScreen = ZERO;

    static {
        Core.update.onEvent(() -> {
            while (Keyboard.next()) {
                int key = Keyboard.getEventKey();
                if (!keyMap.containsKey(key)) {
                    keyMap.put(key, new Signal<>(Keyboard.getEventKeyState()));
                } else {
                    keyMap.get(key).set(Keyboard.getEventKeyState());
                }
            }

            while (Mouse.next()) {
                int button = Mouse.getEventButton();
                if (!mouseMap.containsKey(button)) {
                    mouseMap.put(button, new Signal<>(Mouse.getEventButtonState()));
                } else {
                    mouseMap.get(button).set(Mouse.getEventButtonState());
                }
            }

            int dWheel = Mouse.getDWheel();
            if (dWheel != 0) {
                mouseWheel.set(dWheel);
            }

            //Calculate mouse position
            double w = Display.getWidth();
            double h = Display.getHeight();
            double ar = Core.is3D ? Window3D.aspectRatio : Window2D.aspectRatio();
            double vw, vh;

            if (w / h > ar) {
                vw = ar * h;
                vh = h;
            } else {
                vw = w;
                vh = w / ar;
            }
            double left = (w - vw) / 2;
            double bottom = (h - vh) / 2;

            mouseScreen = new Vec2((Mouse.getX() - left) / vw, (Mouse.getY() - bottom) / vh).multiply(new Vec2(1200, 800));
            mouse = Core.is3D ? mouseScreen : new Vec2((Mouse.getX() - left) / vw, (Mouse.getY() - bottom) / vh).multiply(Window2D.viewSize)
                    .subtract(Window2D.viewSize.multiply(.5)).add(Window2D.viewPos);
            mouseDelta = new Vec2(Mouse.getDX() / vw, Mouse.getDY() / vh).multiply(Core.is3D ? new Vec2(1) : Window2D.viewSize);
        });
    }

    public static Vec2 getMouse() {
        return mouse;
    }

    public static Vec2 getMouseDelta() {
        return mouseDelta;
    }

    public static Vec2 getMouseScreen() {
        return mouseScreen;
    }

    public static Signal<Boolean> keySignal(int key) {
        if (!keyMap.containsKey(key)) {
            keyMap.put(key, new Signal<>(false));
        }
        return keyMap.get(key);
    }

    public static Signal<Boolean> mouseSignal(int button) {
        if (!mouseMap.containsKey(button)) {
            mouseMap.put(button, new Signal<>(false));
        }
        return mouseMap.get(button);
    }

    public static EventStream whenKey(int key, boolean val) {
        return keySignal(key).filter(x -> x == val);
    }

    public static EventStream whenMouse(int button, boolean val) {
        return mouseSignal(button).filter(x -> x == val);
    }

    public static Signal<Double> whileKeyDown(int key) {
        return Core.update.filter(keySignal(key));
    }

    public static Signal<Double> whileMouseDown(int mouse) {
        return Core.update.filter(mouseSignal(mouse));
    }

}
