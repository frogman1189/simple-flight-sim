
package obj;

import com.jogamp.opengl.GL2;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import project.Renderable;

/**
 * Class that extends Obj using the Well .obj file. Takes coordinate on x-z
 * plane to simplify terrain generation.
 * @author Logan Warner
 */
public class Well extends Obj implements Renderable{
    private static int instanceCount = 0;
    private static int displayListIndex;
    private double x;
    private double z;
    
    public Well(GL2 gl, double x, double z) {
        super(gl);
        this.x = x;
        this.z = z;
        id = instanceCount++;
        //System.out.println(id + "Well");
        // instantiate model and display list
        if(id == 0) {
            displayListIndex = gl.glGenLists(1);
            try {
                super.load("obj/well/well.obj", "obj/well/plank.png");
                super.initDisplayList(displayListIndex);
            } catch (IOException ex) {
                Logger.getLogger(Well.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    /**
     * set position on x-z plane
     * @param x coordinate
     * @param z coordinate
     */
    public void setPosition(double x, double z) {
        this.x = x;
        this.z = z;
    }
    @Override
    public void render(GL2 gl) {
        gl.glPushMatrix();
        gl.glTranslated(x,8, z);
        gl.glEnable(GL2.GL_COLOR_MATERIAL);
        gl.glColorMaterial(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE);
        gl.glColor3d(0.5, 0.5, 0.5);
        
        gl.glTranslated(x,-8, z);
        gl.glScaled(0.5, 0.5, 0.5);
        gl.glCallList(displayListIndex);
        
        gl.glColor3d(1, 1, 1);
        gl.glDisable(GL2.GL_COLOR_MATERIAL);
        gl.glPopMatrix();
    }
    
    
}
