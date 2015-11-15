package core;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class EventStream extends Destructible {

    private final Map<Destructible, Runnable> toCall = new LinkedHashMap();

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
        new ArrayList<>(toCall.values()).forEach(Runnable::run);
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
        return toSignal(count).forEach(i -> count.set(0));
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

    public EventStream onEvent(Runnable r) {
        return with(new EventStream(), s -> {
            r.run();
            s.sendEvent();
        });
    }

    public <R> Signal<R> reduce(R r, UnaryOperator<R> f) {
        return with(new Signal<>(r), s -> s.edit(f));
    }

    public EventStream throttle(double interval) {
        Signal<Double> time = with(Core.time(), t -> t.set(0.));
        return time.filter(t -> t > interval).forEach(t -> time.set(t - interval));
    }

    public EventStream toEventStream() {
        return with(new EventStream(), EventStream::sendEvent);
    }
}
