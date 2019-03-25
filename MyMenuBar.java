import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;

import javax.swing.JEditorPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.awt.Desktop;
import java.net.URL;
/**
 * This class extends JMenuBar. The constructor returns a menu bar that is customized for the CheckersAI. It contains the function to exit
 * the game and functions to provide some information to the player.
 *
 * @205232
 * 
 */
public class MyMenuBar extends JMenuBar
{
    private JFrame mainFrame;
     /**
     * Constructor for objects of class MyMenuBar. Its submenues and items are created here. 
     */
    public MyMenuBar(JFrame mainFrame)
    {
        super();
        this.mainFrame = mainFrame;
        JMenu gameMenu = new JMenu("Game");//new menues
        JMenu aboutMenu = new JMenu("About");
        
        JMenuItem closeItem = new JMenuItem("Close");//create menu items and their action commands
        closeItem.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int ret = JOptionPane.showConfirmDialog(mainFrame, "Are you sure you want to exit? ");
                    if(ret == JOptionPane.YES_OPTION)
                        System.exit(0);//exits the game
                }
            }
            
        );
        
        JMenuItem helpItem = new JMenuItem("Rules");//create menu items and their action commands
        helpItem.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Object[] options = {"OK","Open rules online", "Cancel"};
                    
                    int n = JOptionPane.showOptionDialog(mainFrame, " Quick rule info:"
                    + " \n Black moves first, afterwards players move alternately. "
                    + " \n Checkers only move forwards diagonally."
                    + " \n Opponent's checkers can be caught in a capturing move by jumping the checker and landing on a free spot behind it."
                    + " \n The same checkers can make multiple capturing moves in a row."
                    + " \n Forced captures: If a player can make a capture, they must make it. They can choose between differing capturing moves if they have multiple options."
                    ,"This is some help info for the CheckersAI.",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[2]);
                    
                    if(n==1){
                        try {
                            Desktop.getDesktop().browse(new URL("http://www.indepthinfo.com/checkers/play.shtml").toURI());
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                    
                }
            }
            
        );
        
        JMenuItem sourceItem = new JMenuItem("Sources");
        sourceItem.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    JOptionPane.showMessageDialog(mainFrame, "Picture sources: "
                + " \n https://www.freepik.com/free-photos-vectors/paper >Paper image created by Freepik"
                + " \n https://www.freepik.com/free-photos-vectors/food >Food image created by Yanalya - Freepik.com"
                + " \n https://www.freepik.com/free-photos-vectors/background >Background image created by Nikitabuida - Freepik.com"
                + " \n https://www.freepik.com/free-photos-vectors/business >Business image created by Peoplecreations - Freepik.com"
                + " \n https://www.freepik.com/free-photos-vectors/business >Business image created by Rawpixel.com - Freepik.com"
                + " \n https://www.freepik.com/free-photo/restaurant_3563675.htm >Designed by Freepik"
                + " \n https://www.freepik.com/free-photo/public-toilet_1278624.htm >Designed by 4045"
                + " \n https://www.freepik.com/free-photos-vectors/business >Business image created by Katemangostar - Freepik.com");
                }
            }
            
        );
        
        gameMenu.add(closeItem);//adding menu item to their menu
        gameMenu.add(helpItem);
        aboutMenu.add(sourceItem);
        add(gameMenu);//adding the menus to the menu bar
        add(aboutMenu);
    }

}

