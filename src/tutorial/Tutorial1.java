package tutorial;

import engine.Core;
import graphics.Window2D;
import util.Color4;

public class Tutorial1 {

    //Everybody loves main methods
    public static void main(String[] args) {
        /*
        Welcome to my series of tutorials on how to use my Functional Reactive Game Engine (FoRGE).
        Let's start by making a basic window appear on the screen.
        Core.init() creates a window on the screen and generally starts the game.
         */
        Core.init();

        /*
        The screen would normally be all black or something, so let's make it a little more interesting.
        Window2D.background stores the background color of the screen.
        Color4 is a class that represents a color. Color4.RED is a premade color for red.
         */
        Window2D.background = Color4.RED;

        /*
        The game's set up and ready to run, so we use Core.run() to start the game. It handles updating the game and refreshing the screen.
        Core.run() should almost always be the last line of code in the main method.
         */
        Core.run();
    }
}
