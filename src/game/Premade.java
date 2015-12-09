package game;

import core.AbstractEntity;
import core.Core;
import core.Input;
import core.Signal;
import graphics.Graphics2D;
import graphics.Shader;
import graphics.Sprite;
import java.util.function.Supplier;
import org.lwjgl.input.Keyboard;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import util.Color4;
import util.Vec2;
import static util.Vec2.ZERO;

public abstract class Premade {

    //Movement
    public static Signal<Vec2> makePosition(AbstractEntity e) {
        return e.addChild(new Signal(ZERO), "position");
    }

    //Movement
    public static Signal<Double> makeRotation(AbstractEntity e) {
        return e.addChild(new Signal(0.), "rotation");
    }

    public static Signal<Vec2> makeVelocity(AbstractEntity e) {
        Signal<Vec2> position = e.get("position", Vec2.class);
        return e.addChild(Core.update.collect(ZERO, (v, dt) -> position.edit(v.multiply(dt)::add)), "velocity");
    }

    public static void makeWASDMovement(AbstractEntity e, double speed, boolean arrowKeys) {
        Signal<Vec2> velocity = e.get("velocity", Vec2.class);
        e.onUpdate(dt -> velocity.set(ZERO));
        if (!arrowKeys) {
            e.add(Input.whileKeyDown(Keyboard.KEY_A).forEach(dt -> velocity.edit(new Vec2(-speed, 0)::add)),
                    Input.whileKeyDown(Keyboard.KEY_D).forEach(dt -> velocity.edit(new Vec2(speed, 0)::add)),
                    Input.whileKeyDown(Keyboard.KEY_W).forEach(dt -> velocity.edit(new Vec2(0, speed)::add)),
                    Input.whileKeyDown(Keyboard.KEY_S).forEach(dt -> velocity.edit(new Vec2(0, -speed)::add)));
        } else {
            e.add(Input.whileKeyDown(Keyboard.KEY_LEFT).forEach(dt -> velocity.edit(new Vec2(-speed, 0)::add)),
                    Input.whileKeyDown(Keyboard.KEY_RIGHT).forEach(dt -> velocity.edit(new Vec2(speed, 0)::add)),
                    Input.whileKeyDown(Keyboard.KEY_UP).forEach(dt -> velocity.edit(new Vec2(0, speed)::add)),
                    Input.whileKeyDown(Keyboard.KEY_DOWN).forEach(dt -> velocity.edit(new Vec2(0, -speed)::add)));
        }
    }

    //Graphics
    public static void makeCircleGraphics(AbstractEntity e, double size, Color4 color) {
        Signal<Vec2> position = e.get("position", Vec2.class);
        e.onRender(() -> Graphics2D.fillEllipse(position.get(), new Vec2(size, size), color, 20));
    }

    public static void makeSpriteGraphics(AbstractEntity e, Sprite s) {
        Signal<Vec2> position = e.get("position", Vec2.class);
        Supplier<Double> rotation = e.getOrDefault("rotation", () -> 0.);
        e.onUpdate(dt -> s.imageIndex += dt * s.imageSpeed);
        e.onRender(() -> s.draw(position.get(), rotation.get()));
    }

    public static void makeSpriteGraphicsBM(AbstractEntity e, Sprite s, int bm1, int bm2) {
        Signal<Vec2> position = e.get("position", Vec2.class);
        Supplier<Double> rotation = e.getOrDefault("rotation", () -> 0.);
        e.onUpdate(dt -> s.imageIndex += dt * s.imageSpeed);
        e.onRender(() -> {
            glBlendFunc(bm1, bm2);
            s.draw(position.get(), rotation.get());
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        });
    }

    public static void makeSpriteGraphicsS(AbstractEntity e, Sprite s, Shader d) {
        Signal<Vec2> position = e.get("position", Vec2.class);
        Supplier<Double> rotation = e.getOrDefault("rotation", () -> 0.);
        e.onUpdate(dt -> s.imageIndex += dt * s.imageSpeed);
        e.onRender(() -> {
            d.enable();
            s.draw(position.get(), rotation.get());
            Shader.clear();
        });
    }
}
