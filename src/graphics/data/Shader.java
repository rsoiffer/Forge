package graphics.data;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;
import java.util.Stack;
import static org.lwjgl.opengl.ARBFragmentShader.GL_FRAGMENT_SHADER_ARB;
import static org.lwjgl.opengl.ARBShaderObjects.*;
import static org.lwjgl.opengl.ARBVertexShader.GL_VERTEX_SHADER_ARB;
import org.lwjgl.opengl.GL11;
import static org.lwjgl.opengl.GL20.*;
import util.Log;
import util.Util;

public class Shader {

    //Stack
    private static final Stack<Shader> SHADER_STACK = new Stack();

    public static void popShader() {
        SHADER_STACK.pop();
        if (SHADER_STACK.isEmpty()) {
            clear();
        } else {
            SHADER_STACK.peek().enable();
        }
    }

    public static void pushShader(Shader s) {
        SHADER_STACK.push(s);
        s.enable();
    }

    //Shaders
    private int id;

    public Shader(String name) {
        this(name + ".vert", name + ".frag");
    }

    public Shader(String vert, String frag) {
        if (vert.endsWith(".vert")) {
            vert = fileToString(vert);
        }
        if (frag.endsWith(".frag")) {
            frag = fileToString(frag);
        }
        create(createShader(vert, GL_VERTEX_SHADER_ARB), createShader(frag, GL_FRAGMENT_SHADER_ARB));
    }

    public static void clear() {
        glUseProgramObjectARB(0);
    }

    private void create(int vertShader, int fragShader) {
        id = glCreateProgramObjectARB();
        glAttachObjectARB(id, vertShader);
        glAttachObjectARB(id, fragShader);
        glLinkProgramARB(id);
        glValidateProgramARB(id);
    }

    private int createShader(String text, int shaderType) {
        int shader = glCreateShaderObjectARB(shaderType);
        glShaderSourceARB(shader, text);
        glCompileShaderARB(shader);
        if (glGetObjectParameteriARB(shader, GL_OBJECT_COMPILE_STATUS_ARB) == GL11.GL_FALSE) {
            throw new RuntimeException("Error creating shader: " + shader + "\n" + glGetShaderInfoLog(shader, glGetShaderi(shader, GL_INFO_LOG_LENGTH)) + "\n" + text);
        }
        return shader;
    }

    private void enable() {
        glUseProgramObjectARB(id);
    }

    private static String fileToString(String filename) {
        filename = "src/shaders/" + filename;
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

    public void set(String name, double... vals) {
        switch (vals.length) {
            case 1:
                setFloat(name, vals[0]);
                break;
            case 2:
                setVec2(name, Util.floatBuffer(vals));
                break;
            case 3:
                setVec3(name, Util.floatBuffer(vals));
                break;
            case 4:
                setVec4(name, Util.floatBuffer(vals));
                break;
            default:
                throw new RuntimeException("Illegal number of arguments");
        }
    }

    public void setBoolean(String name, boolean val) {
        setInt(name, val ? 1 : 0);
    }

    public void setFloat(String name, double val) {
        with(() -> glUniform1f(glGetUniformLocation(id, name), (float) val));
    }

    public void setInt(String name, int val) {
        with(() -> glUniform1i(glGetUniformLocation(id, name), val));
    }

    public void setVec2(String name, FloatBuffer val) {
        with(() -> glUniform2(glGetUniformLocation(id, name), val));
    }

    public void setVec3(String name, FloatBuffer val) {
        with(() -> glUniform3(glGetUniformLocation(id, name), val));
    }

    public void setVec4(String name, FloatBuffer val) {
        with(() -> glUniform4(glGetUniformLocation(id, name), val));
    }

    public void with(Runnable r) {
        if (!SHADER_STACK.isEmpty() && SHADER_STACK.peek() == this) {
            r.run();
        } else {
            pushShader(this);
            r.run();
            popShader();
        }
    }
}
