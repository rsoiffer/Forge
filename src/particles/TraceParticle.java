package particles;

import java.util.LinkedList;
import java.util.List;
import util.Vec2;

public class TraceParticle extends Particle {

    /**
     * List of the past positions the tracer particle has been
     */
    public List<Vec2> pastPositions = new LinkedList();
    /**
     * How long the particle should be
     */
    public int store;

    /**
     * Constructor for the tracer particle, has a store value that determines
     * how long the tracer particle will be
     *
     * @param pos
     * @param vel
     * @param store
     */
    public TraceParticle(Vec2 pos, Vec2 vel, int store) {
        super(pos, vel);
        this.store = store;
    }

    /**
     * Adds a new position to the end of the pastPositions list and removes the
     * first positions if it's bigger than the store value
     */
    @Override
    public void render() {
        pastPositions.add(pos);
        if (pastPositions.size() > store) {
            pastPositions.remove(0);
        }
    }
}
