
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
}
