import java.awt.*;  
import javax.swing.*;
import java.util.*;
/**
 * Write a description of class AllComponents here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class AllComponents extends JPanel
{
    // instance variables - replace the example below with your own
    protected Board boardPane;
    private JPanel sidebar;
    private JLabel sideLabel;
    
    private GridBagConstraints c;
    
    private static final int wide = 1000;
    private static final int high = 700;
    
    /**
     * Constructor for objects of class AllComponents
     */
    public AllComponents(Main game)
    {
        // initialise instance variables
        super(new GridBagLayout());
        c = new GridBagConstraints();//to manage layout
        c.fill = GridBagConstraints.BOTH;//stretch both horizontally and vertically
        c.weighty = 1;//sets size of this panel compared with other panels: they all stretch over the whole height
        c.weightx = 0.8;//sets size of this panel compared with other panels: this one is slightly small in x direction
        c.gridx = 0;//grid coordinates where the panel will sit
        c.gridy = 0;
        c.anchor = GridBagConstraints.FIRST_LINE_START;//if window is resized
        
        boardPane=new Board(game);
        this.add(boardPane, c);
        addSidebar();
        //this.setSize(getPreferredSize());
        
    }

    /**
     * An example of a method - replace this comment with your own
     *
     * @param  y  a sample parameter for a method
     * @return    the sum of x and y
     */
    private void addBoard()
    {
        
    }
    
    private void addSidebar(){
        c.weightx = 0.1;
        c.gridx = 1;
        c.gridy = 0;
        c.anchor = GridBagConstraints.PAGE_START;

        sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));//here, a box layout makes sense to display labels below each other
        sidebar.setBackground(Color.white);
        //sidebar.setBorder(new TitledBorder(standardBorder, "Information"));//uses the border customized before and adds a title
        sideLabel = new JLabel("Some stats, updated at runtime");/////label displaying game stats
        //sidebar.setBorder(contentBorder);
        sidebar.add(Box.createRigidArea(new Dimension(0,15)));//some space between the components
        sidebar.add(sideLabel);
        this.add(sidebar, c);
    }
    
    
    
    //public void updateBoard(ArrayList<Node> initState){
        //boardPane.update(initState);
    //}
    
    
    /**
     * Overriding the preferred size method.
     * @param  none
     * @return    void
     */
    @Override
    public Dimension getPreferredSize() {
       return new Dimension(wide, high);
    }
}
