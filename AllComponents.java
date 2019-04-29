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
 * GUI class that contains the main visible elements; board and sideboard. While board is controlled in the board class, this class here is responsible for the sideboard options panel. 
 * 
 */
public class AllComponents extends JPanel
{
    protected Board boardPane;//all gui elements
    private JPanel sidebar;
    private JPanel settings;
    private JLabel nrLabel;
    private JLabel playerLossLabel;
    private JLabel aiLossLabel;
    private JLabel seCount;
    private JLabel deCount;
    private JLabel pCount;
    private JButton showOptions;
    private JComboBox level;
    private JComboBox heuristicBox;
    private JCheckBox mode;
    private JButton reStart;
    private JComboBox heuristic;
    private GridBagConstraints c;
    
    private static final int wide = 1000;
    private static final int high = 700;
    
    private Border contentBorder;
    private Border standardBorder;
    //////////////////////////////////////vars for main
    protected String selectedLevel;
    protected String heur;
    protected boolean longJumps;
    
    
    /**
     * Constructor for objects of class AllComponents
     */
    public AllComponents()
    {
        super(new GridBagLayout());
        c = new GridBagConstraints();//to manage layout
        c.fill = GridBagConstraints.BOTH;//stretch both horizontally and vertically
        c.weighty = 1;//sets size of this panel compared with other panels: they all stretch over the whole height
        c.weightx = 0.9;//sets size of this panel compared with other panels: this one is slightly small in x direction
        c.gridx = 0;//grid coordinates where the panel will sit
        c.gridy = 0;
        c.anchor = GridBagConstraints.FIRST_LINE_START;//if window is resized
        contentBorder = new BevelBorder(BevelBorder.RAISED, Color.GREEN, Color.BLACK);//border for all content
        standardBorder = new LineBorder(Color.GREEN);//Customizes the line colour for the following TitledBorder instances
        selectedLevel = "Intermediate";
        longJumps=false;
        heur= "Pieces + Weights + Positions";
    }
    
    public void addBoardPane(Board p){//creates and adds the checkers board on the left, as the big component
        this.boardPane=p;
        this.add(boardPane, c);
        addSidebar();
    }

