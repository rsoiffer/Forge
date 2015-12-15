package util;

public class Vec3Polar {

    public final double r;
    public final double t;
    public final double p;

    public Vec3Polar(double r, double t, double p) {
        this.r = r;
        this.t = t;
        this.p = p;
    }

    @Override
    public String toString() {
        return "(" + r + ", " + t + ", " + p + ")";
    }

    public Vec3 toVec3() {
        return new Vec3(r * Math.cos(t) * Math.cos(p), r * Math.sin(t) * Math.cos(p), r * Math.sin(p));
    }

    public Vec3Polar withR(double r) {
        return new Vec3Polar(r, t, p);
    }

    public Vec3Polar withT(double t) {
        return new Vec3Polar(r, t, p);
    }

    public Vec3Polar withP(double p) {
        return new Vec3Polar(r, t, p);
    }
}
