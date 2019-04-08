
/**
 * Write a description of class Evaluator here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Evaluator
{
    // instance variables - replace the example below with your own
    static final int NR_PIECES=1;
    static final int NR_AND_SCORE=2;
    static final int NR_AND_SCORE_AND_POSITION=3;
    
    /**
     * Constructor for objects of class Evaluator
     */
    public Evaluator()
    {
        // initialise instance variables
        
    }
    
    private static int pieces(int[][] candidate){//////simple heuristic that counts own pieces and enemy pieces
        int sumOwn=0;
        int sumEnemy=0;
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                
                if (candidate[i][j] == 2|| candidate[i][j] == 3) {
                    sumOwn++;
                } else if (candidate[i][j] == 1|| candidate[i][j] == 4){
                    sumEnemy++;
                }
                
            }
        }
        return sumOwn-sumEnemy;
    }
    
    private static int piecesWeighted(int[][] candidate){//////simple heuristic that counts own pieces and enemy pieces
        int sumOwn=0;
        int sumEnemy=0;
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                
                switch(candidate[i][j]){///similar to the pieces() heuristic, but this one gives extra points for kings
                    case 2:
                        sumOwn++;
                        break;
                    case 1:
                    
                        sumEnemy++;
                        break;
                    case 3:
                        sumOwn +=4;
                        break;
                    case 4:
                        sumEnemy +=4;
                        break;
                }
                
            }
        }
        return sumOwn-sumEnemy;
    }
    
    private static int piecesUltimate(int[][] candidate){//////heuristic that counts own pieces and enemy pieces plus values
        int sumOwn=0;
        int sumEnemy=0;
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                
                switch(candidate[i][j]){///similar to the pieces() heuristic, but this one gives extra points for kings
                    case 2:
                        
                        if(i<3){
                            sumOwn++;
                        } else if (i<6){
                            sumOwn += 2;
                        } else {
                            sumOwn += 3;
                        }
                            
                        break;
                    case 1:
                    
                        if(i<3){
                            sumEnemy+= 3;
                        } else if (i<6){
                            sumEnemy += 2;
                        } else {
                            sumEnemy ++;
                        }
                        break;
                    case 3:
                        sumOwn +=5;
                        break;
                    case 4:
                        sumEnemy +=5;
                        break;
                }
                
            }
        }
        return sumOwn-sumEnemy;
    }
    /**
     * An example of a method - replace this comment with your own
     *
     * @param  y  a sample parameter for a method
     * @return    the sum of x and y
     */
    public static int evaluate(int[][] candidate, int heur){
        int result = Integer.MIN_VALUE;
        switch (heur) {
            case 1:  
                    result= pieces(candidate);
            case 2:
                    result= piecesWeighted(candidate);
            case 3: 
                    result= piecesUltimate(candidate);
            }
        //System.out.println("Heuristic eval is: " + result);
        return result;    
    }
}
