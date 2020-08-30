
package obj;

import com.jogamp.opengl.GL2;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import project.Renderable;

/**
 * Cactus is an implementation of the Obj which uses the cactus .obj file.
 * It takes a coordinate on the x-z plane to simplify terrain generation code
 * @author Logan Warner
 */
public class Cactus extends Obj implements Renderable{
    private static int instanceCount = 0;
    private static int displayListIndex;
    private double x;
    private double z;
    private double scale;
    
    public Cactus(GL2 gl, double x, double z) {
        super(gl);
        this.x = x;
        this.z = z;
        scale = 1;
        id = instanceCount++;
        //System.out.println(id + "Cactus");
        // load model and init display list
        if(id == 0) {
            displayListIndex = gl.glGenLists(1);
            try {
                super.load("obj/cactus/cactus_3.obj", "./images/sand_1024.jpg");
                initDisplayList(displayListIndex);
            } catch (IOException ex) {
                Logger.getLogger(Cactus.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * Sets the Cactus's position on the x-z plane
     * @param x x coordinate
     * @param z z coordinate
     */
    public void setPosition(double x, double z) {
        this.x = x;
        this.z = z;
    }
    /**
     * Changes the y (vertical) scale. Used to create interesting pillars that
     * are fun to fly between
     * @param scale double that specifies the y scale
     */
    public void setScale(double scale) {
        this.scale = scale; // scale on y axis. It's quite fun to navigate through
    }
    
    @Override
    public void render(GL2 gl) {
        gl.glPushMatrix();
        // 8 is the offset required to put the cactus level with the ground
        // at a scale of one due to the origin not being placed correctly in the
        // file
        gl.glTranslated(x,8, z);
        gl.glScaled(1, scale, 1);
        gl.glEnable(GL2.GL_COLOR_MATERIAL);
        gl.glColorMaterial(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE);
        // change the base color to a more green color so that sand texture
        // looks like a cactus texture.
        gl.glColor3d(0.4, 1, 0.3);
        gl.glCallList(displayListIndex);
        gl.glColor3d(1, 1, 1);
        gl.glDisable(GL2.GL_COLOR_MATERIAL);
        gl.glPopMatrix();
    }
    
    
}
