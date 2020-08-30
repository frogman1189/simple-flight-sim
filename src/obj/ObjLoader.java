
package obj;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A utility class that parses .obj files and converts them into v, vt, vn and 
 * face arrays. Currently it ignores groups and other metadata.
 * @author Logan Warner
 */
public class ObjLoader {
    public Double[] v;
    public Double[] vt;
    public Double[] vn;
    public Integer[][][] f;
    /**
     * Create new ObjLoader
     * @param path path to .obj file to load.
     * @throws IOException if path does not point to a file.
     */
    public ObjLoader(String path) throws IOException {
        File file = new File(path);
        ArrayList<Double> vArray = new ArrayList<>();
        ArrayList<Double> vnArray = new ArrayList<>();
        ArrayList<Double> vtArray = new ArrayList<>();
        ArrayList<Integer[][]> fArray = new ArrayList<>();
        
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader b = new BufferedReader(fileReader);
            Scanner sf = new Scanner(fileReader);
            while(sf.hasNext()) {
                //load values into respective arraylists
                Scanner st = new Scanner(sf.nextLine());
                if(st.hasNext("^[vf][nt]?")) {
                    String type = st.next("^[vf][nt]?");
                    if("v".equals(type)) {
                       while(st.hasNextDouble()) {
                           vArray.add(st.nextDouble());
                       }
                    }
                    else if("vn".equals(type)) {
                       while(st.hasNextDouble()) {
                           vnArray.add(st.nextDouble());
                       } 
                    }
                    else if("vt".equals(type)) {
                       while(st.hasNextDouble()) {
                           vtArray.add(st.nextDouble());
                       }
                    }
                    // face takes more complex system
                    else if("f".equals(type)) {
                        // grab the rest of the input as text again
                        String tmp = st.nextLine();
                        // split string to get list of each different "point"
                        String[] points = tmp.split(" ");
                        Integer[][] face = new Integer[points.length-1][];
                        // for each "point" (1/2/3 is an example of a single 
                        // point describing the v, vn and vt
                        
                        // first point is always null due to split
                        for(int i = 1; i < points.length; i++) {
                            //split along / and iterate through points parsing in
                            // each value to the array
                            String[] vertexIndices = points[i].split("/");
                            Integer[] point = new Integer[vertexIndices.length];
                            for(int k = 0; k < vertexIndices.length; k++) {
                                // - 1 to set it to index
                                try {
                                    point[k] = Integer.parseInt(vertexIndices[k]) - 1;
                                }
                                catch (NumberFormatException e) {
                                    point[k] = null;
                                }
                            }
                            face[i-1] = point;
                        }
                        fArray.add(face);
                    }
                }
            }
            
            //convert arrayLists to arrays for quicker access
            v = vArray.toArray(new Double[vArray.size()]);
            vt = vtArray.toArray(new Double[vtArray.size()]);
            vn = vnArray.toArray(new Double[vnArray.size()]);
            f = fArray.toArray(new Integer[fArray.size()][][]);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ObjLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
