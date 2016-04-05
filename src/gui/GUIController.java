package gui;

import engine.Core;
import graphics.Camera;
import graphics.Window3D;
import java.util.ArrayList;
import java.util.Arrays;
import util.Vec2;

public class GUIController {

    private static final ArrayList<GUI> guis = new ArrayList();
    
    public static void add(GUI... gui){
        Arrays.asList(gui).forEach(g -> {
            guis.add(g);
        });
    }
    public static void remove(GUI g){
        if(guis.contains(g)) guis.remove(g);
    }
    public static void draw(){
        Window3D.guiProjection();
        Camera.setProjection2D(Vec2.ZERO, new Vec2(1200,800));
        guis.forEach(g -> {
            if(g.isOpen()) g.draw();
        });
        Window3D.resetProjection();
    }
    public static void update(){
        guis.forEach(g -> {
            g.update();
        });
    }
    public static ArrayList<GUI> getGUIList(){
        return guis;
    }

}
