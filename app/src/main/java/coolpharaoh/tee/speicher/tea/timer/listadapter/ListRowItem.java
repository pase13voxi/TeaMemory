package coolpharaoh.tee.speicher.tea.timer.listadapter;

/**
 * Created by paseb on 03.11.2016.
 */

public class ListRowItem {
    private String heading;
    private String description;
    public ListRowItem(String heading, String description){
        this.heading = heading;
        this.description = description;
    }

    public String getHeading() {
        return heading;
    }

    public String getDescription() {
        return description;
    }
}
