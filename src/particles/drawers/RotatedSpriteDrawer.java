package particles.drawers;

import graphics.Graphics2D;
import java.util.function.Function;
import particles.Particle;
import util.Color4;
import util.Vec2;

public class RotatedSpriteDrawer<P extends Particle> extends SpriteDrawer<P> {

    final Function<Particle, Double> rotation;

    public RotatedSpriteDrawer(int blendMode, Function<P, Color4> color, Function<P, Vec2> size, Function<Particle, Double> rotation, String texture) {
        super(blendMode, color, size, texture);
        this.rotation = rotation;
    }

    @Override
    public void draw(P p) {
        Vec2 size = this.size.apply(p);
        double rotation = this.rotation.apply(p);
        Vec2 size1 = size.rotate(rotation);
        Vec2 size2 = size.multiply(new Vec2(-1, 1)).rotate(rotation);
        color.apply(p).glColor();
        Graphics2D.drawSpriteFast(texture, p.pos.subtract(size1), p.pos.subtract(size2), p.pos.add(size1), p.pos.add(size2));
    }

}
