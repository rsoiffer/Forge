package graphics.data;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import static org.lwjgl.opengl.GL11.*;
import util.Vec2;
import util.Vec3;

public class Model {

    private final ArrayList<Vec3> vertices = new ArrayList(); // Vertex Coordinates
    private final ArrayList<Vec3> vertexNormals = new ArrayList(); // Vertex Coordinates Normals
    private final ArrayList<Vec2> vertexTex = new ArrayList(); // Vertex Coordinates Textures
    private final ArrayList<int[]> faces = new ArrayList(); // Array of Faces (vertex sets)
    private final ArrayList<int[]> facestexs = new ArrayList(); // Array of of Faces textures
    private final ArrayList<int[]> facesnorms = new ArrayList(); // Array of Faces normals

    private int objectlist;
    private int polyCount = 0;

    private Vec3 topCorner, bottomCorner;

//    //// Statisitcs for drawing ////
//    public double toppoint = 0;		// y+
//    public double bottompoint = 0;	// y-
//    public double leftpoint = 0;		// x-
//    public double rightpoint = 0;	// x+
//    public double farpoint = 0;		// z-
//    public double nearpoint = 0;		// z+
    public Model(String name) {
        try {
            loadobject(new BufferedReader(new FileReader(name)));
            //translate(new Vec3(0, 0, -bottomCorner.z));
            opengldrawtolist();
            polyCount = faces.size();
            cleanup();
        } catch (FileNotFoundException ex) {
            System.out.println("Could not load model: " + name);
        }
//        for (int i = 0; i < vertices.size(); i++) {
//            int ocr = 0;
//            for (int j = 0; i < faces.size(); j++) {
//
//            }
//        }
    }

    private void cleanup() {
        vertices.clear();
        vertexNormals.clear();
        vertexTex.clear();
        faces.clear();
        facestexs.clear();
        facesnorms.clear();
    }

    private void loadobject(BufferedReader br) {
        int linecounter = 0;
        try {

            String newline;
            boolean firstpass = true;

            while ((newline = br.readLine()) != null) {
                linecounter++;
                newline = newline.trim();
                if (newline.length() > 0) {
                    if (newline.charAt(0) == 'v' && newline.charAt(1) == ' ') {
                        String[] coordstext = newline.split("\\s+");
                        Vec3 coords = new Vec3(Double.valueOf(coordstext[3]), Double.valueOf(coordstext[1]), Double.valueOf(coordstext[2]));
                        vertices.add(coords);
                        if (firstpass) {
                            topCorner = bottomCorner = coords;
                            firstpass = false;
                        } else {
                            topCorner = topCorner.perComponent(coords, Math::max);
                            bottomCorner = bottomCorner.perComponent(coords, Math::min);
                        }
                    }
                    if (newline.charAt(0) == 'v' && newline.charAt(1) == 't') {
                        String[] coordstext = newline.split("\\s+");
                        Vec2 coords = new Vec2(Double.valueOf(coordstext[1]), 1 - Double.valueOf(coordstext[2]));
                        vertexTex.add(coords);
                    }
                    if (newline.charAt(0) == 'v' && newline.charAt(1) == 'n') {
                        String[] coordstext = newline.split("\\s+");
                        Vec3 coords = new Vec3(Double.valueOf(coordstext[3]), Double.valueOf(coordstext[1]), Double.valueOf(coordstext[2]));
                        vertexNormals.add(coords);
                    }
                    if (newline.charAt(0) == 'f' && newline.charAt(1) == ' ') {
                        String[] coordstext = newline.split("\\s+");
                        int[] v = new int[coordstext.length - 1];
                        int[] vt = new int[coordstext.length - 1];
                        int[] vn = new int[coordstext.length - 1];

                        for (int i = 1; i < coordstext.length; i++) {
                            String fixstring = coordstext[i].replaceAll("//", "/0/");
                            String[] tempstring = fixstring.split("/");
                            v[i - 1] = Integer.valueOf(tempstring[0]) - 1;
                            if (tempstring.length > 1) {
                                vt[i - 1] = Integer.valueOf(tempstring[1]) - 1;
                            } else {
                                vt[i - 1] = -1;
                            }
                            if (tempstring.length > 2) {
                                vn[i - 1] = Integer.valueOf(tempstring[2]) - 1;
                            } else {
                                vn[i - 1] = -1;
                            }
                        }
                        faces.add(v);
                        facestexs.add(vt);
                        facesnorms.add(vn);
                    }
                }
            }

        } catch (IOException e) {
            System.out.println("Failed to read file: " + br.toString());
        } catch (NumberFormatException e) {
            System.out.println("Malformed OBJ (on line " + linecounter + "): " + br.toString() + "\r \r" + e.getMessage());
        }
    }

    public void opengldraw() {
        glCallList(objectlist);
    }

    private void opengldrawtolist() {
        objectlist = glGenLists(1);
        glNewList(objectlist, GL_COMPILE);

        glBegin(GL_TRIANGLES);
        for (int i = 0; i < faces.size(); i++) {
            if (faces.get(i).length == 3) {
                for (int j = 0; j < faces.get(i).length; j++) {
                    if (facesnorms.get(i)[j] >= 0) {
                        vertexNormals.get(facesnorms.get(i)[j]).glNormal();
                    }
                    if (facestexs.get(i)[j] >= 0) {
                        vertexTex.get(facestexs.get(i)[j]).glTexCoord();
                    }
                    vertices.get(faces.get(i)[j]).glVertex();
                }
            }
        }
        glEnd();
        glBegin(GL_QUADS);
        for (int i = 0; i < faces.size(); i++) {
            if (faces.get(i).length == 4) {
                for (int j = 0; j < faces.get(i).length; j++) {
                    if (facesnorms.get(i)[j] >= 0) {
                        vertexNormals.get(facesnorms.get(i)[j]).glNormal();
                    }
                    if (facestexs.get(i)[j] >= 0) {
                        vertexTex.get(facestexs.get(i)[j]).glTexCoord();
                    }
                    vertices.get(faces.get(i)[j]).glVertex();
                }
            }
        }
        glEnd();
        for (int i = 0; i < faces.size(); i++) {
            if (faces.get(i).length > 4) {
                glBegin(GL_POLYGON);
                for (int j = 0; j < faces.get(i).length; j++) {
                    if (facesnorms.get(i)[j] >= 0) {
                        vertexNormals.get(facesnorms.get(i)[j]).glNormal();
                    }
                    if (facestexs.get(i)[j] >= 0) {
                        vertexTex.get(facestexs.get(i)[j]).glTexCoord();
                    }
                    vertices.get(faces.get(i)[j]).glVertex();
                }
                glEnd();
            }
        }
        glEndList();
    }

    public int polyCount() {
        return polyCount;
    }

    private void translate(Vec3 amt) {
        for (int i = 0; i < vertices.size(); i++) {
            vertices.set(i, vertices.get(i).add(amt));
        }
    }
}
