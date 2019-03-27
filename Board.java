import java.awt.*;  
import javax.swing.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;
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
    LinkedHashMap<String, Field> squares = new LinkedHashMap<String, Field>();
    
    /**
     * Constructor for objects of class Board
     */
    public Board()
    {
        
  
        
        boolean black = false;////create initial gui state
        for (int i=0; i<8;i++){
            for (int j=0; j<8;j++){
                Field gameField = new Field(new int[] {i,j}, black);
                //guiState.add(gameField);
                squares.put(String.valueOf(i)+","+String.valueOf(j), gameField);
                black= !black;//flipping colour variable
            }
            black= !black;//flipping colour variable
        }
        
        
        visualise();
    }
    
    ////visualising board according to the current gui state
    protected void visualise(){
        this.removeAll();
        this.setLayout(new GridLayout(8,8));  
        
        //setting grid layout of 3 rows and 3 columns  
        Set<String> keys = squares.keySet();
        
        for(String index :keys){
            this.add(squares.get(index));
        }
        this.setSize(300,300); 
        this.revalidate();
        this.repaint();
        this.setVisible(true);
    }
    
    /**
     * checking if this is the right field, and updating it
     */
    public void update(int[] indexNew, String link)
    {
        //squares.put(indexNew, new Field(indexNew, link));
        
        String key = String.valueOf(indexNew[0])+","+String.valueOf(indexNew[1]);
        Field f = squares.get(key);
        //System.out.println(squares.keySet());
        f.setIC(link);
        //System.out.println("adding black");
        squares.put(key, f);
        //System.out.println(indexNew[0]+" "+indexNew[1]);
        
    }
}
