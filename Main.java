import java.util.*;
/**
 * Write a description of class Main here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Main
{
    // instance variables - replace the example below with your own
    private Gui gui;
    private ArrayList<Node> initState;

    /**
     * Constructor for objects of class Main
     */
    public Main()
    {
        gui = new Gui();
        initState = new ArrayList<Node>();
        makeInit();
        updateBlack();
    }
    
    private void updateBlack(){
        for (Node n: initState){
            if(n.oc.equals(OCCUPY.BLACK)){
                
                gui.componentPane.boardPane.update(n.index, n.oc.getLink());
                
            }
        }
        gui.componentPane.boardPane.visualise();
    }

    /**
     * An example of a method - replace this comment with your own
     *
     * @param  y  a sample parameter for a method
     * @return    the sum of x and y
     */
    private void makeInit()
    {
        int[] white = new int[]{7, 0, 7,2,7,4,7,6,6,1,6,3,6,5,6,7,5,0,5,2,5,4,5,6};
        //int[] white = new int[]{7, 0, 7,2};
        for (int i = 0; i<white.length-1; i+=2){
            initState.add(new Node(white[i],white[i+1], OCCUPY.WHITE));
        }
        
        int[] black = new int[]{0, 1, 0,3,0,5,0,7,1,0,1,2,1,4,1,6,2,1,2,3,2,5,2,7};
        //int[] black = new int[]{1,6,0, 1, 0,3,0,5,0,7,1,0,1,2, 1,4};
        for (int i = 0; i<black.length-1; i+=2){
            initState.add(new Node(black[i],black[i+1], OCCUPY.BLACK));
            
        }
        
        int[] free = new int[]{4,1,4,3,4,5,4,7,3,0,3,2,3,4,3,6};
        //int[] free = new int[]{4,1,4,3};
        
        for (int i = 0; i<free.length-1; i+=2){
            initState.add(new Node(free[i],free[i+1], OCCUPY.FREE));
        }
        
        for (Node n:initState){
            n.print();
        }
              
    }
}
