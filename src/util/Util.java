package util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import org.lwjgl.BufferUtils;

public abstract class Util {

    public static double clamp(double val, double min, double max) {
        return Math.max(min, Math.min(max, val));
    }

    public static String fileToString(String filename) {
        StringBuilder source = new StringBuilder();
        try {
            FileInputStream in = new FileInputStream(filename);
            BufferedReader reader;
            reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String line;
            while ((line = reader.readLine()) != null) {
                source.append(line).append('\n');
            }
            reader.close();
            in.close();
        } catch (Exception e) {
            Log.error("Could not read file " + filename + ": " + e);
        }
        return source.toString();
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
