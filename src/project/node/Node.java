package project.node;

import java.util.ArrayList;
import com.jogamp.opengl.GL2;
import project.Renderable;
/**
 * Node class that enables Hierarchal modeling
 * @author Logan Warner zdv5950 17991274
 */
public class Node {
    private ArrayList<Node> childList;
    private Renderable item;
    
    public Node(Renderable item) {
        childList = new ArrayList<>();
        this.item = item;
    }
    public Node() {
        this(null);
    }
    // render item first then children
    public void render(GL2 gl) {
        if(item != null) {
            item.render(gl);
        }
        childList.forEach((c)->{
            gl.glPushMatrix();
            c.render(gl);
            gl.glPopMatrix();
        });
    }
    public Renderable getItem() {
        return item;
    }
    public void setItem(Renderable item) {
        this.item = item;
    }
    public void addChildNode(Node node) {
        childList.add(node);
    }
    public Node removeChildNode(int i) {
        return childList.remove(i);
    }
    public boolean removeChildNode(Node n) {
        return childList.remove(n);
    }
    
}
