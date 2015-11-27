package particles.effects;

import graphics.Window;
import static org.lwjgl.opengl.GL11.GL_ONE;
import particles.LifeTimeParticle;
import particles.ParticleEmitter;
import particles.drawers.LineDrawer;
import util.Color4;
import util.Util;
import util.Vec2;

public class Sound1 extends ParticleEmitter<LifeTimeParticle> {

    public Vec2 position;

    public Sound1(Vec2 position) {
        this.position = position;
    }

    @Override
    public void create() {
        super.create();
        drawer = new LineDrawer<>(GL_ONE, new Color4(.6, 0, 1));
        onUpdate.add((dt, p) -> {
            if (Math.abs(p.pos.x) > Window.viewSize.x / 2) {
                p.vel = p.vel.multiply(new Vec2(-1, 1));
            }
            if (Math.abs(p.pos.y) > Window.viewSize.y / 2) {
                p.vel = p.vel.multiply(new Vec2(1, -1));
            }
            p.pos = p.pos.clamp(Window.viewSize.multiply(-.5), Window.viewSize.multiply(.5));
        });
        Util.repeat(10000, i -> particles.add(new LifeTimeParticle(position, Vec2.fromPolar(200, i * Math.PI / 5000), 100)));
        onRemove.add(p -> {
            if (particles.isEmpty()) {
                destroy();
            }
        });
    }
}
