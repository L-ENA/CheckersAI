import java.util.*;
/**
 * Write a description of class Node here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Node
{
    // instance variables - replace the example below with your own
    protected ArrayList<Integer> index;
    protected OCCUPY oc;
    

    /**
     * Constructor for objects of class Node
     */
    public Node(int i, int j,OCCUPY oc)
    {
        this.oc=oc;
        this.index=new ArrayList<>();
        this.index.add(i);
        this.index.add(j);
        
    }

    /**
     * An example of a method - replace this comment with your own
     *
     * @param  y  a sample parameter for a method
     * @return    the sum of x and y
     */
    public void print()
    {
        // put your code here
        System.out.println(index.get(0) + ", "+ index.get(1));
    }
    
    
    public boolean equals(ArrayList<Integer> comp) {
        if(comp.equals(index))
            return true;
        else
            return false;
    }
    
    
}
