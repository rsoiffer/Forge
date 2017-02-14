package examples.cake;

import engine.Core;
import engine.Input;
import engine.Signal;
import graphics.data.Sprite;
import org.lwjgl.input.Keyboard;
import util.Vec2;

public class Cake {

    public static void main(String[] args) {
        Core.init();

        boolean[] hideCake = new boolean[8];

        Signal<Double> speed = new Signal(0.);
        Signal<Double> spin = Core.update.reduce(0., (dt, t) -> t + dt * speed.get());

        Sprite cake = new Sprite("cake3");
        Core.render.onEvent(() -> {
            cake.scale = new Vec2(1);
            for (int i = 0; i < 8; i += 1) {
                if (!hideCake[i]) {
                    cake.draw(new Vec2(0, -3).rotate(Math.PI / 4 * i + spin.get()), Math.PI / 4 * i + spin.get());
                }
            }
        });

        Input.whenMouse(0, true).onEvent(() -> {
            int sec = (int) ((Input.getMouse().direction() - spin.get()) / Math.PI * 4 + 80000) % 8;
            hideCake[sec] = !hideCake[sec];
        });

        Input.whileKeyDown(Keyboard.KEY_LEFT).forEach(dt -> speed.edit(x -> x + dt));
        Input.whileKeyDown(Keyboard.KEY_RIGHT).forEach(dt -> speed.edit(x -> x - dt));

        Core.run();
    }
}
