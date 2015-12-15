package graphics;

import graphics.data.Texture;
import graphics.loading.FontContainer;
import static org.lwjgl.opengl.GL11.*;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.TextureImpl;
import util.Color4;
import util.Vec2;
import util.Vec3;

public abstract class Graphics3D {

    public static void drawLine(Vec3 start, Vec3 end) {
        drawLine(start, end, Color4.BLACK);
    }

    public static void drawLine(Vec3 start, Vec3 end, Color4 color) {
        glDisable(GL_TEXTURE_2D);
        glLineWidth(2);
        color.glColor();
        glBegin(GL_LINES);
        {
            start.glVertex();
            end.glVertex();
        }
        glEnd();
    }

    public static void drawSprite(Texture s, Vec3 pos, Vec2 size, double tilt, double angle, Color4 color) {
        glPushMatrix();
        glEnable(GL_TEXTURE_2D);
        s.bind();
        double dir = Math.signum(size.x * size.y);

        color.glColor();
        glTranslated(pos.x, pos.y, pos.z);
        glRotated(angle * 180 / Math.PI, 0, 0, 1);
        glRotated(tilt * 180 / Math.PI, 1, 0, 0);

        glBegin(GL_QUADS);
        {
            glNormal3d(0, 0, dir);
            glTexCoord2d(0, s.getHeight());
            glVertex3d(0, 0, 0);
            glNormal3d(0, 0, dir);
            glTexCoord2d(s.getWidth(), s.getHeight());
            glVertex3d(size.x, 0, 0);
            glNormal3d(0, 0, dir);
            glTexCoord2d(s.getWidth(), 0);
            glVertex3d(size.x, size.y, 0);
            glNormal3d(0, 0, dir);
            glTexCoord2d(0, 0);
            glVertex3d(0, size.y, 0);
        }
        glEnd();
        glPopMatrix();
    }

    public static void drawSpriteFast(Texture s, Vec3 p1, Vec3 p2, Vec3 p3, Vec3 p4, Vec3 nor) {
        nor.glNormal();
        glTexCoord2d(0, s.getHeight());
        p1.glVertex();
        nor.glNormal();
        glTexCoord2d(s.getWidth(), s.getHeight());
        p2.glVertex();
        nor.glNormal();
        glTexCoord2d(s.getWidth(), 0);
        p3.glVertex();
        nor.glNormal();
        glTexCoord2d(0, 0);
        p4.glVertex();
    }

    public static void drawText(String s, Vec3 pos) {
        drawText(s, "Default", pos, Color.black);
    }

    public static void drawText(String s, String font, Vec3 pos, Color c) {
        TextureImpl.bindNone();
        FontContainer.get(font).drawString((float) pos.x, (float) pos.y, s, c);
    }

    public static void fillRect(Vec3 pos, Vec2 size, double tilt, double angle, Color4 color) {
        glPushMatrix();
        glDisable(GL_TEXTURE_2D);
        double dir = Math.signum(size.x * size.y);

        color.glColor();
        glTranslated(pos.x, pos.y, pos.z);
        glRotated(angle * 180 / Math.PI, 0, 0, 1);
        glRotated(tilt * 180 / Math.PI, 1, 0, 0);

        glBegin(GL_QUADS);
        {
            glNormal3d(0, 0, dir);
            glVertex3d(0, 0, 0);
            glNormal3d(0, 0, dir);
            glVertex3d(size.x, 0, 0);
            glNormal3d(0, 0, dir);
            glVertex3d(size.x, size.y, 0);
            glNormal3d(0, 0, dir);
            glVertex3d(0, size.y, 0);
        }
        glEnd();
        glPopMatrix();
    }
}
