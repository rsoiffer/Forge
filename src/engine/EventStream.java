package engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class EventStream extends Destructible {

    private final Map<Destructible, Runnable> toCall = Collections.synchronizedMap(new LinkedHashMap());

    private <R extends Destructible> R addChild(R child, Runnable r) {
        addChild(child);
        toCall.put(child, r);
        return child;
    }

    @Override
    protected void removeChild(Destructible d) {
        toCall.remove(d);
        super.removeChild(d);
    }

    /**
     * Sends an event to this eventstream, which executes each of the runnables
     * that is listening to this eventstream.
     */
    public void sendEvent() {
        ArrayList<Runnable> toRun;
        toRun = new ArrayList<>(toCall.values());
        toRun.forEach(Runnable::run);
    }

    /**
     * Converts this eventstream to a signal, with values based on the value of
     * a given signal.
     *
     * @param <R> The type of the signal.
     * @param r The signal to draw values from.
     * @return A signal with events at the same time as this eventstream and
     * with values from a given signal.
     */
    public <R> Signal<R> toSignal(Signal<R> r) {
        return r.addChild(with(new Signal(r.get()), s -> s.set(r.get())));
    }

    /**
     * Adds an EventStream as a child, and also executes a given consumer
     * whenever this eventstream has an event. Is used as a helper function for
     * other functions in eventstream and signal.
     *
     * @param <R> The type of the child eventstream.
     * @param r The child eventstream.
     * @param c The consumer to execute whenever this eventstream has an event.
     * @return The eventstream that was added as a child.
     */
    <R extends EventStream> R with(R r, Consumer<R> c) {
        return addChild(r, () -> c.accept(r));
    }

    //Interesting functions
    /**
     * Returns a signal that counts how many times this eventstream fires in
     * between each event of a given eventstream.
     *
     * @param e The eventstream to count event between.
     * @return A signal that counts how many times this eventstream fires.
     */
    public Signal<Integer> bufferCount(EventStream e) {
        Signal<Integer> count = count();
        return e.toSignal(count).forEach(i -> count.set(0));
    }

    /**
     * Calls bufferCount on this eventstream throttled by a given interval.
     * Calls bufferCount(throttle(interval)). It counts how many times this
     * eventstream fires in quick succession.
     *
     * @param interval The interval to throttle by.
     * @return A signal that counts how many times this eventstream fires in
     * quick succession.
     */
    public Signal<Integer> bufferCountThrottle(double interval) {
        return bufferCount(throttle(interval));
    }

    /**
     * Combines many eventstreams into a single eventstream that fires whenever
     * any of the component eventstreams fire.
     *
     * @param other The list of eventstreams to combine with this eventstream.
     * @return An eventstream that fires whenever any of the component
     * eventstreams fire.
     */
    public EventStream combineEventStreams(EventStream... other) {
        EventStream newStr = toEventStream();
        for (EventStream o : other) {
            o.with(newStr, EventStream::sendEvent);
        }
        return newStr;
    }

    /**
     * Returns a signal that counts how many times this eventstream has fired.
     * It can be reset to a new value, and will continue counting from that
     * value.
     *
     * @return A signal that counts how many times this eventstream has fired.
     */
    public Signal<Integer> count() {
        return reduce(0, i -> i + 1);
    }

    /**
     * Counts how many times the eventstream has fired within a given time
     * interval, then resets at the end of the interval.
     *
     * @param interval The length of time (in seconds) after which to reset the
     * count.
     * @return A signal that counts up whenever this eventstream fires an event.
     */
    public Signal<Integer> countWithin(double interval) {
        return throttle(interval).with(count(), s -> s.set(0));
    }

    /**
     * Returns an eventstream that only contains the events when a supplier is
     * true.
     *
     * @param p The supplier to check.
     * @return
     */
    public EventStream filter(Supplier<Boolean> p) {
        return with(new EventStream(), s -> {
            if (p.get()) {
                s.sendEvent();
            }
        });
    }

    public EventStream first(int n) {
        return until(count().map(i -> i <= n));
    }

    public EventStream limit(double interval) {
        return with(Core.time(), t -> {
            if (t.get() > interval) {
                t.set(0.);
            }
        }).map(t -> t > interval).distinct().filter(b -> !b);
    }

    public <R> Signal<R> map(Supplier<R> r) {
        return with(new Signal(r.get()), s -> s.set(r.get()));
    }

    public EventStream onEvent(Runnable r) {
        return with(new EventStream(), s -> {
            r.run();
            s.sendEvent();
        });
    }

    public <R> Signal<R> reduce(R r, UnaryOperator<R> f) {
        return with(new Signal(r), s -> s.edit(f));
    }

    public EventStream throttle(double interval) {
        return with(Core.time(), t -> t.set(0.)).map(t -> t > interval).distinct().filter(b -> b);
    }

    public EventStream toEventStream() {
        return with(new EventStream(), EventStream::sendEvent);
    }

    public EventStream until(Supplier<Boolean> p) {
        return with(new EventStream(), e -> {
            if (p.get()) {
                e.sendEvent();
            } else {
                e.destroy();
            }
        });
    }
}
