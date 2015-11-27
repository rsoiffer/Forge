package particles;

import util.Vec2;

public class Particle {

    public Vec2 pos;
    public Vec2 vel;
    public boolean shouldRemove;

    public Particle(Vec2 pos, Vec2 vel) {
        this.pos = pos;
        this.vel = vel;
    }

    public void render() {
    }

    public void update(double dt) {
        pos = pos.add(vel.multiply(dt));
    }
}
