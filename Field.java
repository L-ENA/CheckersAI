import javax.swing.*;
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
/**
 * Write a description of class Field here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Field extends JLabel
{
    // instance variables - replace the example below with your own
    private int[] index;
    private boolean black;//black is true
    final int DIMENSION = 15;
    

     
    /**
     * Constructor for objects of class Field
     */
    public Field(int[] index, boolean black)
    {
        
        this.index=index;
        this.black=black;
        this.setSize(DIMENSION, DIMENSION);
        this.setOpaque(true);
        if(black){
            this.setBackground(Color.BLACK);
        } else {
            this.setBackground(Color.WHITE);
        }
        
        Border border = BorderFactory.createLineBorder(Color.RED, 1);
        this.setBorder(border);
        
    }

    /**
     * An example of a method - replace this comment with your own
     *
     * @param  y  a sample parameter for a method
     * @return    the sum of x and y
     */
    public int sampleMethod(int y)
    {
        // put your code here
        return 3;
    }
}
