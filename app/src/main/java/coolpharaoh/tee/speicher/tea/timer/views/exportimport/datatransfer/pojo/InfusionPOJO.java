package coolpharaoh.tee.speicher.tea.timer.views.exportimport.datatransfer.pojo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InfusionPOJO {

    private int infusionindex;
    private String time;
    private String cooldowntime;
    private int temperaturecelsius;
    private int temperaturefahrenheit;
}
