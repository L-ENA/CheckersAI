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
    List<PositionsAndScores> successorEvaluations;
    ArrayList<int[][]> statesAvailable;
    /**
     * Constructor for objects of class Ai
     */
    public Ai(int level)
    {
        // initialise instance variables
        this.level = level;
        
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
            System.out.println("Today is Sunday");
            return randomMove();
          case 3:
            System.out.println("Today is Sunday");
            return randomMove();
          default:
            System.out.println("No level selected");
            return randomMove();
        }
        
    }
    
    private int[][] checkKingConversion(int [][] state){//if the 7th row includes a normal black piece
        
        for(int i = 0;i<state[7].length; i++){
            if(state[7][i]==2){
                state[7][i]=3;
            }
            //System.out.println(i);
        }
        
        return state;
    }
    
    private int[][] randomMove(){
        startEvaluation();
        Random r = new Random();
        //System.out.println("States available: " + statesAvailable.size());
        int index = r.nextInt(statesAvailable.size());
        return statesAvailable.get(index);
    }
    
    private void startEvaluation(){
        deCount = 0;
        seCount = 0;
        pCount = 0;
        successorEvaluations = new ArrayList<>();
        statesAvailable = getAvailableStates();
    }
    
    private ArrayList<int[][]> getAvailableStates(){
        ArrayList<int[][]> freePositions = new ArrayList<>();
        boolean forced =false;
        //System.out.println("trying to grt states... ");
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                // is the current position available in general?
                
                    
                ArrayList<int[][]> candidates = forcedMove(new Position(i,j));
                for(int[][] candidate : candidates){
                    freePositions.add(candidate);
                    
                    }
                
            }
        }
        
        if(freePositions.size()==0){
            for (int i = 0; i < 8; ++i) {
                for (int j = 0; j < 8; ++j) {
                // is the current position available in general?
                
                //System.out.println("looking for nonforced moves");    
                ArrayList<int[][]> candidates = generalMove(new Position(i,j));
                for(int[][] candidate : candidates){
                    freePositions.add(candidate);
                    
                    }
                
            }
        }
        }
        
        return freePositions;
    }
    
    private ArrayList<int[][]> generalMove(Position pos){
        ArrayList<int[][]> generalPositions = new ArrayList<int[][]>();
        if(state[pos.i][pos.j]==2){//a normal black stone
            //System.out.println("testing a black piece");
            if(pos.i<7 && pos.j>0){
                if (state[pos.i+1][pos.j-1]==0){ //move left
                    int[][] stateCopy = cloneState(state);
                    stateCopy[pos.i+1][pos.j-1]=2;//our stone moved there
                    stateCopy[pos.i][pos.j]=0;//the original position is vacated
                    stateCopy = checkKingConversion(stateCopy);//see if this move led to a king
                    generalPositions.add(stateCopy);//add this prospective state
                    }
                }    
            if(pos.i<7 && pos.j<7) {   
                if (state[pos.i+1][pos.j+1]==0){ //move right
                    int[][] stateCopy = cloneState(state);
                    stateCopy[pos.i+1][pos.j+1]=2;//our stone moved there
                    stateCopy[pos.i][pos.j]=0;//the original position is vacated
                    stateCopy = checkKingConversion(stateCopy);//see if this move led to a king
                    generalPositions.add(stateCopy);//add this prospective state
                    }
                } 
        } else if (state[pos.i][pos.j]==3){///////////////////////////////////////////////////////////////it is a King
            int jLeft = pos.j-1; 
            int jRight = pos.j+1;
            for(int i = pos.i-1; i>0;i--){//up diagonal
                if(jLeft>0){
                    int[][] stateCopy = cloneState(state);
                    stateCopy[i][jLeft]=3;//our stone moved there
                    stateCopy[pos.i][pos.j]=0;//the original position is vacated
                    generalPositions.add(stateCopy);//add this prospective state
                    jLeft--;
                }
                if(jRight<7){
                    int[][] stateCopy = cloneState(state);
                    stateCopy[i][jRight]=3;//our stone moved there
                    stateCopy[pos.i][pos.j]=0;//the original position is vacated
                    generalPositions.add(stateCopy);//add this prospective state
                    jRight++;
                }
                
            }
            jLeft = pos.j-1; 
            jRight = pos.j+1;
            for(int i = pos.i+1; i<7;i++){//down diagonal
                if(jLeft>0){
                    int[][] stateCopy = cloneState(state);
                    stateCopy[i][jLeft]=3;//our stone moved there
                    stateCopy[pos.i][pos.j]=0;//the original position is vacated
                    generalPositions.add(stateCopy);//add this prospective state
                    jLeft--;
                }
                if(jRight<7){
                    int[][] stateCopy = cloneState(state);
                    stateCopy[i][jRight]=3;//our stone moved there
                    stateCopy[pos.i][pos.j]=0;//the original position is vacated
                    generalPositions.add(stateCopy);//add this prospective state
                    jRight++;
                }
            }
        }
        return generalPositions;
    }
    
    private ArrayList<int[][]> forcedMove(Position pos){
        ArrayList<int[][]> forcedPositions = new ArrayList<int[][]>();
        if (state[pos.i][pos.j]==2){//it is a normal black stone
            
            if(pos.i<7 && pos.j>0){//looking at front left. smaller than 0 because board ends at 7, so if I am at 7 there is no more piece to go
                if (state[pos.i+1][pos.j-1]==1||state[pos.i+1][pos.j-1]==4){ //if there is an enemy ahead to the left
                    try{
                    if(state[pos.i+2][pos.j-2]==0){//left back of enemy is empty
                        //forcedPositions.add(new Position(pos.i-2,pos.j-2));
                        int[][] stateCopy = cloneState(state);
                        stateCopy[pos.i+2][pos.j-2]=2;//our stone moved there
                        stateCopy[pos.i][pos.j]=0;//the original position is vacated
                        stateCopy[pos.i+1][pos.j-1]=0;//an enemy was eliminated
                        stateCopy = checkKingConversion(stateCopy);//see if this move led to a king
                        forcedPositions.add(stateCopy);//add this prospective state
                        //recursion for getting all forced moves forcedMove(player, new Position(pos.i-2,pos.j-2));
                    }
                }catch (Exception e){}
                }
            }
            
            if(pos.i<7 && pos.j<7) {   
                if (state[pos.i+1][pos.j+1]==1||state[pos.i+1][pos.j+1]==4){ //enemy to front right
                    
                    try{
                    if(state[pos.i+2][pos.j+2]==0){//right back
                        int[][] stateCopy = cloneState(state);
                        stateCopy[pos.i+2][pos.j+2]=2;//our stone moved there
                        stateCopy[pos.i][pos.j]=0;//the original position is vacated
                        stateCopy[pos.i+1][pos.j+1]=0;//an enemy was eliminated
                        stateCopy = checkKingConversion(stateCopy);//see if this move led to a king
                        forcedPositions.add(stateCopy);//add this prospective state
                    }
                    }catch (Exception e){}
                }
            }
        
        
        } else if(state[pos.i][pos.j]==3){///////////////////////////////////if the checker is a King, all diagonals need to be checked
            int jLeft = pos.j -1;
            int jRight = pos.j +1;
            for(int i = pos.i-1; i>0;i--){//up diagonal
                if(jLeft >0){
                    if(state[i][jLeft]==1 || state[i][jLeft]==4){//there is an enemy piece
                        if(state[i-1][jLeft-1]==0){//there is an empty place behind it
                            int[][] stateCopy = cloneState(state);
                            stateCopy[i-1][jLeft-1]=3;//our stone moved there
                            stateCopy[pos.i][pos.j]=0;//the original position is vacated
                            stateCopy[i][jLeft]=0;//an enemy was eliminated
                            forcedPositions.add(stateCopy);//add this prospective state
                        }
                    }
                    jLeft --;
                }
                if(jRight<7){
                    if(state[i][jRight]==1 || state[i][jRight]==4){//there is an enemy piece
                        if(state[i-1][jRight+1]==0){//there is an empty place behind it
                            int[][] stateCopy = cloneState(state);
                            stateCopy[i-1][jRight+1]=3;//our stone moved there
                            stateCopy[pos.i][pos.j]=0;//the original position is vacated
                            stateCopy[i][jRight]=0;//an enemy was eliminated
                            forcedPositions.add(stateCopy);//add this prospective state
                        }
                    }
                    jRight ++;
                }
            }
            
            ///////////////////////down diagonal
            jLeft = pos.j -1;
            jRight = pos.j +1;
            for(int i = pos.i+1; i<7;i++){//down diagonal
                if(jLeft >0){
                    if(state[i][jLeft]==1 || state[i][jLeft]==4){//there is an enemy piece
                        if(state[i+1][jLeft-1]==0){//there is an empty place behind it
                            int[][] stateCopy = cloneState(state);
                            stateCopy[i+1][jLeft-1]=3;//our stone moved there
                            stateCopy[pos.i][pos.j]=0;//the original position is vacated
                            stateCopy[i][jLeft]=0;//an enemy was eliminated
                            //stateCopy = checkKingConversion(stateCopy);//see if this move led to a king
                            forcedPositions.add(stateCopy);//add this prospective state
                        }
                    }
                    jLeft --;
                }
                if(jRight<7){
                    if(state[i][jRight]==1 || state[i][jRight]==4){//there is an enemy piece
                        if(state[i+1][jRight+1]==0){//there is an empty place behind it
                            int[][] stateCopy = cloneState(state);
                            stateCopy[i+1][jRight+1]=3;//our stone moved there
                            stateCopy[pos.i][pos.j]=0;//the original position is vacated
                            stateCopy[i][jRight]=0;//an enemy was eliminated
                            //stateCopy = checkKingConversion(stateCopy);//see if this move led to a king
                            forcedPositions.add(stateCopy);//add this prospective state
                        }
                    }
                    jRight ++;
                }
            }
            
        }
        return forcedPositions;
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
    
}

class PositionsAndScores {
    int score;
    Position pos;
    PositionsAndScores(int score, Position pos) {
        this.score = score;
        this.pos = pos;
    }
}
