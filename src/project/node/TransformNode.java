package project.node;

import com.jogamp.opengl.GL2;
import project.Point3;
import project.Renderable;

/**
 * Node that incorporates 3d matrix operations for simple positioning of static
 * elements in a node tree scene
 * @author Logan Warner zdv5950 17991274
 */
public class TransformNode extends Node {
    public Point3 translate;
    public Point3 scale;
    public Point3 rotate;
    public TransformNode(Renderable item, Point3 translate, Point3 rotate, Point3 scale) {
        super(item);
        this.translate = translate;
        this.scale = scale;
        this.rotate = rotate;
    }
    public TransformNode(Point3 translate, Point3 rotate, Point3 scale) {
        this(null, translate, rotate, scale);
    }
    
    @Override
    public void render(GL2 gl) {
        gl.glScaled(scale.x, scale.y, scale.z);
        gl.glTranslated(translate.x, translate.y, translate.z);
        gl.glRotated(rotate.x, 1, 0, 0);
        gl.glRotated(rotate.z, 0, 0, 1);
        gl.glRotated(rotate.y, 0, 1, 0);
        super.render(gl);
    }
    
}
