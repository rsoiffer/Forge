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

    public void sendEvent() {
        ArrayList<Runnable> toRun;
        toRun = new ArrayList<>(toCall.values());
        toRun.forEach(Runnable::run);
    }

    <R> Signal<R> toSignal(Supplier<R> r) {
        return with(new Signal(r.get()), s -> s.set(r.get()));
    }

    <R> Signal<R> toSignal(Signal<R> r) {
        return r.addChild(with(new Signal(r.get()), s -> s.set(r.get())));
    }

    <R extends EventStream> R with(R r, Consumer<R> c) {
        return addChild(r, () -> c.accept(r));
    }

    //Interesting functions
    public Signal<Integer> bufferCount(EventStream e) {
        Signal<Integer> count = count();
        return e.toSignal(count).forEach(i -> count.set(0));
    }

    public Signal<Integer> bufferCountThrottle(double interval) {
        return bufferCount(throttle(interval));
    }

    public EventStream combineEventStreams(EventStream... other) {
        EventStream newStr = toEventStream();
        for (EventStream o : other) {
            o.with(newStr, EventStream::sendEvent);
        }
        return newStr;
    }

    public Signal<Integer> count() {
        return reduce(0, i -> i + 1);
    }

    public Signal<Integer> countWithin(double interval) {
        return throttle(interval).with(count(), s -> s.set(0));
    }

    public EventStream filter_E(Signal<Boolean> p) {
        return filterElse_E(p, s -> {
        });
    }

    public EventStream filterElse_E(Signal<Boolean> p, Consumer<EventStream> c) {
        return with(new EventStream(), s -> {
            if (p.get()) {
                s.sendEvent();
            } else {
                c.accept(s);
            }
        });
    }

    public EventStream first_E(int n) {
        return filterElse_E(count().map(i -> i <= n), EventStream::destroy);
    }

    public EventStream limit(double interval) {
        return with(Core.time(), t -> {
            if (t.get() > interval) {
                t.set(0.);
            }
        }).map(t -> t > interval).distinct().filter(b -> !b);
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
}
