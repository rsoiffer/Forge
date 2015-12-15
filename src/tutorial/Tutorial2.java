package tutorial;

import engine.Core;
import engine.EventStream;
import engine.Input;
import graphics.Window2D;
import util.Color4;

public class Tutorial2 {

    public static void main(String[] args) {
        //Remember this line of code? It should usually be the first line of code in the game.
        Core.init();

        //We might as well keep the red background. I like it.
        Window2D.background = Color4.RED;

        /*
        Now that we have the very basics, let's start make our game more interesting.
        We want the game to print "hi" every time you click the screen.

        To do this, we use a class called an EventStream.
        EventStreams are classes that can have events happen on them and that can have things listening to them.
        Whenever an event happens on an EventStream, all the things that are listening to the EventStream are notified of the event.

        You can think of EventStreams as like an arrow with a bunch of dots on it.
        The arrow represents time, and each dot represents one event.
        The EventStream is the list of all of the events, it's the arrow on which the dots sit.

        We get the EventStream that corresponds to the left mouse button.
        Each event in the EventStream is one time that the mouse is clicked.
        The 0 represents the Left Mouse Button (1 is right, 2 is middle).
        The true represents that we care about when the mouse is pushed down, not when the mouse is released.
         */
        EventStream leftMouse = Input.whenMouse(0, true);

        /*
        Now that we have an EventStream, we want something to happen on each event.
        The EventStream.onEvent() function lets us assign some bit of code to happen on each event.

        That bit of code is stored in a class that implements the Runnable interface.
        Whenever an event happens, that class's run() method is called.

        I made a short class below called HiPrinter. It prints "hi" whenever the run() method is called.
        Whenever a Left Mouse event happens, the run() method of HiPrinter is called.
         */
        HiPrinter myHiPrinter = new HiPrinter();
        leftMouse.onEvent(myHiPrinter);

        //Look familiar at all? It should always be the last line of code in the game.
        Core.run();
    }

    public static class HiPrinter implements Runnable {

        /*
        HiPrinter implements the Runnable interface.
        This just means that HiPrinter must have the method public void run().
        If you don't know how interfaces work, feel free to google them or just ignore them. They don't really matter.
         */
        @Override
        public void run() {
            //Whenever the run method is called, print "hi"
            System.out.println("hi");
        }
    }
}
