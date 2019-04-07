import java.util.*;
/**
 * Write a description of class Ai here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Ai
{
    // instance variables - replace the example below with your own
    private int level;
    protected int[][] state;
    int deCount, seCount, pCount; // cost for dynamic & static evaluation; number of pruning operations
    List<Position> availablePositions;
    List<StateAndScores> successorEvaluations;
    ArrayList<int[][]> statesAvailable;
    boolean forced = false;
    ArrayList<int[][]> candidatesG;//all candidates for a simple diagonal move
    int heuristic;
    Random r;
    
    /**
     * Constructor for objects of class Ai
     */
    public Ai(int level, int heuristic)
    {
        // initialise instance variables
        this.level = level;
        this.heuristic = heuristic;
        this.r = new Random();
    }

    /**
     * An example of a method - replace this comment with your own
     *
     * @param  y  a sample parameter for a method
     * @return    the sum of x and y
     */
    public int[][] getMove(int[][] myBoard)
    {
        // put your code here
        this.state = myBoard;
        int[][]returnState;
        switch (level) {
          case 1:
            return randomMove();
            
          case 2:
            
            return bestMove();
          case 3:
            System.out.println("Today is Sunday");
            return randomMove();
          default:
            System.out.println("No level selected");
            return randomMove();
        }
        
    }
    
    private int[][] checkKingConversion(int [][] st){//if the 7th row includes a normal black piece
        
        for(int i = 0;i<st[7].length; i++){
            if(st[7][i]==2){
                st[7][i]=3;
                System.out.println("Converted to king");
            }
            //System.out.println(i);
        }
        
        return st;
    }
    
    private int[][] randomMove(){
        statesAvailable = getAvailableStates(state);
        
        int index = r.nextInt(statesAvailable.size());
        return statesAvailable.get(index);
    }
    
    private int[][] bestMove(){/////returns the best possible state, or, a random selection of equally good best states if there is more than 1
        startEvaluation();//determine successors
        int max = Integer.MIN_VALUE;
        int [][] ret = state;//get best successor
        ArrayList<int[][]> bestStates = new ArrayList<int[][]>();
        for(int i=0; i<successorEvaluations.size(); i++){//determine best score
            if(successorEvaluations.get(i).score > max){
                max= successorEvaluations.get(i).score;
            }
        }
        
        for(StateAndScores candidate : successorEvaluations){//filter out the best states
            if(candidate.score==max){
                bestStates.add(candidate.state);
            }
        }
        int index = r.nextInt(bestStates.size());//choose best state if there is more than 1
        return bestStates.get(index);
    }
    
    private void startEvaluation(){
        deCount = 0;
        seCount = 0;
        pCount = 0;
        successorEvaluations = new ArrayList<>();
        statesAvailable = getAvailableStates(state);
        scoreForState();
        
    }
    
    private void scoreForState(){
        for(int[][] state : statesAvailable){
            int score = Evaluator.evaluate(state, heuristic);
            successorEvaluations.add(new StateAndScores(score, state));
        }
    }
    
    private ArrayList<int[][]> getAvailableStates(int[][] someState){
        this.forced=false;
        ArrayList<int[][]> freePositions = new ArrayList<>();
        //int[][] pastPosition = new int[8][8];
        //System.out.println("trying to grt states... ");
        ArrayList<int[][]> candidates = forcedMove(someState);
                
        for(int i = 0; i<candidates.size(); i++){
            this.forced= true;
            freePositions.add(candidates.get(i));
            ArrayList<int[][]> secondary = forcedMove(candidates.get(i));
            if(secondary.size() > 0){
                freePositions = new ArrayList<>();
                for(int[][] candidate : secondary){
                    freePositions.add(candidate);
                    candidates.add(candidate);
                    }
                }
            
            }
        
        if(!this.forced){///there is no forced position
            for (int i = 0; i < 8; ++i) {
                for (int j = 0; j < 8; ++j) {
                 //is the current position available in general?
                generalMove(new Position(i,j),someState);
                for(int[][] candidate : candidatesG){
                    freePositions.add(candidate);
                    
                    }
                
                }
            }
        
        }
            
        
        
        //if(recursionDepth)
        //System.out.println("has free pos: "+freePositions.size());
        return freePositions;
    }
    
    private boolean arrCompare(int[][] arr1, int[][] arr2){
        for (int i = 0; i < arr1.length; i++) {
            if (!Arrays.equals(arr1[i], arr2[i])) {
                return false;
            }       
     
      }
 
      return true;
    }
    
    
    private boolean isFree(int test){
        if(test==0||test==5)
            return true;
        return false;    
    }
    private boolean isEnemy(int test){
        if(test==1||test==4)
            return true;
        return false;    
    }
    
    
    
    
    public static int[][] cloneState(int[][] source) {
        int length = source.length;//nr of dimensions
        int[][] ret = new int[length][source[0].length];
        for (int i = 0; i < length; i++) {
            System.arraycopy(source[i], 0, ret[i], 0, source[i].length);//copy each dimension
        }
        return ret;
    }
    // Recursive DFS
    public  void dfs(Node node)
    {
        System.out.print(node.data + " ");
        List<Node> neighbours=node.getNeighbours();
        node.visited=true;
        for (int i = 0; i < neighbours.size(); i++) {
            Node n=neighbours.get(i);
            if(n!=null && !n.visited)
            {
                dfs(n);
            }
        }
    } 
    
    private void simpleMove(int iNew,int jNew,Position pos, int[][] someState){//moving and updating the candidate state
        int[][] stateCopy = cloneState(someState);
        stateCopy[iNew][jNew]=stateCopy[pos.i][pos.j];//our stone moved there
        stateCopy[pos.i][pos.j]=5;//the original position is vacated
        stateCopy = checkKingConversion(stateCopy);//see if this move led to a king
        candidatesG.add(stateCopy);//add this prospective state
    }
    
    private int[][] captureMove(int iNew,int jNew,Position pos, int[][] someState){//moving and updating the candidate state
        int[][] stateCopy = cloneState(someState);
        stateCopy[pos.i+iNew][pos.j+jNew]=stateCopy[pos.i][pos.j];//our stone moved there
        stateCopy[pos.i][pos.j]=5;//the original position is vacated
        stateCopy[pos.i+(iNew/2)][pos.j+(jNew/2)]=0;//an enemy was eliminated in the middle
        stateCopy = checkKingConversion(stateCopy);//see if this move led to a king
        return stateCopy;
    }
    
    private int[][] captureDiagonal(int i, int iAdded, int j, int jAdded,Position pos, int[][] someState){
        int[][] stateCopy = cloneState(someState);
        stateCopy[i+ iAdded][j+ jAdded]=3;//our stone moved there
        stateCopy[pos.i][pos.j]=5;//the original position is vacated
        stateCopy[i][j]=0;//an enemy was eliminated
        return stateCopy;//add this prospective state
    }
    
    private void generalMove(Position pos, int[][] someState){
        candidatesG = new ArrayList<int[][]>();
        if(someState[pos.i][pos.j]==2){//a normal black stone
            //System.out.println("testing a black piece");
            if(pos.i<7 && pos.j>0){
                if (isFree(someState[pos.i+1][pos.j-1])){ //move left
                    simpleMove(pos.i+1,pos.j-1,pos, someState);
                    }
                }    
            if(pos.i<7 && pos.j<7) {   
                if (isFree(someState[pos.i+1][pos.j+1])){ //move right
                    simpleMove(pos.i+1,pos.j+1,pos, someState);
                    }
                } 
         } else if (someState[pos.i][pos.j]==3){///////////////////////////////////////////////////////////////it is a King
            int jLeft = pos.j-1; 
            int jRight = pos.j+1;
            for(int i = pos.i-1; i>0;i--){//up diagonal
                if(jLeft>0){
                    if(isFree(someState[i][jLeft])){
                        simpleMove(i, jLeft, pos, someState);
                        jLeft--;
                    } else {
                        jLeft=-1;
                    }
                }
                if(jRight<7){
                    if(isFree(someState[i][jRight])){    
                        simpleMove(i, jRight, pos, someState);
                        jRight++;
                    }else {
                        jRight=8;
                    }
                 }    
            }
            jLeft = pos.j-1; 
            jRight = pos.j+1;
            for(int i = pos.i+1; i<7;i++){//down diagonal
                if(jLeft>0){
                    if(isFree(someState[i][jLeft])){
                        simpleMove(i, jLeft, pos, someState);
                        jLeft--;
                    }else {
                        jLeft=-1;
                    }
                }    
                if(jRight<7){    
                    if(isFree(someState[i][jRight])){    
                        simpleMove(i, jRight, pos, someState);
                        jRight++;
                    }else {
                        jRight=8;
                    }
                 }    
            }
        }
        
    }
    
    private ArrayList<int[][]> forcedMove(int[][] someState){
        
        ArrayList<int[][]> forcedPositions = new ArrayList<int[][]>();
        
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                //looking for catching moves for the following position
                Position pos = new Position(i,j);
                
                if (someState[pos.i][pos.j]==2){//it is a normal black stone
                    
                    if(pos.i<7 && pos.j>0){//looking at front left. smaller than 0 because board ends at 7, so if I am at 7 there is no more piece to go
                        if (isEnemy(someState[pos.i+1][pos.j-1])){ //if there is an enemy ahead to the left
                            try{
                            if(isFree(someState[pos.i+2][pos.j-2])){//left back of enemy is empty
                                forcedPositions.add(captureMove(2,-2,pos, someState));
                            }
                        }catch (Exception e){}
                        }
                    }
                    
                    if(pos.i<7 && pos.j<7) {   
                        if (isEnemy(someState[pos.i+1][pos.j+1])){ //enemy to front right
                            
                            try{
                            if(isFree(someState[pos.i+2][pos.j+2])){//right back
                                forcedPositions.add(captureMove(2,2,pos, someState));//add this prospective state
                            }
                            }catch (Exception e){}
                        }
                    }
            
            
                } else if(someState[pos.i][pos.j]==3){///////////////////////////////////if the checker is a King, all diagonals need to be checked
                    int jLeft = pos.j -1;
                    int jRight = pos.j +1;
                
                    for(int i1 = pos.i-1; i1>0;i1--){//up diagonal
                        if(jLeft >0 ){
                            if (someState[i1][jLeft]==2 || someState[i1][jLeft]==3){//an own piece is in the way
                                jLeft = -1;
                            } 
                            else if(isEnemy(someState[i1][jLeft])){//there is an enemy piece
                                if(isFree(someState[i1-1][jLeft-1])){//there is an empty place behind it
                                    captureDiagonal(i1, -1, jLeft, -1,pos, someState);
                                    jLeft = -1;//the diagonal move ends here
                                } else{
                                    jLeft = -1;//cant jump 2 at once in any move, so this diagonal will not hold more moves
                                }
                            }
                            jLeft --;
                        }
                        if(jRight<7){
                            if(someState[i1][jRight]==2 || someState[i1][jRight]==3){
                                jRight = 8;
                            } 
                            else if(isEnemy(someState[i1][jRight])){//there is an enemy piece
                                if(isFree(someState[i1-1][jRight+1])){//there is an empty place behind it
                                    captureDiagonal(i1, -1, jRight, 1,pos, someState);
                                    jRight = 8;
                                } else{
                                    jRight = 8;//cant jump 2 at once in any move, so this diagonal will not hold more moves
                                }
                            }
                            jRight ++;
                        }
                    }
            
                    ///////////////////////down diagonal
                    jLeft = pos.j -1;
                    jRight = pos.j +1;
                    for(int i2 = pos.i+1; i2<7;i2++){//down diagonal
                        if(jLeft >0){
                            if (someState[i2][jLeft]==2 || someState[i2][jLeft]==3){//an own piece is in the way
                                jLeft = -1;
                            } 
                            else if(isEnemy(someState[i2][jLeft])){//there is an enemy piece
                                if(isFree(someState[i2+1][jLeft-1])){//there is an empty place behind it
                                    captureDiagonal(i2, 1, jLeft, -1,pos, someState);
                                    jLeft = -1;
                                } else{
                                    jLeft = -1;//cant jump 2 at once in any move, so this diagonal will not hold more moves
                                }
                            }
                            jLeft --;
                        }
                        if(jRight<7){
                            if(someState[i2][jRight]==2 || someState[i2][jRight]==3){
                                jRight = 8;
                            } 
                            else if(isEnemy(someState[i2][jRight])){//there is an enemy piece
                                if(isFree(someState[i2+1][jRight+1])){//there is an empty place behind it
                                    captureDiagonal(i2, 1, jRight, 1,pos, someState);
                                    jRight = 8;
                                } else{
                                    jRight = 8;//cant jump 2 at once in any move, so this diagonal will not hold more moves
                                }
                               }
                             jRight ++;
                            }
                    }
            
                }
                
            }
        }
        
        //System.out.println("Possible capturing moves for this state: " + forcedPositions.size());
        return forcedPositions;
    }
}


