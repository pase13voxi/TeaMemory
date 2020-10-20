package coolpharaoh.tee.speicher.tea.timer.views.exportimport.datatransfer.pojo;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CounterPOJO {

    private int day;
    private int week;
    private int month;
    private long overall;
    private Date daydate;
    private Date weekdate;
    private Date monthdate;
}
