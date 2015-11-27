package particles;

import core.AbstractEntity;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ParticleEmitter<P extends Particle> extends AbstractEntity {

    public final Collection<P> particles = new LinkedList();

    public Drawer<P> drawer;
    public List<BiConsumer<Double, P>> onUpdate = new LinkedList();
    public List<Consumer<P>> onRemove = new LinkedList();

    @Override
    public void create() {
        onUpdate(dt -> {
            Iterator<P> i = particles.iterator();
            while (i.hasNext()) {
                P p = i.next();
                onUpdate.forEach(u -> u.accept(dt, p));
                p.update(dt);

                if (p.shouldRemove) {
                    i.remove();
                    onRemove.forEach(r -> r.accept(p));
                }
            }
        });
        onRender(() -> {
            drawer.begin();
            particles.forEach(Particle::render);
            particles.forEach(drawer::draw);
            drawer.end();
        });
    }

    public static interface Drawer<P extends Particle> {

        public abstract void begin();

        public abstract void draw(P p);

        public abstract void end();
    }
}
