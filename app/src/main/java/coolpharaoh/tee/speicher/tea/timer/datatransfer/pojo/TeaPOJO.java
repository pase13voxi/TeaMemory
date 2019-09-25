package coolpharaoh.tee.speicher.tea.timer.datatransfer.pojo;

import androidx.room.TypeConverters;

import java.util.Date;
import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.entities.converter.DateConverter;

public class TeaPOJO {
    private String name;
    private String variety;
    private int amount;
    private String amountkind;
    private int color;
    private int lastInfusion;
    @TypeConverters(DateConverter.class)
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

    public String getAmountkind() {
        return amountkind;
    }

    public void setAmountkind(String amountkind) {
        this.amountkind = amountkind;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getLastInfusion() {
        return lastInfusion;
    }

    public void setLastInfusion(int lastInfusion) {
        this.lastInfusion = lastInfusion;
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
