package coolpharaoh.tee.speicher.tea.timer.models.datatransfer.pojo;

import java.util.Date;
import java.util.List;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVariety() {
        return variety;
    }

    public void setVariety(String variety) {
        this.variety = variety;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getAmountKind() {
        return amountKind;
    }

    public void setAmountKind(String amountKind) {
        this.amountKind = amountKind;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getNextInfusion() {
        return nextInfusion;
    }

    public void setNextInfusion(int nextInfusion) {
        this.nextInfusion = nextInfusion;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<InfusionPOJO> getInfusions() {
        return infusions;
    }

    public void setInfusions(List<InfusionPOJO> infusions) {
        this.infusions = infusions;
    }

    public List<CounterPOJO> getCounters() {
        return counters;
    }

    public void setCounters(List<CounterPOJO> counters) {
        this.counters = counters;
    }

    public List<NotePOJO> getNotes() {
        return notes;
    }

    public void setNotes(List<NotePOJO> notes) {
        this.notes = notes;
    }
}
