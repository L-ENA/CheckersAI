import java.util.*;
/**
 * AI is the opponent and it returns a new game state
 */
public class Ai
{
    protected int[][] state;
    List<Position> availablePositions;
    List<StateAndScores> successorEvaluations;
    ArrayList<int[][]> statesAvailable;
    boolean forced = false;
    ArrayList<int[][]> candidatesG;//all candidates for a simple diagonal move
    private String heuristic;
    private boolean kingConversion;
    Random r;
    int[][] lastState;
    
    private boolean longJump;
    private int maxDepth, nrMoves;
    private int player;
    private String level;
    protected int deCount, seCount, pCount; // cost for dynamic & static evaluation; number of pruning operations
    /**
     * Constructor for objects of class Ai
     */
    public Ai(String level, String heuristic, boolean longJump, int nrMoves)
    {
        // initialise instance variables
        this.level = level;
        this.heuristic = heuristic;
        this.longJump=longJump;
        this.nrMoves=nrMoves;
        this.r = new Random();
        this.deCount=0;
        this.seCount=0;
        this.pCount=0;
    }

    /**
     * Returns a successor state to main. Chooses between different options to obtain the successor.
     */
    public int[][] getMove(int[][] myBoard)
    {
        this.state = myBoard;
        int[][]returnState;
        lastState = cloneState(myBoard);
        switch (level) {
          case "Kindergarden"://just a random move
            return randomMove();
          case "Novice"://the best move of all available successors
            return bestMove();
          case "Intermediate"://gets best minimax move with respect to maximal depth
            if(nrMoves<2)//first 2 moves don't matter, can be just the best immediate moves
              return bestMove();
            this.maxDepth = 2;
            return mmEvaluation();
          case "Professional"://gets best minimax move with respect to maximal depth
            if(nrMoves<2)//first 2 moves don't matter, can be just the best immediate moves
              return bestMove();
            this.maxDepth = 6;
            return mmEvaluation();
          case "Ultimate Genius"://gets best minimax move with respect to maximal depth
            if(nrMoves<2)//first 2 moves don't matter, can be just the best immediate moves
              return bestMove();
            this.maxDepth = 8;
            return mmEvaluation();  
          default:
            return randomMove();
        }
    }
    
    private int[][] randomMove(){//use successor function to get successors, and choose a random move
        player=1;
        statesAvailable = getAvailableStates(state);
        int index = r.nextInt(statesAvailable.size());
        return statesAvailable.get(index);
    }
    
    private int[][] bestMove(){/////returns the best possible state, or, a random selection of equally good best states if there is more than 1
        player=1;
        startEvaluation();//determine successors
        return getBestSuccessor();
    }
    
    private int[][] mmEvaluation(){
        player=1;//setting ai as starting player
        System.out.println("Calculating");
        /////////check if there is more than 1 move to start with. No minimax necessary if there is only 1 move
        List<int[][]> positionsAvailable = getAvailableStates(this.state);
        if(positionsAvailable.size() == 1){
            System.out.println("there was only 1 option");
            return positionsAvailable.get(0);
        }
        minimaxEvaluation();//determine successors
        int max = -10000;
        int best = -1;
        System.out.println(successorEvaluations.size() + " options");
        // iterate over successors and return the highest result
        ArrayList<StateAndScores> bestSuccessors = new ArrayList();
        for (int i = 0; i < successorEvaluations.size(); ++i) { 
            if (max < successorEvaluations.get(i).score) {
                max = successorEvaluations.get(i).score;
                
            }
            System.out.println("Option "+ i + " results in "+ successorEvaluations.get(i).score);
            
        }
        //System.out.println("best score is "+ successorEvaluations.get(best).score);
        //printState(successorEvaluations.get(best).state);
        for(StateAndScores bestScore:successorEvaluations){//getting the best successors and returning a random one of these
            if(bestScore.score == max)
                bestSuccessors.add(bestScore);
        }
        int index = r.nextInt(bestSuccessors.size());
        System.out.println("Using one that returns "+ bestSuccessors.get(index).score);
        return bestSuccessors.get(index).state;
        
        
    }
    
    private void minimaxEvaluation(){
        successorEvaluations = new ArrayList<>();
        
        minimax(0, 1, Integer.MIN_VALUE, Integer.MAX_VALUE);
        
    }
    
