package particles;

import util.Vec2;

public class LifeTimeParticle extends Particle {

    /**
     * How long the particle has been alive
     */
    public double lifeTime;
    /**
     * Warrants how long the particle is allowed to exist in the game
     */
    public double maxLifeTime;

    /**
     * Constructor for LifeTimeParticle. Sets the position, velocity and the
     * maximum time it can be alive
     *
     * @param pos
     * @param vel
     * @param maxLifeTime
     */
    public LifeTimeParticle(Vec2 pos, Vec2 vel, double maxLifeTime) {
        super(pos, vel);
        this.maxLifeTime = maxLifeTime;
    }

    /**
     * Returns the the percent of time the particle will be alive
     *
     * @return
     */
    public double percTimeRemaining() {
        return 1 - lifeTime / maxLifeTime;
    }

    /**
     * Returns the time the particle has remaining
     *
     * @return
     */
    public double timeRemaining() {
        return maxLifeTime - lifeTime;
    }

    /**
     * Updates the position of the particle with delta time, dt. The delta time
     * is updated to be equal to the sum of delta time and lifeTime. The
     * particle's shouldRemove boolean is set to true if lifeTime is greater
     * than maxLifeTime
     *
     * @param dt
     */
    @Override
    public void update(double dt) {
        super.update(dt);
        lifeTime += dt;
        if (lifeTime > maxLifeTime) {
            shouldRemove = true;
        }
    }
}
