package particles.drawers;

import graphics.Graphics2D;
import java.util.function.Function;
import static org.lwjgl.opengl.GL11.*;
import particles.Particle;
import particles.ParticleEmitter.Drawer;
import util.Color4;
import util.Vec2;

public class CircleDrawer<P extends Particle> implements Drawer<P> {

    private final int blendMode;
    private final Function<P, Color4> color;
    private final Function<P, Double> size;

    public CircleDrawer(int blendMode, Function<P, Color4> color, Function<P, Double> size) {
        this.blendMode = blendMode;
        this.color = color;
        this.size = size;
    }

    @Override
    public void begin() {
        glBlendFunc(GL_SRC_ALPHA, blendMode);
        glDisable(GL_TEXTURE_2D);
    }

    @Override
    public void draw(P p) {
        Graphics2D.fillEllipse(p.pos, new Vec2(size.apply(p)), color.apply(p), 20);
    }

    @Override
    public void end() {
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }
}
