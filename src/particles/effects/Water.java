package particles.effects;

import engine.AbstractEntity;
import engine.Core;
import java.util.Arrays;
import static org.lwjgl.opengl.GL11.*;
import particles.Particle;
import particles.ParticleEmitter;
import util.Color4;
import util.Util;
import util.Vec2;
import static util.Vec2.ZERO;

public class Water extends AbstractEntity {

    public Vec2 position = ZERO;
    public double width = 5000;
    public double defaultHeight = -100;
    public double bottom = -4000;

    public final int detail = 1000;
    public final double[] heights = new double[detail];
    public final double[] speeds = new double[detail];
    private final double[] deltas = new double[detail - 1];

    public boolean contains(Vec2 v) {
        double i = (v.x - position.x + width) * detail / (width * 2);
        if (i <= 0 || i >= detail - 1 || v.y < bottom) {
            return false;
        }
        double height = heights[(int) i] * (1 - i + (int) i) + heights[(int) i + 1] * (i - (int) i);
        return v.y < height;
    }

    @Override
    public void create() {
        Arrays.fill(heights, defaultHeight);

        Core.interval(.0005).onEvent(this::step).addChild(this);

//        onUpdate(dt -> {
//            Util.repeat(detail, i -> heights[i] += dt * speeds[i]);
//            Util.repeat(detail - 1, i -> {
//                deltas[i] = heights[i + 1] - heights[i];
//                speeds[i] += deltas[i] * dt * 3000;
//                speeds[i + 1] -= deltas[i] * dt * 3000;
//            });
//            Util.repeat(detail, i -> speeds[i] = speeds[i] * Math.pow(.1, dt * dt) + dt * 10 * (defaultHeight - heights[i]));
//            Util.repeat(detail - 1, i -> {
//                heights[i] += deltas[i] * dt;
//                heights[i + 1] -= deltas[i] * dt;
//            });
//        });
        onRender(() -> {
            glDisable(GL_TEXTURE_2D);
            glBegin(GL_TRIANGLE_STRIP);
            Util.repeat(detail, i -> {
                double x = position.x + width * (2. * i / detail - 1);
                new Color4(0, 0, .6).glColor();
                new Vec2(x, bottom).glVertex();
                new Color4(0, .2, 1).glColor();
                new Vec2(x, heights[i]).glVertex();
            });
            glEnd();
        });
        Core.renderLayer(1).onEvent(() -> {
            glDisable(GL_TEXTURE_2D);
            glBegin(GL_TRIANGLE_STRIP);
            Util.repeat(detail, i -> {
                double x = position.x + width * (2. * i / detail - 1);
                new Color4(0, 0, .6, .5).glColor();
                new Vec2(x, bottom).glVertex();
                new Color4(0, .2, 1, .5).glColor();
                new Vec2(x, heights[i]).glVertex();
            });
            glEnd();
        }).addChild(this);
    }

    public int indexOf(double x) {
        return (int) Math.round((x - position.x + width) * detail / (width * 2));
    }

    public void splash(double x, double strength, ParticleEmitter drops) {
        int index = indexOf(x);
        speeds[index] = strength * strength / 500;
        Util.repeat((int) (strength * strength) / 2000, () -> {
            Vec2 dir = Vec2.fromPolar(Math.random(), Math.random() * Math.PI);
            drops.particles.add(new Particle(new Vec2(x, heights[index]).add(dir), dir.multiply(50 * Math.sqrt(strength))));
        });
    }

    public void step() {
        Util.repeat(detail, i -> heights[i] += .01 * speeds[i]);
        Util.repeat(detail - 1, i -> {
            deltas[i] = heights[i + 1] - heights[i];
            speeds[i] += deltas[i];
            speeds[i + 1] -= deltas[i];
        });
        Util.repeat(detail, i -> speeds[i] = speeds[i] * Math.pow(.5, .001) + .01 * (defaultHeight - heights[i]));
        Util.repeat(detail - 1, i -> {
            heights[i] += deltas[i] * .01;
            heights[i + 1] -= deltas[i] * .01;
        });
    }
}
