
/**
 * Evaluator performs heuristic evaluation for a given state. There is a choiice of 3 heuristics
 */
public class Evaluator
{
    static final String P="Pieces";
    static final String P_W="Pieces + Weights";
    static final String P_W_P="Pieces + Weights + Positions";
    static final String P_AI="Pieces AI";
    static final String P_AI_P="Pieces AI + Positions";
    public static int evaluate(int[][] candidate, String heur){
        int result = Integer.MIN_VALUE;
        switch (heur) {
            case P:  
                    result= pieces(candidate);
            case P_W:
                    result= piecesWeighted(candidate);
            case P_W_P: 
                    result= piecesUltimate(candidate);
            case P_AI: 
                result= piecesOwn(candidate);
            case P_AI_P: 
                result= piecesOwnW(candidate);    
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
                        sumOwn +=2;
                        if(i>1&&i<6)
                            sumOwn++;
                        break;
                    case 4:
                        sumEnemy +=2;
                        if(i>1&&i<6)
                            sumEnemy++;
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
                        if(i>1&&i<6)
                            sumOwn++;
                        break;
                    case 4:
                        sumEnemy +=6;
                        if(i>1&&i<6)
                            sumEnemy++;
                        break;
                }
            }
        }
        return sumOwn-sumEnemy;
    }
    
    
    private static int piecesOwn(int[][] candidate){//////heuristic that counts own pieces and enemy pieces plus values
        int sumOwn=0;
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                
                switch(candidate[i][j]){///similar to the pieces() heuristic, but this one gives extra points for kings
                    case 2:
                        
                        if(i<3){
                            sumOwn++;
                        } else if (i<6){
                            sumOwn += 1;
                        } else {
                            sumOwn += 2;
                        }
                            
                        break;
                    case 3:
                        sumOwn +=4;
                        if(i>1&&i<6)
                            sumOwn++;
                        break;
                        
                    
                }
            }
        }
        return sumOwn;
    }
    private static int piecesOwnW(int[][] candidate){//////heuristic that counts own pieces and enemy pieces plus values
        int sumOwn=0;
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                
                switch(candidate[i][j]){///similar to the pieces() heuristic, but this one gives extra points for kings
                    case 2:
                        if(i==0){
                            sumOwn +=2;
                        } else if(i<3){
                            sumOwn++;
                            if(j<2||j>6){
                                sumOwn++;
                            }
                        } else if (i<5){
                            sumOwn += 2;
                            if(j<2||j>6){
                                sumOwn++;
                            }
                        } else {
                            sumOwn += 3;
                            if(j<2||j>6){
                                sumOwn++;
                            }
                        }
                            
                        break;
                    case 3:
                        sumOwn +=6;
                        if(i>1&&i<6)
                            sumOwn++;
                        break;
                    case 4:
                        sumOwn --;
                        break;
                      
                        
                    
                }
            }
        }
        return sumOwn;
    }
    
}
