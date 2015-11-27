package game;

import core.Core;

public class Test {

    public static void main(String[] args) {
        Core.init();

        new Player().create();

        Core.run();
    }
}
