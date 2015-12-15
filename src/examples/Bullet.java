package examples;

import engine.AbstractEntity;
import engine.Core;
import engine.Signal;
import static util.Color4.BLUE;
import util.Vec2;

public class Bullet extends AbstractEntity {

    public Signal<Vec2> position, velocity;

    @Override
    public void create() {
        position = Premade2D.makePosition(this);
        velocity = Premade2D.makeVelocity(this);
        Premade2D.makeCircleGraphics(this, 4, BLUE);

        Core.timer(1, () -> destroy());
    }
}
