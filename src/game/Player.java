package game;

import core.AbstractEntity;
import core.Signal;
import graphics.Sprite;
import static util.Color4.*;
import util.Input;
import util.Vec2;

public class Player extends AbstractEntity {

    @Override
    public void create() {
        Signal<Vec2> position = Premade.makePosition(this);
        Premade.makeVelocity(this);
        Premade.makeWASDMovement(this, 300);
        Sprite s = new Sprite("box");
        s.color = GREEN;
        Premade.makeSpriteGraphics(this, s);
        onUpdate(dt -> s.imageIndex += s.imageSpeed*dt);

        Input.whenMouse(0, true).onEvent(() -> {
            Bullet b = new Bullet();
            b.create();
            b.position.set(position.get());
            b.velocity.set(Input.getMouse().subtract(position.get()).withLength(1000));
        }).addChild(this);

        Input.whenMouse(0, true).countWithin(1).map(i -> i == 0).forEach(b -> s.color = b ? GREEN : RED).addChild(this);
        Input.whenMouse(1, true).bufferCountThrottle(.2).filter(i -> i >= 2).onEvent(() -> s.color = BLUE).addChild(this);
    }
}
