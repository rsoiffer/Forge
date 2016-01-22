package graphics.data;

import engine.Core;
import engine.Signal;
import graphics.Camera;
import java.nio.ByteBuffer;
import org.lwjgl.opengl.Display;
import static org.lwjgl.opengl.EXTFramebufferObject.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL14.GL_DEPTH_COMPONENT24;
import static util.Color4.WHITE;
import util.Pair;
import util.Vec2;

public class Framebuffer {

    private int framebufferID;
    private int colorTextureID;
    private int depthRenderBufferID;

    public Framebuffer() {
        framebufferID = glGenFramebuffersEXT();
        colorTextureID = glGenTextures();
        depthRenderBufferID = glGenRenderbuffersEXT();

        displaySize().doForEach(this::reset);
    }

    public void bindTexture() {
        glBindTexture(GL_TEXTURE_2D, colorTextureID);
    }

    public static void clear() {
        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);
    }

    public static Signal<Pair> displaySize() {
        return Core.render.toSignal(() -> new Pair(Display.getWidth(), Display.getHeight())).distinct();
    }

    public void draw() {
        Camera.calculateViewport((double) Display.getWidth() / Display.getHeight());
        Camera.setProjection2D(new Vec2(0), new Vec2(1));

        bindTexture();
        WHITE.glColor();
        glEnable(GL_TEXTURE_2D);
        glBegin(GL_QUADS);
        new Vec2(0, 0).glTexCoord();
        new Vec2(0, 0).glVertex();
        new Vec2(1, 0).glTexCoord();
        new Vec2(1, 0).glVertex();
        new Vec2(1, 1).glTexCoord();
        new Vec2(1, 1).glVertex();
        new Vec2(0, 1).glTexCoord();
        new Vec2(0, 1).glVertex();
        glEnd();
    }

    public void enable() {
        glBindTexture(GL_TEXTURE_2D, 0);
        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, framebufferID);
    }

    public void reset(Pair size) {
        enable();

        glBindTexture(GL_TEXTURE_2D, colorTextureID);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, size.x, size.y, 0, GL_RGBA, GL_INT, (ByteBuffer) null);
        glFramebufferTexture2DEXT(GL_FRAMEBUFFER_EXT, GL_COLOR_ATTACHMENT0_EXT, GL_TEXTURE_2D, colorTextureID, 0);

        glBindRenderbufferEXT(GL_RENDERBUFFER_EXT, depthRenderBufferID);
        glRenderbufferStorageEXT(GL_RENDERBUFFER_EXT, GL_DEPTH_COMPONENT24, size.x, size.y);
        glFramebufferRenderbufferEXT(GL_FRAMEBUFFER_EXT, GL_DEPTH_ATTACHMENT_EXT, GL_RENDERBUFFER_EXT, depthRenderBufferID);

        clear();
    }
}
