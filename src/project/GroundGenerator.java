
package project;

import obj.Rock;
import obj.Cactus;
import com.jogamp.opengl.GL2;
import java.util.Random;
import obj.Well;

/**
 * Ground generator is used to generate terrain around the glider
 * @author Logan Warner
 */
public class GroundGenerator implements Renderable{
    public static final double CELL_SIZE = 1000;
    public static final int CACTUS_COUNT = 100;
    public static final int ROCK_COUNT = 50;
    private final Controller controller;
    private Integer cellXCur;
    private Integer cellZCur;
    private final Random rand;
    private final Cactus[][] cacti;
    private final Rock[][] rocks;
    private final Ground[] ground;
    private final Well[] wells;
    private final int displayListIndex;
    private boolean update;
    public GroundGenerator(Controller controller, GL2 gl) {
        this.controller = controller;
        displayListIndex = gl.glGenLists(1);
        update = true;
        cellXCur = null;
        cellZCur = null;
        rand = new Random();
        
        // generate cactus pool
        cacti = new Cactus[9][CACTUS_COUNT];
        for(int i = 0; i < 9; i++) {
            for(int k = 0; k < CACTUS_COUNT; k++) {
                cacti[i][k] = new Cactus(gl, 0, 0);
            }
        }
        //generate rock pool
        rocks = new Rock[9][ROCK_COUNT];
        for(int i = 0; i < 9; i++) {
            for(int k = 0; k < ROCK_COUNT; k++) {
                rocks[i][k] = new Rock(gl, 0, 0);
            }
        }
        //generate well pool
        wells = new Well[9];
        for(int i=0; i<9; i++) {
            wells[i] = new Well(gl, 0, 0);
        }
        //generate ground pool
        ground = new Ground[9];
        for(int i=0; i<9; i++) {
            ground[i] = new Ground(gl, (float) CELL_SIZE);
        }
        
        //initialise terrain
        gen((int) ((controller.posCur.x) / CELL_SIZE), (int) ((controller.posCur.z) / CELL_SIZE));
    }

    /**
     * Generates terrain using a seed based on the current x and z cells.
     * sets the current cellX and cellZ to the provided arguments
     * @param cellX gliders current x cell
     * @param cellZ gliders current z cell
     */
    public final void gen(int cellX, int cellZ) {
        cellXCur = cellX;
        cellZCur = cellZ;
        for(int x = 0; x < 3; x++) {
            for(int z = 0; z < 3; z++) {
                // create seed based on cell positions
                long seed = ((cellX + x-1) << 16) + cellZ+z-1;
                rand.setSeed(seed);
                // generate cacti
                for(int i = 0; i < CACTUS_COUNT; i++) {
                    cacti[x * 3 + z][i].setPosition(
                            (cellXCur + x-1) * CELL_SIZE + rand.nextInt((int) CELL_SIZE) - CELL_SIZE/2,
                            (cellZCur + z-1) * CELL_SIZE + rand.nextInt((int) CELL_SIZE) - CELL_SIZE/2
                    );
                    // generate giant cacti poles 1 in 10 times when generating a cactus
                    if(rand.nextInt(10) == 1) {
                        cacti[x * 3 + z][i].setScale(rand.nextInt(100) + 10);
                    }
                    else {
                        cacti[x * 3 + z][i].setScale(1);
                    }
                }
                //generate rock positions and scale
                for(int i = 0; i < ROCK_COUNT; i++) {
                    rocks[x * 3 + z][i].setPosition(
                            (cellXCur + x-1) * CELL_SIZE + rand.nextInt((int) CELL_SIZE) - CELL_SIZE/2,
                            (cellZCur + z-1) * CELL_SIZE + rand.nextInt((int) CELL_SIZE) - CELL_SIZE/2
                    );
                    rocks[x * 3 + z][i].setScale(new Point3(1+rand.nextInt(50), 1+rand.nextInt(50), 1+rand.nextInt(50)));
                }
                //generate wells positions
                wells[x * 3 + z].setPosition(
                    (cellXCur + x-1)*CELL_SIZE + rand.nextInt((int) CELL_SIZE) - CELL_SIZE/2,
                    (cellZCur + z-1)*CELL_SIZE + rand.nextInt((int) CELL_SIZE) - CELL_SIZE/2
                );
                // layout ground
                ground[x * 3 + z].setPosition(
                    (cellXCur + x-1)*CELL_SIZE,
                    (cellZCur + z-1)*CELL_SIZE
                );
                System.out.println("Ground " +x + "," + z +": " + (cellXCur + x-1)*CELL_SIZE + "," + (cellZCur + z-1)*CELL_SIZE);
            }
        }
    }
    @Override
    public void render(GL2 gl) {
        // calculate current cell position
        int cellX = (int) (controller.posCur.x / CELL_SIZE);
        int cellZ = (int) (controller.posCur.z / CELL_SIZE);
        // if current cells are not the same as the cached current cell 
        // regenerate terrain
        if(cellX != cellXCur || cellZ != cellZCur) {
            gen(cellX, cellZ);
            update = true;
        }
        // if update is true (terrain regenerated) then rebuild the display list
        if(update == true) {
            update = false;
            gl.glNewList(displayListIndex, GL2.GL_COMPILE);
            gl.glPushMatrix();
            for(int i = 0; i < 9; i++) {
                for(Cactus c: cacti[i]) {
                    c.render(gl);
                }
            }
            for(int i = 0; i < 9; i++) {
                for(Rock r: rocks[i]) {
                    r.render(gl);
                }
            }
            for(Well w: wells) {
                w.render(gl);
            }
            for(Ground g: ground) {
                g.render(gl);
            }
            gl.glPopMatrix();
            gl.glEndList();
        }
        
        // render the terrain
        gl.glPushMatrix();
        gl.glCallList(displayListIndex);
        gl.glPopMatrix();
    }
}
