import java.awt.*;  
import javax.swing.*;
/**
 * Write a description of class Board here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Board extends JPanel
{
    // instance variables - replace the example below with your own
    int length = 8;
    int width = 8;

    /**
     * Constructor for objects of class Board
     */
    public Board()
    {
        this.setLayout(new GridLayout(8,8));  
        //setting grid layout of 3 rows and 3 columns  
  
        
        boolean black = false;
        for (int i=0; i<8;i++){
            for (int j=0; j<8;j++){
                Field gameField = new Field(new int[] {i,j}, black);
                this.add(gameField);
                
                black= !black;//flipping colour variable
            }
            black= !black;//flipping colour variable
        }
        
        this.setSize(300,300);  
        this.setVisible(true);

    }
    /**
     * An example of a method - replace this comment with your own
     *
     * @param  y  a sample parameter for a method
     * @return    the sum of x and y
     */
    public void updateBoard()
    {
        for (int i=0; i<8;i++){
            //
        }
        
    }
}
