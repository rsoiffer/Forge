package util;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public abstract class Util {

    public static void forRange(int start, int end, Consumer<Integer> c) {
        for (int i = start; i < end; i++) {
            c.accept(i);
        }
    }

    public static void forRange(int start1, int end1, int start2, int end2, BiConsumer<Integer, Integer> c) {
        forRange(start1, end1, i1 -> forRange(start2, end2, i2 -> c.accept(i1, i2)));
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
