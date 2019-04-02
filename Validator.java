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
    }
    
    
    
    public void getFeedback(){
        
        System.out.println("I am unhappy with this move!!!!!!!");
    }
    
    public void setClicked(int i, int j){
        lastClicked = new ArrayList<Integer>();
        lastClicked.add(i);
        lastClicked.add(j);
        System.out.println(i +" "+ j);
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
        //System.out.println("Validating drop");
        try{
            //System.out.println("LC: " + lastClicked.get(0) + lastClicked.get(1));
            
                
            //System.out.println(validatingPositions.size());
            
            ArrayList<Position> allowedDrops = validatingPositions.get(lastClicked);
            
            for (Position pos : allowedDrops ){//check if last drop is an entry in the list we fetched
                //System.out.println("try pos " + pos.i+pos.j);
                //System.out.println("dropped pos " + iDropped+jDropped);
                if(pos.i == iDropped && pos.j == jDropped){
                    checkHit();
                    return true;
                }
            }
            } catch(Exception e){//the map was empty at that key
                System.out.println("caught");
            return false;
            }    
        return false;
    }
    
    private void checkHit(){//find coordinates of hit checkers
        int iDiff= (iDropped - lastClicked.get(0));
        if (Math.abs(iDiff)>1){
            iDiff = iDiff/2;
            int iHit = iDropped-iDiff;
            int jDiff= (jDropped - lastClicked.get(1))/2;
            int jHit = jDropped-jDiff;
            //System.out.println("Hit me at " + iHit+jHit);
            this.hit=true;
        } else{
            this.hit=false;
        }
        
        
        
    }
}
