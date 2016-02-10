package examples.colordemo;

import examples.Premade2D;
import engine.AbstractEntity.LAE;
import engine.Core;
import engine.Input;
import engine.Signal;
import graphics.Graphics2D;
import graphics.Window2D;
import graphics.data.Sprite;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import util.Color4;
import util.Vec2;

public class ColorDemo {

    public static void main(String[] args) {
        Core.init();
        Window2D.background = Color4.gray(.5);

        //Show the fps
        Core.update.forEach(t -> Display.setTitle("FPS: " + 1 / t));

        //Draw the floor
        Core.render.onEvent(() -> Graphics2D.fillRect(Window2D.LL(),
                new Vec2(Window2D.viewSize.x, Window2D.viewSize.y / 2 - 16), Color4.BLACK));

        //Create the hero
        new LAE(hero -> {
            //Create the hero's variables
            Signal<Vec2> position = Premade2D.makePosition(hero);
            Signal<Vec2> velocity = Premade2D.makeVelocity(hero);
            Premade2D.makeGravity(hero, new Vec2(0, -1000));
            Signal<Sprite> sprite = Premade2D.makeSpriteGraphics(hero, "rock");

            //Set initial position
            position.set(new Vec2(0, 500));

            //Do all these things each update
            hero.onUpdate(dt -> { //dt is the change in time since the last step
                //Friction
                velocity.edit(v -> v.withX(v.x * Math.pow(.01, dt)));

                //Left-right movement
                if (Input.keySignal(Keyboard.KEY_A).get()) {
                    velocity.edit(v -> v.withX(v.x - dt * 1000));
                }
                if (Input.keySignal(Keyboard.KEY_D).get()) {
                    velocity.edit(v -> v.withX(v.x + dt * 1000));
                }

                //Flip to correct direction
                if (velocity.get().x != 0) {
                    sprite.get().scale = sprite.get().scale.withX(Math.signum(velocity.get().x));
                }

                //Collisions with the floor (temporary, will change later)
                if (position.get().y < 0) {
                    position.edit(v -> v.withY(0));
                    if(velocity.get().y>-200) velocity.edit(v -> v.withY(0));
                    else velocity.edit(v -> v.withY(velocity.get().y*-2/3));
                    //Jumping
                    if (Input.keySignal(Keyboard.KEY_SPACE).get()) {
                        velocity.edit(v -> v.withY(500));
                    }
                }
                
                //Collisions with window
                if(position.get().x < -600 || position.get().x > 600){
                    position.edit(v -> v.withX(position.get().x-velocity.get().x*dt));
                    velocity.edit(v -> v.withX(0));
                }
            });
        }).create();

        Core.run();
    }
}
