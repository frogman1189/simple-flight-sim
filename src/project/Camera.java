package project;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;

/**
 * Class that handles OpenGL camera functions
 * @author Logan Warner zdv5950 17991274
 */
public class Camera implements Renderable {
    private static final double FOV = 45;
    private double windowWidth = 1000;
    private double windowHeight = 500;
    private Point3 pos = new Point3(-10, 10, -10);
    private Point3 look = new Point3(0, 0, 0);
    public double DISTANCE = 10;
    private double angleX = 0;
    private double angleY = 45;
    private double lastX = 0;
    private double lastY = 0;
    private boolean follow = true;

    /**
     * Deals with rendering the object within the seen
     * @param gl GL2 from the opengl canvas
     */
    @Override
    public void render(GL2 gl){
        // set up projection first
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        GLU glu = new GLU();
        
        glu.gluPerspective(90, (double) windowWidth/windowHeight, 0.1, 1100);
        
        // set up the camera position and orientation
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
        
        glu.gluLookAt(pos.x,   pos.y,   pos.z, //eye
                        look.x,   look.y,   look.z, // looking at 
                        0.0, 1.0, 0.0); // up   
    }
    
    /**
     * Sets up the lookAt point - could be a specified object's location
     * @param x X coordinate of the lookAt point
     * @param y Y coordinate of the lookAt point
     * @param z Z coordinate of the lookAt point
     */
    public void follow() {
        follow = true;
    }
    
    /**
     * Sets the point that the camera is looking at and recalculates the camera
     * position based on the provided angle
     * @param look Point3 specifying position of object camera is looking at
     * @param angle angle in degrees from the xz plane that the camera is 
     * looking from
     */
    public void setLookAt(Point3 look, double angle) {
       this.look = look;
       calculatePos(angle);
    }
    
    /**
     * Calculates camera's x-z position based upon yaw, and y position based 
     * upon roll (angleX)
     * @param angle Angle on the Y-Axis (Yaw)
     */
    public void calculatePos(double angle) {
        if(follow) {
            pos.x =  look.x + Math.sin(Math.toRadians(angle + 180)) * DISTANCE;
            pos.z =  look.z + Math.cos(Math.toRadians(angle + 180)) * DISTANCE;
            angleX = angle + 180;
        }
        else {
            pos.x =  look.x + Math.sin(Math.toRadians(angleX)) * DISTANCE;
            pos.z =  look.z + Math.cos(Math.toRadians(angleX)) * DISTANCE;
        }
        pos.y = look.y + Math.sin(Math.toRadians(angleY)) * DISTANCE;
    }
 
    /**
     * Setter for angleX which also updates position. Specifically used by the 
     * MouseMotionListener in GUI in order to control the vertical angle
     * @param y Current y position of the mouse pointer
     */
    public void updateAngleX(double y, double x) {
        follow = false;
        // if y and lastPoint are within 10 pixels then they are probably part 
        // of the same movement so calculate position. Otherwise we are probably 
        // beginning a new drag operation
        if(Math.abs(y - lastY) < 10) {
            angleY += y - lastY;
            pos.y = look.y + Math.sin(Math.toRadians(angleY)) * DISTANCE;
        }
        if(Math.abs(x - lastX) < 10) {
            angleX -= x - lastX;
            pos.x = look.x + Math.sin(Math.toRadians(angleX)) * DISTANCE;
            pos.z = look.z + Math.cos(Math.toRadians(angleX)) * DISTANCE;
        }
        
        lastY = y;
        lastX = x;
    }
    
    /**
     * Changes the camera's distance from the object/point it is looking at
     * @param distance double specifying how much to change the camera's 
     * distance by.
     */
    public void updateDistance(double distance) {
        DISTANCE += distance;
        if(distance < 1) {
            distance = 1;
        }
        updateAngleX(lastY, lastX);
    }
    /**
     * Passes a new window size to the camera.
     * This method should be called from the <code>reshape()</code> method
     * of the main program.
     *
     * @param width the new window width in pixels
     * @param height the new window height in pixels
     */
    public void newWindowSize(int width, int height) {
       windowHeight = height;
       windowWidth = width;
    }

}
