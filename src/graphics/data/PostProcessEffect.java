package graphics.data;

import engine.Core;
import graphics.Window3D;
import static org.lwjgl.opengl.GL11.*;

public class PostProcessEffect {

    public PostProcessEffect(String name, double depth) {
        Shader shader = new Shader(name);
        Framebuffer framebuffer = new Framebuffer();

        Core.renderLayer(-depth).onEvent(framebuffer::enable);
        Core.renderLayer(depth).onEvent(() -> {
            Framebuffer.clear();
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            Window3D.background.glClearColor();
            shader.enable();
            framebuffer.draw();
            Shader.clear();
        });
    }
}
