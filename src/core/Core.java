package core;

import graphics.Window;
import graphics.loading.FontContainer;
import java.io.File;
import java.util.Map;
import java.util.TreeMap;
import org.lwjgl.opengl.Display;
import util.Mutable;

public abstract class Core {

    private static final Map<Double, Signal<Double>> updateLayers = new TreeMap();
    public static final Signal<Double> update = updateLayer(0);
    private static final Map<Double, EventStream> renderLayers = new TreeMap();
    public static final EventStream render = renderLayer(0);
    public static int speed = 60;
    public static double timeMult = 1;
    public static double timeCap = .1;
    public static double timeMin = .001;

    public static void init() {
        System.setProperty("org.lwjgl.librarypath", new File("natives").getAbsolutePath());
        Window.initialize(1200, 800, "So how are you today?");
        FontContainer.init();
    }

    public static EventStream renderLayer(double d) {
        EventStream current = renderLayers.get(d);
        if (current == null) {
            current = new EventStream();
            renderLayers.put(d, current);
        }
        return current;
    }

    public static void run() {
        long lastStep = System.nanoTime();
        Mutable<Boolean> updateScreen = new Mutable(true);
        while (!Display.isCloseRequested()) {
            //Timing
            long now;
            double deltaTime;
            do {
                now = System.nanoTime();
                deltaTime = (now - lastStep) * 0.000000001 * timeMult;
            } while (deltaTime < timeMin);
            if (timeCap < deltaTime) {
                deltaTime = timeCap;
            }
            lastStep = now;
            //Update
            double dt = deltaTime;
            updateLayers.values().forEach(s -> s.set(dt));
            //Graphics
            if (updateScreen.o) {
                renderLayers.values().forEach(EventStream::sendEvent);
                updateScreen.o = false;
                Display.update();
                new Thread(() -> {
                    //Display.sync(speed);
                    updateScreen.o = true;
                }).start();
            }
        }
    }

    public static Signal<Double> updateLayer(double d) {
        Signal<Double> current = updateLayers.get(d);
        if (current == null) {
            current = new Signal(0);
            updateLayers.put(d, current);
        }
        return current;
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

    public static Signal<Double> timer(double delay, Runnable r) {
        Signal<Double> time = time();
        time.filter(t -> t > delay).first(1).onEvent(r);
        return time;
    }
}
