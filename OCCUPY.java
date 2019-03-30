
/**
 * Enumeration class OCCUPY - write a description of the enum class here
 *
 * @author (your name here)
 * @version (version number or date here)
 */
public enum OCCUPY
{
    FREE("",0), WHITE("whiteNorm.png",1), BLACK("blackNorm.png",2), BK("blackKing.png",3), WK("whiteKing.png",4);
    
    private final String description;
    private final int status;
    
    public String getLink() {///access to link
        return description;
    }
    
    public int getStatus() {///access to link
        return status;
    }

    private OCCUPY(String description, int status) {
        this.description = description;
        this.status=status;
    }
    
    public static int mapString(String link){
        switch(link) 
        { 
            case "whiteNorm.png": 
                return 1; 
            case "blackNorm.png": 
                return 2; 
            case "blackKing.png": 
                return 3; 
            case "whiteKing.png": 
                return 4;     
            default: 
                return 0; 
        } 
    }
}
