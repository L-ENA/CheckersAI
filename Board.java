import java.awt.*;  
import javax.swing.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.*;
import java.util.Set;
import java.awt.event.MouseListener;
import java.awt.datatransfer.Transferable;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

 
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
    int humanMoves;
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
    
    protected void addValidationMap(HashMap<ArrayList<Integer>, ArrayList<Position>> validatingPositions){
        val.updateHashMap(validatingPositions);
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
    
    protected void showOptions(){
        int[] ind = val.getIndex();
        Field f = new Field();
        for (Component c : this.getComponents()){
            //System.out.println(c.toString());

            if (c instanceof Field){
                f = (Field) c;
                
                if(f.i==ind[0] && f.j==ind[1]){
                    f.setIC("empty.png");
                    
                    break;
                }
                    
                    
            }
            
        }
        
        this.setSize(300,300); 
        this.revalidate();
        this.repaint();
        this.setVisible(true);
        
        //f.setIC("");
        
    }
    private void deleteTrails(){
        for (Component c : this.getComponents()){
            //System.out.println(c.toString());

            if (c instanceof Field){
                Field f = (Field) c;
                if(f.link=="feet.png" || f.link=="kick.png")
                    f.setIC("");
            }
            
        }
        
        this.setSize(300,300); 
        this.revalidate();
        this.repaint();
        this.setVisible(true);
    }
    public void updateNoListeners(ArrayList<Integer> indexNew, String link){
        //System.out.println(link+ " no listeners "   +indexNew.get(0) + indexNew.get(1));
        Field f = squares.get(indexNew);
        f = new Field(f.index, f.black);
        //System.out.println(squares.keySet());
        f.setIC(link);
        
        squares.put(indexNew, f);
    }
    
    public void addTransfer(ArrayList<Integer> indexNew){
        Field f = squares.get(indexNew);//get the corresponding field
        
        //System.out.println("addTransfer items to " + indexNew.get(0) + " " + indexNew.get(1));
        f.setTransferHandler(new TransferHandler("icon"){
            
        
            /////////enable moving instead of copying
            
            
            
            ///importing new checkers picture
            @Override
            public boolean importData(TransferHandler.TransferSupport info) {
                //System.out.println("imp data (addTransfer method)");
                Field dropped = (Field) info.getComponent();
                
                game.setDest(dropped.i, dropped.j);
                
                return super.importData(info);
            }
            @Override
            public boolean canImport(TransferHandler.TransferSupport info) {
                //System.out.println("canImport (addTransfer method)");
                //info.setShowDropLocation(true);
                Field dropped = (Field) info.getComponent();
                val.tryDestination(dropped.i, dropped.j);
                //System.out.println("try import "+dropped.i+ " "+ dropped.j);
                if(val.validateDrop())
                    return true;
                else {
                    
                    return false;
                }
                    
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
               //System.out.println("mousePressed (update() )");
               Field f = (Field)e.getSource();
               
               TransferHandler handler = f.getTransferHandler();
               //System.out.println("Click!");
               
               //if(game.canMove(this.pos)){
               handler.exportAsDrag(f, e, TransferHandler.MOVE);
               // }
               
               val.setClicked(f.i, f.j);
               deleteTrails();
               System.out.println("val setclicked finished");
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
                //System.out.println("imp data (update method, getSourceactions)");
                return COPY | MOVE;
                
            }
            
            //////delete old label
            @Override
            protected void exportDone(JComponent source, Transferable data, int action) {//to delete the source image
                System.out.println("exportDone update() method");
                if (action == MOVE){
                    //System.out.println("exp done");
                    if (!val.getDontUpdate()){//if we are allowed to update
                        ///////////////////////////////////////////////checking for eliminated checkers
                        int[] enemyPosition = val.getFeedback();
                        if(enemyPosition != null){
                            game.doEnemyElimination(enemyPosition[0], enemyPosition[1]);
                        }
                        
                        if(val.iDropped!=8){
                            System.out.println("update");
                            
                            
                            game.setSource(((Field) source).i, ((Field) source).j, ((Field) source).link);//updating the current source
                        } else{
                            System.out.println("no update");
                        }
                            
                        val.iDropped=8;//to verift that the value has changed when updating it next
                        
                        
                    }
                    
                }
                
            }
            
            
                
                
        });
        System.out.println("updated " + indexNew.get(0)+" to " + f.link );
        squares.put(indexNew, f);
        visualise();
        //System.out.println(indexNew[0]+" "+indexNew[1]);
        
    }
}
