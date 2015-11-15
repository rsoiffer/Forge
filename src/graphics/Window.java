package graphics;

import util.Input;
import util.Color4;
import util.Vec2;
import core.Core;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import static org.lwjgl.opengl.GL11.*;

public abstract class Window {

    public static Vec2 viewPos;
    public static Vec2 viewSize;
    public static Color4 background;

    public static void initialize(int width, int height, String title) {
        viewSize = new Vec2(width, height);
        viewPos = new Vec2();
        boolean startFullscreen = false;
        background = Color4.WHITE;

        try {
            //Display Init
            Camera.setDisplayMode(viewSize, startFullscreen);
            Display.setVSyncEnabled(true);
            Display.setResizable(true);
            Display.setTitle(title);
            Display.create();
            Keyboard.create();
            Mouse.create();
            //OpenGL Init
            glEnable(GL_BLEND);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            glDisable(GL_DEPTH_TEST);
            glDisable(GL_LIGHTING);

        } catch (LWJGLException ex) {
            ex.printStackTrace();
        }

        Input.whenKey(Keyboard.KEY_F11, true).onEvent(() -> Camera.setDisplayMode(viewSize, !Display.isFullscreen()));
        Input.whenKey(Keyboard.KEY_ESCAPE, true).onEvent(() -> System.exit(0));
        Core.update.onEvent(() -> update());
    }

    public static void update() {
        Camera.calculateViewport(viewSize);
        Camera.setProjection2D(LL(), UR());

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glClearColor(0, 0, 0, 1);

        Graphics2D.fillRect(LL(), viewSize, background);
    }

    //Window utility functions
    public static double aspectRatio() {
        return viewSize.x / viewSize.y;
    }

    public static boolean inView(Vec2 pos) {
        return pos.containedBy(LL(), UR());
    }

    public static Vec2 LL() {
        return viewPos.subtract(viewSize.multiply(.5));
    }

    public static boolean nearInView(Vec2 pos, Vec2 buffer) {
        return pos.containedBy(LL().subtract(buffer), UR().add(buffer));
    }

    public static Vec2 UR() {
        return viewPos.add(viewSize.multiply(.5));
    }
}
