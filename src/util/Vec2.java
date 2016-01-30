package util;

import java.nio.FloatBuffer;
import static org.lwjgl.opengl.GL11.glTexCoord2d;
import static org.lwjgl.opengl.GL11.glVertex2d;

public class Vec2 {

    public static final Vec2 ZERO = new Vec2(0);

    public static double tol = 1E-14;
    public final double x;
    public final double y;

    public Vec2(double a) {
        this(a, a);
    }

    public Vec2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vec2 add(Vec2 other) {
        return new Vec2(x + other.x, y + other.y);
    }

    public Vec2 clamp(Vec2 LL, Vec2 UR) {
        double nx = x;
        double ny = y;
        if (nx < LL.x) {
            nx = LL.x;
        } else if (nx > UR.x) {
            nx = UR.x;
        }
        if (ny < LL.y) {
            ny = LL.y;
        } else if (ny > UR.y) {
            ny = UR.y;
        }
        return new Vec2(nx, ny);
    }

    public boolean containedBy(Vec2 v1, Vec2 v2) {
        int q1 = v1.quadrant(this);
        int q2 = v2.quadrant(this);
        return q1 != q2 && q1 % 2 == q2 % 2;
    }

    public double cross(Vec2 other) {
        return x * other.y - y * other.x;
    }

    public double direction() {
        return Math.atan2(y, x);
    }

    public Vec2 divide(double d) {
        return new Vec2(x / d, y / d);
    }

    public Vec2 divide(Vec2 v) {
        return new Vec2(x / v.x, y / v.y);
    }

    public double dot(Vec2 other) {
        return x * other.x + y * other.y;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Vec2) {
            Vec2 v = (Vec2) o;
            return (x - v.x) <= tol && (y - v.y) <= tol;
        }
        return false;
    }

    public static Vec2 fromPolar(double r, double t) {
        return new Vec2(r * Math.cos(t), r * Math.sin(t));
    }

    public void glTexCoord() {
        glTexCoord2d(x, y);
    }

    public void glVertex() {
        glVertex2d(x, y);
    }

    public Vec2 interpolate(Vec2 other, double amt) {
        return multiply(amt).add(other.multiply(1 - amt));
    }

    public double length() {
        return Math.sqrt(lengthSquared());
    }

    public double lengthSquared() {
        return x * x + y * y;
    }

    public Vec2 multiply(double d) {
        return new Vec2(x * d, y * d);
    }

    public Vec2 multiply(Vec2 other) {
        return new Vec2(x * other.x, y * other.y);
    }

    public Vec2 normal() {
        return new Vec2(-y, x);
    }

    public Vec2 normalize() {
        final double len = length();
        if (len == 0) {
            return new Vec2(1.0, 0.0);
        }
        return multiply(1 / len);
    }

    public int quadrant(Vec2 other) {
        if (other.x >= x) {
            if (other.y >= y) {
                return 1;
            } else {
                return 4;
            }
        } else if (other.y >= y) {
            return 2;
        } else {
            return 3;
        }
    }

    public static Vec2 randomCircle(double r) {
        return randomShell(r * Math.random());
    }

    public static Vec2 randomShell(double r) {
        return fromPolar(r, 2 * Math.PI * Math.random());
    }

    public static Vec2 randomSquare(double r) {
        return new Vec2(Math.random() * 2 * r - r, Math.random() * 2 * r - r);
    }

    public Vec2 reverse() {
        return new Vec2(-x, -y);
    }

    public Vec2 rotate(double t) {
        return new Vec2(x * Math.cos(t) - y * Math.sin(t), x * Math.sin(t) + y * Math.cos(t));
    }

    public Vec2 subtract(Vec2 other) {
        return new Vec2(x - other.x, y - other.y);
    }

    public FloatBuffer toFloatBuffer() {
        return Util.floatBuffer(x, y);
    }

    @Override
    public String toString() {
        return "(" + (float) x + ", " + (float) y + ")";
    }

    public Vec3 toVec3() {
        return new Vec3(x, y, 0);
    }

    public Vec2 withLength(double l) {
        if (l == 0.0) {
            return ZERO;
        }
        return multiply(l / length());
    }

    public Vec2 withX(double newx) {
        return new Vec2(newx, y);
    }

    public Vec2 withY(double newy) {
        return new Vec2(x, newy);
    }
}
