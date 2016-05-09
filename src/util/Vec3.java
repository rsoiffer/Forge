package util;

import java.nio.FloatBuffer;
import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;
import static org.lwjgl.opengl.GL11.*;

public class Vec3 {

    public static final Vec3 ZERO = new Vec3(0);

    public final double x;
    public final double y;
    public final double z;

    public Vec3(double a) {
        this(a, a, a);
    }

    public Vec3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec3(double[] coords) {
        this(coords[0], coords[1], coords[2]);
    }

    public Vec3 add(Vec3 other) {
        return new Vec3(x + other.x, y + other.y, z + other.z);
    }

    public Vec3 clamp(Vec3 LL, Vec3 UR) {
        double nx = x;
        double ny = y;
        double nz = z;
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
        if (nz < LL.z) {
            nz = LL.z;
        } else if (nz > UR.z) {
            nz = UR.z;
        }
        return new Vec3(nx, ny, nz);
    }

    public boolean containedBy(Vec3 v1, Vec3 v2) {
        int q1 = v1.quadrantXY(this);
        int q2 = v2.quadrantXY(this);
        boolean con = q1 != q2 && q1 % 2 == q2 % 2;

        q1 = v1.quadrantXZ(this);
        q2 = v2.quadrantXZ(this);
        con &= q1 != q2 && q1 % 2 == q2 % 2;
        return con;
    }

    public Vec3 cross(Vec3 other) {
        return new Vec3(y * other.z - z * other.y, z * other.x - x * other.z, x * other.y - y * other.x);
    }

    public double direction() {
        return Math.atan2(y, x);
    }

    public double direction2() {
        return Math.atan2(z, Math.sqrt(x * x + y * y));
    }

    public Vec3 divide(double d) {
        return new Vec3(x / d, y / d, z / d);
    }

    public Vec3 divide(Vec3 v) {
        return new Vec3(x / v.x, y / v.y, z / v.z);
    }

    public double dot(Vec3 other) {
        return x * other.x + y * other.y + z * other.z;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Vec3) {
            Vec3 v = (Vec3) o;
            return x == v.x && y == v.y && z == v.z;
        }
        return false;
    }

    public static Vec3 fromPolar(double r, double t, double p) {
        return new Vec3(r * Math.cos(t) * Math.sin(p), r * Math.sin(t) * Math.sin(p), r * Math.cos(p));
    }

    public void glNormal() {
        glNormal3d(x, y, z);
    }

    public void glTexCoord() {
        glTexCoord3d(x, y, z);
    }

    public void glVertex() {
        glVertex3d(x, y, z);
    }

    public Vec3 interpolate(Vec3 other, double amt) {
        return multiply(amt).add(other.multiply(1 - amt));
    }

    public double length() {
        return Math.sqrt(lengthSquared());
    }

    public double lengthSquared() {
        return x * x + y * y + z * z;
    }

    public Vec3 multiply(double d) {
        return new Vec3(x * d, y * d, z * d);
    }

    public Vec3 multiply(Vec3 other) {
        return new Vec3(x * other.x, y * other.y, z * other.z);
    }

    public Vec3 normalize() {
        final double len = length();
        if (len == 0) {
            return new Vec3(1, 0, 0);
        }
        return multiply(1 / len);
    }

    public Vec3 perComponent(UnaryOperator<Double> u) {
        return new Vec3(u.apply(x), u.apply(y), u.apply(z));
    }

    public Vec3 perComponent(Vec3 other, BinaryOperator<Double> u) {
        return new Vec3(u.apply(x, other.x), u.apply(y, other.y), u.apply(z, other.z));
    }

    public int quadrantXY(Vec3 other) {
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

    public int quadrantYZ(Vec3 other) {
        if (other.y >= y) {
            if (other.z >= z) {
                return 1;
            } else {
                return 4;
            }
        } else if (other.z >= z) {
            return 2;
        } else {
            return 3;
        }
    }

    public int quadrantXZ(Vec3 other) {
        if (other.x >= x) {
            if (other.z >= z) {
                return 1;
            } else {
                return 4;
            }
        } else if (other.z >= z) {
            return 2;
        } else {
            return 3;
        }
    }

    public static Vec3 randomCircle(double r) {
        return randomShell(r * Math.random());
    }

    public static Vec3 randomShell(double r) {
        return fromPolar(r, 2 * Math.PI * Math.random(), Math.PI * Math.random() - Math.PI / 2);
    }

    public static Vec3 randomSquare(double r) {
        return new Vec3(Math.random() * 2 * r - r, Math.random() * 2 * r - r, Math.random() * 2 * r - r);
    }

    public Vec3 reverse() {
        return new Vec3(-x, -y, -z);
    }

    public Vec3 subtract(Vec3 other) {
        return new Vec3(x - other.x, y - other.y, z - other.z);
    }

    public double[] toArray() {
        return new double[]{x, y, z};
    }

    public FloatBuffer toFloatBuffer() {
        return Util.floatBuffer(x, y, z);
    }

    public Vec3Polar toPolar() {
        return new Vec3Polar(length(), direction(), direction2());
    }

    @Override
    public String toString() {
        return "(" + (float) x + ", " + (float) y + ", " + (float) z + ")";
    }

    public Vec2 toVec2() {
        return new Vec2(x, y);
    }

    public Vec3 withLength(double l) {
        if (l == 0.0) {
            return ZERO;
        }
        return multiply(l / length());
    }

    public Vec3 withX(double newx) {
        return new Vec3(newx, y, z);
    }

    public Vec3 withY(double newy) {
        return new Vec3(x, newy, z);
    }

    public Vec3 withZ(double newZ) {
        return new Vec3(x, y, newZ);
    }
}
