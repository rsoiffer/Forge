package game;

import core.AbstractEntity;
import game.Particles.Particle;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.function.Consumer;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_POINTS;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glEnd;

public class ParticleEmitter extends AbstractEntity {

    public final Collection<Particle> particles = new LinkedList();

    public int blendMode = GL_ONE_MINUS_SRC_ALPHA;

    public Consumer<Particle> onUpdate = p -> {
    };
    public Consumer<Particle> onRemove = p -> {
    };

    @Override
    public void create() {
        onUpdate(dt -> {
            glBlendFunc(GL_SRC_ALPHA, blendMode);
            Iterator<Particle> i = particles.iterator();
            glBegin(GL_POINTS);
            while (i.hasNext()) {
                Particle p = i.next();

                p.pos = p.pos.add(p.vel.multiply(dt));
                p.lifeTime += dt;
                if (p.maxLifeTime > 0 && p.lifeTime > p.maxLifeTime) {
                    p.shouldRemove = true;
                }
                onUpdate.accept(p);

                if (p.shouldRemove) {
                    i.remove();
                    onRemove.accept(p);
                }

                p.color.glColor();
                p.pos.glVertex();
            }
            glEnd();
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        });
    }
}
