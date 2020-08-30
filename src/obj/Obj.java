
package obj;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;
import java.io.File;
import java.io.IOException;

/**
 * Obj Provides the template for instantiating an object that uses an .obj file
 * as its model
 * @author Logan Warner
 */
public abstract class Obj {
    private final GL2 gl;
    private ObjLoader obj;
    private Texture texture;
    protected int id; // set in extending class
    
    public Obj(GL2 gl) {
        this.gl = gl;
        id = -1; // overwritten by extending class
    }
    
    /**
     * This Method is used to load an objects .obj model and texture
     * @param objPath path to .obj file
     * @param texturePath path to texture file (.png, .jpg, etc)
     * @throws IOException if .obj file is not found or texture file not found
     */
    public void load(String objPath, String texturePath) throws IOException {
        obj = new ObjLoader(objPath);
        texture = TextureIO.newTexture(new File(texturePath), true);
    }
    
    /**
     * After {@link obj.Obj#load} has been called initDisplayList may be called
     * to initialise the display list with the .obj model and texture loaded
     * @param displayListIndex displaylist to initialise. Use a variable to 
     * store gl.glGenLists(x) and pass the value to this function
     */
    protected final void initDisplayList(int displayListIndex) {
        gl.glPushMatrix();
        
        gl.glNewList(displayListIndex, GL2.GL_COMPILE);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        
        // set texture
        gl.glEnable(GL2.GL_TEXTURE_2D);
        texture.bind(gl);
        texture.setTexParameteri(gl, GL2.GL_TEXTURE_WRAP_S, GL2.GL_REPEAT);
        texture.setTexParameteri(gl, GL2.GL_TEXTURE_WRAP_T, GL2.GL_REPEAT);

        // load obj faces
        Integer mode = null;
        for(Integer[][] face: obj.f) {
            // create a triangles object while faces have 3 points
            if(face.length == 3) {
                if(mode == null) {
                    mode = GL2.GL_TRIANGLES;
                    gl.glBegin(GL2.GL_TRIANGLES);
                }
                else {
                    if(mode != GL2.GL_TRIANGLES) {
                        mode = GL2.GL_TRIANGLES;
                        gl.glEnd(); // close previous object made from triangles
                        gl.glBegin(GL2.GL_TRIANGLES); // start this object
                    }
                }    
            }
            // create quads object while faces have 4 points
            else if(face.length == 4) {
                if(mode == null) {
                    mode = GL2.GL_QUADS;
                    gl.glBegin(GL2.GL_QUADS);
                }
                else {
                    if(mode != GL2.GL_QUADS) {
                        mode = GL2.GL_QUADS;
                        gl.glEnd(); // close previous object made from triangles
                        gl.glBegin(GL2.GL_QUADS); // start this object
                    }
                }
                gl.glBegin(GL2.GL_QUADS);
            }
            // for each vertex type for each point in face place it
            for(Integer[] point: face) {
                if(point.length == 3) {
                    if(point[2] != null) {
                        gl.glNormal3d(obj.vn[point[2] * 3], obj.vn[point[2] * 3+1], obj.vn[point[2] * 3+2]);
                    }
                    if(point[1] != null) {
                        gl.glTexCoord2d(obj.vt[point[1] * 2]*10, obj.vt[point[1] * 2+1]*5);
                    }
                    if(point[0] != null) {
                        gl.glVertex3d(obj.v[point[0] * 3], obj.v[point[0] * 3+1], obj.v[point[0] * 3+2]);
                    }
                }
            }
        }
        // final glEnd to close the last opened object
        gl.glEnd();
        gl.glDisable(GL2.GL_TEXTURE_2D);
        gl.glEndList();
            
        gl.glPopMatrix();
    }
    
}
