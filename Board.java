import java.awt.*;  
import javax.swing.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;
import java.awt.event.MouseListener;
import java.awt.datatransfer.Transferable;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.util.Observable;
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
    Validator val = new Validator();//checks if drag and drops are valid moves
    LinkedHashMap<ArrayList<Integer>, Field> squares = new LinkedHashMap<ArrayList<Integer>, Field>();
    Main game;
    /**
     * Constructor for objects of class Board
     */
    public Board(Main game)
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
        this.game=game;
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
    
    public void updateNoListeners(ArrayList<Integer> indexNew, String link){
        Field f = squares.get(indexNew);
        //System.out.println(squares.keySet());
        f.setIC(link);
        squares.put(indexNew, f);
    }
    
    public void addTransfer(ArrayList<Integer> indexNew){
        Field f = squares.get(indexNew);
        MouseListener listener = new DragMouseAdapter(){
           
           @Override 
           public void mousePressed(MouseEvent e) {
              System.out.println("I was clicked!");
           }
        
        };
        
        f.setTransferHandler(new TransferHandler("icon"){
            
        
            /////////enable moving instead of copying
            @Override
            public int getSourceActions(JComponent c) {///to allow move option
                return COPY | MOVE;
            }
            
            
            ///importing new checkers picture
            @Override
            public boolean importData(TransferHandler.TransferSupport info) {
                System.out.println("imp data");
                Field dropped = (Field) info.getComponent();
                val.setDest(dropped.i, dropped.j);
                game.setDest(dropped.i, dropped.j);
                
                return super.importData(info);
            }
                
                
        });
        
        squares.put(indexNew, f);
    }
    /**
     * checking if this is the right field, and updating it with clickable and transferhandler that can handle if the checkers is dropped back
     */
    public void update(ArrayList<Integer> indexNew, String link)
    {
        //squares.put(indexNew, new Field(indexNew, link));
        
        MouseListener listener = new DragMouseAdapter(indexNew, link){
           
           @Override 
           public void mousePressed(MouseEvent e) {
               JComponent c = (JComponent) e.getSource();
               
               TransferHandler handler = c.getTransferHandler();
               System.out.println("Click!");
               
               if(game.canMove(this.pos)){
                   handler.exportAsDrag(c, e, TransferHandler.MOVE);
                }
                
               
           }
        
        };
        
        //String key = String.valueOf(indexNew[0])+","+String.valueOf(indexNew[1]);
        Field f = squares.get(indexNew);
        //System.out.println(squares.keySet());
        f.setIC(link);
        f.addMouseListener(listener);
        f.setTransferHandler(new TransferHandler("icon"){
            
        
            /////////enable moving instead of copying
            @Override
            public int getSourceActions(JComponent c) {///to allow move option
                return COPY | MOVE;
            }
            
            //////delete old label
            @Override
            protected void exportDone(JComponent source, Transferable data, int action) {//to delete the source image
                if (action == MOVE){
                    System.out.println("exp done");
                    val.setSource(((Field) source).i, ((Field) source).j);
                    val.getFeedback();
                    game.setSource(((Field) source).i, ((Field) source).j, ((Field) source).link);//updating the current source
                    //((Field) source).setIC("");///removing item from source
                }
                
            }
            
            
                
                
        });
        
        squares.put(indexNew, f);
        //System.out.println(indexNew[0]+" "+indexNew[1]);
        
    }
}
