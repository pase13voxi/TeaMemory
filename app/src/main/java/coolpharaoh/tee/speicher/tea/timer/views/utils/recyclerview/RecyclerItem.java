package coolpharaoh.tee.speicher.tea.timer.views.utils.recyclerview;

/**
 * Created by paseb on 03.11.2016.
 */

public class RecyclerItem {
    private final String heading;
    private final String description;

    public RecyclerItem(String heading, String description) {
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
