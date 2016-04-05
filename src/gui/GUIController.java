package gui;

import graphics.Window3D;
import java.util.ArrayList;
import java.util.Arrays;

public class GUIController {

    private static final ArrayList<GUI> guis = new ArrayList();

    public static void add(GUI... gui) {
        Arrays.asList(gui).forEach(g -> {
            guis.add(g);
        });
    }

    public static void remove(GUI g) {
        if (guis.contains(g)) {
            guis.remove(g);
        }
    }

    public static void draw() {
        Window3D.guiProjection();
        guis.forEach(g -> {
            if (g.isOpen()) {
                g.draw();
            }
        });
        Window3D.resetProjection();
    }

    public static void update() {
        guis.forEach(g -> {
            g.update();
        });
    }

    public static ArrayList<GUI> getGUIList() {
        return guis;
    }

}
