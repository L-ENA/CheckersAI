/**
 * Enumeration class OCCUPY - maps pictures to links
 */
public enum OCCUPY
{
    FREE("",0), WHITE("whiteNorm.png",1), BLACK("blackNorm.png",2), BK("blackKing.png",3), WK("whiteKing.png",4), FEET("feet.png", 5), KICK("kick.png", 6);
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
                return WHITE.getStatus(); 
            case "blackNorm.png": 
                return BLACK.getStatus(); 
            case "blackKing.png": 
                return BK.getStatus(); 
            case "whiteKing.png": 
                return WK.getStatus();  
            case "feet.png": 
                return FEET.getStatus();
            case "kick.png": 
                return KICK.getStatus(); 
            default: 
                return 0; 
        } 
    }
    public static String mapStatus(int status){
        switch(status) 
        { 
            case 1: 
                return WHITE.getLink(); 
            case 2: 
                return BLACK.getLink(); 
            case 3: 
                return BK.getLink(); 
            case 4: 
                return WK.getLink();
            case 5: 
                return FEET.getLink();
            case 6: 
                return KICK.getLink();    
            default: 
                return ""; 
        } 
    }
}
