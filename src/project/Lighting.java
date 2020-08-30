package project;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;

/**
 * Enables lighting in the OpenGl scene
 * @author Jacquie, extended by Logan
 */
public class Lighting implements Renderable{
    private final GLU glu;
    private final GL2 gl;
    private GLUquadric quadric;
    float globalAmbient[] = { 0.2f, 0.2f, 0.2f, 1 }; // global light properties

    //public float[] spotPosition = { 5.0f, 10.0f, 5.0f, 1}; //direction light
    protected float[] position2 = { 0f, 0f, 0f, 1f}; //direction light
    protected float[] position1 = { 0f, 0f, 0f, 1f}; //direction light
    protected float[] position0 = { 10.0f, 10.0f, 0.0f, 0}; //direction light
    protected float[] ambient0 = { 0.2f, 0.2f, 0.2f, 1 };
    protected float[] diffuse0 = { 1f, 1f, 1f, 1 };
    protected float[] diffuse1 = { 1f, 1f, 1f, 1f };
    protected float[] specular0 = { 1, 1, 1, 1 };
    protected float[] direction1 = { 1f, 0, 0 };
    protected float fogColor[] = {0.3f, 0.3f, 0.6f, 1};
    protected float fogDensity = 0.1f;
    //private float fogColor[] = {0.3f, 0.2f, 0.1f, 1};
    private final int displayListIndex;
    private boolean update;
    private double angle;
    /**
     * Creates and enables scene's lights using default light0 position
     * @param gl
     */
    public Lighting(GL2 gl) {
        glu = new GLU();
        this.gl = gl;
        gl.glEnable(GL2.GL_COLOR_MATERIAL);
        // set the global ambient light level
        gl.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT, globalAmbient, 0);

        // setup the light 0 properties
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, ambient0, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, diffuse0, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, specular0, 0);
        
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_AMBIENT, ambient0, 0);
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_DIFFUSE, diffuse1, 0);
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_SPECULAR, specular0, 0);

        // normalize the normals
        gl.glEnable(GL2.GL_NORMALIZE);

        // position the light
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, position0, 0);
        
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_POSITION, position1, 0);
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_SPOT_DIRECTION, direction1, 0);
        gl.glLightf(GL2.GL_LIGHT1, GL2.GL_SPOT_CUTOFF, 180);

        gl.glLightf(GL2.GL_LIGHT0, GL2.GL_CONSTANT_ATTENUATION, 1f);
        gl.glLightf(GL2.GL_LIGHT0, GL2.GL_LINEAR_ATTENUATION, 1f);
	gl.glLightf(GL2.GL_LIGHT0, GL2.GL_QUADRATIC_ATTENUATION, 1f);
        
        enable();
        gl.glDisable(GL2.GL_COLOR_MATERIAL);
        
        update = true;
        displayListIndex = gl.glGenLists(1);
        angle = 0;
        quadric = glu.gluNewQuadric();
    }

    /**
     * enable both global ambient and light0
     */
    public void enable(){
        // enable lighting
        gl.glEnable(GL2.GL_LIGHTING);
        gl.glEnable(GL2.GL_LIGHT0);
        gl.glEnable(GL2.GL_LIGHT1);
    }

    /**
     * Updates the intensity, color and location of lights
     */
    private void updateLights() {
        // update light angle
        angle += 0.1;
        angle = angle % 360;
        // calculate position of sun and direction of spotlight
        position0[0] = (float) Math.cos(Math.toRadians(angle));
        position0[1] = (float) Math.sin(Math.toRadians(angle));
        direction1[0] = (float) Math.cos(Math.toRadians(angle))*2;
        direction1[1] = (float) Math.sin(Math.toRadians(angle))*2;
        position1[0] = position0[0] * 20;
        position1[1] = position0[1] * 20;
        position2[0] = position0[0] * -200;
        position2[1] = position0[1] * -200;

        // calculate suns (directional lights) diffuse intensity
        diffuse0[0] = position0[1];
        diffuse0[1] = position0[1];
        diffuse0[2] = position0[1];
        diffuse0[3] = position0[1];
        diffuse1 = diffuse0;
        
        // calculate fog density
        fogColor[0] = position0[1] *2* 0.3f;
        fogColor[1] = position0[1] *2* 0.3f;
        fogColor[2] = position0[1] *2* 0.6f;
        fogDensity = position0[1] * 270;
        
        // update gl parameters
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_SPOT_DIRECTION, direction1, 0);
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_POSITION, position1, 0);
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_DIFFUSE, diffuse1, 0);
        
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, position0, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, ambient0, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, diffuse0, 0);
    }
    /**
     * Renders Sun and enables fog
     * @param gl
     */
    @Override
    public void render(GL2 gl) {
        updateLights();
        
        gl.glPushMatrix();
        gl.glEnable(GL2.GL_COLOR_MATERIAL);
        
        gl.glColorMaterial(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE);
        gl.glColor3d(1, 1, 1);
        gl.glColorMaterial(GL2.GL_FRONT_AND_BACK, GL2.GL_EMISSION);
        
        gl.glColor4d(0.8, 0.3, 0, 1);
        gl.glTranslated(position0[0]*400, position0[1]*400, position0[2]);
        //-----------------SUN
        glu.gluSphere(quadric, 50, 100, 100);
        gl.glColor4d(0, 0, 0, 1);
        gl.glColorMaterial(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE);
        
        gl.glColor3d(1, 1, 1);
        gl.glDisable(GL2.GL_COLOR_MATERIAL);
        gl.glPopMatrix();
        
        // add fog
        gl.glEnable(GL2.GL_FOG);
        
        gl.glFogfv(GL2.GL_FOG_COLOR, fogColor, 0);
        gl.glFogf(GL2.GL_FOG_MODE, GL2.GL_LINEAR);
        gl.glFogf(GL2.GL_FOG_START, fogDensity);
        gl.glFogf(GL2.GL_FOG_END, 450);
        //is disabled in gui
    }
}
