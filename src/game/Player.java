package game;

import engine.AbstractEntity;
import engine.Core;
import engine.Input;
import engine.Signal;
import graphics.Sprite;
import static util.Color4.*;
import util.Vec2;

public class Player extends AbstractEntity {

    public static void main(String[] args) {
        Core.init();
        new Player().create();
        Core.run();
    }

    @Override
    public void create() {
        Signal<Vec2> position = Premade.makePosition(this);
        Premade.makeVelocity(this);
        Premade.makeWASDMovement(this, 300);
        Signal<Sprite> s = Premade.makeSpriteGraphics(this, "box");

        Input.whenMouse(0, true).onEvent(() -> {
            Bullet b = new Bullet();
            b.create();
            b.position.set(position.get());
            b.velocity.set(Input.getMouse().subtract(position.get()).withLength(1000));
        }).addChild(this);

        Input.whenMouse(0, true).countWithin(1).map(i -> i == 0).forEach(b -> s.get().color = b ? GREEN : RED).addChild(this);
        Input.whenMouse(1, true).bufferCountThrottle(.2).filter(i -> i >= 2).onEvent(() -> s.get().color = BLUE).addChild(this);
    }
}
