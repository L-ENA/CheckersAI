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
    protected static ArrayList<Node> initState;
    protected static LinkedHashMap<ArrayList<Integer>, Node> state = new LinkedHashMap<ArrayList<Integer>, Node>();
    protected static int[][] easyState = new int[8][8];
    
    /**
     * Constructor for objects of class Main
     */
    public Main()
    {
        gui = new Gui();
        initState = new ArrayList<Node>();
        makeInit();
        updateAll();
    }
    
    private void updateAll(){
        for (ArrayList<Integer> n: state.keySet()){
            
                
            gui.componentPane.boardPane.update(n, state.get(n).oc.getLink());
                
            
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
            state.put(n.index,n);
        }
        
        easyState();
    }
    
    private static void easyState(){
        Iterator it = state.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            Node n = (Node) pair.getValue();
            int i=n.i;
            int j=n.j;
            easyState[i][j]=n.oc.getStatus();
            //it.remove(); // avoids a ConcurrentModificationException
        }
        
    }    
    
    private static void printEasyState(){
        for (int[] x : easyState)
        {
            for (int y : x)
            {
                System.out.print(y + " ");
            }
            System.out.println();
        }
    }
        
    
    
    public static boolean canMove(ArrayList<Integer> pos){
        ArrayList<Node> candidates = new ArrayList<Node>();//to store all possible candidate nodes
        Node current=state.get(pos);//current node
        int i = pos.get(0);
        int j = pos.get(1);
        
        
        if(current.oc.equals(OCCUPY.WHITE)){
            /////////////////////////////////////ccetting the candidate notes that are possible in a forward move by creating their keys and pulling the nodes
            if(i>0 && j>0){
                if (state.get(toList(i-1, j-1)).oc.equals(OCCUPY.FREE)) 
                    candidates.add(state.get(toList(i-1, j-1)));
                }    
            if(i>0 && j<7)    
                if (state.get(toList(i-1, j+1)).oc.equals(OCCUPY.FREE)) 
                    candidates.add(state.get(toList(i-1, j+1)));
                } 
            
              
        if(candidates.size()>0)
            return true;
        else
            return false;
    }

    private static ArrayList<Integer> toList(int i, int j){///helper to quickly init array list
        ArrayList<Integer> ret = new ArrayList<Integer>();
        ret.add(i);
        ret.add(j);
        return ret;
    }
    
    //////problematic
    public static void updateState(int i, int j, String lastDragged){
        for(OCCUPY o : OCCUPY.values()){
            if(o.getLink().equals(lastDragged)){
                
                System.out.println("update value: "+ o.getStatus());
                System.out.println("Old: " + easyState[i][j]);
                easyState[i][j]=o.getStatus();
                System.out.println("New: " + easyState[i][j]);
                printEasyState();
                break;
            }
                
                
        }
        
        
    }
}
