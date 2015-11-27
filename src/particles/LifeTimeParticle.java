package particles;

import util.Vec2;

public class LifeTimeParticle extends Particle {

    public double lifeTime, maxLifeTime;

    public LifeTimeParticle(Vec2 pos, Vec2 vel, double maxLifeTime) {
        super(pos, vel);
        this.maxLifeTime = maxLifeTime;
    }

    public double percTimeRemaining() {
        return 1 - lifeTime / maxLifeTime;
    }

    public double timeRemaining() {
        return maxLifeTime - lifeTime;
    }

    @Override
    public void update(double dt) {
        super.update(dt);
        lifeTime += dt;
        if (lifeTime > maxLifeTime) {
            shouldRemove = true;
        }
    }
}
