package core;

import java.util.HashSet;
import java.util.Set;

public abstract class Destructible {

    private static long maxID = 0;
    private final long id = maxID++;

    final Set<Destructible> parents = new HashSet();
    final Set<Destructible> children = new HashSet();

    public <R extends Destructible> R addChild(R child) {
        if (!children.contains(child)) {
            children.add(child);
            child.parents.add(this);
        }
        return child;
    }

    protected <R extends Destructible> R addParent(R parent) {
        if (!parents.contains(parent)) {
            parents.add(parent);
            parent.children.add(this);
        }
        return parent;
    }

    public void destroy() {
        children.clear();
        parents.forEach(p -> p.removeChild(this));
        parents.clear();
    }

    protected void removeChild(Destructible d) {
        children.remove(d);
        if (children.isEmpty()) {
            destroy();
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName().charAt(0) + ":" + id;
    }
}
