package graphics;

import engine.Core;
import engine.Input;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import static org.lwjgl.opengl.GL11.*;
import util.Color4;
import static util.Color4.WHITE;
import util.Vec2;
import static util.Vec2.ZERO;

public abstract class Window2D {

    public static Vec2 viewPos;
    public static Vec2 viewSize;
    public static Color4 background;

    public static void initialize(int width, int height, String title) {
        viewSize = new Vec2(width, height);
        viewPos = ZERO;
        boolean startFullscreen = false;

        background = WHITE;

        try {
            //Display Init
            Camera.setDisplayMode(width, height, startFullscreen);
            //Display.setVSyncEnabled(true);
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

        Input.whenKey(Keyboard.KEY_F11, true).onEvent(() -> Camera.setDisplayMode(width, height, !Display.isFullscreen()));
        Input.whenKey(Keyboard.KEY_ESCAPE, true).onEvent(() -> System.exit(0));
        Core.update.onEvent(() -> update());
    }

    public static void update() {
        Camera.calculateViewport(aspectRatio());
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

    public static Vec2 LR() {
        return viewPos.add(viewSize.multiply(new Vec2(.5, -.5)));
    }

    public static boolean nearInView(Vec2 pos, Vec2 buffer) {
        return pos.containedBy(LL().subtract(buffer), UR().add(buffer));
    }

    public static Vec2 UR() {
        return viewPos.add(viewSize.multiply(.5));
    }

    public static Vec2 UL() {
        return viewPos.add(viewSize.multiply(new Vec2(-.5, .5)));
    }
}
