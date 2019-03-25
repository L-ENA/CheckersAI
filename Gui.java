import javax.swing.*;
/**
 * Write a description of class Gui here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Gui
{
     private JFrame mainFrame;
     private MyMenuBar menuBar; 
     private AllComponents componentPane;
     //private Board myBoard;
     //private piece aPiece;

    /**
     * Constructor for objects of class Gui
     */
    public Gui()
    {
        mainFrame = new JFrame("CheckersAI");//init mainframe
        componentPane = new AllComponents();
        mainFrame.add(componentPane);
        mainFrame.setContentPane(componentPane);//adding the com
        menuBar = new MyMenuBar(mainFrame);//init menu bar for this game
        mainFrame.setJMenuBar(menuBar);
        mainFrame.pack();//to finalise changes and visualise them
        mainFrame.setVisible(true);
    }

    /**
     * An example of a method - replace this comment with your own
     *
     * @param  y  a sample parameter for a method
     * @return    the sum of x and y
     */
    public void sampleMethod(int y)
    {
        // put your code here
        
    }
    
    
    
    
    
    protected boolean exitMessage(String message, String title, boolean won){
        if(won==true){
            //logger.warning("Player won");
        } else{
            //logger.warning("Player lost");
        }
        String[] options = {"Play again","Exit"};
        int choice = JOptionPane.showOptionDialog(mainFrame, //Component parentComponent
               message, //Object message,
               title, //String title
               JOptionPane.YES_NO_OPTION, //int optionType
               JOptionPane.INFORMATION_MESSAGE, //int messageType
               null, //Icon icon,
               options,
               options[1]);
        if(choice == 0 ){
           //logger.info("Player restarted the game");
           return true;
         }else{
           //logger.info("Player quit the game");
           return false;//not gonna get executed
         }
        }
        
    /**
     * The player wants to play again, therefore the current main frame is terminated.
     * @param  none
     * @return    void
     */
    protected void killGui(){
        mainFrame.dispose();
}    
}
