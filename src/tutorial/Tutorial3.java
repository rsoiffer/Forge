package tutorial;

import engine.Core;
import engine.EventStream;
import engine.Input;
import graphics.Window2D;
import util.Color4;

public class Tutorial3 {

    public static void main(String[] args) {
        Core.init();
        Window2D.background = Color4.RED;

        //We're using the leftMouse EventStream again in this example.
        EventStream leftMouse = Input.whenMouse(0, true);

        /*
        That last example was waaay too much code to just print "hi".
        We even had to make a whole new clase for just one line of code!
        But there's a better way...

        LAMBDA EXPRESSIONS!!!

        I like these things.
        () -> System.out.println("hi") is just a fancy shorthand for everything in the last example
        Java makes a new class that implements Runnable for you, and automatically fills it out with whatever you want.
        The run() method in the Runnable you make is just the code in the curly braces.
         */
        Runnable myHiPrinter = () -> {
            System.out.println("hi");
        };
        leftMouse.onEvent(myHiPrinter);

        /*
        If you want to be even more concise, you can put all of this code on one line.
        This line of code prints "ok" whenever you click the Right Mouse Button.
        If you look closely, the code is almost the same, except that we don't bother giving names to variabes that we only use once.
         */
        Input.whenMouse(1, true).onEvent(() -> System.out.println("ok"));

        Core.run();
    }
}
