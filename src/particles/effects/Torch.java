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

public class Torch extends AbstractEntity {

    public Vec2 position = ZERO;

    @Override
    public void create() {
        //Simplex noise
        Noise n = new Noise(Math.random() * 1000);
        Signal<Double> offset = addChild(Core.time().map(t -> n.multi(t, 0, 1, 2)));

        //Smoke
        ParticleEmitter<LifeTimeParticle> smoke = new ParticleEmitter();
        smoke.drawer = new SpriteDrawer<>(GL_ONE_MINUS_SRC_ALPHA, p -> new Color4(.2, .2, .2, p.percTimeRemaining()), p -> new Vec2(20 + 20 * p.lifeTime), "smoke");
        smoke.onUpdate.add((dt, p) -> {
            p.vel = p.vel.add(Vec2.randomSquare(240 * dt));
            p.pos = p.pos.add(new Vec2(dt * 60 * offset.get() * p.percTimeRemaining() * p.percTimeRemaining(), 0));
        });
        smoke.create();
        Core.interval(.1).onEvent(() -> smoke.particles.add(new LifeTimeParticle(position, new Vec2(0, 150), 2 + 8 * Math.random()))).addChild(smoke);

        //Torch
        ParticleEmitter<LifeTimeParticle> torch = new ParticleEmitter();
        torch.create();
        torch.drawer = new PointDrawer<>(GL_ONE, 4, p -> new Color4(1, .2, .05, p.percTimeRemaining()));
        torch.onUpdate.add((dt, p) -> p.vel = p.vel.add(Vec2.randomSquare(60 * dt)));
        Core.update.forEach(dt -> {
            Util.repeat((int) (6000 * dt), i -> {
                double height = Math.random();
                double randomness = 1 - height;
                torch.particles.add(new LifeTimeParticle(position.add(Vec2.randomCircle(50 * Math.sqrt(randomness))),
                        new Vec2(offset.get() * 100 * height + (Math.random() - .5) * 200 * randomness, 150).multiply(1 + Math.random() * height), height));
            });
        }).addChild(torch);

        //Sparks
        ParticleEmitter<LifeTimeParticle> sparks = new ParticleEmitter();
        sparks.create();
        sparks.drawer = new PointDrawer<>(GL_ONE, 4, p -> new Color4(1, .2, .05, p.percTimeRemaining()));
        sparks.onUpdate.add((dt, p) -> p.vel = p.vel.add(Vec2.randomSquare(3600 * dt)));
        Core.interval(.1).onEvent(() -> sparks.particles.add(new LifeTimeParticle(position,
                Vec2.randomCircle(150).add(new Vec2(0, 400)), .5 + Math.random()))).addChild(sparks);

        add(torch, smoke, sparks);
    }
}
