package game;

import core.AbstractEntity;
import core.Core;
import core.Signal;
import graphics.Shader;
import graphics.Sprite;
import util.Mutable;
import util.Vec2;
import static util.Vec2.ZERO;

public class InvisibleMan extends AbstractEntity {
    
    private boolean p1;

    public InvisibleMan(boolean p1) {
        this.p1 = p1;
    }

    @Override
    public void create() {
        Signal<Vec2> position = Premade.makePosition(this);
        Signal<Vec2> velocity = Premade.makeVelocity(this);
        Signal<Double> rotation = Premade.makeRotation(this);
        //Premade.makeCircleGraphics(this, 50, new Color4(1, .5, 0));
        Core.update.filter(dt ->
                !ZERO.equals(velocity.get()))
                .forEach(dt -> rotation.set(velocity.get().direction()));
        Premade.makeWASDMovement(this, 200, p1);

        Mutable<Boolean> isLeft = new Mutable(true);
        Core.interval(.2).onEvent(() -> new LAE(footstep -> {
            Premade.makePosition(footstep).set(position.get());
            Premade.makeRotation(footstep).set(rotation.get() - Math.PI/2);
            Sprite s = new Sprite("footstep");
            if (!isLeft.o) {
                s.scale = new Vec2(-1, 1);
            }
            isLeft.o = !isLeft.o;
            onUpdate(dt -> s.color = s.color.withA(s.color.a - dt / 10));
            Premade.makeSpriteGraphics(footstep, s);
            Core.timer(10, footstep::destroy);
        }).create());
    }

    public static void main(String[] args) {
        Core.init();
        new InvisibleMan(true).create();
        new InvisibleMan(false).create();
        Core.run();
    }
}
