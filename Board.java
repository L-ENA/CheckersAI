import java.awt.*;  
import javax.swing.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;
import java.awt.event.MouseListener;
import java.awt.datatransfer.Transferable;
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
    
    LinkedHashMap<ArrayList<Integer>, Field> squares = new LinkedHashMap<ArrayList<Integer>, Field>();
    
    /**
     * Constructor for objects of class Board
     */
    public Board()
    {
        
  
        
        boolean black = false;////create initial gui state
        for (int i=0; i<8;i++){
            for (int j=0; j<8;j++){
                
                Field gameField = new Field(new int[] {i,j}, black);
                ArrayList<Integer> l = new ArrayList<Integer>();
                l.add(i);
                l.add(j);
                //guiState.add(gameField);
                squares.put(l, gameField);
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
        Set<ArrayList<Integer>> keys = squares.keySet();
        
        for(ArrayList<Integer> index :keys){
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
    public void update(ArrayList<Integer> indexNew, String link)
    {
        //squares.put(indexNew, new Field(indexNew, link));
        
        MouseListener listener = new DragMouseAdapter(indexNew, link);
        
        //String key = String.valueOf(indexNew[0])+","+String.valueOf(indexNew[1]);
        Field f = squares.get(indexNew);
        //System.out.println(squares.keySet());
        f.setIC(link);
        f.addMouseListener(listener);
        f.setTransferHandler(new TransferHandler("icon") {
            
            int dropI;
            int dropJ;
            
            int sourceI;
            int sourceJ;
            /////////enable moving instead of copying
            @Override
            public int getSourceActions(JComponent c) {///to allow move option
                return COPY | MOVE;
            }
            
            //////delete old label
            @Override
            protected void exportDone(JComponent source, Transferable data, int action) {//to delete the source image
                if (action == MOVE){
                    Main.updateState(dropI, dropJ, ((Field) source).link);//updating the state
                    ((Field) source).setIC("");///removing item from source
                }
                
            }
            
            @Override
            public boolean importData(TransferHandler.TransferSupport info) {
                
                Field dropped = (Field) info.getComponent();
                System.out.println("Dropped at" + dropped.i);
                dropI=dropped.i;
                dropJ=dropped.j;
                return super.importData(info);
            }
            
        });
        
        squares.put(indexNew, f);
        //System.out.println(indexNew[0]+" "+indexNew[1]);
        
    }
}
