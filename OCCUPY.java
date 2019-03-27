
/**
 * Enumeration class OCCUPY - write a description of the enum class here
 *
 * @author (your name here)
 * @version (version number or date here)
 */
public enum OCCUPY
{
    FREE(""), WHITE("whiteNorm.png"), BLACK("blackNorm.png"), BK("blackKing.png"), WK("whiteKing.png");
    
    private final String description;
    
    public String getLink() {///access to link
        return description;
    }

    private OCCUPY(String description) {
        this.description = description;
    }
}
