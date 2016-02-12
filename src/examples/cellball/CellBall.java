package examples.cellball;

import engine.AbstractEntity.LAE;
import engine.Core;
import engine.EventStream;
import engine.Input;
import engine.Signal;
import examples.Premade2D;
import graphics.Graphics2D;
import graphics.Window2D;
import static java.util.Comparator.comparingDouble;
import java.util.Optional;
import java.util.function.Supplier;
import org.lwjgl.opengl.Display;
import util.Color4;
import static util.Color4.*;
import util.RegisteredEntity;
import util.Vec2;

public class CellBall {

    public static void main(String[] args) {
        Core.init();
        Window2D.background = BLACK;

        Core.render.bufferCount(Core.interval(1)).forEach(i -> Display.setTitle("FPS: " + i));

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
                    if (to.length() < 200) {
                        velocity.edit(to.withLength(20)::add);
                    }
                    if (to.length() < 20) {
                        sig.destroy();
                        ((EventStream) p.get("notify")).sendEvent();
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

            Signal<Double> fade = Core.update.reduce(.5, (dt, t) -> Math.min(t + dt, .5));
            Signal<Color4> color = addChild(fade.map(x -> (grow ? GREEN : RED).multiply(1 - x)), "color");
            EventStream notify = addChild(new EventStream(), "notify");
            notify.onEvent(() -> fade.set(0.));

            Supplier<Optional<Protein>> to = () -> RegisteredEntity.getAll(Protein.class).stream()
                    .filter(p -> p != this && p.grow == grow)
                    .filter(p -> p.get("position", Vec2.class).get().x > position.get().x
                            && p.get("position", Vec2.class).get().subtract(position.get()).length() < 200)
                    .min(comparingDouble(p -> p.get("position", Vec2.class).get().subtract(position.get()).lengthSquared()));

            onRender(() -> to.get().ifPresent(p -> Graphics2D.drawLine(p.get("position", Vec2.class).get(), position.get(), Color4.gray(.5), 2)));

            EventStream circleGraphics = Core.renderLayer(1).onEvent(() -> Graphics2D.fillEllipse(position.get(), new Vec2(20), color.get(), 20));

            EventStream notifyOthers = Core.delay(.1, notify).onEvent(() -> to.get().map(p -> (EventStream) p.get("notify")).ifPresent(EventStream::sendEvent));

            add(fade, color, notify, circleGraphics, notifyOthers);
        }
    }
}
