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
   
    
    protected int[][] myBoard;
    
    int iDest,jDest,iSource,jSource;
    String currentLink;
    
    List<Position> freePositions;
    
    List<PositionsAndScores> successorEvaluations;
    HashMap<ArrayList<Integer>, ArrayList<Position>> validatingPositions;//stores which field corresponds with which potential moves
    private Ai ai;
    boolean forced;///indicates if a forced move requires checking of another move
    public Board guiBoard;
    
    private int nrMoves;
    private int plLoss;
    private int aiLoss;
    private int seCount;
    private int deCount;
    private int pCount;
    private String heuristic;
    private String level;
    private boolean longJump;
    /**
     * Constructor for objects of class Main
     */
    public Main()
    {
        gui = new Gui();
        guiBoard =  new Board(this);
        gui.componentPane.addBoardPane(guiBoard);
        myBoard = new int[][]{
        {0,2,0,2,0,2,0,2},////multicatch
        {2,0,2,0,2,0,2,0},
        {0,2,0,2,0,2,0,2},
        {0,0,0,0,0,0,0,0},
        {0,0,0,0,0,0,0,0},
        {1,0,1,0,1,0,1,0},
        {0,1,0,1,0,1,0,1},
        {1,0,1,0,1,0,1,0},
        
        };//creating internal state
        
        // myBoard = new int[][]{
        // {0,0,0,0,0,0,0,0},////multicatch
        // {0,0,0,0,0,0,0,0},
        // {0,2,0,0,0,0,0,2},
        // {2,0,0,0,1,0,0,0},
        // {0,1,0,1,0,0,0,2},
        // {0,0,0,0,1,0,0,0},
        // {0,1,0,0,0,0,0,0},
        // {0,0,0,0,0,0,3,0},
        
        // };
        
        // myBoard = new int[][]{
        // {0,0,0,2,0,0,0,0},////multicatch
        // {0,0,2,0,0,0,0,0},
        // {0,0,0,0,0,0,0,0},
        // {0,0,0,0,2,0,0,0},
        // {0,0,0,0,0,1,0,0},
        // {0,0,0,0,0,0,0,0},
        // {0,0,0,0,0,0,0,0},
        // {0,0,0,0,0,0,0,0},
        
        // };
        
        
        //gd.addObserver(this);
        
        //////////////variables for settings and stats
        nrMoves=1;
        plLoss=0;
        aiLoss=0;
        seCount=0;
        deCount=0;
        pCount=0;
        heuristic=gui.componentPane.heur;
        level= gui.componentPane.selectedLevel;
        longJump=gui.componentPane.longJumps;
        forced =false;
        visualiseState();
        updateAll();//using this state to update the gui
        
        
    }
    private void updateAI(){
        heuristic=gui.componentPane.heur;
        level= gui.componentPane.selectedLevel;
        longJump=gui.componentPane.longJumps;
        System.out.println();
        System.out.println(heuristic);
        System.out.println(level);
        System.out.println();
        ai=new Ai(level, heuristic, longJump);
    }
    private void newPly() {
        
        if(!gameOver()){
            gui.componentPane.boardPane.visualise();
            
            forced=false;//reset forced move status
            deleteTrails();
            
            System.out.println("new round");
            ///////////////AI moves
            updateAI();
            
            myBoard = ai.getMove(myBoard);
            lostPiece();
            gui.componentPane.updateSidebar(nrMoves, plLoss, aiLoss, ai.seCount, ai.deCount, ai.pCount);
            
            visualiseState();
            
            gui.componentPane.boardPane.visualise();
            
            
            nrMoves++;
            
        } else {
            System.out.println("Game over, player won");
        }
        if(!gameOver()){
            
            updateAll();
            System.out.println("updated all fields");
        }else {
            System.out.println("Game over, AI won");
        }
    }
    private void lostPiece(){
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) {
               if(myBoard[i][j] == 6){
                    
                    plLoss++;
                }
            }
        }
    }
    private void deleteTrails(){
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                // is the current position available?
                if (myBoard[i][j] == 5) {
                    myBoard[i][j] = 0;
                    
                } else if(myBoard[i][j] == 6){
                    
                    myBoard[i][j] = 0;
                }
            }
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
    

    private void visualiseState(){//visualising the pieces on the board, withour listeners etc.
        for (int i = 0; i < 8; ++i) {
                for (int j = 0; j < 8; ++j) {
                    gui.componentPane.boardPane.updateNoListeners(toList(i,j), OCCUPY.mapStatus(myBoard[i][j]));
                }
            }
        gui.componentPane.boardPane.visualise();    
    }
    
    private void updateAll(){//////updating the Gui to display results of the last move and to set the new listeners and constraints
        //System.out.println("updating all");
        
        validatingPositions =new HashMap<ArrayList<Integer>, ArrayList<Position>>(); 
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                // current board state to gui
                
                ArrayList<Position> forcedPositions= forcedMove(new Position(i,j));//sees if there are forced moves
        
                
                if(forcedPositions.size()>0){//updating gui with listeners
                   gui.componentPane.boardPane.update(toList(i,j), OCCUPY.mapStatus(myBoard[i][j]));
                   aiLoss++;
                   forced=true;//from now on, only states that lead to kicking out an enemy will be updated with the action listeners in gui
                   for(Position pos: forcedPositions){
                       gui.componentPane.boardPane.addTransfer(pos.toAList());//delivers target as array list, because the GUI likes arrayLists more
                       //System.out.println("FPOS: can drop to " + pos.i + " " + pos.j);
                    }
                    //adding the forced positions and the candidate fields to the hashmap, this is used by the validator in the GUI to terminate illegal moves
                   
                   validatingPositions.put(toList(i,j), copyPositions(forcedPositions));
                } 
            }
        }
        //System.out.println(forced);
        
        if(forced == false){
           
           for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                // current board state to gui
                ArrayList<Position> candidates = canMove(i,j);
                if(candidates.size()>0){//updating gui with listeners
                   gui.componentPane.boardPane.update(toList(i,j), OCCUPY.mapStatus(myBoard[i][j]));
                   for(Position pos: candidates){
                       gui.componentPane.boardPane.addTransfer(pos.toAList());//delivers target as array list, because the GUI likes arrayLists more
                       //System.out.println("Can go to " + pos.i +" " + pos.j);
                    }
                   //adding the forced positions and the candidate fields to the hashmap
                   validatingPositions.put(toList(i,j), copyPositions(candidates));
                } 
                ///gui.componentPane.boardPane.updateNoListeners(toList(i,j), OCCUPY.mapStatus(myBoard[i][j]));
            }
        } 
        }
        gui.componentPane.boardPane.addValidationMap(validatingPositions);
        //System.out.println("val size: " + validatingPositions.size());
        gui.componentPane.boardPane.visualise();
        //printmyBoard();
    }
    
    private boolean isFree(int i,int j){
        if (myBoard[i][j]==0||myBoard[i][j]==5||myBoard[i][j]==6)
            return true;
        else
            return false;
    }
    private ArrayList<Position> forcedMove(Position pos){
        boolean beats = false;
        ArrayList<Position> forcedPositions = new ArrayList<Position>();
        
              //gui.componentPane.boardPane.updateNoListeners(toList(i,j), OCCUPY.mapStatus(myBoard[i][j]));
                if (myBoard[pos.i][pos.j]==1){//it is a normal white stone
                
                    if(pos.i>0 && pos.j>0){//looking at front left
                        if (myBoard[pos.i-1][pos.j-1]==2||myBoard[pos.i-1][pos.j-1]==3){ //if there is an enemy ahead
                            try{
                            if(isFree(pos.i-2,pos.j-2)){//left back of enemy
                                forcedPositions.add(new Position(pos.i-2,pos.j-2));
                                //recursion for getting all forced moves forcedMove(player, new Position(pos.i-2,pos.j-2));
                            }
                        }catch (Exception e){}
                        }
                    }
                    
                    if(pos.i>0 && pos.j<7) {   
                        if (myBoard[pos.i-1][pos.j+1]==2||myBoard[pos.i-1][pos.j+1]==3){ //enemy to front right
                            
                            try{
                            if(isFree(pos.i-2,pos.j+2)){//right back
                                forcedPositions.add(new Position(pos.i-2,pos.j+2));
                            }
                            }catch (Exception e){}
                        }
                }
            } 
            
            if (myBoard[pos.i][pos.j]==4){/////a white king
                int jLeft=pos.j-1;
                int jRight=pos.j+1;
                for(int down = pos.i+1; down<7; down++){
                    if(jLeft>=1){
                    if(myBoard[down][jLeft]==1||myBoard[down][jLeft]==4){//there an own piece
                        jLeft=-1;
                    
                    }else if(myBoard[down][jLeft]==2||myBoard[down][jLeft]==3){//there is an enemy piece
                            if(isFree(down + 1,pos.j-1)){//if place behind is free
                                forcedPositions.add(new Position(down+1,jLeft-1));
                                
                                jLeft=-1;
                            } else {//there are 2 pieces behind each other
                                jLeft=-1;
                            }
                    }
                    jLeft--;
                    }
                    if(jRight<=6){
                    if(myBoard[down][jRight]==1||myBoard[down][jRight]==4){//there an own piece
                        jRight=7;
                    
                    }else if(myBoard[down][jRight]==2||myBoard[down][jRight]==3){
                            if(isFree(down + 1,jRight+1)){
                                forcedPositions.add(new Position(down+1,jRight+1));
                                
                                jRight=7;
                            }else {//there are 2 pieces behind each other
                                jRight=7;
                            }
                        
                    }
                    jRight++;
                    }
                    
                    if(!longJump)//not allowing more than 1 jump
                        break;
                    }
                jLeft=pos.j-1;
                jRight=pos.j+1;
                for(int up = pos.i-1; up>0; up--){
                    if(jLeft>=1){
                    if(myBoard[up][jLeft]==1||myBoard[up][jLeft]==4){//there an own piece
                        jLeft=-1;
                    
                    }else if(myBoard[up][jLeft]==2||myBoard[up][jLeft]==3){
                            if(isFree(up -1,jLeft-1)){
                                forcedPositions.add(new Position(up-1,jLeft-1));
                                
                                jLeft=-1;
                            }else {//there are 2 pieces behind each other
                                jLeft=-1;
                            }
                    }
                    jLeft--;
                    }
                    if(jRight<=6){
                    if(myBoard[up][jRight]==1||myBoard[up][jRight]==4){//there an own piece
                        jRight=7;
                    
                    }else if(myBoard[up][jRight]==2||myBoard[up][jRight]==3){
                            if(isFree(up-1,jRight+1)){
                                forcedPositions.add(new Position(up-1,jRight+1));
                                
                                jRight=7;
                            }else {//there are 2 pieces behind each other
                                jRight=7;
                            }
                        
                    }
                    jRight++;
                    }
                    
                    if(!longJump)//not allowing more than 1 jump
                        break;
                }
            }
                
        return forcedPositions;   
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
        
    
    
    private ArrayList <Position> canMove(int i, int j){
        
        ArrayList <Position> candidates = new ArrayList<Position>();//to store all possible candidate nodes
        //Node current=state.get(pos);//current node
        
        
        int type = myBoard[i][j];
        
        if(type==1){//a white piece was selected
            
            if(i>0 && j>0){
                if (isFree(i-1,j-1)) 
                    candidates.add(new Position(i-1,j-1));
                }    
            if(i>0 && j<7) {   
                if (isFree(i-1,j+1)) 
                    candidates.add(new Position(i-1,j+1));
                } 
        } else if (type==4){///a white king
            int jLeft=j-1;
            int jRight=j+1;
            for(int down = i+1; down<=7; down++){
                if(jLeft>=0){
                    if(isFree(down,jLeft)){
                    candidates.add(new Position(down,jLeft));
                }
                jLeft--;
                }
                if(jRight<=7){
                    if(isFree(down,jRight)){
                    candidates.add(new Position(down,jRight));
                }
                jRight++;
                }
                
                if(!longJump)//not allowing more than 1 jump
                        break;
            }
            jLeft=j-1;
            jRight=j+1;
            for(int up = i-1; up>=0; up--){
                if(jLeft>=0){
                    if(isFree(up,jLeft)){
                    candidates.add(new Position(up,jLeft));
                }
                jLeft--;
                }
                if(jRight<=7){
                    if(isFree(up,jRight)){
                    candidates.add(new Position(up,jRight));
                }
                jRight++;
                }
                
                if(!longJump)//not allowing more than 1 jump
                        break;
            }
        }
        
        
            
              
        return candidates;
    }

    private static ArrayList<Integer> toList(int i, int j){///helper to quickly init array list
        ArrayList<Integer> ret = new ArrayList<Integer>();
        ret.add(i);
        ret.add(j);
        return ret;
    }
    
    
    //////problematic
    private void updateState(){
        boolean kingConversion = false;
        int newVal=OCCUPY.mapString(currentLink);/////change the value of the piece that was moved
        myBoard[iSource][jSource]=0;
        
        if(iDest==0 && myBoard[iDest][jDest]!=4){///player king conversion, therefore, no extra moves possible
            myBoard[iDest][jDest]=4;
            
            kingConversion=true;
        } else{//piece stays the same
            myBoard[iDest][jDest]=newVal;
            
        }
        
        
        visualiseState();
        //printmyBoard();
            
        gui.componentPane.boardPane.visualise();
        
       ///////////////////////////check if player  can make more moves
       
       boolean moreMoves= additionalMoves();
           /////////////now the ai can make the next move
       if(!moreMoves||kingConversion) 
           newPly();
        
       
        
        
    }
    private boolean additionalMoves(){
        /////////////////////////check if previous move was capturing!!!!!!!!!!!!!
        //System.out.println("Searching for new moves"); 
        if(!forced){
            System.out.println("Forced is false in additionalmove");
            return false;
        }  
        boolean goAgain = false;//indicates another move    
        validatingPositions =new HashMap<ArrayList<Integer>, ArrayList<Position>>(); 
        
                // current board state to gui
                
        ArrayList<Position> forcedPositions= forcedMove(new Position(iDest,jDest));//sees if there are forced moves


        if(forcedPositions.size()>0){//updating gui with listeners
           aiLoss++;
           gui.componentPane.boardPane.update(toList(iDest,jDest), OCCUPY.mapStatus(myBoard[iDest][jDest]));
           //from now on, only states that lead to kicking out an enemy will be updated with the action listeners in gui
           for(Position pos: forcedPositions){
               gui.componentPane.boardPane.addTransfer(pos.toAList());//delivers target as array list, because the GUI likes arrayLists more
               //System.out.println("FPOS: can drop to " + pos.i + " " + pos.j);
            }
            //adding the forced positions and the candidate fields to the hashmap, this is used by the validator in the GUI to terminate illegal moves
           
           validatingPositions.put(toList(iDest,jDest), copyPositions(forcedPositions));
           goAgain=true;
        } 
            
        if(goAgain){
            gui.componentPane.boardPane.addValidationMap(validatingPositions);
            //System.out.println("val size: " + validatingPositions.size());
            gui.componentPane.boardPane.visualise();
            return true;
        }
        return false;
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
     * setting the source of the last gui event
     */
    public void doEnemyElimination(int i,int j)
    {
        // deleting the enemy from state
        myBoard[i][j]=0;
        
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
    
        private ArrayList<Position> copyPositions(ArrayList<Position> source){
        ArrayList<Position> copy = new ArrayList<Position>();
        Iterator<Position> iterator = source.iterator();//to traverse the source list
        while(iterator.hasNext()){
            copy.add(iterator.next().clone());//creating a clone so that changes on the clone don't affect the original List
        }
        return copy;
    }
}
