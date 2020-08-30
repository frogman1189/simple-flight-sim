package project;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Creates a plane centered on the x,z axis at y 0
 * @author Logan Warner zdv5950 17991274
 */
public class Ground implements Renderable{
    private GL2 gl;
    
    private final float width;
    private final int slices;
    private int displayListIndex;
    private boolean update;
    private Texture texture;
    private double x;
    private double z;
    
    public Ground(GL2 gl, float width) {
        this.gl = gl;
        this.width = width;
        slices = (int) width/10;
        displayListIndex = gl.glGenLists(1);
        update = true;
        x=0;
        z=0;
        try {
            texture = TextureIO.newTexture(new File("./images/sand256.png"), true);
        } catch (IOException ex) {
            Logger.getLogger(Ground.class.getName()).log(Level.SEVERE, null, ex);
        }
        init(displayListIndex);
    }
    
    public void setPosition(double x, double z) {
        this.x = x;
        this.z = z;
    }
    
    private final void init(int index) {
        gl.glNewList(index, GL2.GL_COMPILE);
        gl.glPushMatrix();
        gl.glEnable(GL2.GL_TEXTURE_2D);
        gl.glMatrixMode(GL2.GL_MODELVIEW);

        texture.bind(gl);
        texture.setTexParameteri(gl, GL2.GL_TEXTURE_WRAP_S, GL2.GL_REPEAT);
        texture.setTexParameteri(gl, GL2.GL_TEXTURE_WRAP_T, GL2.GL_REPEAT);
            
        //enable color
        gl.glEnable(GL2.GL_COLOR_MATERIAL);
        gl.glColorMaterial(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE);
        gl.glColor3d(1, 1, 1);
            
        // generating the actual plane
        gl.glBegin(GL2.GL_QUADS);
        for(int x = 0; x < slices; x++) {
            for(int y = 0; y < slices; y++) {
                gl.glNormal3d(0, 1, 0);
                gl.glTexCoord2d(0,1);
                gl.glVertex3f(x * width/slices, 0,y * width/slices); // top left
                gl.glTexCoord2d(0,0);
                gl.glVertex3f(x * width/slices, 0,(y+1) * width/slices); // bottom left
                gl.glTexCoord2d(1,0);
                gl.glVertex3f((x+1) * width/slices, 0,(y+1) * width/slices); // bottom right
                gl.glTexCoord2d(1,1);
                gl.glVertex3f((x + 1) * width/slices, 0,y * width/slices); // top right
                
            }
        }
        gl.glEnd();
        gl.glDisable(GL2.GL_COLOR_MATERIAL);
        gl.glDisable(GL2.GL_TEXTURE_2D);
        gl.glPopMatrix();
        gl.glEndList();
    }
    
    /**
     * Renders ground
     * @param gl 
     */
    @Override
    public void render(GL2 gl) {
        gl.glPushMatrix();
        gl.glTranslated(x-width/2, 0, z-width/2);
        gl.glCallList(displayListIndex);
        gl.glPopMatrix();
    }
    
}
