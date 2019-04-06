import java.util.*;
/**
 * Write a description of class Position here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
class Position implements Cloneable{
    int i, j;
    public Position(int i, int j) {
        this.i = i;
        this.j = j;
    }
    @Override
    public String toString() {
        return "(" + (i) + "," + (j) + ")";
    }
    public ArrayList<Integer> toAList(){
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
}

class StateAndScores {
    int score;
    int[][] state;
    StateAndScores(int score, int[][] state) {
        this.score = score;
        this.state = state;
    }
    
    
}