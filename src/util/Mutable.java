package util;

import java.util.function.Supplier;

public class Mutable<O> implements Supplier<O> {

    public O o;

    public Mutable(O o) {
        this.o = o;
    }

    @Override
    public O get() {
        return o;
    }
}
