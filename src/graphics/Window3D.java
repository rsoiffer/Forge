package graphics;

import engine.Core;
import engine.Input;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import static org.lwjgl.opengl.GL11.*;
import static util.Color4.BLACK;
import static util.Vec3.ZERO;
import util.*;

public abstract class Window3D {

    public static boolean startFullscreen;
    public static Vec3 pos;
    public static Vec3Polar facing;
    public static double fov, aspectRatio;
    public static Color4 background;
    public static final Vec3 UP = new Vec3(0, 0, 1);

    public static void initialize(int width, int height, String title) {
        startFullscreen = false;
        pos = ZERO;
        facing = new Vec3Polar(1, 0, 0);
        fov = 70;
        aspectRatio = (double) width / height;
        background = BLACK;

        try {
            //Display Init
            Camera.setDisplayMode(width, height, startFullscreen);
            //Display.setVSyncEnabled(true);
            Display.setResizable(true);
            Display.setTitle(title);
            Display.create();
            //OpenGL Init
            glEnable(GL_BLEND);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            //3D
            glEnable(GL_DEPTH_TEST);
            glDepthFunc(GL_LEQUAL);
            glAlphaFunc(GL_GREATER, 0.5f);

            glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);

            glEnable(GL_FOG);
            glFogi(GL_FOG_MODE, GL_LINEAR);
            glFog(GL_FOG_COLOR, Util.floatBuffer(.95, .95, .95, 1));
            glFogf(GL_FOG_DENSITY, 1);
            glHint(GL_FOG_HINT, GL_NICEST);
            glFogf(GL_FOG_START, 0);
            glFogf(GL_FOG_END, 30);
            //----------- Variables & method calls added for Lighting Test -----------//
//            glShadeModel(GL_SMOOTH);
//            glLightModel(GL_LIGHT_MODEL_AMBIENT, Util.floatBuffer(.5, .5, .5, 1));		// global ambient light
//
//            glEnable(GL_COLOR_MATERIAL);								// enables opengl to use glColor3f to define material color
//            glColorMaterial(GL_FRONT, GL_AMBIENT_AND_DIFFUSE);			// tell opengl glColor3f effects the ambient and diffuse properties of material
            //----------- END: Variables & method calls added for Lighting Test -----------//
        } catch (LWJGLException ex) {
            ex.printStackTrace();
        }

        Input.whenKey(Keyboard.KEY_F11, true).onEvent(() -> Camera.setDisplayMode(width, height, !Display.isFullscreen()));
        Input.whenKey(Keyboard.KEY_ESCAPE, true).onEvent(() -> System.exit(0));
        Core.update.onEvent(() -> update());
    }

    public static Vec3 forwards() {
        Vec3 dir = facing.toVec3();
        return dir.subtract(UP.multiply(dir.dot(UP))).normalize();
    }

    public static void guiProjection() {
        Camera.setProjection2D(new Vec2(0), new Vec2(1));
    }

    public static void resetProjection() {
        Camera.setProjection3D(fov, aspectRatio, pos, pos.add(facing.toVec3()), UP);
    }

    public static void update() {
        Camera.calculateViewport(aspectRatio);
        resetProjection();

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glClearColor((float) background.r, (float) background.g, (float) background.b, (float) background.a);
    }
}
