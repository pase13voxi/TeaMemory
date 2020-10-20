package coolpharaoh.tee.speicher.tea.timer.views.exportimport.datatransfer.pojo;

import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeaPOJO {
    private String name;
    private String variety;
    private int amount;
    private String amountKind;
    private int color;
    private int nextInfusion;
    private Date date;
    private List<InfusionPOJO> infusions;
    private List<CounterPOJO> counters;
    private List<NotePOJO> notes;
}
