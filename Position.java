import java.util.*;
/**
 * Write a description of class Position here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
class Position {
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
}

class PositionsAndScores {
    int score;
    Position pos;
    PositionsAndScores(int score, Position pos) {
        this.score = score;
        this.pos = pos;
    }
    
    
}