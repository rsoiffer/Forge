package particles.drawers;

import java.util.function.Function;
import static org.lwjgl.opengl.GL11.*;
import particles.Particle;
import particles.ParticleEmitter.Drawer;
import util.Color4;

public class PointDrawer<P extends Particle> implements Drawer<P> {

    private final int blendMode;
    private final int size;
    private final Function<P, Color4> color;
    private final boolean solidColor;

    public PointDrawer(int blendMode, int size, Function<P, Color4> color) {
        this.color = color;
        this.blendMode = blendMode;
        this.size = size;
        solidColor = false;
    }

    public PointDrawer(int blendMode, int size, Color4 color) {
        this.color = p -> color;
        this.blendMode = blendMode;
        this.size = size;
        solidColor = true;
    }

    @Override
    public void begin() {
        glPointSize(size);
        glBlendFunc(GL_SRC_ALPHA, blendMode);
        glDisable(GL_TEXTURE_2D);
        glBegin(GL_POINTS);
        if (solidColor) {
            color.apply(null).glColor();
        }
    }

    @Override
    public void draw(P p) {
        if (!solidColor) {
            color.apply(p).glColor();
        }
        p.pos.glVertex();
    }

    @Override
    public void end() {
        glEnd();
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }
}
