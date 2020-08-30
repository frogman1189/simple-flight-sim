package project;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Sphere that encapsulates the view of the controller and displays a sky 
 * texture.
 * @author Logan Warner
 */
public class SkyOrb implements Renderable{
    private GLU glu;
    private GL2 gl;
    private GLUquadric quadric;
    private double radius;
    private final int displayListIndex;
    private boolean update;
    private Texture texture;
    private float emission[] = {0.5f, 0.5f, 0.5f, 1};
    private float noEmission[] = {0.1f, 0.1f, 0.1f, 1};
    public SkyOrb(GL2 gl, double radius) {
        glu = new GLU();
        this.gl = gl;
        this.radius = radius;
        displayListIndex = gl.glGenLists(1);
        update = true;
        quadric = glu.gluNewQuadric();
        // enable quadric to have texture and place it on the inside
        glu.gluQuadricTexture(quadric, true);
        glu.gluQuadricOrientation(quadric, GLU.GLU_INSIDE);
        //load texture
        try {
            texture = TextureIO.newTexture(new File("./images/sky_2048_5.jpg"), true);
        } catch (IOException ex) {
            Logger.getLogger(Ground.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void render(GL2 gl) {
        if(update == true) {
            update = false;
            gl.glNewList(displayListIndex, GL2.GL_COMPILE);
            
            gl.glPushMatrix();
            gl.glEnable(GL2.GL_TEXTURE_2D);
            
            gl.glEnable(GL2.GL_COLOR_MATERIAL);
            gl.glColorMaterial(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE);
            gl.glColor4d(1, 1, 1, 1);
            texture.bind(gl);
            texture.setTexParameteri(gl, GL2.GL_TEXTURE_WRAP_S, GL2.GL_REPEAT);
            texture.setTexParameteri(gl, GL2.GL_TEXTURE_WRAP_T, GL2.GL_REPEAT);
            
            glu.gluSphere(quadric, radius, 500, 500);
            
            gl.glColor4d(1, 1, 1, 1);
            gl.glDisable(GL2.GL_COLOR_MATERIAL);
            gl.glDisable(GL2.GL_TEXTURE_2D);
            gl.glPopMatrix();
            gl.glEndList();
        }
        gl.glPushMatrix();
        gl.glRotated(90, 1, 0, 0);
        gl.glCallList(displayListIndex);
        gl.glPopMatrix();
    }
}
