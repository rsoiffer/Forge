package particles;

import engine.AbstractEntity.LAE;
import engine.Core;
import engine.Input;
import engine.Signal;
import game.Premade;
import graphics.Window;
import org.lwjgl.opengl.Display;
import static org.lwjgl.opengl.GL11.*;
import particles.drawers.MetaballDrawer;
import particles.drawers.TraceDrawer;
import particles.effects.Water;
import util.Color4;
import util.Mutable;
import util.Util;
import util.Vec2;

public class Particles {

    public static void main(String[] args) {
        Core.init();

//        Core.timeMult = .1;
        Core.update.forEach(dt -> Display.setTitle("FPS: " + (int) (1 / dt)));

        Water w = new Water();
        w.create();

        ParticleEmitter<Particle> drops = new ParticleEmitter();
        drops.create();
        drops.drawer = new MetaballDrawer(new Color4(0, .2, 1), 15, () -> {
            glDisable(GL_TEXTURE_2D);
            glBegin(GL_TRIANGLE_STRIP);
            Util.repeat(w.detail, i -> {
                double x = w.position.x + w.width * (2. * i / w.detail - 1);
                new Color4(1, 1, 1, 0).glColor();
                new Vec2(x, w.heights[i] + 2).glVertex();
                new Color4(1, 1, 1, .8).glColor();
                new Vec2(x, w.heights[i]).glVertex();
            });
            glEnd();
        });
        drops.onUpdate.add((dt, p) -> {
            p.vel = p.vel.add(new Vec2(0, -2000 * dt));
            if (p.vel.y < 0 && w.contains(p.pos.add(new Vec2(0, 15))) || p.pos.y < w.bottom) {
                p.shouldRemove = true;
            }
        });

        ParticleEmitter<TraceParticle> rain = new ParticleEmitter();
        rain.create();
        rain.drawer = new TraceDrawer(GL_ONE_MINUS_SRC_ALPHA, p -> new Color4(0, .2, .8, .5));
        Core.interval(.01).onEvent(() -> rain.particles.add(new TraceParticle(Window.viewSize.multiply(new Vec2(Math.random() - .5, 1.5)), new Vec2(0, -3500), 5)));
        rain.onUpdate.add((dt, p) -> {
            if (w.contains(p.pos)) {
                w.splash(p.pos.x, 100, drops);
                p.shouldRemove = true;
            }
        });

        Input.whenMouse(0, true).onEvent(() -> {
            new LAE(rock -> {
                Signal<Vec2> position = Premade.makePosition(rock);
                position.set(Input.getMouse());
                Signal<Vec2> velocity = Premade.makeVelocity(rock);
                rock.onUpdate(dt -> velocity.edit(new Vec2(0, -1000 * dt)::add));
                Premade.makeSpriteGraphics(rock, "rock");

                Mutable<Boolean> canSplash = new Mutable(true);
                rock.onUpdate(dt -> {
                    if (canSplash.o) {
                        if (w.contains(position.get())) {
                            canSplash.o = false;
                            w.splash(position.get().x, Math.abs(velocity.get().y), drops);
                        }
                    } else {
                        velocity.set(new Vec2(0, -200));
                    }
                });

                Core.update.filter(position.map(p -> p.y < w.bottom - 20)).first(1).onEvent(rock::destroy);
            }).create();
        });
        //Input.whenMouse(1, true).onEvent(() -> new Sound1(Input.getMouse()).create());
//        Input.whenMouse(1, true).onEvent(() -> new Explosion(Input.getMouse()).create());
//
//        Flames f = new Flames();
//        f.create();
//        f.width = 700;
//        f.position = new Vec2(0, -Window.viewSize.y / 2);
//        Input.whenKey(Keyboard.KEY_SPACE, true).first_E(1).onEvent(f::destroy);
//
//        Torch t = new Torch();
//        t.create();
//        Input.whileMouseDown(0).forEach(dt -> t.position = Input.getMouse());
        //
        Core.run();
    }
}
