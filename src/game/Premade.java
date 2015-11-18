package game;

import core.AbstractEntity;
import core.Core;
import core.Signal;
import graphics.Graphics2D;
import graphics.Sprite;
import org.lwjgl.input.Keyboard;
import util.Color4;
import util.Input;
import util.Vec2;

public abstract class Premade {

    //Movement
    public static Signal<Vec2> makePosition(AbstractEntity e) {
        return e.addChild(new Signal(new Vec2()), "position");
    }

    public static Signal<Vec2> makeVelocity(AbstractEntity e) {
        Signal<Vec2> position = e.get("position", Vec2.class);
        return e.addChild(Core.update.collect(new Vec2(), (v, dt) -> position.edit(v.multiply(dt)::add)), "velocity");
    }

    public static void makeWASDMovement(AbstractEntity e, double speed) {
        Signal<Vec2> velocity = e.get("velocity", Vec2.class);
        e.onUpdate(dt -> velocity.set(new Vec2()));
        e.add(Input.whileKeyDown(Keyboard.KEY_A).forEach(dt -> velocity.edit(new Vec2(-speed, 0)::add)),
                Input.whileKeyDown(Keyboard.KEY_D).forEach(dt -> velocity.edit(new Vec2(speed, 0)::add)),
                Input.whileKeyDown(Keyboard.KEY_W).forEach(dt -> velocity.edit(new Vec2(0, speed)::add)),
                Input.whileKeyDown(Keyboard.KEY_S).forEach(dt -> velocity.edit(new Vec2(0, -speed)::add)));
    }

    //Graphics
    public static void makeCircleGraphics(AbstractEntity e, double size, Color4 color) {
        Signal<Vec2> position = e.get("position", Vec2.class);
        e.onUpdate(dt -> Graphics2D.fillEllipse(position.get(), new Vec2(size, size), color, 20));
    }

    public static void makeSpriteGraphics(AbstractEntity e, Sprite s) {
        Signal<Vec2> position = e.get("position", Vec2.class);
        e.onUpdate(dt -> s.draw(position.get(), 0));
    }
}
