package game;

import core.Core;
import org.lwjgl.opengl.GL11;
import static org.lwjgl.opengl.GL11.*;
import util.Color4;
import util.Vec2;

public class Particles {

    public static void main(String[] args) {
        Core.init();

        glBlendFunc(GL_SRC_ALPHA, GL_ONE);
        GL11.glPointSize(4);

        ParticleEmitter fire = new ParticleEmitter();
        Core.update.forEach(dt -> {
            fire.particles.add(new Particle(1, .2, .05, .1));
        });

        Core.run();
    }

    public static class Particle {

        public Vec2 pos;
        public Vec2 vel;
        public Color4 color;
        public boolean shouldRemove;
        public double lifeTime, maxLifeTime;

        public Particle(Vec2 pos, Vec2 vel, Color4 color) {
            this.pos = pos;
            this.vel = vel;
            this.color = color;
        }
    }
}
