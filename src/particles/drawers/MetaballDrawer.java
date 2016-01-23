package particles.drawers;

import graphics.Camera;
import graphics.Graphics2D;
import graphics.Window2D;
import graphics.data.Shader;
import graphics.data.Texture;
import graphics.loading.SpriteContainer;
import static org.lwjgl.opengl.EXTFramebufferObject.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL14.GL_DEPTH_COMPONENT24;
import particles.Particle;
import particles.ParticleEmitter.Drawer;
import util.Color4;
import static util.Color4.WHITE;
import util.Vec2;

public class MetaballDrawer implements Drawer {

    private static final Shader metaball = new Shader("screen");
    private static final Texture texture = SpriteContainer.loadSprite("metaball2");

    private final int framebufferID, colorTextureID, depthRenderBufferID;

    private final Color4 color;
    private final double size;
    private final Runnable additional;

    public MetaballDrawer(Color4 color, double size, Runnable additional) {
        this.color = color;
        this.size = size;
        this.additional = additional;

        //int width = 1200;
        //int height = 800;
        int width = 2048, height = 2048;

        framebufferID = glGenFramebuffersEXT();                                         // create a new framebuffer
        colorTextureID = glGenTextures();                                               // and a new texture used as a color buffer
        depthRenderBufferID = glGenRenderbuffersEXT();                                  // And finally a new depthbuffer

        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, framebufferID);                        // switch to the new framebuffer

        // initialize color texture
        glBindTexture(GL_TEXTURE_2D, colorTextureID);                                   // Bind the colorbuffer texture
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);               // make it linear filterd
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL_INT, (java.nio.ByteBuffer) null);  // Create the texture data
        glFramebufferTexture2DEXT(GL_FRAMEBUFFER_EXT, GL_COLOR_ATTACHMENT0_EXT, GL_TEXTURE_2D, colorTextureID, 0); // attach it to the framebuffer

        // initialize depth renderbuffer
        glBindRenderbufferEXT(GL_RENDERBUFFER_EXT, depthRenderBufferID);                // bind the depth renderbuffer
        glRenderbufferStorageEXT(GL_RENDERBUFFER_EXT, GL_DEPTH_COMPONENT24, width, height); // get the data space for it
        glFramebufferRenderbufferEXT(GL_FRAMEBUFFER_EXT, GL_DEPTH_ATTACHMENT_EXT, GL_RENDERBUFFER_EXT, depthRenderBufferID); // bind it to the renderbuffer

        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);
    }

    @Override
    public void begin() {
        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, framebufferID);
        glViewport(0, 0, 2048, 2048);

        glClearColor(0, 0, 0, 1);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        WHITE.glColor();

        glBlendFunc(GL_SRC_ALPHA, GL_ONE);
        glEnable(GL_TEXTURE_2D);
        texture.bind();
        glBegin(GL_QUADS);
    }

    @Override
    public void draw(Particle p) {
        Vec2 size = new Vec2(this.size);
        Graphics2D.drawSpriteFast(texture, p.pos.subtract(size), p.pos.add(size.multiply(new Vec2(1, -1))),
                p.pos.add(size), p.pos.add(size.multiply(new Vec2(-1, 1))));
    }

    @Override
    public void end() {
        glEnd();
        additional.run();

        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);
        Camera.calculateViewport(Window2D.aspectRatio());

        Shader.pushShader(metaball);
        glEnable(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, colorTextureID);
        color.glColor();

        glBegin(GL_QUADS);
        glTexCoord2d(0, 0);
        Window2D.LL().glVertex();
        glTexCoord2d(0, 1);
        Window2D.UL().glVertex();
        glTexCoord2d(1, 1);
        Window2D.UR().glVertex();
        glTexCoord2d(1, 0);
        Window2D.LR().glVertex();
        glEnd();
        Shader.popShader();
    }
}
