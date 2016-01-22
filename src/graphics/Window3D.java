package graphics;

import engine.Core;
import engine.Input;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import static org.lwjgl.opengl.EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.opengl.PixelFormat;
import util.Color4;
import static util.Color4.BLACK;
import util.Vec2;
import util.Vec3;
import static util.Vec3.ZERO;
import util.Vec3Polar;

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
            Display.create(new PixelFormat(8, 8, 0, 8));
            //OpenGL Init
            glEnable(GL_BLEND);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            //3D
            glEnable(GL_DEPTH_TEST);
            glDepthFunc(GL_LEQUAL);
            glAlphaFunc(GL_GREATER, 0.5f);

            glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);

            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAX_ANISOTROPY_EXT, 16);
        } catch (LWJGLException ex) {
            ex.printStackTrace();
        }

        Input.whenKey(Keyboard.KEY_F11, true).onEvent(() -> Camera.setDisplayMode(width, height, !Display.isFullscreen()));
        Input.whenKey(Keyboard.KEY_ESCAPE, true).onEvent(() -> System.exit(0));
        Core.render.onEvent(() -> update());
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
        background.glClearColor();
    }
}
