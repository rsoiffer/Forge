package particles.drawers;

import graphics.Graphics2D;
import graphics.Texture;
import graphics.loading.SpriteContainer;
import java.util.function.Function;
import static org.lwjgl.opengl.GL11.*;
import particles.Particle;
import particles.ParticleEmitter.Drawer;
import util.Color4;
import util.Vec2;

public class SpriteDrawer<P extends Particle> implements Drawer<P> {

    final int blendMode;
    final Function<P, Color4> color;
    final Function<P, Vec2> size;
    final Texture texture;

    public SpriteDrawer(int blendMode, Function<P, Color4> color, Function<P, Vec2> size, String texture) {
        this.blendMode = blendMode;
        this.color = color;
        this.size = size;
        this.texture = SpriteContainer.loadSprite(texture);
    }

    @Override
    public void begin() {
        glBlendFunc(GL_SRC_ALPHA, blendMode);
        glEnable(GL_TEXTURE_2D);
        texture.bind();
        glBegin(GL_QUADS);
    }

    @Override
    public void draw(P p) {
        Vec2 size = this.size.apply(p);
        color.apply(p).glColor();
        Graphics2D.drawSpriteFast(texture, p.pos.subtract(size), p.pos.add(size.multiply(new Vec2(1, -1))),
                p.pos.add(size), p.pos.add(size.multiply(new Vec2(-1, 1))));
    }

    @Override
    public void end() {
        glEnd();
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }
}
