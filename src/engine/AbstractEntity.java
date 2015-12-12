package engine;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class AbstractEntity extends Destructible {

    private final Map<String, Destructible> components = new HashMap();

    public void add(Destructible... a) {
        Arrays.asList(a).forEach(d -> d.addChild(this));
    }

    public <R extends Destructible> R addChild(R child, String name) {
        addChild(child);
        components.put(name, child);
        return child;
    }

    public abstract void create();

    public static AbstractEntity from(Consumer<AbstractEntity> create) {
        return new LAE(create);
    }

    public <R> Signal<R> get(String name, Class<R> c) {
        Destructible r = components.get(name);
        if (r == null) {
            throw new RuntimeException("Missing component " + name);
        }
        return (Signal) r;
    }

    public <S extends Supplier> S getOrDefault(String name, S s) {
        Destructible r = components.get(name);
        if (r == null) {
            return s;
        }
        return (S) r;
    }

    public void onRender(Runnable r) {
        Core.render.onEvent(r).addChild(this);
    }

    public void onUpdate(Consumer<Double> c) {
        Core.update.forEach(c).addChild(this);
    }

    public static class LAE extends AbstractEntity {

        private final Consumer<AbstractEntity> create;

        public LAE(Consumer<AbstractEntity> create) {
            this.create = create;
        }

        @Override
        public void create() {
            create.accept(this);
        }
    }
}
