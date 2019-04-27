import java.util.*;
import java.lang.Math;
/**
 * Validator is a helper class for board. It is responsible for allowing or refusing drops.
 */
public class Validator
{
    private boolean dontUpdate;
    ////////coordinates for attempts to move
    public ArrayList<Integer> lastClicked;
    public int iDropped;
    private int jDropped;
    /////////////////contains all acceptable positions that the player could legally carry out
    private HashMap<ArrayList<Integer>, ArrayList<Position>> validatingPositions;
    ///////coordinate of opponent, if it was eliminated
    private int enemyI;
    private int enemyJ;
    private boolean hit;//caught an enemy!!!
    private boolean isKing;
    /**
     * Constructor for objects of class Validator
     */
    public Validator()
    {
        hit=false;
        dontUpdate=false;
        isKing=false;
        iDropped=8;
    }
    
    public void setKing(boolean isK){
        this.isKing = isK;
    }
    
    public int[] getFeedback(){/////////querying info on checkers eliminated in this move
        if(this.hit){
            return new int[]{enemyI, enemyJ};
        } else {
            return null;
        }
    }
    public void setClicked(int i, int j){
        lastClicked = new ArrayList<Integer>();
        lastClicked.add(i);
        lastClicked.add(j);
        //System.out.println("val set clicked (Validator setClicked())" +i +" "+ j);
    }
    
    public void tryDestination(int i, int j){
        iDropped = i;
        jDropped = j;
    }
    
    public void updateHashMap(HashMap<ArrayList<Integer>, ArrayList<Position>> validatingPositions){
        //validatingPositions = new HashMap<ArrayList<Integer>, ArrayList<Position>>();
        this.validatingPositions = validatingPositions;
    }
    
    public boolean validateDrop(){///check if the last clicked source field is a key in the positions map
        //System.out.println("ValidateDrop() validator");
        try{
            ArrayList<Position> allowedDrops = validatingPositions.get(lastClicked);
            for (Position pos : allowedDrops ){//check if last drop is an entry in the list we fetched
                if(lastClicked.get(0)==iDropped && lastClicked.get(1)==jDropped){
                    return false;
                }
                    if(pos.i == iDropped && pos.j == jDropped){
                    this.hit = checkHit();//determines if enemy was hit
                    dontUpdate=false;
                    return true;
                }
            }
            } catch(Exception e){//the map was empty at that key
                System.out.println("caught");
                dontUpdate=true;
            return false;
            } 
        dontUpdate=true;
        return false;
    }
    
    protected int[] getIndex(){
        List<ArrayList<Position>> valuesList = new ArrayList<ArrayList<Position>>(validatingPositions.values());
        int randomIndex = new Random().nextInt(valuesList.size());
        ArrayList<Position> randomValue = valuesList.get(randomIndex);
        Position posi = randomValue.get(new Random().nextInt(randomValue.size()));
        return new int[]{posi.i, posi.j};
    }
    private boolean checkHit(){//find coordinates of hit checkers
        
        int iDiff= (iDropped - lastClicked.get(0));
        if(Math.abs(iDiff)>2){
            int jDiff= (jDropped - lastClicked.get(1));
            if(iDiff<0){
                this.enemyI = iDropped+1;
            } else {
                this.enemyI = iDropped-1;
            }
            
            if (jDiff>0){
                this.enemyJ = jDropped -1;
                
            } else {
                this.enemyJ = jDropped +1;
                
            }
            return true;
        } else if (Math.abs(iDiff)>1){///////////////the checkers has moved 2 places, so there was a normal hit and we need to determine the coordinates of the hit piece
            iDiff = iDiff/2;
            this.enemyI = iDropped-iDiff;
            int jDiff= (jDropped - lastClicked.get(1))/2;
            this.enemyJ = jDropped-jDiff;
            return true;
        } else{
            return false;
        }
    }
    
    public boolean getDontUpdate(){
        return dontUpdate;
    }
}

