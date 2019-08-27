package coolpharaoh.tee.speicher.tea.timer.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;

import coolpharaoh.tee.speicher.tea.timer.entities.converter.DateConverter;

@Entity(tableName = "tea")
public class Tea {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "tea_id")
    private Long id;

    private String name;
    private String variety;
    private int amount;
    private String amountkind;
    private int color;
    private int lastInfusion;
    @TypeConverters(DateConverter.class)
    private Date date;

    public Tea(){}

    @Ignore
    public Tea(String name, String variety, int amount, String amountkind, int color, int lastInfusion, Date date) {
        this.name = name;
        this.variety = variety;
        this.amount = amount;
        this.amountkind = amountkind;
        this.color = color;
        this.lastInfusion = lastInfusion;
        this.date = date;
    }

    @NonNull
    public Long getId() {
        return id;
    }

    public void setId(@NonNull Long id) {
        this.id = id;
    }

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

}
