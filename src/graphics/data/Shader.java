package graphics.data;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;
import static org.lwjgl.opengl.ARBFragmentShader.GL_FRAGMENT_SHADER_ARB;
import static org.lwjgl.opengl.ARBShaderObjects.*;
import static org.lwjgl.opengl.ARBVertexShader.GL_VERTEX_SHADER_ARB;
import org.lwjgl.opengl.GL11;
import static org.lwjgl.opengl.GL11.glGetInteger;
import static org.lwjgl.opengl.GL20.*;
import util.Log;

public class Shader {

    private final int program;

    public Shader(String name) {
        program = glCreateProgramObjectARB();

        int vertShader = createShader("src/shaders/" + name + ".vert", GL_VERTEX_SHADER_ARB);
        glAttachObjectARB(program, vertShader);

        int fragShader = createShader("src/shaders/" + name + ".frag", GL_FRAGMENT_SHADER_ARB);
        glAttachObjectARB(program, fragShader);

        glLinkProgramARB(program);
        glValidateProgramARB(program);
    }

    public static void clear() {
        glUseProgramObjectARB(0);
    }

    private int createShader(String filename, int shaderType) {
        int shader = glCreateShaderObjectARB(shaderType);

        glShaderSourceARB(shader, readFileAsString(filename));
        glCompileShaderARB(shader);

        if (glGetObjectParameteriARB(shader, GL_OBJECT_COMPILE_STATUS_ARB) == GL11.GL_FALSE) {
            throw new RuntimeException("Error creating shader: " + filename + ", " + shader);
        }

        return shader;
    }

    public void enable() {
        glUseProgramObjectARB(program);
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

    public void setFloat(String name, double val) {
        if (glGetInteger(GL_CURRENT_PROGRAM) != program) {
            throw new RuntimeException("Shader must be enabled");
        }
        glUniform1f(glGetUniformLocation(program, name), (float) val);
    }

    public void setInt(String name, int val) {
        if (glGetInteger(GL_CURRENT_PROGRAM) != program) {
            throw new RuntimeException("Shader must be enabled");
        }
        glUniform1i(glGetUniformLocation(program, name), val);
    }

    public void setVec2(String name, FloatBuffer val) {
        if (glGetInteger(GL_CURRENT_PROGRAM) != program) {
            throw new RuntimeException("Shader must be enabled");
        }
        glUniform2(glGetUniformLocation(program, name), val);
    }

    public void setVec3(String name, FloatBuffer val) {
        if (glGetInteger(GL_CURRENT_PROGRAM) != program) {
            throw new RuntimeException("Shader must be enabled");
        }
        glUniform3(glGetUniformLocation(program, name), val);
    }

    public void setVec4(String name, FloatBuffer val) {
        if (glGetInteger(GL_CURRENT_PROGRAM) != program) {
            throw new RuntimeException("Shader must be enabled");
        }
        glUniform4(glGetUniformLocation(program, name), val);
    }
}
