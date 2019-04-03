import java.util.ArrayList;
import java.util.List;
/**
 * Write a description of class Node here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Node
{
    int[][] data;//the game state that this node represents
    boolean visited;
    List<Node> neighbours;
    
    Node(int[][] potentialBoard)
    {
    	this.data=potentialBoard;
    	this.neighbours=new ArrayList<>();
    
    }
    
    public void addNeighbour(Node neighbourNode)
    {
    	this.neighbours.add(neighbourNode);
    }
    
    public List getNeighbours() {
    	return neighbours;
    }
    
    public void setNeighbours(List neighbours) {
    	this.neighbours = neighbours;
    }
}
