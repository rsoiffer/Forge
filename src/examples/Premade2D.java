package examples;

import engine.AbstractEntity;
import engine.Core;
import engine.Input;
import engine.Signal;
import graphics.Graphics2D;
import graphics.data.Sprite;
import java.util.function.Supplier;
import org.lwjgl.input.Keyboard;
import util.Color4;
import util.Vec2;
import static util.Vec2.ZERO;

public abstract class Premade2D {

    //Movement
    public static Signal<Vec2> makePosition(AbstractEntity e) {
        return e.addChild(new Signal(ZERO), "position");
    }

    public static Signal<Double> makeRotation(AbstractEntity e) {
        return e.addChild(new Signal(0.), "rotation");
    }

    public static Signal<Vec2> makeVelocity(AbstractEntity e) {
        Signal<Vec2> position = e.get("position", Vec2.class);
        return e.addChild(Core.update.collect(ZERO, (v, dt) -> position.edit(v.multiply(dt)::add)), "velocity");
    }

    public static Signal<Vec2> makeGravity(AbstractEntity e, Vec2 g) {
        Signal<Vec2> velocity = e.get("velocity", Vec2.class);
        return e.addChild(Core.update.collect(g, (v, dt) -> velocity.edit(v.multiply(dt)::add)), "gravity");
    }

    public static void makeArrowKeyMovement(AbstractEntity e, double speed) {
        Signal<Vec2> velocity = e.get("velocity", Vec2.class);
        e.onUpdate(dt -> velocity.set(ZERO));
        e.add(Input.whileKeyDown(Keyboard.KEY_LEFT).forEach(dt -> velocity.edit(new Vec2(-speed, 0)::add)),
                Input.whileKeyDown(Keyboard.KEY_RIGHT).forEach(dt -> velocity.edit(new Vec2(speed, 0)::add)),
                Input.whileKeyDown(Keyboard.KEY_UP).forEach(dt -> velocity.edit(new Vec2(0, speed)::add)),
                Input.whileKeyDown(Keyboard.KEY_DOWN).forEach(dt -> velocity.edit(new Vec2(0, -speed)::add)));
    }

    public static void makeWASDMovement(AbstractEntity e, double speed) {
        Signal<Vec2> velocity = e.get("velocity", Vec2.class);
        e.onUpdate(dt -> velocity.set(ZERO));
        e.add(Input.whileKeyDown(Keyboard.KEY_A).forEach(dt -> velocity.edit(new Vec2(-speed, 0)::add)),
                Input.whileKeyDown(Keyboard.KEY_D).forEach(dt -> velocity.edit(new Vec2(speed, 0)::add)),
                Input.whileKeyDown(Keyboard.KEY_W).forEach(dt -> velocity.edit(new Vec2(0, speed)::add)),
                Input.whileKeyDown(Keyboard.KEY_S).forEach(dt -> velocity.edit(new Vec2(0, -speed)::add)));
        e.onUpdate(dt -> {
            if (velocity.get().lengthSquared() > speed * speed) {
                velocity.edit(v -> v.withLength(speed));
            }
        });
    }

    //Graphics
    public static void makeCircleGraphics(AbstractEntity e, double size, Color4 color) {
        Signal<Vec2> position = e.get("position", Vec2.class);
        e.onRender(() -> Graphics2D.fillEllipse(position.get(), new Vec2(size), color, 20));
    }

    public static void makeCircleGraphics(AbstractEntity e, Supplier<Double> size, Supplier<Color4> color) {
        Signal<Vec2> position = e.get("position", Vec2.class);
        e.onRender(() -> Graphics2D.fillEllipse(position.get(), new Vec2(size.get()), color.get(), 20));
    }

    public static Signal<Sprite> makeSpriteGraphics(AbstractEntity e, String name) {
        Signal<Vec2> position = e.get("position", Vec2.class);
        Supplier<Double> rotation = e.getOrDefault("rotation", () -> 0.);
        Signal<Sprite> sprite = e.addChild(new Signal(new Sprite(name)), "sprite");
        e.onUpdate(dt -> sprite.get().imageIndex += dt * sprite.get().imageSpeed);
        e.onRender(() -> sprite.get().draw(position.get(), rotation.get()));
        return sprite;
    }
}
