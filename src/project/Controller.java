package project;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import obj.Glider;
import project.node.Node;
/**
 * Controller deals with the "Physics" related to controlling the glider
 * and all controls maps to various functions within the controller.
 * @author Logan Warner zdv5950 17991274
 */
public class Controller extends Node implements Renderable {
    private GLU glu;
    private GL2 gl;
    
    private final Camera camera;
    
    private static final double m = 1000; // mass
    private double negativeEk; // kinetic energy you need to overcome in order to stop stalling
    private double ek; //kinetic energy
    private double ep; // gravitational potential energy
    private static final double G = 0.1; // gravity
    private double velRoll;
    private double velPitch;
    public Point3 posCur;
    private Point3 angleCur;
    private Glider glider;
    private boolean reset;
    
    public Controller(GL2 gl, Camera camera, Point3 pos, Point3 angle) {
        super();
        this.gl = gl;
        this.camera = camera;
        posCur = pos;
        angleCur = angle;
        velRoll = 0;
        velPitch = 0;
        glider = new Glider(gl);
        ek = 1000;
        ep = pos.y * m * G;
        negativeEk = 0;
        reset = false;
    }

    /**
     * change Roll by specified angle in degrees
     * @param angle in degrees
     */
    public void changeRoll(double angle) {
         velRoll += angle;
    }
    
    /**
     * Change the Pitch by specified angle in degrees
     * @param angle in degrees
     */
    public void changePitch(double angle) {
        velPitch += angle;
    }
    
    /**
     * Reset the glider to its initial height. Used to reset a run in case of
     * a crash
     */
    public void reset() {
        reset = true;
    }
    
    /**
     * Calculate new glider position based upon kinetic/gravitational potential
     * energy model and current status
     */
    private void calculatePosition() {
        // cache the original gravitational potential energy
        double oldEp = ep;
        double magnitude = Math.sqrt(ek*2/m);
        
        // calculate roll
        angleCur.z += velRoll;
        angleCur.z = angleCur.z % 360;
        velRoll = 0;
        
        // roll used in turning calc (don't use angleCur.z as that makes the 
        // plane flip weirdly
        double roll = angleCur.z % 90;
        if(roll > 90) {
            roll = -90;
        }
        if(roll < -90) {
            roll = 90;
        }
        // calculate turn based upon new roll
        angleCur.y -= roll * magnitude/100;
        
        //calculate pitch
        angleCur.x += velPitch;
        if(angleCur.x > 180) {
            angleCur.x = -180;
        }
        if(angleCur.x < -180) {
            angleCur.x = 180;
        }
        velPitch = 0;
        
        // add energy to system through "lift" when plane is within 45 degree 
        // arc of x-z plane
        double lift=0;
        if(angleCur.x < 0 && angleCur.x > -45) {
            ek += angleCur.x/-45 * ek/30;
        }
        
        // set up our current direcion vector
        Point3 vector = new Point3();
        // if negativeEk then check whether ek is greater than negativeEK
        // if it is set negativeEK to 0 (stop stalling) and provide it with
        // 100ek then set up movement with reduced horizontal speed and 
        // increased vertical speed
        // otherwise load the speeds based on current direction
        if(negativeEk > 0) {
            if(ek >= negativeEk && angleCur.x > -10 && angleCur.x < 200) {
                negativeEk = 0;
                ek = 100;
            }
            vector.z = Math.cos(Math.toRadians(angleCur.x)) * magnitude/10;
            vector.y -= Math.abs(Math.sin(Math.toRadians(angleCur.x)) * magnitude) * 0.8;
        }
        else {
            ek -= ek/200;
            vector.z = Math.cos(Math.toRadians(angleCur.x)) * magnitude;
            vector.y = -Math.sin(Math.toRadians(angleCur.x)) * magnitude;
        }
        // if you have less than 50 ek you stall
        if(ek < 50) {
            negativeEk = 1000;
        }
        
        //update y pos
        posCur.y += vector.y;
        // update z and x pos mapping vector.z to z and x using current direction
        posCur.z += vector.z * Math.cos(Math.toRadians(angleCur.y));
        posCur.x += vector.z * Math.sin(Math.toRadians(angleCur.y));

        
        // prevent Glider from going through the ground plane
        if(posCur.y < 0) {
            posCur.y = 0;
            // rapidly remove ek from friction with ground
            if(ek > 0) {
                ek-=ek * 0.8;
            }
        }
        
        // calculate new ek based upon old ek and the difference between old ep
        // current ep.
        ep = m * G * posCur.y;
        ek += oldEp - ep;
        if(ek < 0) {
            ek = 0;
        }
        
        // limit ek to 10000 to help prevent system from gaining too much energy
        // from lift
        if(ek > 10000) {
            ek = 10000;
        }
        // if reset then set position to 100 in the air and set ek to 1000
        if(reset) {
            reset = false;
            posCur.y = 100;
            ek = 1000;
            ep = m * G * posCur.y;
            negativeEk=0;
        }
    }
    
    @Override
    public void render(GL2 gl) {
        calculatePosition();
        
        // render actual position
        gl.glPushMatrix();
        gl.glTranslated(posCur.x, posCur.y, posCur.z); // +1 is to offset helicopter so not in ground
        // render children nodes at the same position but not rotation
        super.render(gl);
        gl.glRotated(angleCur.y, 0, 1, 0);
        gl.glRotated(angleCur.x, 1, 0, 0);
        gl.glRotated(angleCur.z, 0, 0, 1);
        
        // render glider and camera at position and rotation
        glider.render(gl);
        camera.setLookAt(posCur, angleCur.y);
        gl.glPopMatrix();
    }
    
}
