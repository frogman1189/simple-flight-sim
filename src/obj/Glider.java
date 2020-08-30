
package obj;

import com.jogamp.opengl.GL2;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import project.Renderable;

/**
 * Glider is an object that uses the glider obj file. All gliders share the same
 * display list in order to decrease resources used.
 * @author Logan Warner
 */
public class Glider extends Obj implements Renderable{
    private static int instanceCount = 0;
    private static int displayListIndex;
    
    public Glider(GL2 gl) {
        super(gl);
        id = instanceCount++;
        //System.out.println(id + "glider");
        // load and initialise model
        if(id == 0) {
            displayListIndex = gl.glGenLists(1);
            try {
                //super.load("obj/glider/glider_3.obj", "./images/stone_256.png");
                super.load("obj/glider/glider_3.obj", "./images/chrome.png");
                initDisplayList(displayListIndex);
            } catch (IOException ex) {
                Logger.getLogger(Glider.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void render(GL2 gl) {
        gl.glPushMatrix();
        gl.glEnable(GL2.GL_COLOR_MATERIAL);
        gl.glColorMaterial(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE);
        gl.glColor3d(1, 1, 1); // set color to default in case I've missed it somewhere
        gl.glCallList(displayListIndex);
        gl.glDisable(GL2.GL_COLOR_MATERIAL);
        gl.glPopMatrix();
    }
    
    
}
