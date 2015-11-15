package game;

import core.AbstractEntity;
import core.Core;
import core.Signal;
import static util.Color4.BLUE;
import util.Vec2;

public class Bullet extends AbstractEntity {

    public Signal<Vec2> position, velocity;

    @Override
    public void create() {
        position = Premade.makePosition(this);
        velocity = Premade.makeVelocity(this);
        Premade.makeCircleGraphics(this, 4, BLUE);

        Core.timer(1, () -> destroy());
    }
}
