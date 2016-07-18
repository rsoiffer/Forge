package particles.effects;

import static org.lwjgl.opengl.GL11.GL_ONE;
import particles.LifeTimeParticle;
import particles.ParticleEmitter;
import particles.drawers.PointDrawer;
import util.Color4;
import util.Util;
import util.Vec2;

public class Explosion extends ParticleEmitter<LifeTimeParticle> {

    /**
     * Position of the explosion, X and Y values
     */
    public Vec2 position;

    /**
     * constructor of the Explosion object, sets the position of the object
     *
     * @param position
     */
    public Explosion(Vec2 position) {
        this.position = position;
    }

    /**
     *
     */
    @Override
    public void create() {
        super.create();
        drawer = new PointDrawer<>(GL_ONE, 4, p -> new Color4(.5, .1, .025, p.percTimeRemaining()));
        onUpdate.add((dt, p) -> p.vel = p.vel.multiply(Math.pow(.0018, dt)));
        Util.repeat(10000, () -> {
            double dist = Math.random();
            particles.add(new LifeTimeParticle(position, Vec2.randomShell(1000 * Math.sqrt(Math.random()) * dist), dist));
        });
        onRemove.add(p -> {
            if (particles.isEmpty()) {
                destroy();
            }
        });
    }
}
