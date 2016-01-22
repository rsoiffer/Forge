package util;

public class Pair {

    public final int x, y;

    public Pair(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        return x == ((Pair) obj).x && y == ((Pair) obj).y;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
