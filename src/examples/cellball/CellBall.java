package examples.cellball;

import engine.AbstractEntity.LAE;
import engine.Core;
import engine.Input;
import engine.Signal;
import examples.Premade2D;
import graphics.Graphics2D;
import graphics.Window2D;
import static java.util.Comparator.comparingDouble;
import util.Color4;
import static util.Color4.BLACK;
import static util.Color4.GREEN;
import static util.Color4.RED;
import util.RegisteredEntity;
import util.Vec2;

public class CellBall {

    public static void main(String[] args) {
        Core.init();
        Window2D.background = BLACK;

        Input.whenMouse(0, true).onEvent(() -> {
            Protein p = new Protein(true);
            p.create();
            p.get("position", Vec2.class).set(Input.getMouse());
        });
        Input.whenMouse(1, true).onEvent(() -> {
            Protein p = new Protein(false);
            p.create();
            p.get("position", Vec2.class).set(Input.getMouse());
        });

        Core.interval(.5).onEvent(() -> {
            new LAE(sig -> {
                Signal<Vec2> position = Premade2D.makePosition(sig);
                Signal<Vec2> velocity = Premade2D.makeVelocity(sig);
                position.set(new Vec2(-550, Math.random() * 800 - 400));

                boolean grow = Math.random() > .5;
                sig.onUpdate(dt -> RegisteredEntity.getAll(Protein.class).stream()
                        .filter(p -> p.grow == grow)
                        .min(comparingDouble(p -> p.get("position", Vec2.class).get().subtract(position.get()).length())).ifPresent(p -> {
                    Vec2 to = p.get("position", Vec2.class).get().subtract(position.get());
                    if (to.length() < 100) {
                        velocity.edit(to.withLength(50)::add);
                    }
                    if (to.length() < 20) {
                        sig.destroy();
                        p.get("active", Boolean.class).set(true);
                    }
                }));

                Premade2D.makeCircleGraphics(sig, 8, grow ? new Color4(0, .5, 1) : new Color4(.5, 0, 1));
            }).create();
        });

        Core.run();
    }

    public static class Protein extends RegisteredEntity {

        public final boolean grow;

        public Protein(boolean team) {
            this.grow = team;
        }

        @Override
        protected void createInner() {
            Signal<Vec2> position = Premade2D.makePosition(this);
            Signal<Double> active = addChild(Core.time(), "active");
            Signal<Color4> color = addChild(active.map(x -> (grow ? GREEN : RED).multiply(1 - Math.min(x, .5))), "color");

            onRender(() -> RegisteredEntity.getAll(Protein.class).stream()
                    .filter(p -> p != this && p.grow == grow)
                    .map(p -> p.get("position", Vec2.class).get())
                    .filter(v -> v.x > position.get().x && v.subtract(position.get()).length() < 200)
                    .forEach(v -> Graphics2D.drawLine(v, position.get(), Color4.gray(.5), 2)));

            add(Core.renderLayer(1).onEvent(() -> Graphics2D.fillEllipse(position.get(), new Vec2(20), color.get(), 20)));
            
            active.filter(x -> x < .5).onEvent(() -> {
                RegisteredEntity.getAll(Protein.class).stream()
                        .filter(p -> p != this && p.grow == grow)
                        .filter(p -> p.get("position", Vec2.class).get().x > position.get().x
                                && p.get("position", Vec2.class).get().subtract(position.get()).length() < 200)
                        .forEach(p -> {
                            p.get("active", Boolean.class).set(true);
                        });
                Core.timer(1, () -> active.set(false));
            });
        }
    }
}
