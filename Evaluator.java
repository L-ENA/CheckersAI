
/**
 * Evaluator performs heuristic evaluation for a given state. There is a choiice of 3 heuristics
 */
public class Evaluator
{
    static final String P="Pieces";
    static final String P_W="Pieces + Weights";
    static final String P_W_P="Pieces + Weights + Positions";
    static final String D="Distance";
    public static int evaluate(int[][] candidate, String heur){
        int result = Integer.MIN_VALUE;
        switch (heur) {
            case P:  
                    result= pieces(candidate);
            case P_W:
                    result= piecesWeighted(candidate);
            case P_W_P: 
                    result= piecesUltimate(candidate);
            }
        return result;    
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
                        sumOwn +=3;
                        break;
                    case 4:
                        sumEnemy +=3;
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
                        sumOwn +=6;
                        
                        break;
                    case 4:
                        sumEnemy +=6;
                        //System.out.println("white king");
                        break;
                }
            }
        }
        return sumOwn-sumEnemy;
    }
}
