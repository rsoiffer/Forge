package core;

import graphics.Window;
import graphics.loading.FontContainer;
import java.io.File;
import org.lwjgl.opengl.Display;

public abstract class Core {

    public static final Signal<Double> update = new Signal(0.);
    public static int speed = 60;
    public static double timeMult = 1;

    public static void init() {
        System.setProperty("org.lwjgl.librarypath", new File("natives").getAbsolutePath());
        Window.initialize(1200, 800, "So how are you today?");
        FontContainer.init();
    }

    public static void run() {
        long lastStep = System.nanoTime();
        while (!Display.isCloseRequested()) {
            //Timing
            long now = System.nanoTime();
            double deltaTime = (now - lastStep) * 0.000000001 * timeMult;
            lastStep = now;
            //Update
            update.set(deltaTime);
            //Graphics
            Display.update();
            Display.sync(speed);
        }
    }

    //Time utility functions
    public static EventStream delay(double delay, EventStream e) {
        return e.with(new EventStream(), s -> timer(delay, () -> s.sendEvent()));
    }

    public static <R> Signal<R> delay(double delay, Signal<R> e) {
        return e.with(new Signal(null), s -> {
            R r = e.get();
            timer(delay, () -> s.set(r));
        });
    }

    public static EventStream interval(double interval) {
        Signal<Double> time = time();
        return time.filter(t -> t > interval).forEach(t -> time.set(t - interval));
    }

    public static Signal<Double> time() {
        return update.reduce(0., (dt, t) -> t + dt);
    }

    public static void timer(double delay, Runnable r) {
        time().filter(t -> t > delay).first(1).onEvent(r);
    }
}
