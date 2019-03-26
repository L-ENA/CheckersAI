
/**
 * Write a description of class Node here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Node
{
    // instance variables - replace the example below with your own
    protected int[] index;
    protected OCCUPY oc;
    protected boolean king;

    /**
     * Constructor for objects of class Node
     */
    public Node(int i, int j,OCCUPY oc)
    {
        this.oc=oc;
        this.index=new int[]{i, j};
        this.king=false;
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
        System.out.println(index[0] + ", "+ index[1]);
    }
}
