
package project;

import project.node.Node;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;
import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import project.eventlisteners.AutoDispatchListener;
import project.eventlisteners.DispatchEvent;
import project.eventlisteners.KeyAutoDispatcher;

/**
 * Class that handles GUI related events
 * @author Logan Warner zdv5950 17991274
 */
public class GUI implements GLEventListener, KeyListener, AutoDispatchListener, MouseMotionListener,MouseWheelListener{

	//scene objects
	private Camera camera;
        private Node root;
        private KeyAutoDispatcher autoDispatcher;
        private Controller controller;
        
        /**
         * Sets up AWT Frame with GLCanvas in order to run application
         * @param args commandline args (unused)
         */
	public static void main(String[] args) {
		
		Frame frame = new Frame("Desert Glider");
		GLCanvas canvas = new GLCanvas();
		GUI app = new GUI();
                //add event listeners
		canvas.addGLEventListener(app);
                canvas.addKeyListener((java.awt.event.KeyListener) app);
                canvas.addMouseMotionListener(app);
                canvas.addMouseWheelListener(app);
		frame.add(canvas);
		frame.setSize(1000, 500);
		final FPSAnimator animator = new FPSAnimator(canvas,60);
		frame.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				// Run this on another thread than the AWT event queue to
				// make sure the call to Animator.stop() completes before
				// exiting
				new Thread(new Runnable() {

					@Override
					public void run() {
						animator.stop();
						System.exit(0);
					}
				}).start();
			}
		});
		// Center frame
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		animator.start();
	}

        /**
         * Generates key mapping and returns it
         * @return String outlining key mapping
         */
        public String controls() {
            String output = "";
            output += "Copyright Information\n";
            output += "--------------------------------------------------------------------------------------------\n";
            output += "All models used in this program are from turbo squid https://www.turbosquid.com/ and are free\n";
            output += "for use.\n";
            output += "The stone and sand textures used in this program are from the miranda minecraft texture pack\n";
            output += "and are used in this project with permission from the creator\n";
            output += "https://www.planetminecraft.com/texture-pack/miranda-4514231/\n";
            output += "The cactus texture is from https://www.sketchuptextureclub.com/terms-of-use and is free for\n";
            output += "non commercial use\n\n\n";
            output += "Key Mapping:\n";
            output += "--------------------------------------------------------------------------------------------\n";
            output += " UP/DOWN ARROWS: Increase/Decrease pitch\n";
            output += " LEFT/RIGHT ARROWS: Increase/Decrease roll\n";
            output += " SPACE BAR: Reset glider height and speed\n";
            output += " MOUSEDOWN+DRAG: Change camera angle\n";
            output += " MOUSEWHEEL: Zoom in/out\n";
            
            return output;
        }

        /**
         * OpenGL init function. Set up canvas and initialize objects
         * @param drawable 
         */
	@Override
	public void init(GLAutoDrawable drawable) {
            // output controls
            System.out.println(controls());
            GL2 gl = drawable.getGL().getGL2();
            // Enable VSync
            gl.setSwapInterval(1);
            // Setup the drawing area and shading mode
            gl.glEnable (GL2.GL_BLEND);
            gl.glBlendFunc (GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
            gl.glClearColor(1f, 1f, 1f, 1f);
            gl.glShadeModel(GL2.GL_SMOOTH);
            gl.glEnable(GL2.GL_DEPTH_TEST);
            
            // set up Key auto dispatcher for smoother key input
            autoDispatcher = new KeyAutoDispatcher();
            autoDispatcher.addEventListener(this);
            
            // set up base objects
            camera = new Camera();
            controller = new Controller(gl, camera, new Point3(10, 100, -500), new Point3(0, 0, 0));
            root = new Node(camera);
            root.addChildNode(new Node(new Origin(gl)));
            root.addChildNode(controller);
            root.addChildNode(new Node(new GroundGenerator(controller, gl)));
            controller.addChildNode(new Node(new Lighting(gl)));
            controller.addChildNode(new Node(new SkyOrb(gl, 500)));
	}

        /**
         * OpenGL reshape function. Called when window resized. Updates camera's
         * windowWidth and windowHeight parameters
         * @param drawable
         * @param x
         * @param y
         * @param width
         * @param height 
         */
	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
            camera.newWindowSize(width, height);
	}

        /**
         * OpenGL render function. Draw scene
         * @param drawable 
         */
	@Override
	public void display(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();

		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
		gl.glEnable(GL2.GL_COLOR_MATERIAL);

                // makes key events occur at roughly frame rate. should be more responsive
		autoDispatcher.triggerEvent();
                
                // render scene
                root.render(gl);
                
                // disable fog out here rather than in class so that it affects
                // all classes but does get disabled eventually
                gl.glDisable(GL2.GL_FOG);
		
		// Flush all drawing operations to the graphics card
		gl.glFlush();
	}

    @Override
    public void dispose(GLAutoDrawable drawable) {
        // unused
    }

    @Override
    public void keyTyped(KeyEvent ke) {
        // unused
    }
    
    /**
     * Add key presses into the KeyAutoDispatcher so that keyevents can be more
     * More responsive. Three Exceptions. L (Which changes wireframe) and the H (Hover).
     *Those don't really need the same 
     * responsiveness and java's default key handling seems good enough, if not 
     * preferable
     * @param ke KeyEvent
    */
    @Override
    public void keyPressed(KeyEvent ke) {
        if(! autoDispatcher.eventAdded(ke.getKeyCode())) {
            switch (ke.getKeyCode()) {
                case KeyEvent.VK_SPACE:
                    controller.reset();
                    break;
                case KeyEvent.VK_F:
                    camera.follow();
                    break;
                case KeyEvent.VK_UP:
                    autoDispatcher.addDispatchEvent(KeyEvent.VK_UP);
                    break;
                case KeyEvent.VK_DOWN:
                    autoDispatcher.addDispatchEvent(KeyEvent.VK_DOWN);
                    break;
                case KeyEvent.VK_LEFT:
                    autoDispatcher.addDispatchEvent(KeyEvent.VK_LEFT);
                    break;
                case KeyEvent.VK_RIGHT:
                    autoDispatcher.addDispatchEvent(KeyEvent.VK_RIGHT);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Remove keys from AutoKeyDispatcher when key released
     * @param ke Key Event
     */
    @Override
    public void keyReleased(KeyEvent ke) {
        //System.out.println("remove: " + ke);
        switch (ke.getKeyCode()) {
            case KeyEvent.VK_UP:
                autoDispatcher.removeDispatchEvent(KeyEvent.VK_UP);
                break;
            case KeyEvent.VK_DOWN:
                autoDispatcher.removeDispatchEvent(KeyEvent.VK_DOWN);
                break;
            case KeyEvent.VK_LEFT:
                autoDispatcher.removeDispatchEvent(KeyEvent.VK_LEFT);
                break;
            case KeyEvent.VK_RIGHT:
                autoDispatcher.removeDispatchEvent(KeyEvent.VK_RIGHT);
                break;
            default:
                break;
        }
    }

    /**
     * Event Handler for AutoKeyDispatcher. Calls the code to be run when a key 
     * is "Pressed" by the AutoKeyDispatcher. Using this gives more responsive
     * Key Input than java's default Key handler
     * @param e 
     */
    @Override
    public void EventDispatched(DispatchEvent e) {
        switch(e.type) {
            case KeyEvent.VK_UP:
                controller.changePitch(1);
                break;
            case KeyEvent.VK_DOWN:
                controller.changePitch(-1);
                break;
            case KeyEvent.VK_LEFT:
                controller.changeRoll(-2);
                break;
            case KeyEvent.VK_RIGHT:
                controller.changeRoll(2);
                break;
        }
    }

    /**
     * Change camera's vertical position when mouse dragged.
     * @param me 
     */
    @Override
    public void mouseDragged(MouseEvent me) {
        camera.updateAngleX(me.getY(), me.getX());
    }

    @Override
    public void mouseMoved(MouseEvent me) {
        // unused
    }
    /**
     * Change camera's distance from look object
     * @param mwe 
     */
    @Override
    public void mouseWheelMoved(MouseWheelEvent mwe) {
        camera.updateDistance(mwe.getPreciseWheelRotation());
    }
    


}
