package particles;

import util.Vec2;

public class Particle {

    /**
     * The position of the particle 
     */
    public Vec2 pos; 
    /**
     * The velocity of the particle
     */
    public Vec2 vel;
    /**
     * A boolean that determines if this particles should be removed from the game world
     */
    public boolean shouldRemove;

    /**
     * The constructor for the Particle class.
     * Sets the position and velocity for new particle
     * @param pos
     * @param vel 
     */
    public Particle(Vec2 pos, Vec2 vel) {
        this.pos = pos;
        this.vel = vel;
    }

    /**
     * At first the method is blank and void. Will be overwritten in what ever extends Particle, so render will very on the class that extends it
     */
    public void render() {
    }

    /**
     * Changes the position of the particle by adding to the current position the current velocity and delta time
     * @param dt 
     */
    public void update(double dt) {
        pos = pos.add(vel.multiply(dt));
    }
}