    private void addSidebar(){//adding the settings panel
        c.weightx = 0.1;
        c.gridx = 1;
        c.gridy = 0;
        c.anchor = GridBagConstraints.PAGE_START;
        sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));//here, a box layout makes sense to display labels below each other
        sidebar.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.setBackground(Color.white);
        sidebar.setBorder(new TitledBorder(standardBorder, "Information"));//uses the border customized before and adds a title
        
        nrLabel = new JLabel("Number of moves: 0 ");/////label displaying game stats
        nrLabel.setBorder(contentBorder);
        nrLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        playerLossLabel = new JLabel("Player loss: 0 ");/////label displaying game stats
        playerLossLabel.setBorder(contentBorder);
        playerLossLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        aiLossLabel = new JLabel("AI loss: 0 ");/////label displaying game stats
        aiLossLabel.setBorder(contentBorder);
        aiLossLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        showOptions = new JButton("Show moves");
        showOptions.setBorder(contentBorder);
        showOptions.setBackground(Color.LIGHT_GRAY);//Black By Default
        showOptions.setForeground(Color.BLACK);//Set as a Gray Colour
        showOptions.setAlignmentX(Component.CENTER_ALIGNMENT);
        showOptions.addMouseListener(new MouseAdapter()
        {
            @Override 
           public void mousePressed(MouseEvent e) {
               //System.out.println("mousePressed (update() )");
               boardPane.showOptions();
           }
        });
        //sidebar.setBorder(contentBorder);
        
        seCount= new JLabel("Static evaluations: 0 ");
        seCount.setBorder(contentBorder);
        seCount.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        deCount= new JLabel("Dynamic evaluations: 0 ");
        deCount.setBorder(contentBorder);
        deCount.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        pCount= new JLabel("Pruning count: 0 ");
        pCount.setBorder(contentBorder);
        pCount.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        sidebar.add(Box.createRigidArea(new Dimension(0,15)));//some space between the components
        sidebar.add(nrLabel);
        sidebar.add(Box.createRigidArea(new Dimension(0,15)));//some space between the components
        
        sidebar.add(playerLossLabel);
        sidebar.add(Box.createRigidArea(new Dimension(0,15)));//some space between the components
        
        sidebar.add(Box.createRigidArea(new Dimension(0,15)));//some space between the components
        sidebar.add(aiLossLabel);
        
        sidebar.add(Box.createRigidArea(new Dimension(0,15)));//some space between the components
        sidebar.add(showOptions);
        
        sidebar.add(Box.createRigidArea(new Dimension(0,15)));//some space between the components
        addSettings();//settings part of the panel
        
        sidebar.add(Box.createRigidArea(new Dimension(0,15)));//some space between the components
        sidebar.add(seCount);
        
        sidebar.add(Box.createRigidArea(new Dimension(0,15)));//some space between the components
        sidebar.add(deCount);
        
        sidebar.add(Box.createRigidArea(new Dimension(0,15)));//some space between the components
        sidebar.add(pCount);
        
        sidebar.add(Box.createRigidArea(new Dimension(0,15)));//some space between the components
        reStart = new JButton("Restart the game");
        reStart.setBorder(contentBorder);
        reStart.setBackground(Color.LIGHT_GRAY);//Black By Default
        reStart.setForeground(Color.BLACK);//Set as a Gray Colour
        reStart.setAlignmentX(Component.CENTER_ALIGNMENT);
        reStart.addMouseListener(new MouseAdapter()
        {
            @Override 
           public void mousePressed(MouseEvent e) {
               //System.out.println("mousePressed (update() )");
               boardPane.triggerRestart();
           }
        });
        sidebar.add(reStart);
        this.add(sidebar, c);
    }
    private void addSettings(){//adding the options of the settings panel
        settings = new JPanel();
        settings.setLayout(new BoxLayout(settings, BoxLayout.Y_AXIS));//here, a box layout makes sense to display labels below each other
        settings.setBackground(Color.white);
        settings.setBorder(new TitledBorder(contentBorder, "Settings"));//uses the border customized before and adds a title
        String[] difLevels = { "Kindergarden", "Novice", "Intermediate", "Professional", "Ultimate Genius" };
        level = new JComboBox(difLevels){
            @Override
            public Dimension getMaximumSize() {
                Dimension max = super.getMaximumSize();
                max.height = getPreferredSize().height;
                return max;
            }
        
        };
        level.setRenderer(new MyComboBoxRenderer("Difficulty"));//for title string of the combo box
        level.setSelectedIndex(-1);
        
        level.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox cb = (JComboBox)e.getSource();
                selectedLevel = (String)cb.getSelectedItem();
                System.out.println("Difficulty was changed");
            }
        });
        
        String[] heuristics = { "Pieces", "Pieces + Weights", "Pieces + Weights + Positions"};
        heuristicBox = new JComboBox(heuristics){
            @Override
            public Dimension getMaximumSize() {
                Dimension max = super.getMaximumSize();
                max.height = getPreferredSize().height;
                return max;
            }
        };
        heuristicBox.setRenderer(new MyComboBoxRenderer("Heuristic"));
        heuristicBox.setSelectedIndex(-1);
        heuristicBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox cb = (JComboBox)e.getSource();
                heur = (String)cb.getSelectedItem();
                System.out.println("Heuristic was changed");
            }
        });
        
        mode = new JCheckBox("Long King Jumps");
        mode.setSelected(false);
        mode.addItemListener(new ItemListener(){
            @Override
            public void itemStateChanged(ItemEvent e) {
            if(mode.isSelected()){
                longJumps=true;
            } else {
                longJumps=false;
            }
          }
        });
        settings.add(Box.createRigidArea(new Dimension(0,15)));//some space between the components
        settings.add(level);//adding all gui components initialised above
        settings.add(Box.createRigidArea(new Dimension(0,15)));//some space between the components
        settings.add(heuristicBox);
        settings.add(Box.createRigidArea(new Dimension(0,15)));//some space between the components
        settings.add(mode);
        settings.add(Box.createRigidArea(new Dimension(0,15)));//some space between the components
        sidebar.add(settings);
    }
    
    public void updateSidebar(int nrLab, int plLoss, int aiLoss,  int seC, int deC, int pC){
         //function to update this part of the gui with stats on pruning, game progress etx
         nrLabel.setText("Number of moves: "+Integer.toString(nrLab));
         playerLossLabel.setText("Player loss: "+Integer.toString(plLoss));
         aiLossLabel.setText("AI loss: "+Integer.toString(aiLoss));
         seCount.setText("Static evaluations:  "+ seC);
         deCount.setText("Dynamic evaluations:  "+ deC);
         pCount.setText("Pruning counts:  "+ pC);
         //showOptions;
         sidebar.revalidate();
         sidebar.repaint();
    }
    @Override
    public Dimension getPreferredSize() {//Overriding the preferred size method to get a properly sized window at startup
       return new Dimension(wide, high);
    }
}

class MyComboBoxRenderer extends JLabel implements ListCellRenderer {
        private String title;//getting pretty and descriptive titles for comboboxes before they are used for first time
        public MyComboBoxRenderer(String newTitle) {
            title = newTitle;
        }
        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean hasFocus) {
            if (index == -1 && value == null) setText(title );//just at the beginning when index is -1, combobox displays nice title
            else setText(value.toString());
            return this;
        }
}