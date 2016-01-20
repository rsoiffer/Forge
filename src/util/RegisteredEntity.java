package util;

import engine.AbstractEntity;
import java.util.*;
import java.util.stream.Collectors;

public abstract class RegisteredEntity extends AbstractEntity {

    private static final Map<Class<? extends RegisteredEntity>, List<RegisteredEntity>> ALL = new HashMap();

    @Override
    public void create() {
        if (!ALL.containsKey(getClass())) {
            ALL.put(getClass(), new LinkedList());
        }
        ALL.get(getClass()).add(this);
        createInner();
    }

    protected abstract void createInner();

    @Override
    public void destroy() {
        ALL.get(getClass()).remove(this);
        super.destroy();
    }

    public static <T extends RegisteredEntity> List<T> getAll(Class<T> c) {
        if (ALL.containsKey(c)) {
            return (List) ALL.get(c);
        } else {
            return new LinkedList();
        }
    }

    public static <T extends RegisteredEntity> List<T> getAll(Class<? extends T>... a) {
        return (List) Arrays.stream(a).flatMap(c -> getAll(c).stream()).collect(Collectors.toList());
    }
}
