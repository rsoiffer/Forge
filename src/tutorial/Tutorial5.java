package tutorial;

import engine.Core;
import engine.Signal;
import java.util.Scanner;

public class Tutorial5 {

    public static void main(String[] args) {
        /*
        No Core.init() this time - it's time to learn about Signals.

        For recap, EventStreams are like lists of events.
        Things can listen to the EventStream, and their run() method is called whenever there's an event.

        Signal actually extends EventStream, so Signal can do everything EventStream can and more.
        Signals are like EventStreams, but they also store data.
        Whenever a Signal sends an event, it also sends the data associated with that event.

        For example, the Core.update Signal represents each update step in the game,
        just like how the Core.render EventStream represents each render step in the game.
        However, while render steps are always spaced equally far apart, update steps can have any amount of time between them.
        So, whenever there's an event on the Core.update Signal, it also sends the amount of time that passed since the last update.
        That amount of time is stored in a double, so Core.update is a Signal of type Double.
         */
        Signal<Double> update = Core.update; //See? No error. Core.update is of type Signal<Double>.
        /*
        The triangle brackets say the type of the data that is associated with the Signal.
        In the previous example, we had a Signal<Vec2> because the data stored in the Signal needed to be a Vec2.
        The triangle bracket notation is called generics, and it's common whereever you have to store data.
        If you don't know how generics work, feel free to google them or just ignore them. They don't really matter.
         */

 /*
        Here's a simple example of why Signals can be useful.
        Imagine we have a Signal<String> that sends an event whenever the player types a word.
        We want to print a nice message when they type the word "potato".
         */
        Signal<String> wordSignal = new Signal("");

        wordSignal.forEach((String word) -> {
            /*
            This next bit of code is executed whenever we get a new word.
            Signal.forEach() is like EventStream.onEvent(), except that it also gives us the data associated with the event.
            If the word is equal to "potato", we want to print a nice message.
            Otherwise, we want to print a message telling the user they got it wrong.
             */
            if (word.equals("potato")) {
                System.out.println("YAY");
            } else {
                System.out.println("Wrong word: " + word + " is not equal to potato");
            }
        });

        /*
        We also want to keep track of the numbers of times the player has said "potato" and print it too.
        We could make another variable to keep track of it, but I'm lazy.
        Signals have a bunch of useful functions to do things.
        In this case, the two functions we'll use are called filter() and count().
        filter() turns the Signal into another Signal with just the things we care about.
        count() counts all the things in a Signal.

        The filter() function removes all the words that are not "potato",
        because word.equals("potato") is true only when word is "potato".
        The count() function turns the Signal<String> that stores words into a Signal<Integer> that counts words.
        We then use the forEach() function to print the count (like above).
         */
        wordSignal.filter((String word) -> word.equals("potato"))
                .count().forEach((Integer i) -> System.out.println(i + " points"));

        //The rest of the code just uses a Scanner to read user input.
        Scanner in = new Scanner(System.in);
        while (true) {
            wordSignal.set(in.nextLine());
        }
    }
}
