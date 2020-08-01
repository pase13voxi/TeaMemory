package coolpharaoh.tee.speicher.tea.timer.core.tea;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;

import coolpharaoh.tee.speicher.tea.timer.core.converter.DateConverter;

@Entity(tableName = "tea")
public class Tea {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "tea_id")
    private Long id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "variety")
    private String variety;

    @ColumnInfo(name = "amount")
    private int amount;

    @ColumnInfo(name = "amountkind")
    private String amountKind;

    @ColumnInfo(name = "color")
    private int color;

    @ColumnInfo(name = "nextinfusion")
    private int nextInfusion;

    @TypeConverters(DateConverter.class)
    @ColumnInfo(name = "date")
    private Date date;

    public Tea(){}

    @Ignore
    public Tea(String name, String variety, int amount, String amountKind, int color, int nextInfusion, Date date) {
        this.name = name;
        this.variety = variety;
        this.amount = amount;
        this.amountKind = amountKind;
        this.color = color;
        this.nextInfusion = nextInfusion;
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

}
