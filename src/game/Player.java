package game;

import core.AbstractEntity;
import core.Signal;
import graphics.Sprite;
import static util.Color4.GREEN;
import static util.Color4.RED;
import util.Input;
import util.Vec2;

public class Player extends AbstractEntity {

    @Override
    public void create() {
        Signal<Vec2> position = Premade.makePosition(this);
        Premade.makeVelocity(this);
        Premade.makeWASDMovement(this, 300);
        //Premade.makeCircleGraphics(this, 20, RED);
        Sprite s = new Sprite("box");
        s.color = GREEN;
        Premade.makeSpriteGraphics(this, s);

        Input.whenMouse(0, true).onEvent(() -> {
            Bullet b = new Bullet();
            b.create();
            b.position.set(position.get());
            b.velocity.set(Input.getMouse().subtract(position.get()).withLength(1000));
        }).addChild(this);

        Input.whenMouse(0, true).bufferCountThrottle(1).printEach().map(i -> i == 0).forEach(b -> s.color = b ? GREEN : RED).addChild(this);
        //Signal<Boolean> canShoot = new Signal<>(true).forEach(b -> s.color = b ? GREEN : RED);
        //Input.whenMouse(0, true).onEvent(() -> canShoot.set(true)).throttle(1).onEvent(() -> canShoot.set(false));
    }
}
