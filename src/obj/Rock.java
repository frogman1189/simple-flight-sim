
package obj;

import com.jogamp.opengl.GL2;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import project.Point3;
import project.Renderable;

/**
 * Class the extends Obj using the rock .obj model
 * Takes a position on the x-z plane for easier terrain generation and takes a
 * 3 dimensional scale to create interesting rock formations.
 * @author Logan Warner
 */
public class Rock extends Obj implements Renderable{
    private static int instanceCount = 0;
    private static int displayListIndex;
    private double x;
    private double z;
    private Point3 scale;
    public Rock(GL2 gl, double x, double z) {
        super(gl);
        this.x = x;
        this.z = z;
        scale = new Point3(1,1,1);
        id = instanceCount++;
        //System.out.println(id + "rock");
        // initialise model and display list
        if(id == 0) {
            displayListIndex = gl.glGenLists(1);
            try {
                super.load("obj/rocks/rock.obj", "./images/stone_256.png");
                super.initDisplayList(displayListIndex);
            } catch (IOException ex) {
                Logger.getLogger(Rock.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    /**
     * Set position in x-z plane
     * @param x coordinate
     * @param z coordinate
     */
    public void setPosition(double x, double z) {
        this.x = x;
        this.z = z;
    }
    /**
     * Set the rock scale
     * @param scale Point3 object constructed as (x, y, z) where x represents 
     * scale on the x axis, y the scale on the y axis and z the scale on the z 
     * axis
     */
    public void setScale(Point3 scale) {
        this.scale = scale;
    }
    
    @Override
    public void render(GL2 gl) {
        gl.glPushMatrix();
        gl.glEnable(GL2.GL_COLOR_MATERIAL);
        gl.glColorMaterial(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE);
        gl.glColor3d(1, 1, 1); // set color to default in case I've missed it somewhere
        gl.glTranslated(x, 0, z);
        gl.glScaled(scale.x, scale.y, scale.z);
        gl.glCallList(displayListIndex);
        gl.glDisable(GL2.GL_COLOR_MATERIAL);
        gl.glPopMatrix();
    }
    
    
}
