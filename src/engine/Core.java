package engine;

import graphics.Window2D;
import graphics.Window3D;
import graphics.loading.FontContainer;
import java.io.File;
import java.util.Map;
import java.util.TreeMap;
import org.lwjgl.opengl.Display;
import util.Mutable;

public abstract class Core {

    private static final Map<Double, Signal<Double>> updateLayers = new TreeMap();
    /**
     * A Signal<Double> that updates continuously with the delta-time since the
     * last update. Is equivalent to Core.updateLayer(0).
     */
    public static final Signal<Double> update = updateLayer(0);
    private static final Map<Double, EventStream> renderLayers = new TreeMap();
    /**
     * An EventStream that updates 60 times per second and contains all the
     * drawing code for the game. Is equivalent to Core.renderLayer(0).
     */
    public static final EventStream render = renderLayer(0);
    /**
     * The number of times the game should try to update each second.
     */
    public static int speed = 60;
    /**
     * The delta-time value in the update signals is multiplied by this
     * variable.
     */
    public static double timeMult = 1;
    /**
     * The maximum delta-time value (before timeMult) in the update signals.
     */
    public static double timeCap = .1;
    /**
     * The minimum delta-time value (before timeMult) in the update signals.
     */
    public static double timeMin = .01;

    /**
     * The default width (in pixels) of the window. Only change this before
     * calling Core.init().
     */
    public static int screenWidth = 1200;
    /**
     * The default height (in pixels) of the window. Only change this before
     * calling Core.init().
     */
    public static int screenHeight = 800;
    /**
     * The default title of the window.
     */
    public static String title = "So how are you today?";

    /**
     * Whether the game is in 3D or 2D. Only change this before calling
     * Core.init(). If the game is only partially 3D, set it to true.
     */
    public static boolean is3D;
    /**
     * Whether the game is currently running. This is likely a useless variable.
     */
    public static boolean running;

    /**
     * Initializes the game. This function should be the first line of code for
     * most projects. It sets up the window, user input, and opengl.
     */
    public static void init() {
        running = true;
        System.setProperty("org.lwjgl.librarypath", new File("natives").getAbsolutePath());
        if (!is3D) {
            Window2D.initialize(screenWidth, screenHeight, title);
        } else {
            Window3D.initialize(screenWidth, screenHeight, title);
        }
        FontContainer.init();
    }

    /**
     * Returns a version of Core.render that updates at a certain layer relative
     * to Core.render. Higher layers happen later, lower layers happen earlier.
     * Core.render and all of the default graphics functions are layer 0.
     *
     * @param d The layer to render on.
     * @return The EventStream that updates on that layer.
     */
    public static EventStream renderLayer(double d) {
        EventStream current = renderLayers.get(d);
        if (current == null) {
            current = new EventStream();
            renderLayers.put(d, current);
        }
        return current;
    }

    /**
     * Tells the game to start running. This function returns when the game
     * exits. This function should be the last line of code for almost all
     * projects. It handles the update layers, the render layers, and updating
     * the display.
     */
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
            renderLayers.values().forEach(EventStream::sendEvent);
            Display.update();
        }
    }

    /**
     * Returns a version of Core.update that updates at a certain layer relative
     * to Core.update. Higher layers happen later, lower layers happen earlier.
     * Core.update and all of the default functions are layer 0.
     *
     * @param d The layer to update on.
     * @return The Signal<Double> that updates on that layer.
     */
    public static Signal<Double> updateLayer(double d) {
        Signal<Double> current = updateLayers.get(d);
        if (current == null) {
            current = new Signal(0.);
            updateLayers.put(d, current);
        }
        return current;
    }

    //Time utility functions
    /**
     * Delays a given EventStream by a certain amount of time. Each event on the
     * first eventstream is sent to the returned eventstream after the given
     * delay. If the time delay is 0, the eventstream is delayed by a single
     * step.
     *
     * @param delay The amount of time (in seconds) to delay the eventstream by.
     * @param e The eventstream to delay.
     * @return The delayed copy of the eventstream.
     */
    public static EventStream delay(double delay, EventStream e) {
        return e.with(new EventStream(), s -> timer(delay, () -> s.sendEvent()));
    }

    /**
     * Delays a given Signal by a certain amount of time. Each event on the
     * first signal is sent to the returned signal after the given delay. If the
     * time delay is 0, the signal is delayed by a single step.
     *
     * @param delay The amount of time (in seconds) to delay the signal by.
     * @param e The signal to delay.
     * @return The delayed copy of the signal.
     */
    public static <R> Signal<R> delay(double delay, Signal<R> e) {
        return e.with(new Signal(null), s -> {
            R r = e.get();
            timer(delay, () -> s.set(r));
        });
    }

    /**
     * Returns an EventStream that fires events separated by a given time
     * interval (in seconds).
     *
     * @param interval The time interval by which the events are separated.
     * @return The eventstream that fires once each interval.
     */
    public static EventStream interval(double interval) {
        Signal<Double> time = time();
        return time.filter(t -> t > interval).forEach(t -> time.set(t - interval));
    }

    /**
     * Returns a Signal<Double> that updates each step with the amount of time
     * (in seconds) since it was created. It is possible to store the value of
     * Core.time() and reset its current time value, and it will continue
     * counting from that value.
     *
     * @return The signal that counts the time.
     */
    public static Signal<Double> time() {
        return update.reduce(0., (dt, t) -> t + dt);
    }

    /**
     * Delays a certain piece of code by a given time (in seconds). The runnable
     * is executed after the given time passes.
     *
     * @param delay The amount of time to delay the runnable by.
     * @param r The code to run after the time delay has passed.
     * @return A handle to the internal Core.time() signal used to measure the
     * passage of time. This can be edited to make the timer take longer.
     */
    public static Signal<Double> timer(double delay, Runnable r) {
        Signal<Double> time = time();
        time.filter(t -> t > delay).first(1).onEvent(r);
        return time;
    }
}
