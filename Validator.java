import java.util.*;
import java.lang.Math;
/**
 * Write a description of class Validator here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Validator
{
    // variables used to update the state. these are the coordinates of successful source-destination moves
    
    private boolean dontUpdate;
    ////////coordinates for attempts to move
    private ArrayList<Integer> lastClicked;
    private int iDropped;
    private int jDropped;
    
    /////////////////contains all acceptable positions that the player could legally carry out
    private HashMap<ArrayList<Integer>, ArrayList<Position>> validatingPositions;
    
    ///////coordinate of opponent, if it was eliminated
    private int enemyI;
    private int enemyJ;
    private boolean hit;//caught an enemy!!!
    
    
    /**
     * Constructor for objects of class Validator
     */
    public Validator()
    {
        hit=false;
        dontUpdate=false;
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
        System.out.println("val set clicked (Validator setClicked())" +i +" "+ j);
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
        System.out.println("ValidateDrop() validator");
        try{
            //System.out.println("LC: " + lastClicked.get(0) + lastClicked.get(1));
            
                
            //System.out.println(validatingPositions.size());
            //if(lastClicked.get(0)==iDropped){
               // System.out.println("Droplast");
                //return false;
                
            //}
            ArrayList<Position> allowedDrops = validatingPositions.get(lastClicked);
            
            for (Position pos : allowedDrops ){//check if last drop is an entry in the list we fetched
                //System.out.println("try pos " + pos.i+pos.j);
                //System.out.println("Allowed drop " + pos.i+ " " + pos.j);
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
    
    private boolean checkHit(){//find coordinates of hit checkers
        
        int iDiff= (iDropped - lastClicked.get(0));
        if (Math.abs(iDiff)>1){///////////////the checkers has moved 2 places, so there was a hit and we need to determine the coordinates of the hit piece
            iDiff = iDiff/2;
            this.enemyI = iDropped-iDiff;
            int jDiff= (jDropped - lastClicked.get(1))/2;
            this.enemyJ = jDropped-jDiff;
            System.out.println("Hit me at " + enemyI+enemyJ);
            return true;
        } else{
            return false;
        }
        
        
        
    }
    
    public boolean getDontUpdate(){
        return dontUpdate;
    }
}
