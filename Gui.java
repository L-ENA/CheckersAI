import javax.swing.*;
import java.util.*;
/**
 * Class to contain components and menubar.
 */
public class Gui
{
    private JFrame mainFrame;
    private MyMenuBar menuBar; 
    protected AllComponents componentPane;
    /**
     * Constructor for objects of class Gui, creates and sets further elements such as menu bar and board instance
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

    protected boolean exitMessage(String message, String title){//called when game is over, gives option to restart
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
           return true;
         }else{
           return false;//not gonna get executed
         }
    }
    protected void killGui(){//for restarting: disposes of current gui
        mainFrame.dispose();
    }    
}