package examples.gameoflife;

import engine.Core;
import engine.Input;
import graphics.Graphics2D;
import graphics.Window2D;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import util.Color4;
import static util.Color4.BLACK;
import static util.Color4.WHITE;
import util.Vec2;

public class GameofLife {

    private static final int SIZE = 1000;
    private static boolean[][] grid, old;
    private static boolean paused;
    private static boolean fancy;

    public static void main(String[] args) {
        Core.init();

        Core.render.bufferCount(Core.interval(1)).forEach(i -> Display.setTitle("FPS: " + i));

        grid = new boolean[SIZE][SIZE];
        old = new boolean[SIZE][SIZE];

        grid[0][0] = grid[0][1] = grid[1][1] = grid[2][1] = grid[1][2] = true;
        Window2D.background = WHITE;
        Window2D.viewSize = new Vec2(SIZE);

        Core.render.filter(() -> !paused).onEvent(() -> step());

        Core.render.onEvent(() -> {
            for (int x = 0; x < SIZE; x++) {
                for (int y = 0; y < SIZE; y++) {
                    if (grid[x][y]) {
                        Color4 color = fancy ? new Color4(Math.random(), Math.random(), Math.random()) : BLACK;
                        Graphics2D.fillRect(new Vec2(x, y), new Vec2(1), color);
                        Graphics2D.fillRect(new Vec2(x + SIZE, y), new Vec2(1), color);
                        Graphics2D.fillRect(new Vec2(x, y + SIZE), new Vec2(1), color);
                        Graphics2D.fillRect(new Vec2(x + SIZE, y + SIZE), new Vec2(1), color);
                    }
                }
            }
        });

        Input.whileKeyDown(Keyboard.KEY_W).forEach(dt -> Window2D.viewPos = Window2D.viewPos.add(new Vec2(0, 100 * dt)));
        Input.whileKeyDown(Keyboard.KEY_A).forEach(dt -> Window2D.viewPos = Window2D.viewPos.add(new Vec2(100 * -dt, 0)));
        Input.whileKeyDown(Keyboard.KEY_S).forEach(dt -> Window2D.viewPos = Window2D.viewPos.add(new Vec2(0, 100 * -dt)));
        Input.whileKeyDown(Keyboard.KEY_D).forEach(dt -> Window2D.viewPos = Window2D.viewPos.add(new Vec2(100 * dt, 0)));
        Core.update.forEach(dt -> {
            Window2D.viewPos = new Vec2((Window2D.viewPos.x + SIZE / 2) % SIZE + SIZE / 2, (Window2D.viewPos.y + SIZE / 2) % SIZE + SIZE / 2);
        });

        Input.whenMouse(0, true).onEvent(() -> {
            grid[(int) Input.getMouse().x % SIZE][(int) Input.getMouse().y % SIZE] = !grid[(int) Input.getMouse().x % SIZE][(int) Input.getMouse().y % SIZE];
        });

        Input.whenKey(Keyboard.KEY_SPACE, true).onEvent(() -> paused = !paused);

        Input.whenKey(Keyboard.KEY_F, true).onEvent(() -> fancy = !fancy);

        Input.mouseWheel.forEach(i -> {
            Window2D.viewSize = Window2D.viewSize.multiply(Math.pow(.9, i / 120));
            if (Window2D.viewSize.x > SIZE) {
                Window2D.viewSize = new Vec2(SIZE);
            }
//            System.out.println(i);
//            System.out.println(Window2D.viewSize);
        });

        Input.whenKey(Keyboard.KEY_RETURN, true).filter(() -> paused).onEvent(() -> step());
        
        Input.whenKey(Keyboard.KEY_R, true).onEvent(() -> {
            for (int x = 0; x < SIZE; x++) {
                for (int y = 0; y < SIZE; y++) {
                    grid[x][y] = Math.random() > .5;
                }
            }
        });

        Core.run();
    }

    private static void step() {
        {
            boolean[][] buf = grid;
            grid = old;
            old = buf;
        }

        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                int c = 0;
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        if (i != 0 || j != 0) {
                            int x2 = (x + i + SIZE) % SIZE;
                            int y2 = (y + j + SIZE) % SIZE;
                            if (old[x2][y2]) {
                                c++;
                            }
                        }
                    }
                }
                switch (c) {
                    case 2:
                        grid[x][y] = old[x][y];
                        break;
                    case 3:
                        grid[x][y] = true;
                        break;
                    default:
                        grid[x][y] = false;
                        break;
                }
            }
        }
    }
}
