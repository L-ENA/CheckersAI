import javax.swing.*;
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.image.*;
import java.io.*;

/**
 * Write a description of class Field here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Field extends JLabel
{
    // instance variables - replace the example below with your own
    protected int i;
    protected int j;
    protected int[] index;
    private boolean black;//black is true
    final int DIMENSION = 15;
    protected String link;

    protected String type = "";
    /**
     * Constructor for objects of class Field
     */
    public Field(int[] index, boolean black)
    {
        
        this.i=index[0];
        this.j=index[1];
        this.index= index;
        this.black=black;
        this.setSize(DIMENSION, DIMENSION);
        this.setOpaque(true);
        if(black){
            this.setBackground(Color.BLACK);
        } else {
            this.setBackground(Color.WHITE);
        }
        
        Border border = BorderFactory.createLineBorder(Color.RED, 2);
        TitledBorder tb = new TitledBorder(null, "(" + index[0]+ ", "+ index[1] +")", TitledBorder.LEFT,TitledBorder.ABOVE_BOTTOM);
        tb.setTitleColor(Color.gray);
        this.setBorder(tb);
        
        
        
    }
    
    public Field(int[] index, String link){
        this.i=index[0];
        this.j=index[1];
        this.index= index;
        this.black=true;
        this.setSize(DIMENSION, DIMENSION);
        this.setOpaque(true);
        if(black){
            this.setBackground(Color.BLACK);
        } else {
            this.setBackground(Color.WHITE);
        }
        
        Border border = BorderFactory.createLineBorder(Color.RED, 2);
        TitledBorder tb = new TitledBorder(null, "(" + index[0]+ ", "+ index[1] +")", TitledBorder.LEFT,TitledBorder.ABOVE_BOTTOM);
        tb.setTitleColor(Color.gray);
        this.setBorder(tb);
        setIC(link);
    }
    
    public void setIC(String path){
        this.setIcon(new ImageIcon(new ImageIcon(path).getImage().getScaledInstance(70, 70, 4)));
    }

    /**
     * An example of a method - replace this comment with your own
     *
     * @param  y  a sample parameter for a method
     * @return    the sum of x and y
     */
    public int[] getIndex()
    {
        // put your code here
        return index;
    }
    
    public boolean compare(int[] ind)
    {
        if(this.index[0] == ind[0] && this.index[1] == ind[1])
            return true;
        else
            return false;
    }
    
}
