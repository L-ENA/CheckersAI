import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.TransferHandler;
import java.util.*;
/**
 * Write a description of class DragMouseAdapter here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
class DragMouseAdapter extends MouseAdapter {
   ArrayList<Integer> pos; 
   private String link;
   public DragMouseAdapter(ArrayList<Integer> pos, String link){
       super(); 
       this.pos=pos;
       this.link=link;
   }
   
   
  
}
