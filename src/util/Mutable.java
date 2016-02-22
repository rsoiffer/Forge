package util;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class Mutable<O> implements Consumer<O>, Supplier<O> {

    public O o;

    public Mutable(O o) {
        this.o = o;
    }

    @Override
    public void accept(O o) {
        this.o = o;
    }

    @Override
    public O get() {
        return o;
    }
}
