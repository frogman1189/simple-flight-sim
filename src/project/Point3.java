package project;

/**
 * Utility class for holding 3 doubles. Specifically used for 3d vectors. 
 * Also contains useful mathematical operations between vectors.
 * @author Logan Warner zdv5950 17991274
 */
public class Point3 {
    public double x;
    public double y;
    public double z;
    
    public Point3() {
        this(0, 0, 0);
    }
    
    public Point3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    /**
     * Multiplies the current point with point p and returns a new Point
     * @param p Point to multiply by
     * @return resultant point
     */
    public Point3 multiply(Point3 p) {
        return new Point3(x * p.x, y* p.y, z * p.z);
    }
    
    /**
     * Multiplies the current point with scalar s and returns a new Point
     * @param s Scalar to multiply by
     * @return resultant point
     */
    public Point3 multiply(double s) {
        return new Point3(x * s, y* s, z * s);
    }
    
    /**
     * Adds point p to the current point and returns a new Point
     * @param p Point to add to
     * @return resultant point
     */
    public Point3 add(Point3 p) {
        return new Point3(x + p.x, y + p.y, z + p.z);
    }
    
    /**
     * Adds point p from this point and returns a new Point
     * @param p Point to subtract with
     * @return resultant point
     */
    public Point3 subtract(Point3 p) {
        return new Point3(x - p.x, y - p.y, z - p.z);
    }
    
    /**
     * Divides the this point by scalar s and returns the resultant point
     * @param s Scalar to divide by
     * @return resultant point
     */
    public Point3 divide(double s) {
        return new Point3(x / s, y / s, z / s);
    }
    
    /**
     * Returns this point in string form
     * @return String
     */
    public String toString() {
        return x + ", " + y + ", " + z;
    }
}
