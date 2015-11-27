package particles;

import java.util.LinkedList;
import java.util.List;
import util.Vec2;

public class TraceParticle extends Particle {

    public List<Vec2> pastPositions = new LinkedList();
    public int store;

    public TraceParticle(Vec2 pos, Vec2 vel, int store) {
        super(pos, vel);
        this.store = store;
    }

    @Override
    public void render() {
        pastPositions.add(pos);
        if (pastPositions.size() > store) {
            pastPositions.remove(0);
        }
    }
}
