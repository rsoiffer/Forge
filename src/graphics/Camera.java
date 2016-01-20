package graphics;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.gluLookAt;
import static org.lwjgl.util.glu.GLU.gluPerspective;
import util.Vec2;
import util.Vec3;

public abstract class Camera {

    public static void calculateViewport(double aspectRatio) {
        int w = Display.getWidth();
        int h = Display.getHeight();
        int vw, vh;
        if (w > h * aspectRatio) {
            vh = h;
            vw = (int) (h * aspectRatio);
        } else {
            vw = w;
            vh = (int) (w / aspectRatio);
        }
        int left = (w - vw) / 2;
        int bottom = (h - vh) / 2;
        glViewport(left, bottom, vw, vh);
    }

    public static void setDisplayMode(int width, int height, boolean fullscreen) {
        // return if requested DisplayMode is already set
        if ((Display.getDisplayMode().getWidth() == width)
                && (Display.getDisplayMode().getHeight() == height)
                && (Display.isFullscreen() == fullscreen)) {
            return;
        }
        try {
            DisplayMode targetDisplayMode = null;
            if (fullscreen) {
                DisplayMode[] modes = Display.getAvailableDisplayModes();
                int freq = 0;
                for (DisplayMode current : modes) {
                    if ((current.getWidth() == width) && (current.getHeight() == height)) {
                        if ((targetDisplayMode == null) || (current.getFrequency() >= freq)) {
                            if ((targetDisplayMode == null) || (current.getBitsPerPixel() > targetDisplayMode.getBitsPerPixel())) {
                                targetDisplayMode = current;
                                freq = targetDisplayMode.getFrequency();
                            }
                        }
                        // if we've found a match for bpp and frequence against the
                        // original display mode then it's probably best to go for this one
                        // since it's most likely compatible with the monitor
                        if ((current.getBitsPerPixel() == Display.getDesktopDisplayMode().getBitsPerPixel())
                                && (current.getFrequency() == Display.getDesktopDisplayMode().getFrequency())) {
                            targetDisplayMode = current;
                            break;
                        }
                    }
                }
            } else {
                targetDisplayMode = new DisplayMode(width, height);
            }
            if (targetDisplayMode == null) {
                System.out.println("Failed to find value mode: " + width + "x" + height + " fs=" + fullscreen);
                //Log.print("Failed to find value mode: " + width + "x" + height + " fs=" + fullscreen);
                return;
            }
            Display.setDisplayMode(targetDisplayMode);
            Display.setFullscreen(fullscreen);
        } catch (LWJGLException e) {
            System.out.println("Unable to setup mode " + width + "x" + height + " fullscreen=" + fullscreen + e);
            //Log.print("Unable to setup mode " + width + "x" + height + " fullscreen=" + fullscreen + e);
        }
    }

    public static void setProjection2D(Vec2 LL, Vec2 UR) {
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(LL.x, UR.x, LL.y, UR.y, -1, 1);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();

        glDisable(GL_ALPHA_TEST);
        glDisable(GL_LIGHTING);
    }

    public static void setProjection3D(double fov, double aspectRatio, Vec3 pos, Vec3 lookAt, Vec3 UP) {
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        gluPerspective((float) fov, (float) aspectRatio, 0.1f, 1000);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        gluLookAt((float) pos.x, (float) pos.y, (float) pos.z,
                (float) lookAt.x, (float) lookAt.y, (float) lookAt.z,
                (float) UP.x, (float) UP.y, (float) UP.z);

        //glEnable(GL_LIGHTING);
        glEnable(GL_ALPHA_TEST);
    }
}
