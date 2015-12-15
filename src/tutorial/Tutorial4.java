package tutorial;

import engine.Core;
import engine.EventStream;
import engine.Input;
import engine.Signal;
import graphics.Graphics2D;
import graphics.Window2D;
import util.Color4;
import util.Vec2;

public class Tutorial4 {

    public static void main(String[] args) {
        Core.init();
        Window2D.background = Color4.RED;

        /*
        Printing things is ok, but it doesn't make a very interesting game.
        Let's a circle appear whereever you click.
        Our first step is to make a variable that stores where the circle is.

        Vec2 is short for "Vector with 2 dimensions".
        Vec2s usually represent points on a coordinate plane or a screen.
        Vec2.ZERO is the origin of the coordinate plane.
        Vec2 can represent any value with both an x and a y part, including velocities, accelerations, and sizes.

        Why is it a Signal instead of a Vec2? What is a Signal? Why are there <><><><><?><><>< things?
        Doesn't matter.
        Deal with it.
        Signals will be explained later, pretend it's just a normal Vec2.
         */
        Signal<Vec2> circlePosition = new Signal(Vec2.ZERO);

        /*
        Now, we want the position to change to whereever the mouse is when you click.
        This is another example where we want to use the leftMouse EventStream.
        Instead of printing "hi" when you click, we update the circlePosition variable when you click.
         */
        EventStream leftMouse = Input.whenMouse(0, true);
        Runnable updateCirclePosition = () -> {
            //Input.getMouse() returns the current position of the mouse.
            Vec2 mousePos = Input.getMouse();
            /*
            We use the Signal.set() method to set the value of a Signal.
            This will be explained at some point.
             */
            circlePosition.set(mousePos);
        };
        leftMouse.onEvent(updateCirclePosition);

        /*
        The final step of our amazing circle game is to actually draw the circle on the screen.
        The game is split up into a bunch of discrete "steps" or "updates" - it's similar to Game Maker, if that helps.
        On each step, we want to draw a circle (if we only drew the circle once, it would disappear next step when the screen refreshed).
        Core.render is the EventStream that corresponds to the drawing step.
         */
        EventStream render = Core.render;
        Runnable drawCircle = () -> {
            /*
            To draw things, use Graphics2D.something()
            Graphics2D.fillEllipse() draws a solid ellipse or circle at a given position.
             */
            Vec2 position = circlePosition.get(); //We use the Signal.get() method to get the value of a Signal
            Vec2 size = new Vec2(40, 40); //This is an example of the Vec2 class storing data other than a position
            Color4 color = Color4.BLUE; //I like blue
            double detail = 20; //Computers usually draw circles as polygons with a bunch of sides. I chose 20.
            Graphics2D.fillEllipse(position, size, color, detail);
        };
        render.onEvent(drawCircle);

        /*
        Here's the code for a circle that continuously follows the mouse while the right mouse button is down.
        This is a demonstration that this example isn't as hard as it seems, because the code is actually only a few lines.
         */
        Signal<Vec2> circlePos2 = new Signal(Vec2.ZERO); //Make a new Signal to store the circle position.
        Input.whileMouseDown(1).onEvent(() -> circlePos2.set(Input.getMouse())); //While the right mouse is held down, set the circle position to the mouse position.
        Core.render.onEvent(() -> Graphics2D.fillEllipse(circlePos2.get(), new Vec2(20, 20), Color4.GREEN, 20)); //Each render, draw the circle.

        Core.run();
    }
}
