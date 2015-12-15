package graphics.data;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import org.lwjgl.opengl.ARBFragmentShader;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.ARBVertexShader;
import org.lwjgl.opengl.GL11;
import util.Log;

public class Shader {

    private int program;

    public Shader(String name) {
        int vertShader = createShader("src/shaders/" + name + ".vert", ARBVertexShader.GL_VERTEX_SHADER_ARB);
        int fragShader = createShader("src/shaders/" + name + ".frag", ARBFragmentShader.GL_FRAGMENT_SHADER_ARB);

        program = ARBShaderObjects.glCreateProgramObjectARB();
        if (program == 0) {
            return;
        }

        ARBShaderObjects.glAttachObjectARB(program, vertShader);
        ARBShaderObjects.glAttachObjectARB(program, fragShader);

        ARBShaderObjects.glLinkProgramARB(program);
        if (ARBShaderObjects.glGetObjectParameteriARB(program, ARBShaderObjects.GL_OBJECT_LINK_STATUS_ARB) == GL11.GL_FALSE) {
            return;
        }

        ARBShaderObjects.glValidateProgramARB(program);
        if (ARBShaderObjects.glGetObjectParameteriARB(program, ARBShaderObjects.GL_OBJECT_VALIDATE_STATUS_ARB) == GL11.GL_FALSE) {
            return;
        }
    }

    public static void clear() {
        ARBShaderObjects.glUseProgramObjectARB(0);
    }

    private int createShader(String filename, int shaderType) {
        int shader = ARBShaderObjects.glCreateShaderObjectARB(shaderType);

        if (shader == 0) {
            return 0;
        }

        ARBShaderObjects.glShaderSourceARB(shader, readFileAsString(filename));
        ARBShaderObjects.glCompileShaderARB(shader);

        if (ARBShaderObjects.glGetObjectParameteriARB(shader, ARBShaderObjects.GL_OBJECT_COMPILE_STATUS_ARB) == GL11.GL_FALSE) {
            throw new RuntimeException("Error creating shader: " + filename + ", " + shader);
        }

        return shader;
    }

    public void enable() {
        ARBShaderObjects.glUseProgramObjectARB(program);
    }

    private String readFileAsString(String filename) {
        StringBuilder source = new StringBuilder();
        try {
            FileInputStream in = new FileInputStream(filename);
            BufferedReader reader;
            reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String line;
            while ((line = reader.readLine()) != null) {
                source.append(line).append('\n');
            }
            reader.close();
            in.close();
        } catch (Exception e) {
            Log.error("Could not read file " + filename + ": " + e);
        }
        return source.toString();
    }
}
