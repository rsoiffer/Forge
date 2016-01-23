package graphics.data;

import engine.AbstractEntity;
import engine.Core;
import engine.EventStream;
import engine.Signal;
import graphics.Window2D;
import graphics.Window3D;
import static org.lwjgl.opengl.GL11.*;

public class PostProcessEffect extends AbstractEntity {

    private final double depth;
    private final Framebuffer framebuffer;
    private final Shader shader;

    public PostProcessEffect(double depth, Framebuffer framebuffer, Shader shader) {
        this.depth = depth;
        this.framebuffer = framebuffer;
        this.shader = shader;
    }

    @Override
    public void create() {
        add(Core.renderLayer(-depth).onEvent(() -> Framebuffer.pushFramebuffer(framebuffer)),
                Core.renderLayer(depth).onEvent(() -> {
            Framebuffer.popFramebuffer();
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            (Core.is3D ? Window3D.background : Window2D.background).glClearColor();
            Shader.pushShader(shader);
            framebuffer.render();
            Shader.popShader();
        }));
    }

    public void toggleOn(EventStream e) {
        toggleOn(e.reduce(false, b -> !b));
    }

    public void toggleOn(Signal<Boolean> s) {
        s.distinct().forEach(b -> {
            if (b) {
                create();
            } else {
                destroy();
            }
        });
    }
}
