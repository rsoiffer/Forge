package engine;

import java.util.HashSet;
import java.util.Set;

public abstract class Destructible {

    private static long maxID = 0;
    private final long id = maxID++;
    private boolean destroyed;

    final Set<Destructible> parents = new HashSet();
    final Set<Destructible> children = new HashSet();

    /**
     * Adds a given Destructible as a child of this destructible. When this
     * destructible is destroyed, then the child destructible will also be
     * destroyed.
     *
     * @param <R> The exact type of the child destructible.
     * @param child The destructible to add as a child.
     * @return The destructible that was added as a child.
     */
    public <R extends Destructible> R addChild(R child) {
        if (!children.contains(child)) {
            children.add(child);
            child.parents.add(this);
        }
        return child;
    }

    /**
     * Adds a given Destructible as a parent of this destructible. When the
     * parent destructible is destroyed, then this destructible will also be
     * destroyed.
     *
     * @param <R> The exact type of the parent destructible.
     * @param parent The destructible to add as a parent.
     * @return The destructible that was added as a parent.
     */
    protected <R extends Destructible> R addParent(R parent) {
        if (!parents.contains(parent)) {
            parents.add(parent);
            parent.children.add(this);
        }
        return parent;
    }

    /**
     * Destroys this destructible. It will destroy all the children of this
     * destructible. It will remove this destructible as a child from all of its
     * parents, and if the parents have no more children, it destroys the
     * parents as well.
     */
    public void destroy() {
        if (!destroyed) {
            destroyed = true;
            children.forEach(d -> d.parents.remove(this));
            children.forEach(Destructible::destroy);
            children.clear();
            parents.forEach(p -> p.removeChild(this));
            parents.clear();
        }
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    /**
     * Removes a destructible from this destructible's list of children.
     *
     * @param d The destructible to remove as a child.
     */
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
