import java.util.*;
/**A position, used in main to deal with indexing of the board
 */
class Position implements Cloneable{
    int i, j;
    public Position(int i, int j) {
        this.i = i;
        this.j = j;
    }
    public ArrayList<Integer> toAList(){//to automatically make array list from position obj
        ArrayList<Integer> ret = new ArrayList<Integer>();
        ret.add(this.i);
        ret.add(this.j);
        return ret;
    }
    
    @Override
    protected Position clone() {
        Position clone = null;
        try{
            clone = (Position) super.clone();//casting position and using objects clone method on it
           
        }catch(CloneNotSupportedException e){// catching related exception
            System.out.println("Something went wrong with the cloning.");
            throw new RuntimeException(e); 
        }
       return clone;
    }
    protected boolean equals(Position p) {
        if(p.i == this.i && p.j == this.j)
            return true;
        else
            return false;
    }
}
class StateAndScores {//for evaluating successor state in minimax
    int score;
    int[][] state;
    StateAndScores(int score, int[][] state) {
        this.score = score;
        this.state = state;
    }
}