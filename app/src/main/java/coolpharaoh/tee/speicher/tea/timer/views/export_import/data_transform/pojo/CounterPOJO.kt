package coolpharaoh.tee.speicher.tea.timer.views.export_import.data_transform.pojo;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

// sonar does not recognize the usage because of lombok
@SuppressWarnings("java:S1068")
@Getter
@Setter
public class CounterPOJO {
    private int week;
    private int month;
    private int year;
    private long overall;
    private Date weekDate;
    private Date monthDate;
    private Date yearDate;
}
