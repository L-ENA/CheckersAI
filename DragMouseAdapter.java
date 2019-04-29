import java.awt.event.MouseAdapter;
import java.util.*;
/**
 * The mouse adapter used for transfering data
 */
class DragMouseAdapter extends MouseAdapter {
   ArrayList<Integer> pos; 
   private String link;
   public DragMouseAdapter(ArrayList<Integer> pos, String link){
       super(); 
       this.pos=pos;
       this.link=link;
   }
   
   public DragMouseAdapter(){
       super();
   }
  
}
