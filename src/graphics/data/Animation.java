package graphics.data;

import graphics.loading.ModelContainer;
import graphics.loading.SpriteContainer;
import java.util.ArrayList;
import static org.lwjgl.opengl.GL11.*;
import util.Color4;
import static util.Color4.WHITE;
import util.Vec3;

public class Animation {

    public ArrayList<Model> modelList;
    public String name;
    public double animIndex;
    public double animSpeed;
    public Texture tex;
    public boolean visible;
    public Vec3 scale;
    public Color4 color;

    public Animation(String name, int length, Vec3 scale, Color4 color) {
        modelList = new ArrayList();
        setAnim(name, length);
        visible = true;
        this.scale = scale;
        this.color = color;
    }

    public Animation(String name, Vec3 scale, Color4 color) {
        modelList = new ArrayList();
        setModel(name);
        visible = true;
        this.scale = scale;
        this.color = color;
    }

    public Animation(String name, String tex) {
        this(name, new Vec3(1), WHITE);
        this.tex = SpriteContainer.loadSprite(tex);
    }

    public boolean animComplete() {
        return animIndex >= modelList.size();
    }

    public void draw(Vec3 pos, double rot) {
        if (visible) {
            if (tex == null) {
                glDisable(GL_TEXTURE_2D);
            } else {
                glEnable(GL_TEXTURE_2D);
                tex.bind();
            }

            color.glColor();
            glTranslated(pos.x, pos.y, pos.z);
            glRotated(rot * 180 / Math.PI, 0, 0, 1);
            glScaled(scale.x, scale.y, scale.z);
            getModel().opengldraw();

            //Reverse transform is faster than pop
            Vec3 invScale = new Vec3(1).divide(scale);
            glScaled(invScale.x, invScale.y, invScale.z);
            glRotated(-rot * 180 / Math.PI, 0, 0, 1);
            glTranslated(-pos.x, -pos.y, -pos.z);
        }
    }

    public Model getModel() {
        return modelList.get((int) animIndex % modelList.size());
    }

    public void setAnim(String name, int length) {
        if (this.name.equals(name)) {
            animIndex = animIndex % modelList.size();
            return;
        }
        this.name = name;
        animIndex = 0;
        modelList.clear();
        for (int i = 1; i <= length; i++) {
            modelList.add(ModelContainer.loadModel(name + "/" + i));
        }
    }

    public void setModel(String name) {
        this.name = name;
        animIndex = 0;
        modelList.clear();
        modelList.add(ModelContainer.loadModel(name));
    }
}
