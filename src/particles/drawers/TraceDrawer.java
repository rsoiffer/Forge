package particles.drawers;

import java.util.function.Function;
import static org.lwjgl.opengl.GL11.*;
import particles.ParticleEmitter.Drawer;
import particles.TraceParticle;
import util.Color4;
import util.Vec2;

public class TraceDrawer<P extends TraceParticle> implements Drawer<P> {

    private final int blendMode;
    private final Function<P, Color4> color;

    public TraceDrawer(int blendMode, Function<P, Color4> color) {
        this.blendMode = blendMode;
        this.color = color;
    }

    @Override
    public void begin() {
        glBlendFunc(GL_SRC_ALPHA, blendMode);
        glDisable(GL_TEXTURE_2D);
    }

    @Override
    public void draw(P p) {
        glBegin(GL_LINE_STRIP);
        color.apply(p).glColor();
        p.pastPositions.forEach(Vec2::glVertex);
        glEnd();
        //color
    }

    @Override
    public void end() {
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }
}
