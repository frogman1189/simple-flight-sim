package project;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;

/**
 * Renders the origin objects
 * @author Logan Warner zdv5950 17991274
 */
public class Origin implements Renderable{
    private final GLU glu;
    private final GL2 gl;
    private GLUquadric quadric;
    private final int displayListIndex;
    private boolean update;
    
    public Origin(GL2 gl) {
        glu = new GLU();
        this.gl = gl;
        displayListIndex = gl.glGenLists(1);
        update = true;
    }
    @Override
    public void render(GL2 gl) {
        if(update == true) {
            System.out.println("updating origin displayList");
            update = false;
            gl.glNewList(displayListIndex, GL2.GL_COMPILE);
            quadric = glu.gluNewQuadric();
            gl.glDisable(GL2.GL_LIGHTING);
            
            //generate x axis
            gl.glPushMatrix();
            gl.glMatrixMode(GL2.GL_MODELVIEW);
            gl.glPushMatrix();
            gl.glEnable(GL2.GL_COLOR_MATERIAL);
            gl.glColorMaterial(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE);
            gl.glColor3d(1, 0, 0);
            gl.glBegin(GL2.GL_LINES);
            gl.glVertex3d(-1000, 0, 0);
            gl.glVertex3d(1000, 0, 0);
            gl.glEnd();
            gl.glDisable(GL2.GL_COLOR_MATERIAL);
            gl.glPopMatrix();
            
            //generate y axis
            gl.glPushMatrix();
            gl.glEnable(GL2.GL_COLOR_MATERIAL);
            gl.glColorMaterial(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE);
            gl.glColor3d(0, 1, 0);
            gl.glBegin(GL2.GL_LINES);
            gl.glVertex3d(0, 1000, 0);
            gl.glVertex3d(0, -1000, 0);
            gl.glEnd();
            gl.glDisable(GL2.GL_COLOR_MATERIAL);
            gl.glPopMatrix();

            //generate z axis
            gl.glPushMatrix();
            gl.glEnable(GL2.GL_COLOR_MATERIAL);
            gl.glColorMaterial(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE);
            gl.glColor3d(0, 0, 1);
            gl.glBegin(GL2.GL_LINES);
            gl.glVertex3d(0, 0, 1000);
            gl.glVertex3d(0, 0, -1000);
            gl.glEnd();
            gl.glDisable(GL2.GL_COLOR_MATERIAL);
            gl.glPopMatrix();
            
            // generate sphere
            gl.glColor3d(1, 1, 1);
            glu.gluSphere(quadric, 1, 30, 30);
            gl.glEnable(GL2.GL_LIGHTING);
            gl.glPopMatrix();
            gl.glEndList();
        }
        gl.glCallList(displayListIndex);
    }
}
