package graphics.data;

import engine.Core;
import graphics.Camera;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import static org.lwjgl.opengl.ARBColorBufferFloat.*;
import org.lwjgl.opengl.Display;
import static org.lwjgl.opengl.EXTFramebufferObject.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL14.GL_DEPTH_COMPONENT24;
import static org.lwjgl.opengl.GL30.*;
import util.Color4;
import static util.Color4.WHITE;
import util.Pair;
import util.Vec2;
 
public class Framebuffer {

    //Stack
    private static final Stack<Framebuffer> FRAMEBUFFER_STACK = new Stack();

    public static void popFramebuffer() {
        FRAMEBUFFER_STACK.pop().disable();
        if (FRAMEBUFFER_STACK.isEmpty()) {
            glBindFramebufferEXT(GL_FRAMEBUFFER, 0);
        } else {
            FRAMEBUFFER_STACK.peek().enable();
        }
    }

    public static void pushFramebuffer(Framebuffer fb) {
        if (!FRAMEBUFFER_STACK.isEmpty()) {
            FRAMEBUFFER_STACK.peek().disable();
        }
        FRAMEBUFFER_STACK.push(fb);
        fb.enable();
    }

    //Framebuffers
    private int id;
    private final List<FramebufferAttachment> attachments;

    public Framebuffer(Vec2 scale, FramebufferAttachment... as) {
        attachments = new LinkedList((Arrays.asList(as)));

        id = glGenFramebuffersEXT();
        Core.render.map(() -> new Pair((int) (Display.getWidth() * scale.x), (int) (Display.getHeight() * scale.y))).distinct().doForEach(p -> {
            pushFramebuffer(this);
            attachments.forEach(a -> a.create(p));
            popFramebuffer();
        });
    }

    public Framebuffer(FramebufferAttachment... as) {
        this(new Vec2(1), as);
    }

    public void clear(Color4 color) {
        with(() -> {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            color.glClearColor();
        });
    }

    public void destroy() {
        glDeleteFramebuffersEXT(id);
        attachments.forEach(FramebufferAttachment::destroy);
    }

    private void disable() {
        attachments.forEach(FramebufferAttachment::disable);
    }

    private void enable() {
        glBindFramebufferEXT(GL_FRAMEBUFFER, id);
        attachments.forEach(FramebufferAttachment::enable);
    }

    public void render() {
        Camera.calculateViewport((double) Display.getWidth() / Display.getHeight());
        Camera.setProjection2D(new Vec2(0), new Vec2(1));

        attachments.forEach(FramebufferAttachment::preRender);

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

        attachments.forEach(FramebufferAttachment::postRender);
    }

    public void with(Runnable r) {
        if (!FRAMEBUFFER_STACK.isEmpty() && FRAMEBUFFER_STACK.peek() == this) {
            r.run();
        } else {
            pushFramebuffer(this);
            r.run();
            popFramebuffer();
        }
    }

    //Attachments
    public static interface FramebufferAttachment {

        public void create(Pair size);

        public default void destroy() {
        }

        public default void disable() {
        }

        public default void enable() {
        }

        public default void postRender() {
        }

        public default void preRender() {
        }
    }

    public static class DepthAttachment implements FramebufferAttachment {

        private int id;

        @Override
        public void create(Pair size) {
            id = glGenRenderbuffersEXT();
            glBindRenderbufferEXT(GL_RENDERBUFFER_EXT, id);
            glRenderbufferStorageEXT(GL_RENDERBUFFER_EXT, GL_DEPTH_COMPONENT24, size.x, size.y);
            glBindRenderbufferEXT(GL_RENDERBUFFER_EXT, 0);
            glFramebufferRenderbufferEXT(GL_FRAMEBUFFER_EXT, GL_DEPTH_ATTACHMENT_EXT, GL_RENDERBUFFER_EXT, id);
        }
    }

    public static class HDRTextureAttachment implements FramebufferAttachment {

        private int id;

        @Override
        public void create(Pair size) {
            glClampColorARB(GL_CLAMP_VERTEX_COLOR_ARB, GL_FALSE);
            glClampColorARB(GL_CLAMP_READ_COLOR_ARB, GL_FALSE);
            glClampColorARB(GL_CLAMP_FRAGMENT_COLOR_ARB, GL_FALSE);

            id = glGenTextures();
            glBindTexture(GL_TEXTURE_2D, id);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA16F, size.x, size.y, 0, GL_RGBA, GL_FLOAT, (ByteBuffer) null);
            glBindTexture(GL_TEXTURE_2D, 0);
            glFramebufferTexture2DEXT(GL_FRAMEBUFFER_EXT, GL_COLOR_ATTACHMENT0_EXT, GL_TEXTURE_2D, id, 0);
        }

        @Override
        public void postRender() {
            glBindTexture(GL_TEXTURE_2D, 0);
        }

        @Override
        public void preRender() {
            glBindTexture(GL_TEXTURE_2D, id);
        }
    }

    public static class TextureAttachment implements FramebufferAttachment {

        private int id;

        @Override
        public void create(Pair size) {
            id = glGenTextures();
            glBindTexture(GL_TEXTURE_2D, id);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, size.x, size.y, 0, GL_RGBA, GL_INT, (ByteBuffer) null);
            glBindTexture(GL_TEXTURE_2D, 0);
            glFramebufferTexture2DEXT(GL_FRAMEBUFFER_EXT, GL_COLOR_ATTACHMENT0_EXT, GL_TEXTURE_2D, id, 0);
        }

        @Override
        public void postRender() {
            glBindTexture(GL_TEXTURE_2D, 0);
        }

        @Override
        public void preRender() {
            glBindTexture(GL_TEXTURE_2D, id);
        }
    }
}