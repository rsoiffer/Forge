package game;

import core.Core;
import util.Vec2;

public class Test {

    public static void main(String[] args) {
        Core.init();

        Player yucky = new Player();
        yucky.create();
        yucky.get("position", Vec2.class).set(new Vec2(100, 100));
        new Player().create();

        Core.run();
    }
}
