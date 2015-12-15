package util;

import java.nio.FloatBuffer;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import org.lwjgl.BufferUtils;

public abstract class Util {

    public static double clamp(double val, double min, double max) {
        return Math.max(min, Math.min(max, val));
    }

    public static void forRange(int start, int end, Consumer<Integer> c) {
        for (int i = start; i < end; i++) {
            c.accept(i);
        }
    }

    public static void forRange(int start1, int end1, int start2, int end2, BiConsumer<Integer, Integer> c) {
        forRange(start1, end1, i1 -> forRange(start2, end2, i2 -> c.accept(i1, i2)));
    }

    public static FloatBuffer floatBuffer(double... vals) {
        FloatBuffer r = BufferUtils.createFloatBuffer(vals.length);
        for (double d : vals) {
            r.put((float) d);
        }
        r.flip();
        return r;
    }

    public static void repeat(int num, Consumer<Integer> c) {
        for (int i = 0; i < num; i++) {
            c.accept(i);
        }
    }

    public static void repeat(int num, Runnable r) {
        for (int i = 0; i < num; i++) {
            r.run();
        }
    }
}
