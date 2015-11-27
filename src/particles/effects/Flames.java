package particles.effects;

import core.AbstractEntity;
import core.Core;
import core.Signal;
import static org.lwjgl.opengl.GL11.GL_ONE;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import particles.LifeTimeParticle;
import particles.ParticleEmitter;
import particles.drawers.PointDrawer;
import particles.drawers.SpriteDrawer;
import util.Color4;
import util.Noise;
import util.Util;
import util.Vec2;
import static util.Vec2.ZERO;

public class Flames extends AbstractEntity {

    public Vec2 position = ZERO;
    public int width = 500;
    public double heightMult = 1;

    @Override
    public void create() {
        //Simplex noise
        Noise n = new Noise(0);
        Signal<Double> offset = Core.time().map(t -> n.multi(t, 0, 1, 2));

        //Smoke
        ParticleEmitter<LifeTimeParticle> smoke = new ParticleEmitter();
        smoke.drawer = new SpriteDrawer<>(GL_ONE_MINUS_SRC_ALPHA, p -> new Color4(.2, .2, .2, p.percTimeRemaining()), p -> new Vec2(20 + 20 * p.lifeTime), "smoke");
        smoke.onUpdate.add((dt, p) -> p.vel = p.vel.add(Vec2.randomSquare(240 * dt)));
        smoke.create();
        Core.interval(.1).onEvent(() -> Util.repeat(width / 25, i -> smoke.particles.add(new LifeTimeParticle(
                position.add(new Vec2(Math.random() * width * 2 - width, 0)),
                new Vec2(0, 150), 2 + 4 * Math.random())))).addChild(smoke);

        //Flames
        ParticleEmitter<LifeTimeParticle> flames = new ParticleEmitter();
        flames.create();
        flames.drawer = new PointDrawer<>(GL_ONE, 4, p -> new Color4(1, .2, .05, p.percTimeRemaining()));
        Signal<Double> time = Core.time();
        Core.update.forEach(dt -> Util.repeat((int) (width * dt * 120), () -> {
            double x = Math.random() * 2 * width - width;
            Vec2 pos = position.add(new Vec2(x, 0));
            double xSpeed = 150 * n.multi(time.get() * 100, x, 4, .01);
            Vec2 vel = new Vec2(xSpeed, 400 - 2 * Math.abs(xSpeed)).multiply(Math.random());
            flames.particles.add(new LifeTimeParticle(pos, vel, 1));
        })).addChild(flames);

        //Sparks
        ParticleEmitter<LifeTimeParticle> sparks = new ParticleEmitter();
        sparks.create();
        sparks.drawer = new PointDrawer<>(GL_ONE, 4, p -> new Color4(1, .2, .05, p.percTimeRemaining()));
        sparks.onUpdate.add((dt, p) -> p.vel = p.vel.add(Vec2.randomSquare(3600 * dt)));
        Core.interval(.1).onEvent(() -> Util.repeat(width / 5, i -> sparks.particles.add(new LifeTimeParticle(position.add(new Vec2(i * 10 - width, 0)),
                Vec2.randomCircle(150).add(new Vec2(0, 400)), .5 + Math.random())))).addChild(sparks);

        add(smoke, flames, sparks);
    }
}
