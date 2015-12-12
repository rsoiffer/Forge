package game;

import engine.AbstractEntity;
import engine.Core;
import engine.Signal;
import graphics.Sprite;
import network.Connection;
import network.NetworkUtils;
import util.Mutable;
import util.Vec2;
import static util.Vec2.ZERO;

public class InvisibleMan extends AbstractEntity {

    @Override
    public void create() {
        Signal<Vec2> position = Premade.makePosition(this);
        Signal<Vec2> velocity = Premade.makeVelocity(this);
        Signal<Double> rotation = Premade.makeRotation(this);
        Core.update.filter(dt -> !ZERO.equals(velocity.get())).forEach(dt -> rotation.set(velocity.get().direction()));
        Premade.makeWASDMovement(this, 200);

        Mutable<Boolean> isLeft = new Mutable(true);
        Core.interval(.2).onEvent(() -> {
            conn.sendMessage(0, position.get(), rotation.get(), isLeft.o);

            Footstep f = new Footstep();
            f.create();
            f.set(position.get(), rotation.get(), isLeft.o);
            isLeft.o = !isLeft.o;
        });
    }

    public static class Footstep extends AbstractEntity {

        @Override
        public void create() {
            Premade.makePosition(this);
            Premade.makeRotation(this);
            Signal<Sprite> s = Premade.makeSpriteGraphics(this, "footstep");
            onUpdate(dt -> s.get().color = s.get().color.withA(s.get().color.a * Math.pow(.9, dt)));
            Core.timer(60, this::destroy);
        }

        public void set(Vec2 pos, double rot, boolean isLeft) {
            get("position", Vec2.class).set(pos);
            get("rotation", Double.class).set(rot);
            if (!isLeft) {
                get("sprite", Sprite.class).get().scale = new Vec2(1, -1);
            }
        }
    }

    public static Connection conn;

    public static void main(String[] args) {
        conn = NetworkUtils.connectSimple();

        System.out.println("Connected to server");
        conn.onClose(() -> System.out.println("Disconnected from server"));

        conn.registerHandler(0, () -> {
            Footstep f = new Footstep();
            f.create();
            f.set(conn.read(Vec2.class), conn.read(Double.class), conn.read(Boolean.class));
        });

        Core.init();

        new InvisibleMan().create();

        Core.run();
    }
}
