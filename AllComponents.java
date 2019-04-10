import java.awt.*;  
import javax.swing.*;
import java.util.*;
import java.awt.Color;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.*;
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
    private JLabel nrLabel;
    private JLabel playerLossLabel;
    private JLabel aiLossLabel;
    private JButton showOptions;
    
    private GridBagConstraints c;
    
    private static final int wide = 1000;
    private static final int high = 700;
    
    private Border contentBorder;
    private Border standardBorder;
    /**
     * Constructor for objects of class AllComponents
     */
    public AllComponents()
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
        
        contentBorder = new BevelBorder(BevelBorder.RAISED, Color.GREEN, Color.BLACK);//border for all content
        standardBorder = new LineBorder(Color.GREEN);//Customizes the line colour for the following TitledBorder instances
        //this.setSize(getPreferredSize());
        
    }
    
    public void addBoardPane(Board p){
        this.boardPane=p;
        this.add(boardPane, c);
        addSidebar();
    }

    
    
    private void addSidebar(){
        c.weightx = 0.1;
        c.gridx = 1;
        c.gridy = 0;
        c.anchor = GridBagConstraints.PAGE_START;

        sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));//here, a box layout makes sense to display labels below each other
        sidebar.setBackground(Color.white);
        sidebar.setBorder(new TitledBorder(standardBorder, "Information"));//uses the border customized before and adds a title
        
        
        nrLabel = new JLabel("Number of moves: 0 ");/////label displaying game stats
        nrLabel.setBorder(contentBorder);
        
        playerLossLabel = new JLabel("Player loss: 0 ");/////label displaying game stats
        playerLossLabel.setBorder(contentBorder);
        
        aiLossLabel = new JLabel("AI loss: 0 ");/////label displaying game stats
        aiLossLabel.setBorder(contentBorder);
        
        showOptions = new JButton("Show moves");
        showOptions.setBorder(contentBorder);
        showOptions.setBackground(Color.LIGHT_GRAY);//Black By Default
        showOptions.setForeground(Color.BLACK);//Set as a Gray Colour
        showOptions.addMouseListener(new MouseAdapter()
        {
            @Override 
           public void mousePressed(MouseEvent e) {
               //System.out.println("mousePressed (update() )");
               System.out.println("Press meeeeeee");
               boardPane.showOptions();
           }
        });
        //sidebar.setBorder(contentBorder);
        sidebar.add(Box.createRigidArea(new Dimension(0,15)));//some space between the components
        sidebar.add(nrLabel);
        sidebar.add(Box.createRigidArea(new Dimension(0,15)));//some space between the components
        
        sidebar.add(playerLossLabel);
        sidebar.add(Box.createRigidArea(new Dimension(0,15)));//some space between the components
        
        sidebar.add(aiLossLabel);
        sidebar.add(Box.createRigidArea(new Dimension(0,15)));//some space between the components
        
        sidebar.add(showOptions);
        this.add(sidebar, c);
    }
    
    
    
    public void updateSidebar(int nrLab, int plLoss, int aiLoss, String someOption){
         nrLabel.setText("Number of moves: "+Integer.toString(nrLab));
         playerLossLabel.setText("Player loss: "+Integer.toString(plLoss));
         aiLossLabel.setText("AI loss: "+Integer.toString(aiLoss));
         //showOptions;
         sidebar.revalidate();
         sidebar.repaint();
    }
    
    
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
