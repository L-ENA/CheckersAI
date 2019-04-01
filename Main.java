import java.util.*;
import java.util.Observable;
import java.util.Observer;
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
   
    
    protected int[][] myBoard = new int[8][8];
    
    int iDest,jDest,iSource,jSource;
    String currentLink;
    
    List<Position> freePositions;
    ArrayList<Position> forcedPositions;
    List<PositionsAndScores> successorEvaluations;
    
    
    /**
     * Constructor for objects of class Main
     */
    public Main()
    {
        gui = new Gui(this);
        
        makeInit();//creating internal state
        updateAll();//using this state to update the gui
        //gd.addObserver(this);
        
        
    }
    
    private void play(){
        
        if(!gameOver()){
            System.out.println("new round");
            updateAll();
        }
    }
    
    public List<Position> getAvailableStates() {///looking for free spaces on the board
        freePositions = new ArrayList<>();
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                // is the current position available?
                if (myBoard[i][j] == 0) {
                    freePositions.add(new Position(i, j));
                }
            }
        }
        return freePositions;
    }
    
    
    
    private void updateAll(){//////updating the Gui to display results of the last move and to set the new listeners and constraints
        boolean forced =false;
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                // current board state to gui
                forcedMove(new Position(i,j));//sees if there are forced moves
                if(forcedPositions.size()>0){//updating gui with listeners
                   gui.componentPane.boardPane.update(toList(i,j), OCCUPY.mapStatus(myBoard[i][j]));
                   forced=true;//from now on, only states that lead kicking out an enemy will be updated with the action listeners in gui
                   for(Position pos: forcedPositions){
                       gui.componentPane.boardPane.addTransfer(pos.toAList());//delivers target as array list, because the GUI likes arrayLists more
                   }
                } 
                gui.componentPane.boardPane.updateNoListeners(toList(i,j), OCCUPY.mapStatus(myBoard[i][j]));
            }
        }
        
        gui.componentPane.boardPane.visualise();
        printmyBoard();
    }
    
    private void forcedMove(Position pos){
        boolean beats = false;
        forcedPositions = new ArrayList<Position>();
        if (myBoard[pos.i][pos.j]==1){//it is a normal white stone
            
            if(pos.i>0 && pos.j>0){//looking at front left
                if (myBoard[pos.i-1][pos.j-1]==2||myBoard[pos.i-1][pos.j-1]==3){ //if there is an enemy ahead
                    try{
                    if(myBoard[pos.i-2][pos.j-2]==0){//left back of enemy
                        forcedPositions.add(new Position(pos.i-2,pos.j-2));
                        //recursion for getting all forced moves forcedMove(player, new Position(pos.i-2,pos.j-2));
                    }
                }catch (Exception e){}
                    try{
                    if(myBoard[pos.i-2][pos.j]==0){//right back
                        forcedPositions.add(new Position(pos.i-2,pos.j));
                    }
                }catch(Exception e){}
                }
            }
            
            if(pos.i>0 && pos.j<7) {   
                if (myBoard[pos.i-1][pos.j+1]==2||myBoard[pos.i-1][pos.j+1]==3){ //enemy to front right
                    try{
                    if(myBoard[pos.i-2][pos.j]==0){//left back of enemy
                        forcedPositions.add(new Position(pos.i-2,pos.j));
                    }
                    }catch (Exception e){}
                    try{
                    if(myBoard[pos.i-2][pos.j+2]==0){//right back
                        forcedPositions.add(new Position(pos.i-2,pos.j+2));
                    }
                    }catch (Exception e){}
                }
            }
        }
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
            System.out.println(white[i]);
            myBoard[white[i]][white[i+1]]=1;
        }
        
        int[] black = new int[]{0, 1, 0,3,0,5,0,7,1,0,1,2,1,4,1,6,2,1,2,3,2,5,2,7,4,5};
        //int[] black = new int[]{1,6,0, 1, 0,3,0,5,0,7,1,0,1,2, 1,4};
        for (int i = 0; i<black.length-1; i+=2){
            myBoard[black[i]][black[i+1]]=2;
            
        }
        
        int[] free = new int[]{4,1,4,3,4,7,3,0,3,2,3,4,3,6};
        //int[] free = new int[]{4,1,4,3};
        
        for (int i = 0; i<free.length-1; i+=2){
            myBoard[free[i]][free[i+1]]=0;
        }
        
        
        printmyBoard();
        
        
    }
    
    
    
    private void printmyBoard(){
        for (int[] x : myBoard)
        {
            for (int y : x)
            {
                System.out.print(y + " ");
            }
            System.out.println();
        }
    }
        
    
    
    public boolean canMove(ArrayList<Integer> pos){
        
        ArrayList <int[]> candidates = new ArrayList<int[]>();//to store all possible candidate nodes
        //Node current=state.get(pos);//current node
        
        int i = pos.get(0);
        int j = pos.get(1);
        int type = myBoard[i][j];
        System.out.println("We have a " + type);
        if(type==1){//a white piece was selected
            
            if(i>0 && j>0){
                if (myBoard[i-1][j-1]==0) 
                    candidates.add(new int[]{i-1,j-1});
                }    
            if(i>0 && j<7) {   
                if (myBoard[i-1][j+1]==0) 
                    candidates.add(new int[]{i-1,j+1});
                } 
                
                
                
            if(candidates.size()>0)
                return true;
                else
                return false;
        } else {
            System.out.println("choose a white piece please");
        }
        
        
            
              
        return false;
    }

    private static ArrayList<Integer> toList(int i, int j){///helper to quickly init array list
        ArrayList<Integer> ret = new ArrayList<Integer>();
        ret.add(i);
        ret.add(j);
        return ret;
    }
    
    
    //////problematic
    private void updateState(){
        int newVal=OCCUPY.mapString(currentLink);
        myBoard[iSource][jSource]=0;
        myBoard[iDest][jDest]=newVal;
        
        
        /////////////now the player can make the next move
        play();
        
        
    }
    
    /**
     * setting the source of the last gui event
     */
    public void setSource(int i,int j, String lnk)
    {
        // put your code here
        this.iSource= i;
        this.jSource=j;
        this.currentLink=lnk;
        updateState();
    }
    
    
    
    /**
     * setting the destination info from the last gui event
     */
    public void setDest(int i,int j)
    {
        // put your code here
        this.iDest= i;
        this.jDest=j;
        //System.out.println("Dest" + this.iDest+ this.jDest);
    }
    
    private boolean aiHasWon() {//looks if the player has any pieces left on the board
        // ai is black here
        int countPieces=0;
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                // is the current position available?
                if (myBoard[i][j] == 1 || myBoard[i][j] == 4) {
                    countPieces++;
                }
            }
        }
        if(countPieces>0){
            return false;
        }
        return true;
    }
    private boolean playerHasWon() {//looks if the ai has any pieces left on the board
        // ai is black here
        int countPieces=0;
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                // is the current position available?
                if (myBoard[i][j] == 2 || myBoard[i][j] == 3) {
                    countPieces++;
                }
            }
        }
        if(countPieces>0){
            return false;
        }
        return true;
    }
    
    public boolean gameOver() {
        //Game is over if any player has won, or the board is filled with pieces (a draw)
        return (aiHasWon() ||playerHasWon());
    }
}
