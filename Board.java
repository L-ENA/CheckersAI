import java.awt.*;  
import javax.swing.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.*;
import java.awt.event.MouseListener;
import java.awt.datatransfer.Transferable;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
/**
 * Board is the jpanel that displays the checkers board
 */
public class Board extends JPanel
{
    int length = 8;//dimensions of the board
    int width = 8;
    Validator val = new Validator();//checks if drag and drops are valid moves
    LinkedHashMap<ArrayList<Integer>, Field> squares = new LinkedHashMap<ArrayList<Integer>, Field>();
    Main game;//link back to the main class for updating it
    int humanMoves;
    /**
     * Constructor for objects of class Board
     */
    public Board(Main game)
    {
        boolean black = false;////create the fields for the board
        for (int i=0; i<8;i++){
            for (int j=0; j<8;j++){
                Field gameField = new Field(new int[] {i,j}, black);
                ArrayList<Integer> l = new ArrayList<Integer>();
                l.add(i);
                l.add(j);
                squares.put(l, gameField);
                black= !black;//flipping colour variable
            }
            black= !black;//flipping colour variable
        }
        visualise();
        this.game=game;
    }
    protected void addValidationMap(HashMap<ArrayList<Integer>, ArrayList<Position>> validatingPositions){
        val.updateHashMap(validatingPositions);//add the map that allows val to validate moves
    }
    protected void visualise(){////visualising board according to the current gui state
        this.removeAll();
        this.setLayout(new GridLayout(8,8));  
        Set<ArrayList<Integer>> keys = squares.keySet();
        for(ArrayList<Integer> index :keys){
            this.add(squares.get(index));
        }
        this.setSize(300,300); 
        this.revalidate();
        this.repaint();
        this.setVisible(true);
    }
    protected void showOptions(){//highlight one possible destination
        int[] ind = val.getIndex();//get destination from the validator
        Field f = new Field();
        for (Component c : this.getComponents()){
           if (c instanceof Field){
                f = (Field) c;
                if(f.i==ind[0] && f.j==ind[1]){//if we reached the desired field
                    f.setIC("empty.png");//set the spiral icon and stop searching
                    break;
                }
            }
        }
        this.setSize(300,300); 
        this.revalidate();
        this.repaint();
        this.setVisible(true);
    }
    private void deleteTrails(){//gets rid of the crossed out stones and feet icons
        for (Component c : this.getComponents()){
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
    public void updateNoListeners(ArrayList<Integer> indexNew, String link){//updates a single field
        Field f = squares.get(indexNew);
        f = new Field(f.index, f.black);
        f.setIC(link);
        squares.put(indexNew, f);
    }
    public void addTransfer(ArrayList<Integer> indexNew, boolean deleteTrails){//update a single field with transfer handlers etc
        if(deleteTrails){
            deleteTrails();
        }
        Field f = squares.get(indexNew);//get the corresponding field
        f.setTransferHandler(new TransferHandler("icon"){
          /////////enable moving instead of copying
            @Override
            public boolean importData(TransferHandler.TransferSupport info) {
                Field dropped = (Field) info.getComponent();
                game.setDest(dropped.i, dropped.j);
                return super.importData(info);
            }
            @Override
            public boolean canImport(TransferHandler.TransferSupport info) {//check if drop location is a valid move
                Field dropped = (Field) info.getComponent();
                val.tryDestination(dropped.i, dropped.j);
                if(val.validateDrop())
                    return true;
                else {
                    return false;
                }
            }
        });
        squares.put(indexNew, f);//put field back into squares
    }
    /**
     * checking if this is the right field, and updating it with mouse listeners to make it clickable
     */
    public void update(ArrayList<Integer> indexNew, String link)
    {
        MouseListener listener = new DragMouseAdapter(indexNew, link){
            @Override 
            public void mousePressed(MouseEvent e) {
               Field f = (Field)e.getSource();
               TransferHandler handler = f.getTransferHandler();
               handler.exportAsDrag(f, e, TransferHandler.MOVE);
               val.setClicked(f.i, f.j);
               deleteTrails();
            }
        };
        Field f = squares.get(indexNew);
        f.setIC(link);
        f.addMouseListener(listener);
        f.setTransferHandler(new TransferHandler("icon"){
                @Override/////////enable moving instead of copying
                public int getSourceActions(JComponent c) {///to allow move option
                    return COPY | MOVE;
                }
                @Override//////delete old label
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
                                Field f= (Field) source;
                                val.iDropped=8;//to verift that the value has changed when updating it next
                                System.out.println("UPDATED CURRENT MOVE");
                                game.setSource(f.i, f.j, f.link);//updating the current source
                            } else{
                                System.out.println("no update");
                                val.iDropped=8;//to verift that the value has changed when updating it next
                                System.out.println("UPDATED CURRENT MOVE");
                            }
                        }
                    }
                }
         });
         squares.put(indexNew, f);
         visualise();
    }
    protected void triggerRestart(){//signal to restart
        game.setUp(true);
    }
}
