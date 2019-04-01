
/**
 * Write a description of class Validator here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Validator
{
    // instance variables - replace the example below with your own
    private int iD;
    private int jD;
    private int iS;
    private int jS;

    /**
     * Constructor for objects of class Validator
     */
    public Validator()
    {
        // initialise instance variables
        
    }

    /**
     * An example of a method - replace this comment with your own
     *
     * @param  y  a sample parameter for a method
     * @return    the sum of x and y
     */
    public void setDest(int i, int j)
    {
        // put your code here
        this.iD = i;
        this.jD=j;
    }
    
    public void setSource(int i, int j)
    {
        // put your code here
        this.iS = i;
        this.jS=j;
    }
    
    public void getFeedback(){
        if(jS==jD)
            System.out.println("I am unhappy with this move!!!!!!!");
    }
}
