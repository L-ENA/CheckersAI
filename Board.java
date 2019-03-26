import java.awt.*;  
import javax.swing.*;
import java.util.ArrayList;
import java.util.Iterator;
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
    ArrayList<Field> guiState= new ArrayList<Field>();
    
    /**
     * Constructor for objects of class Board
     */
    public Board()
    {
        
  
        
        boolean black = false;////create initial gui state
        for (int i=0; i<8;i++){
            for (int j=0; j<8;j++){
                Field gameField = new Field(new int[] {i,j}, black);
                guiState.add(gameField);
                
                black= !black;//flipping colour variable
            }
            black= !black;//flipping colour variable
        }
        
        
        visualise();
    }
    
    ////visualising board according to the current gui state
    private void visualise(){
        this.setLayout(new GridLayout(8,8));  
        //setting grid layout of 3 rows and 3 columns  
        for (Field f:guiState){
            this.add(f);
        }
        this.setSize(300,300);  
        this.setVisible(true);
    }
    
    /**
     * checking if this is the right field, and updating it
     */
    public void update(int[] index, String colour, boolean king)
    {
        for(Field f:guiState){
            if(f.compare(index)){
                if (colour == "b"){
                    if (!king){
                        f.setIC("blackNorm.png");
                        System.out.println("updated black");
                    }else
                        f.setIC("blackKing.png");
                } else if (colour == "w"){
                    if(!king)
                        f.setIC("whiteNorm.png");
                    else
                        f.setIC("whiteKing.png");
                } else {
                    f.setIcon(null);
                    
                }
            }
            f.revalidate();   
        }
        
        visualise();
    }
}