    public int minimax(int depth, int player, int alpha, int beta) {
        this.player = player;
        //System.out.println("player is " + player);
        //System.out.println("se count " + seCount);
        
        int bestScore;
        if(player == 1) //ai is maximising player
            bestScore = -100;
        else 
            bestScore = 100;
        List<int[][]> positionsAvailable = getAvailableStates(this.state);
        
        
        if(aiHasWon(this.state)){
            seCount++;
            return 100;
        } else if(playerHasWon(this.state)){
            seCount++;
            return -100;
        } else if(positionsAvailable.isEmpty()){
            seCount++;
            if(player==1)
                return -100;
            else
                return 100;
        } else if(depth >maxDepth) {
            seCount++;    
            int score = Evaluator.evaluate(this.state, heuristic);//apply heuristic evaluation
            return score;
              
        } 
        
        
       
            
        for (int i = 0; i < positionsAvailable.size(); i++) {
            // determine all potential successor states
            int[][] previous = cloneState(this.state);
            int[][] pos = positionsAvailable.get(i);
            
            deCount++; // count dynamic evaluations
            if (player == 1) { //X's turn: get the highest result returned by minimax
                // place a piece at the first available position
                this.state=pos; 
                // get the minimax evaluation result for making the previous move
                int currentScore = minimax(depth + 1, 2, alpha, beta); // Increase 
                if(currentScore > bestScore) 
                    bestScore = currentScore;
                alpha = Math.max(currentScore, alpha); 
                // store a mapping of complete evaluations (at depth 0) and their scores
                if (depth == 0) 
                    successorEvaluations.add(new StateAndScores(currentScore, this.state));
            } 
            else if (player == 2) {//O's turn: get the lowest result returned by minimax
                //bestScoreMin = Integer.MAX_VALUE;
                //int[][] stateToEvaluate=lastState;
                this.state=pos; 
                int currentScore = minimax(depth + 1, 1, alpha, beta);
                if(currentScore < bestScore) 
                    bestScore = currentScore;
                beta = Math.min(currentScore, beta);
                
            }
            this.state = cloneState(previous); //Reset this pos
            
            // pruning
            if(alpha >= beta) {
                pCount++;
                
                //System.out.println();
                //System.out.println("There is no point in going any further (Pruning at level "+depth+" because "+alpha+">="+beta+").");
                
              break;
            }
        }
        return bestScore;        
    }
    private boolean aiHasWon(int[][]state) {//looks if the player has any pieces left on the board
        // ai is black here
        int countPieces=0;
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                // is the current position available?
                if (state[i][j] == 1 || state[i][j] == 4) {
                    countPieces++;
                }
            }
        }
        if(countPieces>0){
            return false;
        }
        return true;
    }
    private boolean playerHasWon(int[][]state) {//looks if the ai has any pieces left on the board
        // ai is black here
        int countPieces=0;
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                // is the current position available?
                if (state[i][j] == 2 || state[i][j] == 3) {
                    countPieces++;
                }
            }
        }
        if(countPieces>0){
            return false;
        }
        return true;
    }
    private void printState(int[][] st){
        for (int[] x : st)
        {
            for (int y : x)
            {
                System.out.print(y + " ");
            }
            System.out.println();
        }
    }
    private int[][] getBestSuccessor(){
        int max = Integer.MIN_VALUE;
        int [][] ret = state;//get best successor
        
        ArrayList<int[][]> bestStates = new ArrayList<int[][]>();
        for(int i=0; i<successorEvaluations.size(); i++){//determine max score
            if(successorEvaluations.get(i).score > max){
                max= successorEvaluations.get(i).score;
            }
        }
        
        for(StateAndScores candidate : successorEvaluations){//filter out the states that meet the max score
            if(candidate.score==max){
                bestStates.add(candidate.state);
            }
        }
        
        int index = r.nextInt(bestStates.size());//choose random best state if there is more than 1
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
    
    private void scoreForState(){//does heuristic evaluation on the available states and adds them to the score and state array
        for(int[][] state : statesAvailable){
            int score = Evaluator.evaluate(state, heuristic);
            successorEvaluations.add(new StateAndScores(score, state));
        }
    }
    
    private boolean conversion(int[][] previous){//checks if there was a king conversion
        for (int j = 0; j < 8; ++j) {
            if(previous[7][j]==3)
                return true;
        }
        return false;
    }
    
    private boolean isValid( int[][] previous,  int[][] candidate){//checks if the move is a valid successor state
        
            for (int i = 0; i < 8; ++i) {
                for (int j = 0; j < 8; ++j) {
                 
                 if(candidate[i][j] ==6 && previous[i][j] ==6){
                    if(candidate[i-1][j-1]==5 && candidate[i+1][j+1]==5){
                        return true;
                    } else if(candidate[i-1][j+1]==5 && candidate[i+1][j-1]==5) {
                        return true;
                    } else{
                        return false;
                    }
                    
                }
                
            }
        }
        
        
        return false;
    }
    private ArrayList<int[][]> getAvailableStates(int[][] someState){
        this.kingConversion=false;
        this.forced=false;
        ArrayList<int[][]> freePositions = new ArrayList<>();
        //int[][] pastPosition = new int[8][8];
        //System.out.println("trying to grt states... ");
        ArrayList<int[][]> candidates = forcedMove(someState);
        int[][] previous;        
        for(int i = 0; i<candidates.size(); i++){
            this.forced= true;
            freePositions.add(candidates.get(i));
            previous = candidates.get(i);
            if(!conversion(previous)){
                ArrayList<int[][]> secondary = forcedMove(candidates.get(i));
                if(secondary.size() > 0){
                    
                    ArrayList<int[][]> validated = new ArrayList<>();
                    
                    for(int[][] candidate : secondary){
                        if(isValid(previous, candidate)){
                            validated.add(candidate);
                            candidates.add(candidate);
                        }
                        
                        }
                    if(validated.size()>0){
                        freePositions = new ArrayList<>();
                        for(int[][] candidateV : validated){
                            freePositions.add(candidateV);
                        
                        }
                       }
                
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
        if(test==0||test==5||test==6)
            return true;
        return false;    
    }
    private boolean isEnemy(int test){
        if(test==1||test==4)
            return true;
        return false;    
    }
    
    private boolean isEnemyW(int test){
        if(test==2||test==3)
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
    
    
    private int[][] checkKingConversion(int [][] st, int iNew, int jNew){//if the 7th row includes a normal black piece
        
        if(player==1){
            //System.out.println("checking for AI");
            if(iNew == 7){//king conversion
                st[iNew][jNew]=3;
                //System.out.println("something changed");
            } 
                
        } else {
            if(iNew == 0){//king conversion
                st[iNew][jNew]=4;
            } 
        }
        
        kingConversion=true;
        return st;
    }
    private void simpleMove(int iNew,int jNew,Position pos, int[][] someState){//moving and updating the candidate state
        int[][] stateCopy = cloneState(someState);///
        stateCopy[iNew][jNew] = stateCopy[pos.i][pos.j];
        stateCopy[pos.i][pos.j]=5;//the original position is vacated
        stateCopy = checkKingConversion(stateCopy, iNew, jNew);
        candidatesG.add(stateCopy);//add this prospective state
    }
    
    private int[][] captureMove(int iNew,int jNew,Position pos, int[][] someState){//moving and updating the candidate state
        int[][] stateCopy = cloneState(someState);
        stateCopy[pos.i+iNew][pos.j+jNew]=stateCopy[pos.i][pos.j];//our stone moved there
        stateCopy[pos.i][pos.j]=5;//the original position is vacated
        stateCopy[pos.i+(iNew/2)][pos.j+(jNew/2)]=6;//an enemy was eliminated in the middle
        stateCopy = checkKingConversion(stateCopy, pos.i+iNew, pos.j+jNew);//see if this move led to a king
        return stateCopy;
    }
    
    private int[][] captureDiagonal(int i, int iAdded, int j, int jAdded,Position pos, int[][] someState){
        int[][] stateCopy = cloneState(someState);
        stateCopy[i+ iAdded][j+ jAdded]=stateCopy[pos.i][pos.j];//our stone moved there
        stateCopy[pos.i][pos.j]=5;//the original position is vacated
        stateCopy[i][j]=6;//an enemy was eliminated
        
        return stateCopy;//add this prospective state
    }
    
    private void generalMove(Position pos, int[][] someState){
        candidatesG = new ArrayList<int[][]>();
        if(player==1){
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
                for(int i = pos.i-1; i>=0;i--){//up diagonal
                    if(jLeft>=0){
                        if(isFree(someState[i][jLeft])){
                            simpleMove(i, jLeft, pos, someState);
                            jLeft--;
                        } else {
                            jLeft=-1;
                        }
                    }
                    if(jRight<=7){
                        if(isFree(someState[i][jRight])){    
                            simpleMove(i, jRight, pos, someState);
                            jRight++;
                        }else {
                            jRight=8;
                        }
                     }    
                    if(!longJump)//not allowing more than 1 jump
                        break; 
                }
                jLeft = pos.j-1; 
                jRight = pos.j+1;
                for(int i = pos.i+1; i<=7;i++){//down diagonal
                    if(jLeft>=0){
                        if(isFree(someState[i][jLeft])){
                            simpleMove(i, jLeft, pos, someState);
                            jLeft--;
                        }else {
                            jLeft=-1;
                        }
                    }    
                    if(jRight<=7){    
                        if(isFree(someState[i][jRight])){    
                            simpleMove(i, jRight, pos, someState);
                            jRight++;
                        }else {
                            jRight=8;
                        }
                     }
                     if(!longJump)//not allowing more than 1 jump
                        break;
                }
            } 
            
        } else {//////////////////simulating player
            if(someState[pos.i][pos.j]==1){//a normal white stone
            //System.out.println("testing a black piece");
            if(pos.i>0 && pos.j>0){
                if (isFree(someState[pos.i-1][pos.j-1])){ //move left
                    simpleMove(pos.i-1,pos.j-1,pos, someState);
                    }
                }    
            if(pos.i>0 && pos.j<7) {   
                if (isFree(someState[pos.i-1][pos.j+1])){ //move right
                    simpleMove(pos.i-1,pos.j+1,pos, someState);
                    }
                } 
         } else if (someState[pos.i][pos.j]==4){///////////////////////////////////////////////////////////////it is a King
            int jLeft = pos.j-1; 
            int jRight = pos.j+1;
            for(int i = pos.i-1; i>=0;i--){//up diagonal
                if(jLeft>=0){
                    if(isFree(someState[i][jLeft])){
                        simpleMove(i, jLeft, pos, someState);
                        jLeft--;
                    } else {
                        jLeft=-1;
                    }
                }
                if(jRight<=7){
                    if(isFree(someState[i][jRight])){    
                        simpleMove(i, jRight, pos, someState);
                        jRight++;
                    }else {
                        jRight=8;
                    }
                 }   
                 if(!longJump)//not allowing more than 1 jump
                        break;
            }
            jLeft = pos.j-1; 
            jRight = pos.j+1;
            for(int i = pos.i+1; i<=7;i++){//down diagonal
                if(jLeft>=0){
                    if(isFree(someState[i][jLeft])){
                        simpleMove(i, jLeft, pos, someState);
                        jLeft--;
                    }else {
                        jLeft=-1;
                    }
                }    
                if(jRight<=7){    
                    if(isFree(someState[i][jRight])){    
                        simpleMove(i, jRight, pos, someState);
                        jRight++;
                    }else {
                        jRight=8;
                    }
                 }
                 if(!longJump)//not allowing more than 1 jump
                        break;
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
                if(player==1){//////simulating the AI to get its moves
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
                                        forcedPositions.add(captureDiagonal(i1, -1, jLeft, -1,pos, someState));
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
                                        forcedPositions.add(captureDiagonal(i1, -1, jRight, 1,pos, someState));
                                        jRight = 8;
                                    } else{
                                        jRight = 8;//cant jump 2 at once in any move, so this diagonal will not hold more moves
                                    }
                                }
                                jRight ++;
                            }
                            if(!longJump)//not allowing more than 1 jump
                                break;
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
                                        forcedPositions.add(captureDiagonal(i2, 1, jLeft, -1,pos, someState));
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
                                        forcedPositions.add(captureDiagonal(i2, 1, jRight, 1,pos, someState));
                                        jRight = 8;
                                    } else{
                                        jRight = 8;//cant jump 2 at once in any move, so this diagonal will not hold more moves
                                    }
                                   }
                                 jRight ++;
                                }
                        if(!longJump)//not allowing more than 1 jump
                            break;    
                        }
                
                    }
                
                } else {///simulating player to get their sucessor states
                    if (someState[pos.i][pos.j]==1){//it is a normal white stone
                    
                    if(pos.i>0 && pos.j>0){//looking at front left. smaller than 0 because board ends at 7, so if I am at 7 there is no more piece to go
                        if (isEnemyW(someState[pos.i-1][pos.j-1])){ //if there is an enemy ahead to the left
                            try{
                            if(isFree(someState[pos.i-2][pos.j-2])){//left back of enemy is empty
                                forcedPositions.add(captureMove(-2,-2,pos, someState));
                            }
                        }catch (Exception e){}
                        }
                    }
                    
                    if(pos.i>0 && pos.j<7) {   
                        if (isEnemyW(someState[pos.i-1][pos.j+1])){ //enemy to front right
                            
                            try{
                            if(isFree(someState[pos.i-2][pos.j+2])){//right back
                                forcedPositions.add(captureMove(-2,2,pos, someState));//add this prospective state
                            }
                            }catch (Exception e){}
                        }
                    }
            
            
                } else if(someState[pos.i][pos.j]==4){///////////////////////////////////if the checker is a King, all diagonals need to be checked
                    int jLeft = pos.j -1;
                    int jRight = pos.j +1;
                
                    for(int i1 = pos.i-1; i1>0;i1--){//up diagonal
                        if(jLeft >0 ){
                            if (someState[i1][jLeft]==1 || someState[i1][jLeft]==4){//an own piece is in the way
                                jLeft = -1;
                            } 
                            else if(isEnemyW(someState[i1][jLeft])){//there is an enemy piece
                                if(isFree(someState[i1-1][jLeft-1])){//there is an empty place behind it
                                    forcedPositions.add(captureDiagonal(i1, -1, jLeft, -1,pos, someState));
                                    jLeft = -1;//the diagonal move ends here
                                } else{
                                    jLeft = -1;//cant jump 2 at once in any move, so this diagonal will not hold more moves
                                }
                            }
                            jLeft --;
                        }
                        if(jRight<7){
                            if(someState[i1][jRight]==1 || someState[i1][jRight]==4){
                                jRight = 8;
                            } 
                            else if(isEnemyW(someState[i1][jRight])){//there is an enemy piece
                                if(isFree(someState[i1-1][jRight+1])){//there is an empty place behind it
                                    forcedPositions.add(captureDiagonal(i1, -1, jRight, 1,pos, someState));
                                    jRight = 8;
                                } else{
                                    jRight = 8;//cant jump 2 at once in any move, so this diagonal will not hold more moves
                                }
                            }
                            jRight ++;
                        }
                        if(!longJump)//not allowing more than 1 jump
                            break;
                    }
            
                    ///////////////////////down diagonal
                    jLeft = pos.j -1;
                    jRight = pos.j +1;
                    for(int i2 = pos.i+1; i2<7;i2++){//down diagonal
                        if(jLeft >0){
                            if (someState[i2][jLeft]==1 || someState[i2][jLeft]==4){//an own piece is in the way
                                jLeft = -1;
                            } 
                            else if(isEnemyW(someState[i2][jLeft])){//there is an enemy piece
                                if(isFree(someState[i2+1][jLeft-1])){//there is an empty place behind it
                                    forcedPositions.add(captureDiagonal(i2, 1, jLeft, -1,pos, someState));
                                    jLeft = -1;
                                } else{
                                    jLeft = -1;//cant jump 2 at once in any move, so this diagonal will not hold more moves
                                }
                            }
                            jLeft --;
                        }
                        if(jRight<7){
                            if(someState[i2][jRight]==1 || someState[i2][jRight]==4){
                                jRight = 8;
                            } 
                            else if(isEnemy(someState[i2][jRight])){//there is an enemy piece
                                if(isFree(someState[i2+1][jRight+1])){//there is an empty place behind it
                                    forcedPositions.add(captureDiagonal(i2, 1, jRight, 1,pos, someState));
                                    jRight = 8;
                                } else{
                                    jRight = 8;//cant jump 2 at once in any move, so this diagonal will not hold more moves
                                }
                               }
                             jRight ++;
                            }
                        if(!longJump)//not allowing more than 1 jump
                            break;    
                    }
            
                }
                }
                
                
            }
        }
        
        //System.out.println("Possible capturing moves for this state: " + forcedPositions.size());
        return forcedPositions;
    }
}


