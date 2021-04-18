package coolpharaoh.tee.speicher.tea.timer.views.export_import.datatransfer.pojo;

import lombok.Getter;
import lombok.Setter;

// sonar does not recognize the usage because of lombok
@SuppressWarnings("java:S1068")
@Getter
@Setter
public class NotePOJO {
    private int position;
    private String header;
    private String description;
}
