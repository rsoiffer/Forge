package game;

import engine.AbstractEntity;
import engine.Core;
import engine.Signal;
import graphics.Graphics3D;
import graphics.Window3D;
import graphics.data.Sprite;
import network.Connection;
import network.NetworkUtils;
import org.lwjgl.input.Mouse;
import util.Color4;
import static util.Color4.WHITE;
import util.Mutable;
import util.Vec2;
import util.Vec3;

public class InvisibleMan extends AbstractEntity {

    @Override
    public void create() {
        Signal<Vec3> position = Premade3D.makePosition(this);
        Signal<Vec3> velocity = Premade3D.makeVelocity(this);
        Premade3D.makeMouseLook(this, 1, -1.5, 1.5);
        Premade3D.makeWASDMovement(this, 50);
        Window3D.pos = new Vec3(0, 0, 1);
        position.forEach(v -> Window3D.pos = v.add(new Vec3(0, 0, 1)));

        Mutable<Boolean> isLeft = new Mutable(true);
        Core.interval(.2).onEvent(() -> {
            conn.sendMessage(0, position.get(), Window3D.facing.t, isLeft.o);

            Footstep f = new Footstep();
            f.create();
            f.set(position.get(), Window3D.facing.t, isLeft.o);
            isLeft.o = !isLeft.o;
        });

        onRender(() -> Graphics3D.fillRect(new Vec3(0, 0, -.001), new Vec2(10), 0, 0, new Color4(.9, .9, .9)));
    }

    public static class Footstep extends AbstractEntity {

        @Override
        public void create() {
            Premade3D.makePosition(this);
            Premade3D.makeRotation(this);
            Signal<Sprite> s = Premade3D.makeSpriteGraphics(this, "footstep");
            onUpdate(dt -> s.get().color = s.get().color.withA(s.get().color.a * Math.pow(.9, dt)));
            Core.timer(60, this::destroy);
        }

        public void set(Vec3 pos, double rot, boolean isLeft) {
            get("position", Vec3.class).set(pos);
            get("rotation", Double.class).set(rot);
            get("sprite", Sprite.class).get().scale = isLeft ? new Vec2(.2, .2) : new Vec2(.2, -.2);
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
            f.set(conn.read(Vec3.class), conn.read(Double.class), conn.read(Boolean.class));
        });

        Core.is3D = true;
        Core.init();

        Mouse.setGrabbed(true);
        Window3D.background = WHITE;
        new InvisibleMan().create();

        Core.run();
    }
}
