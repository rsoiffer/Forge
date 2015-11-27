package particles.drawers;

import java.util.function.Function;
import static org.lwjgl.opengl.GL11.*;
import particles.Particle;
import particles.ParticleEmitter.Drawer;
import util.Color4;

public class LineDrawer<P extends Particle> implements Drawer<P> {

    private final int blendMode;
    private final Function<P, Color4> color;
    private final boolean solidColor;

    public LineDrawer(int blendMode, Function<P, Color4> color) {
        this.color = color;
        this.blendMode = blendMode;
        solidColor = false;
    }

    public LineDrawer(int blendMode, Color4 color) {
        this.color = p -> color;
        this.blendMode = blendMode;
        solidColor = true;
    }

    @Override
    public void begin() {
        glBlendFunc(GL_SRC_ALPHA, blendMode);
        glDisable(GL_TEXTURE_2D);
        glBegin(GL_LINE_LOOP);
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
