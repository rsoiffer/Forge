/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import core.AbstractEntity;
import core.Core;
import core.Signal;
import graphics.Graphics2D;
import graphics.Window;
import util.Color4;
import util.Input;
import util.Vec2;

/**
 *
 * @author RSoiffer16
 */
public class Circles {

    public static void main(String[] args) {
        Core.init();

        Runnable newCircle = () -> new AbstractEntity.LAE(c -> {
            Signal<Vec2> position = Premade.makePosition(c);
            position.set(Input.getMouse());
            Signal<Vec2> velocity = Premade.makeVelocity(c);
            velocity.set(Vec2.random(1000));
            Premade.makeCircleGraphics(c, 20, Color4.RED.withA(.1));

            //c.onUpdate(dt -> velocity.edit(Input.getMouse().subtract(position.get()).multiply(dt)::add));
            c.onUpdate(dt -> Graphics2D.drawEllipse(position.get(), new Vec2(20, 20), Color4.BLACK, 20));

            c.onUpdate(dt -> {
                if (Math.abs(position.get().x) + 20 > Window.viewSize.x / 2) {
                    velocity.edit(new Vec2(-1, 1)::multiply);
                }
                if (Math.abs(position.get().y) + 20 > Window.viewSize.y / 2) {
                    velocity.edit(new Vec2(1, -1)::multiply);
                }
                position.edit(p -> p.clamp(Window.viewSize.multiply(-.5).add(new Vec2(20, 20)),
                        Window.viewSize.multiply(.5).subtract(new Vec2(20, 20))));
            });

            Core.timer(5, c::destroy);
        }).create();

        Input.whenMouse(1, true).onEvent(() -> {
            for (int i = 0; i < 20; i++) {
                newCircle.run();
            }
        });
        Core.interval(.1).filter_E(Input.mouseSignal(0)).onEvent(newCircle);

        Core.run();
    }
}
